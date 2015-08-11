/*4.10.1 class*/
package com.maddox.il2.ai.air;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector2d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.Formation;
import com.maddox.il2.ai.Squadron;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiAcWithReleasedOrdinance;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.D3A;
import com.maddox.il2.objects.air.F4U;
import com.maddox.il2.objects.air.G4M2E;
import com.maddox.il2.objects.air.I_16TYPE24DRONE;
import com.maddox.il2.objects.air.JU_87;
import com.maddox.il2.objects.air.MXY_7;
import com.maddox.il2.objects.air.R_5xyz;
import com.maddox.il2.objects.air.Scheme4;
import com.maddox.il2.objects.air.TB_3_4M_34R_SPB;
import com.maddox.il2.objects.air.TypeBNZFighter;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeDiveBomber;
import com.maddox.il2.objects.air.TypeDockable;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeGlider;
import com.maddox.il2.objects.air.TypeGuidedBombCarrier;
import com.maddox.il2.objects.air.TypeHasToKG;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.il2.objects.air.TypeTNBFighter;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.BombGunNull;
import com.maddox.il2.objects.weapons.BombGunPara;
import com.maddox.il2.objects.weapons.BombGunParafrag8;
import com.maddox.il2.objects.weapons.ParaTorpedoGun;
import com.maddox.il2.objects.weapons.RocketGunFritzX;
import com.maddox.il2.objects.weapons.RocketGunHS_293;
import com.maddox.il2.objects.weapons.TorpedoGun;

public class AirGroup
{
	public int nOfAirc;
	public Aircraft[] airc;
	public Squadron sq;
	public Way w;
	public Vector3d Pos;
	public AirGroupList[] enemies;
	public AirGroupList[] friends;
	public AirGroup clientGroup;
	public AirGroup targetGroup;
	public AirGroup leaderGroup;
	public AirGroup rejoinGroup;
	public int grAttached;
	public int gTargetPreference;
	public int aTargetPreference;
	public boolean enemyFighters;
	private boolean gTargWasFound;
	private boolean gTargDestroyed;
	private int gTargMode;
	private Actor gTargActor;
	private Point3d gTargPoint;
	private float gTargRadius;
	private boolean aTargWasFound;
	private boolean aTargDestroyed;
	private boolean WeWereInGAttack;
	private boolean WeWereInAttack;
	public byte formationType;
	private byte oldFType;
	private float oldFScale;
	private boolean oldFInterp;
	public boolean fInterpolation;
	private int oldEnemyNum;
	public int timeOutForTaskSwitch;
	public int grTask;
	public static final int FLY_WAYPOINT = 1;
	public static final int DEFENDING = 2;
	public static final int ATTACK_AIR = 3;
	public static final int ATTACK_GROUND = 4;
	public static final int TAKEOFF = 5;
	public static final int LANDING = 6;
	public static final int GT_MODE_NONE = 0;
	public static final int GT_MODE_CHIEF = 1;
	public static final int GT_MODE_AROUND_POINT = 2;
	private static String[] GTList = {"NO_TASK.", "FLY_WAYPOINT", "DEFENDING", "ATTACK_AIR", "ATTACK_GROUND", "TAKEOFF", "LANDING"};
	private static Vector3d tmpV = new Vector3d();
	private static Vector3d tmpV1 = new Vector3d();
	private static Point3d tmpP = new Point3d();
	private static Point3d tmpP3d = new Point3d();
	private static Vector2d P1P2vector = new Vector2d();
	private static Vector2d norm1 = new Vector2d();
	private static Vector2d myPoint = new Vector2d();
	
	public String grTaskName()
	{
		return GTList[grTask];
	}
	
	public AirGroup()
	{
		Pos = new Vector3d();
		initVars();
	}
	
	public AirGroup(Squadron squadron, Way way)
	{
		Pos = new Vector3d();
		initVars();
		sq = squadron;
		w = way;
	}
	
	public AirGroup(AirGroup airgroup_0_)
	{
		Pos = new Vector3d();
		initVars();
		if (airgroup_0_ != null)
		{
			sq = airgroup_0_.sq;
			if (airgroup_0_.w != null)
			{
				w = new Way(airgroup_0_.w);
				w.setCur(airgroup_0_.w.Cur());
			}
			else
			{
				w = new Way();
				WayPoint waypoint = new WayPoint((float)airgroup_0_.Pos.x, (float)airgroup_0_.Pos.y, (float)airgroup_0_.Pos.z);
				w.add(waypoint);
			}
			Pos.set(airgroup_0_.Pos);
			int i = AirGroupList.length(airgroup_0_.enemies[0]);
			for (int i_1_ = 0; i_1_ < i; i_1_++)
				AirGroupList.addAirGroup(enemies, 0, AirGroupList.getGroup((airgroup_0_.enemies[0]), i_1_));
			i = AirGroupList.length(airgroup_0_.friends[0]);
			for (int i_2_ = 0; i_2_ < i; i_2_++)
				AirGroupList.addAirGroup(friends, 0, AirGroupList.getGroup((airgroup_0_.friends[0]), i_2_));
			rejoinGroup = airgroup_0_;
			gTargetPreference = airgroup_0_.gTargetPreference;
			aTargetPreference = airgroup_0_.aTargetPreference;
			enemyFighters = airgroup_0_.enemyFighters;
			oldEnemyNum = airgroup_0_.oldEnemyNum;
			if (AirGroupList.groupInList(War.Groups[0], airgroup_0_))
				AirGroupList.addAirGroup(War.Groups, 0, this);
			else
				AirGroupList.addAirGroup(War.Groups, 1, this);
		}
	}
	
	public void initVars()
	{
		nOfAirc = 0;
		airc = new Aircraft[16];
		sq = null;
		w = null;
		Pos = new Vector3d(0.0, 0.0, 0.0);
		enemies = new AirGroupList[1];
		friends = new AirGroupList[1];
		clientGroup = null;
		targetGroup = null;
		leaderGroup = null;
		rejoinGroup = null;
		grAttached = 0;
		gTargetPreference = 0;
		aTargetPreference = 9;
		enemyFighters = false;
		gTargWasFound = false;
		gTargDestroyed = false;
		gTargMode = 0;
		gTargActor = null;
		gTargPoint = new Point3d();
		gTargRadius = 0.0F;
		aTargWasFound = false;
		aTargDestroyed = false;
		WeWereInGAttack = false;
		WeWereInAttack = false;
		formationType = (byte)-1;
		fInterpolation = false;
		oldFType = (byte)-1;
		oldFScale = 0.0F;
		oldFInterp = false;
		oldEnemyNum = 0;
		timeOutForTaskSwitch = 0;
		grTask = 1;
	}
	
	public void release()
	{
		for (int i = 0; i < nOfAirc; i++)
		{
			if (airc[i] != null)
				((Maneuver)airc[i].FM).Group = null;
			airc[i] = null;
		}
		nOfAirc = 0;
		sq = null;
		w = null;
		Pos = null;
		if (enemies[0] != null)
			enemies[0].release();
		if (friends[0] != null)
			friends[0].release();
		enemies = null;
		friends = null;
		clientGroup = null;
		targetGroup = null;
		leaderGroup = null;
		rejoinGroup = null;
		gTargPoint = null;
	}
	
	public void addAircraft(Aircraft aircraft)
	{
		if (nOfAirc >= 16)
			System.out.print("Group > 16 in squadron " + sq.name());
		else
		{
			int i;
			if (aircraft.getSquadron() == sq)
			{
				for (i = 0; (i < nOfAirc && airc[i].getSquadron() == sq && ((airc[i].getWing().indexInSquadron() * 4 + airc[i].aircIndex()) <= (aircraft.getWing().indexInSquadron() * 4 + aircraft.aircIndex()))); i++)
				{
					/* empty */
				}
			}
			else
				i = nOfAirc;
			for (int i_3_ = nOfAirc - 1; i_3_ >= i; i_3_--)
				airc[i_3_ + 1] = airc[i_3_];
			airc[i] = aircraft;
			if (w != null)
			{
				aircraft.FM.AP.way = new Way(w);
				aircraft.FM.AP.way.setCur(w.Cur());
			}
			nOfAirc++;
			if (aircraft.FM instanceof Maneuver)
				((Maneuver)aircraft.FM).Group = this;
		}
	}
	
	public void delAircraft(Aircraft aircraft)
	{
		for (int i = 0; i < nOfAirc; i++)
		{
			if (aircraft == airc[i])
			{
				((Maneuver)airc[i].FM).Group = null;
				for (int i_4_ = i; i_4_ < nOfAirc - 1; i_4_++)
					airc[i_4_] = airc[i_4_ + 1];
				nOfAirc--;
				break;
			}
		}
		if (grTask == 1 || grTask == 2)
			setTaskAndManeuver(0);
	}
	
	public void changeAircraft(Aircraft aircraft, Aircraft aircraft_5_)
	{
		for (int i = 0; i < nOfAirc; i++)
		{
			if (aircraft == airc[i])
			{
				((Maneuver)aircraft.FM).Group = null;
				((Maneuver)aircraft_5_.FM).Group = this;
				((Maneuver)aircraft_5_.FM).setBusy(false);
				airc[i] = aircraft_5_;
				break;
			}
		}
	}
	
	public void rejoinToGroup(AirGroup airgroup_6_)
	{
		if (airgroup_6_ != null)
		{
			for (int i = nOfAirc - 1; i >= 0; i--)
			{
				Aircraft aircraft = airc[i];
				delAircraft(aircraft);
				airgroup_6_.addAircraft(aircraft);
			}
			rejoinGroup = null;
		}
	}
	
	public void attachGroup(AirGroup airgroup_7_)
	{
		if (airgroup_7_ != null)
		{
			for (int i = 0; i < nOfAirc; i++)
			{
				if (airc[i].FM instanceof Maneuver)
				{
					Maneuver maneuver = (Maneuver)airc[i].FM;
					if ((!(maneuver instanceof RealFlightModel) || !((RealFlightModel)maneuver).isRealMode()) && (maneuver.get_maneuver() == 26 || maneuver.get_maneuver() == 64))
						return;
				}
			}
			w = null;
			w = new Way(airgroup_7_.w);
			w.setCur(airgroup_7_.w.Cur());
			for (int i = 0; i < nOfAirc; i++)
			{
				airc[i].FM.AP.way = null;
				airc[i].FM.AP.way = new Way(airgroup_7_.w);
				airc[i].FM.AP.way.setCur(airgroup_7_.w.Cur());
			}
			Formation.leaderOffset(airc[0].FM, formationType, airc[0].FM.Offset);
			leaderGroup = airgroup_7_;
			leaderGroup.grAttached++;
			grTask = 1;
			setFormationAndScale(airgroup_7_.formationType, 1.0F, true);
		}
	}
	
	public void detachGroup(AirGroup airgroup_8_)
	{
		if (airgroup_8_ != null)
		{
			leaderGroup.grAttached--;
			if (leaderGroup.grAttached < 0)
				leaderGroup.grAttached = 0;
			leaderGroup = null;
			grTask = 1;
			setTaskAndManeuver(0);
		}
	}
	
	public int numInGroup(Aircraft aircraft)
	{
		for (int i = 0; i < nOfAirc; i++)
		{
			if (aircraft == airc[i])
				return i;
		}
		return -1;
	}
	
	public void setEnemyFighters()
	{
		int i = AirGroupList.length(enemies[0]);
		enemyFighters = false;
		for (int i_9_ = 0; i_9_ < i; i_9_++)
		{
			AirGroup airgroup_10_ = AirGroupList.getGroup(enemies[0], i_9_);
			if (airgroup_10_.nOfAirc > 0 && airgroup_10_.airc[0] instanceof TypeFighter)
			{
				enemyFighters = true;
				break;
			}
		}
	}
	
	public void setFormationAndScale(byte i, float f, boolean bool)
	{
		if (oldFType != i || oldFScale != f || oldFInterp != bool)
		{
			fInterpolation = bool;
			for (int i_11_ = 1; i_11_ < nOfAirc; i_11_++)
			{
				if (airc[i_11_] instanceof TypeGlider)
					return;
				((Maneuver)airc[i_11_].FM).formationScale = f;
				Formation.gather(airc[i_11_].FM, i, tmpV);
				if (!bool)
					airc[i_11_].FM.Offset.set(tmpV);
				formationType = ((Maneuver)airc[i_11_].FM).formationType;
			}
			if (grTask == 1 || grTask == 2)
				setTaskAndManeuver(0);
			oldFType = i;
			oldFScale = f;
			oldFInterp = bool;
		}
	}
	
	public void formationUpdate()
	{
		if (fInterpolation)
		{
			boolean bool = false;
			for (int i = 1; i < nOfAirc; i++)
			{
				if (Actor.isAlive(airc[i]))
				{
					if (airc[i] instanceof TypeGlider)
						return;
					Formation.gather(airc[i].FM, formationType, tmpV);
					tmpV1.sub(tmpV, airc[i].FM.Offset);
					float f = (float)tmpV1.length();
					if (f != 0.0F)
					{
						bool = true;
						if (f < 0.1F)
							airc[i].FM.Offset.set(tmpV);
						else
						{
							double d = 4.0E-4 * tmpV1.length();
							if (d > 1.0)
								d = 1.0;
							tmpV1.normalize();
							tmpV1.scale(d);
							airc[i].FM.Offset.add(tmpV1);
						}
					}
				}
			}
			if (!bool)
				fInterpolation = false;
			if (grTask == 1 || grTask == 2)
				setTaskAndManeuver(0);
		}
	}
	
	public boolean groupsInContact(AirGroup airgroup_12_)
	{
		for (int i = 0; i < nOfAirc; i++)
		{
			for (int i_13_ = 0; i_13_ < airgroup_12_.nOfAirc; i_13_++)
			{
				tmpV.sub(airc[i].FM.Loc, airgroup_12_.airc[i_13_].FM.Loc);
				if (tmpV.lengthSquared() < 5.0E7)
					return true;
			}
		}
		return false;
	}
	
	public boolean inCorridor(Point3d point3d)
	{
		if (w == null)
			return true;
		int i = w.Cur();
		if (i == 0)
			return true;
		w.prev();
		tmpP = w.curr().getP();
		w.setCur(i);
		tmpV.sub(w.curr().getP(), tmpP);
		P1P2vector.set(tmpV);
		float f = (float)P1P2vector.length();
		if (f > 1.0E-4F)
			P1P2vector.scale((double)(1.0F / f));
		else
			P1P2vector.set(1.0, 0.0);
		tmpV.sub(point3d, tmpP);
		myPoint.set(tmpV);
		if (P1P2vector.dot(myPoint) < -25000.0)
			return false;
		norm1.set(-P1P2vector.y, P1P2vector.x);
		float f_14_ = (float)norm1.dot(myPoint);
		if (f_14_ > 25000.0F)
			return false;
		if (f_14_ < -25000.0F)
			return false;
		tmpV.sub(point3d, w.curr().getP());
		myPoint.set(tmpV);
		if (P1P2vector.dot(myPoint) > 25000.0)
			return false;
		return true;
	}
	
	public void setGroupTask(int i)
	{
		grTask = i;
		if (grTask == 1 || grTask == 2)
			setTaskAndManeuver(0);
		else
		{
			for (int i_15_ = 0; i_15_ < nOfAirc; i_15_++)
			{
				if (!((Maneuver)airc[i_15_].FM).isBusy())
					setTaskAndManeuver(i_15_);
			}
		}
	}
	
	public void dropBombs()
	{
		//TODO: Added by |ZUTI|: temp variablea
		//------------------------
		String acName = null;
		ZutiAcWithReleasedOrdinance aircraft = null;
		//------------------------
		
		for (int i = 0; i < nOfAirc; i++)
		{
			if (!((Maneuver) airc[i].FM).isBusy())
			{
				((Maneuver) airc[i].FM).bombsOut = true;
				
				//TODO: Added by |ZUTI|: remember which AC dropped bombs for later synchronization over net
				//-----------------------------------------------------------
				acName = airc[i].name();
				aircraft = new ZutiAcWithReleasedOrdinance();
				aircraft.setAcName(acName);
				ZutiAcWithReleasedOrdinance.AC_THAT_RELEASED_BOMBS.put(acName, aircraft);
				//-----------------------------------------------------------
			}
		}
		if (friends[0] != null)
		{
			int i = AirGroupList.length(friends[0]);
			for (int i_16_ = 0; i_16_ < i; i_16_++)
			{
				AirGroup airgroup_17_ = AirGroupList.getGroup(friends[0], i_16_);
				if (airgroup_17_ != null && airgroup_17_.leaderGroup == this) airgroup_17_.dropBombs();
			}
		}
	}
	
	public Aircraft firstOkAirc(int i)
	{
		for (int i_18_ = 0; i_18_ < nOfAirc; i_18_++)
		{
			if (i < 0 || i >= nOfAirc || i_18_ != i && (i_18_ != i + 1 || (airc[i_18_].aircIndex() != airc[i].aircIndex() + 1)))
			{
				Maneuver maneuver = (Maneuver)airc[i_18_].FM;
				if ((maneuver.get_task() == 7 || maneuver.get_task() == 6 || maneuver.get_task() == 4) && maneuver.isOk())
					return airc[i_18_];
			}
		}
		return null;
	}
	
	public boolean waitGroup(int i)
	{
		Aircraft aircraft = firstOkAirc(i);
		Maneuver maneuver = (Maneuver)airc[i].FM;
		if (aircraft != null)
		{
			maneuver.airClient = aircraft.FM;
			maneuver.set_task(1);
			maneuver.clear_stack();
			maneuver.set_maneuver(59);
			return true;
		}
		maneuver.set_task(3);
		maneuver.clear_stack();
		maneuver.set_maneuver(21);
		return false;
	}
	
	public void setGTargMode(int i)
	{
		gTargetPreference = i;
	}
	
	public void setGTargMode(Actor actor)
	{
		if (actor != null && Actor.isAlive(actor))
		{
			if (actor instanceof BigshipGeneric || actor instanceof ShipGeneric || actor instanceof Chief || actor instanceof Bridge)
			{
				gTargMode = 1;
				gTargActor = actor;
			}
			else
			{
				gTargMode = 2;
				gTargActor = actor;
				gTargPoint.set(actor.pos.getAbsPoint());
				gTargRadius = 200.0F;
				if (actor instanceof BigshipGeneric)
				{
					gTargRadius = 20.0F;
					setGTargMode(6);
				}
			}
		}
		else
			gTargMode = 0;
	}
	
	public void setGTargMode(Point3d point3d, float f)
	{
		gTargMode = 2;
		gTargPoint.set(point3d);
		gTargRadius = f;
	}
	
	public Actor setGAttackObject(int i)
	{
		if (i > nOfAirc - 1)
			return null;
		if (i < 0)
			return null;
		Actor actor;
		if (gTargMode == 1)
			actor = War.GetRandomFromChief(airc[i], gTargActor);
		else if (gTargMode == 2)
			actor = War.GetNearestEnemy(airc[i], gTargetPreference, gTargPoint, gTargRadius);
		else
			actor = null;
		if (actor != null)
		{
			gTargWasFound = true;
			gTargDestroyed = false;
		}
		if (actor == null && gTargWasFound)
		{
			gTargDestroyed = true;
			gTargWasFound = false;
		}
		return actor;
	}
	
	public void setATargMode(int i)
	{
		aTargetPreference = i;
	}
	
	public AirGroup chooseTargetGroup()
	{
		if (enemies == null)
			return null;
		int i = AirGroupList.length(enemies[0]);
		AirGroup airgroup_19_ = null;
		float f = 1.0E12F;
		boolean bool = false;
		for (int i_20_ = 0; i_20_ < i; i_20_++)
		{
			AirGroup airgroup_21_ = AirGroupList.getGroup(enemies[0], i_20_);
			bool = false;
			if (airgroup_21_ != null && airgroup_21_.nOfAirc > 0)
			{
				if (aTargetPreference == 9)
					bool = true;
				else if (aTargetPreference == 7 && airgroup_21_.airc[0] instanceof TypeFighter)
					bool = true;
				else if (aTargetPreference == 8 && !(airgroup_21_.airc[0] instanceof TypeFighter))
					bool = true;
				if (bool)
				{
					for (int i_22_ = 0; i_22_ < airgroup_21_.nOfAirc; i_22_++)
					{
						if (Actor.isAlive(airgroup_21_.airc[i_22_]) && airgroup_21_.airc[i_22_].FM.isCapableOfBMP() && !airgroup_21_.airc[i_22_].FM.isTakenMortalDamage())
						{
							bool = true;
							break;
						}
					}
				}
				if (bool)
				{
					tmpV.sub(Pos, airgroup_21_.Pos);
					if (tmpV.lengthSquared() < (double)f)
					{
						airgroup_19_ = airgroup_21_;
						f = (float)tmpV.lengthSquared();
					}
				}
			}
		}
		return airgroup_19_;
	}
	
	public boolean somebodyAttacks()
	{
		boolean bool = false;
		for (int i = 0; i < nOfAirc; i++)
		{
			Maneuver maneuver = (Maneuver)airc[i].FM;
			if (maneuver instanceof RealFlightModel && ((RealFlightModel)maneuver).isRealMode() && airc[i].aircIndex() == 0)
			{
				bool = true;
				break;
			}
			if (!isWingman(i) && maneuver.isOk() && maneuver.hasCourseWeaponBullets())
			{
				bool = true;
				break;
			}
		}
		return bool;
	}
	
	public boolean somebodyGAttacks()
	{
		boolean bool = false;
		for (int i = 0; i < nOfAirc; i++)
		{
			Maneuver maneuver = (Maneuver)airc[i].FM;
			if (maneuver instanceof RealFlightModel && ((RealFlightModel)maneuver).isRealMode() && airc[i].aircIndex() == 0)
			{
				bool = true;
				break;
			}
			if (maneuver.isOk() && maneuver.get_task() != 1)
			{
				bool = true;
				break;
			}
		}
		return bool;
	}
	
	public void switchWayPoint()
	{
		Maneuver maneuver = (Maneuver)airc[0].FM;
		tmpV.sub(w.curr().getP(), maneuver.Loc);
		float f = (float)tmpV.lengthSquared();
		int i = w.Cur();
		w.next();
		tmpV.sub(w.curr().getP(), maneuver.Loc);
		float f_23_ = (float)tmpV.lengthSquared();
		w.setCur(i);
		if (f > f_23_)
		{
			String string = airc[0].FM.AP.way.curr().getTargetName();
			airc[0].FM.AP.way.next();
			w.next();
			if (airc[0].FM.AP.way.curr().Action == 0 && airc[0].FM.AP.way.curr().getTarget() == null)
				airc[0].FM.AP.way.curr().setTarget(string);
			if (w.curr().getTarget() == null)
				w.curr().setTarget(string);
		}
	}
	
	public boolean isWingman(int i)
	{
		if (i < 0)
			return false;
		Maneuver maneuver = (Maneuver)airc[i].FM;
		if ((airc[i].aircIndex() & 0x1) != 0 && !maneuver.aggressiveWingman)
		{
			if (i > 0)
				maneuver.Leader = airc[i - 1].FM;
			else
				return false;
			if (maneuver.Leader != null && airc[i - 1].aircIndex() == airc[i].aircIndex() - 1 && enemyFighters && Actor.isAlive(airc[i - 1]) && ((Maneuver)maneuver.Leader).isOk())
				return true;
		}
		return false;
	}
	
	public Aircraft chooseTarget(AirGroup airgroup_24_)
	{
		Aircraft aircraft = null;
		if (airgroup_24_ != null && airgroup_24_.nOfAirc > 0)
			aircraft = (airgroup_24_.airc[World.Rnd().nextInt(0, airgroup_24_.nOfAirc - 1)]);
		if (aircraft != null && (!Actor.isAlive(aircraft) || !aircraft.FM.isCapableOfBMP() || aircraft.FM.isTakenMortalDamage()))
		{
			for (int i = 0; i < airgroup_24_.nOfAirc; i++)
			{
				if (Actor.isAlive(airgroup_24_.airc[i]) && airgroup_24_.airc[i].FM.isCapableOfBMP() && !airgroup_24_.airc[i].FM.isTakenMortalDamage())
					aircraft = airgroup_24_.airc[i];
			}
		}
		return aircraft;
	}
	
	public FlightModel setAAttackObject(int i)
	{
		if (i > nOfAirc - 1)
			return null;
		if (i < 0)
			return null;
		AirGroup airgroup_25_ = targetGroup;
		if (airgroup_25_ == null || airgroup_25_.nOfAirc == 0)
			airgroup_25_ = chooseTargetGroup();
		Aircraft aircraft = chooseTarget(airgroup_25_);
		if (aircraft != null)
		{
			aTargWasFound = true;
			aTargDestroyed = false;
		}
		if (aircraft == null && aTargWasFound)
		{
			aTargDestroyed = true;
			aTargWasFound = false;
		}
		if (aircraft != null)
			return aircraft.FM;
		return null;
	}
	
	public void setTaskAndManeuver(int i)
	{
		if (i <= nOfAirc - 1 && i >= 0)
		{
			Maneuver maneuver = (Maneuver)airc[i].FM;
			switch (grTask)
			{
				case 1 :
				{
					Maneuver maneuver_27_ = null;
					tmpV.set(0.0, 0.0, 0.0);
					for (int i_28_ = 0; i_28_ < nOfAirc; i_28_++)
					{
						Maneuver maneuver_29_ = (Maneuver)airc[i_28_].FM;
						if (airc[i_28_] instanceof TypeGlider)
							maneuver_29_.accurate_set_FOLLOW();
						else
						{
							tmpV.add(maneuver_29_.Offset);
							if (!maneuver_29_.isBusy() || (maneuver_29_ instanceof RealFlightModel && ((RealFlightModel)maneuver_29_).isRealMode() && maneuver_29_.isOk()))
							{
								maneuver_29_.Leader = null;
								if (leaderGroup == null || leaderGroup.nOfAirc == 0)
									maneuver_29_.accurate_set_task_maneuver(3, 21);
								else if (((Maneuver)leaderGroup.airc[0].FM).isBusy())
									maneuver_29_.accurate_set_task_maneuver(3, 21);
								else
								{
									maneuver_29_.accurate_set_FOLLOW();
									maneuver_29_.followOffset.set(tmpV);
									maneuver_29_.Leader = leaderGroup.airc[0].FM;
								}
								tmpV.set(0.0, 0.0, 0.0);
								for (int i_30_ = i_28_ + 1; i_30_ < nOfAirc; i_30_++)
								{
									Maneuver maneuver_31_ = (Maneuver)airc[i_30_].FM;
									tmpV.add(maneuver_31_.Offset);
									if (!maneuver_31_.isBusy())
									{
										maneuver_31_.accurate_set_FOLLOW();
										if (airc[i_30_] instanceof TypeGlider)
											continue;
										if (maneuver_27_ == null)
										{
											maneuver_31_.followOffset.set(tmpV);
											maneuver_31_.Leader = maneuver_29_;
										}
										else
										{
											maneuver_31_.followOffset.set(maneuver_31_.Offset);
											maneuver_31_.Leader = maneuver_27_;
										}
									}
									if (maneuver_31_ instanceof RealFlightModel)
									{
										if ((airc[i_30_].aircIndex() & 0x1) == 0)
											maneuver_27_ = maneuver_31_;
									}
									else
										maneuver_27_ = null;
								}
								break;
							}
						}
					}
					break;
				}
				case 4 :
					if (!maneuver.isBusy())
					{
						if (maneuver.target_ground == null || !Actor.isAlive(maneuver.target_ground) || maneuver.Loc.distance(maneuver.target_ground.pos.getAbsPoint()) > 3000.0)
							maneuver.target_ground = setGAttackObject(i);
						if (maneuver.target_ground == null)
						{
							if (!waitGroup(i) && i == 0)
							{
								if (maneuver.AP.way.curr().Action == 3)
									maneuver.AP.way.next();
								setGroupTask(1);
							}
						}
						else
						{
							if (airc[i] instanceof TypeDockable)
							{
								if (airc[i] instanceof I_16TYPE24DRONE)
									((I_16TYPE24DRONE)airc[i]).typeDockableAttemptDetach();
								if (airc[i] instanceof MXY_7)
									((MXY_7)airc[i]).typeDockableAttemptDetach();
								if (airc[i] instanceof G4M2E)
									((G4M2E)airc[i]).typeDockableAttemptDetach();
								if (airc[i] instanceof TB_3_4M_34R_SPB)
									((TB_3_4M_34R_SPB)airc[i]).typeDockableAttemptDetach();
							}
							if (((maneuver.AP.way.Cur() == maneuver.AP.way.size() - 1) && maneuver.AP.way.curr().Action == 3) || airc[i] instanceof MXY_7)
							{
								maneuver.kamikaze = true;
								maneuver.set_task(7);
								maneuver.clear_stack();
								maneuver.set_maneuver(46);
							}
							else
							{
								boolean bool = true;
								if (maneuver.hasRockets())
									bool = false;
								if (maneuver.CT.Weapons[0] != null && maneuver.CT.Weapons[0][0] != null && (maneuver.CT.Weapons[0][0].bulletMassa() > 0.05F) && (maneuver.CT.Weapons[0][0].countBullets() > 0))
									bool = false;
								if (bool && maneuver.CT.getWeaponMass() < 7.0F || maneuver.CT.getWeaponMass() < 1.0F)
								{
									Voice.speakEndOfAmmo(airc[i]);
									if (!waitGroup(i) && i == 0)
									{
										if (maneuver.AP.way.curr().Action == 3)
											maneuver.AP.way.next();
										setGroupTask(1);
									}
								}
								else
								{
									if (maneuver.target_ground instanceof Prey && (((Prey)maneuver.target_ground).HitbyMask() & 0x1) == 0)
									{
										float f = 0.0F;
										for (int i_32_ = 0; i_32_ < 4; i_32_++)
										{
											if (maneuver.CT.Weapons[i_32_] != null)
											{
												for (int i_33_ = 0; i_33_ < (maneuver.CT.Weapons[i_32_]).length; i_33_++)
												{
													if ((maneuver.CT.Weapons[i_32_][i_33_]) != null && maneuver.CT.Weapons[i_32_][i_33_].countBullets() != 0 && maneuver.CT.Weapons[i_32_][i_33_].bulletMassa() > f)
														f = maneuver.CT.Weapons[i_32_][i_33_].bulletMassa();
												}
											}
										}
										if (f < 0.08F || (maneuver.target_ground instanceof TgtShip) && f < 0.55F)
										{
											maneuver.AP.way.next();
											maneuver.set_task(1);
											maneuver.clear_stack();
											maneuver.set_maneuver(21);
											maneuver.target_ground = null;
											break;
										}
									}
									if (maneuver.CT.Weapons[3] != null)
									{
										for (int i_34_ = 0; i_34_ < maneuver.CT.Weapons[3].length; i_34_++)
										{
											if ((maneuver.CT.Weapons[3][i_34_] != null) && !(maneuver.CT.Weapons[3][i_34_] instanceof BombGunNull) && maneuver.CT.Weapons[3][i_34_].countBullets() != 0)
											{
												if (maneuver.CT.Weapons[3][i_34_] instanceof ParaTorpedoGun)
												{
													maneuver.set_task(7);
													maneuver.clear_stack();
													maneuver.set_maneuver(43);
													return;
												}
												if (maneuver.CT.Weapons[3][i_34_] instanceof TorpedoGun)
												{
													if (maneuver.target_ground instanceof TgtShip)
													{
														maneuver.set_task(7);
														maneuver.clear_stack();
														if (maneuver.target_ground instanceof BigshipGeneric)
														{
															BigshipGeneric bigshipgeneric = ((BigshipGeneric)(maneuver.target_ground));
															if ((airc[i] instanceof TypeHasToKG) && !(bigshipgeneric.zutiIsStatic()) && (airc[i].FM.Skill) >= 2 && (bigshipgeneric.collisionR()) > 50.0F)
																maneuver.set_maneuver(81);
															else
																maneuver.set_maneuver(51);
														}
														else
															maneuver.set_maneuver(51);
													}
													else
													{
														maneuver.set_task(7);
														maneuver.clear_stack();
														maneuver.set_maneuver(43);
														return;
													}
													return;
												}
												if ((airc[i] instanceof TypeGuidedBombCarrier) && ((maneuver.CT.Weapons[3][i_34_]) instanceof RocketGunHS_293))
												{
													maneuver.set_task(7);
													maneuver.clear_stack();
													maneuver.set_maneuver(79);
													return;
												}
												if ((airc[i] instanceof TypeGuidedBombCarrier) && ((maneuver.CT.Weapons[3][i_34_]) instanceof RocketGunFritzX))
												{
													maneuver.set_task(7);
													maneuver.clear_stack();
													maneuver.set_maneuver(80);
													return;
												}
												if (maneuver.CT.Weapons[3][i_34_] instanceof BombGunPara)
												{
													w.curr().setTarget(null);
													maneuver.target_ground = null;
													grTask = 1;
													setTaskAndManeuver(i);
													return;
												}
												if (maneuver.CT.Weapons[3][i_34_].bulletMassa() < 10.0F && !((maneuver.CT.Weapons[3][i_34_]) instanceof BombGunParafrag8))
												{
													maneuver.set_task(7);
													maneuver.clear_stack();
													maneuver.set_maneuver(52);
													return;
												}
												if ((airc[i] instanceof TypeDiveBomber) && maneuver.Alt > 1200.0F)
												{
													maneuver.set_task(7);
													maneuver.clear_stack();
													maneuver.set_maneuver(50);
													return;
												}
												if (i_34_ == (maneuver.CT.Weapons[3]).length - 1)
												{
													maneuver.set_task(7);
													maneuver.clear_stack();
													maneuver.set_maneuver(43);
													return;
												}
											}
										}
									}
									if ((maneuver.target_ground instanceof BridgeSegment) && !maneuver.hasRockets())
									{
										maneuver.set_task(1);
										maneuver.clear_stack();
										maneuver.set_maneuver(59);
										maneuver.target_ground = null;
									}
									else if (airc[i] instanceof F4U && maneuver.CT.Weapons[2] != null && ((double)maneuver.CT.Weapons[2][0].bulletMassa() > 100.0) && maneuver.CT.Weapons[2][0].countBullets() > 0)
									{
										maneuver.set_task(7);
										maneuver.clear_stack();
										maneuver.set_maneuver(47);
									}
									else if (airc[i] instanceof R_5xyz)
									{
										if (((R_5xyz)airc[i]).strafeWithGuns)
										{
											maneuver.set_task(7);
											maneuver.clear_stack();
											maneuver.set_maneuver(43);
										}
										else
										{
											w.curr().setTarget(null);
											maneuver.target_ground = null;
											grTask = 1;
											setTaskAndManeuver(i);
											grTask = 4;
										}
									}
									else if (airc[i] instanceof TypeFighter || (airc[i] instanceof TypeStormovik))
									{
										maneuver.set_task(7);
										maneuver.clear_stack();
										maneuver.set_maneuver(43);
									}
									else
									{
										w.curr().setTarget(null);
										maneuver.target_ground = null;
										grTask = 1;
										setTaskAndManeuver(i);
										grTask = 4;
									}
								}
							}
						}
					}
					break;
				case 3 :
					if (!maneuver.isBusy())
					{
						if (!(maneuver instanceof RealFlightModel) || !((RealFlightModel)maneuver).isRealMode())
						{
							maneuver.bombsOut = true;
							maneuver.CT.dropFuelTanks();
						}
						if (isWingman(i))
						{
							maneuver.airClient = maneuver.Leader;
							maneuver.followOffset.set(200.0, 0.0, 20.0);
							maneuver.set_task(5);
							maneuver.clear_stack();
							maneuver.set_maneuver(65);
						}
						else
						{
							maneuver.airClient = null;
							boolean bool = true;
							if (inCorridor(maneuver.Loc))
								bool = false;
							if (bool)
							{
								int i_35_ = w.Cur();
								w.next();
								if (inCorridor(maneuver.Loc))
									bool = false;
								w.setCur(i_35_);
								if (bool)
								{
									i_35_ = w.Cur();
									w.prev();
									if (inCorridor(maneuver.Loc))
										bool = false;
									w.setCur(i_35_);
								}
							}
							if (bool)
							{
								maneuver.set_task(3);
								maneuver.clear_stack();
								maneuver.set_maneuver(21);
							}
							else
							{
								if (maneuver.target == null || !((Maneuver)maneuver.target).isOk() || (maneuver.Loc.distance(maneuver.target.Loc) > 4000.0))
									maneuver.target = setAAttackObject(i);
								if (maneuver.target == null || !maneuver.hasCourseWeaponBullets())
								{
									if (!waitGroup(i) && i == 0)
										setGroupTask(1);
								}
								else
								{
									maneuver.set_task(6);
									if (maneuver.target.actor instanceof TypeFighter)
									{
										if ((maneuver.actor instanceof TypeBNZFighter) || (maneuver.VmaxH > maneuver.target.VmaxH + 30.0F) || ((maneuver.target.actor instanceof TypeTNBFighter) && !(maneuver.actor instanceof TypeTNBFighter)))
										{
											maneuver.clear_stack();
											maneuver.set_maneuver(62);
										}
										else
										{
											maneuver.clear_stack();
											maneuver.set_maneuver(27);
										}
									}
									else if (maneuver.target.actor instanceof TypeStormovik)
										((Pilot)maneuver).attackStormoviks();
									else
										((Pilot)maneuver).attackBombers();
								}
							}
						}
					}
					break;
				case 2 :
				{
					Maneuver maneuver_37_ = null;
					tmpV.set(0.0, 0.0, 0.0);
					for (int i_38_ = 0; i_38_ < nOfAirc; i_38_++)
					{
						Maneuver maneuver_39_ = (Maneuver)airc[i_38_].FM;
						tmpV.add(maneuver_39_.Offset);
						if (!maneuver_39_.isBusy() || i_38_ == nOfAirc - 1 || (maneuver_39_ instanceof RealFlightModel && ((RealFlightModel)maneuver_39_).isRealMode()))
						{
							maneuver_39_.Leader = null;
							if (clientGroup != null && clientGroup.nOfAirc > 0 && clientGroup.airc[0] != null)
							{
								maneuver_39_.airClient = clientGroup.airc[0].FM;
								maneuver_39_.accurate_set_task_maneuver(5, 59);
							}
							else
								maneuver_39_.accurate_set_task_maneuver(3, 21);
							tmpV.set(0.0, 0.0, 0.0);
							for (int i_40_ = i_38_ + 1; i_40_ < nOfAirc; i_40_++)
							{
								Maneuver maneuver_41_ = (Maneuver)airc[i_40_].FM;
								tmpV.add(maneuver_41_.Offset);
								if (!maneuver_41_.isBusy())
								{
									maneuver_41_.accurate_set_FOLLOW();
									if (maneuver_37_ == null)
									{
										maneuver_41_.followOffset.set(tmpV);
										maneuver_41_.Leader = maneuver_39_;
									}
									else
									{
										maneuver_41_.followOffset.set(maneuver_41_.Offset);
										maneuver_41_.Leader = maneuver_37_;
									}
								}
								if (maneuver_41_ instanceof RealFlightModel)
								{
									if ((airc[i_40_].aircIndex() & 0x1) == 0)
										maneuver_37_ = maneuver_41_;
								}
								else
									maneuver_37_ = null;
							}
							break;
						}
					}
					break;
				}
				case 5 :
					if (!maneuver.isBusy())
						break;
					break;
				case 6 :
					if (!maneuver.isBusy())
						break;
					break;
				default :
					if (!maneuver.isBusy())
						maneuver.set_maneuver(21);
			}
		}
	}
	
	public void update()
	{
		if (nOfAirc != 0 && airc[0] != null)
		{
			for (int i = 1; i < nOfAirc; i++)
			{
				if (!Actor.isAlive(airc[i]))
				{
					delAircraft(airc[i]);
					i--;
				}
			}
			Maneuver maneuver = (Maneuver)airc[0].FM;
			if (leaderGroup != null)
			{
				if (leaderGroup.nOfAirc == 0)
					detachGroup(leaderGroup);
				else if (leaderGroup.airc[0] == null)
					detachGroup(leaderGroup);
				else if (leaderGroup.airc[0].FM.AP.way.isLanding())
					detachGroup(leaderGroup);
				else
				{
					maneuver.AP.way.setCur(leaderGroup.w.Cur());
					if (maneuver.get_maneuver() == 21 && !((Maneuver)leaderGroup.airc[0].FM).isBusy())
						setTaskAndManeuver(0);
				}
			}
			if (w == null)
				w = new Way(maneuver.AP.way);
			if (!maneuver.AP.way.isLanding() && maneuver.isOk())
				w.setCur(maneuver.AP.way.Cur());
			if (!maneuver.AP.way.isLanding())
			{
				for (int i = 1; i < nOfAirc; i++)
				{
					if (!((Maneuver)airc[i].FM).AP.way.isLanding() && !((Maneuver)airc[i].FM).isBusy())
						((Maneuver)airc[i].FM).AP.way.setCur(w.Cur());
				}
			}
			//TODO: Added by |ZUTI|
			if (maneuver.AP.way.curr().isRadioSilence() || Mission.MDS_VARIABLES().zutiMisc_DisableAIRadioChatter)
			{
				for (int i = 0; i < nOfAirc; i++)
					((Maneuver) airc[i].FM).silence = true;
			}
			else
			{
				for (int i = 0; i < nOfAirc; i++)
					((Maneuver) airc[i].FM).silence = false;
			}
			Pos.set(maneuver.Loc);
			if (formationType == -1)
				setFormationAndScale((byte)0, 1.0F, true);
			if (timeOutForTaskSwitch == 0)
			{
				switch (w.curr().Action)
				{
					case 3 :
					{
						boolean bool = (w.curr().getTarget() != null || airc[0] instanceof TypeFighter || (airc[0] instanceof TypeStormovik && (!(airc[0] instanceof TypeBomber) || airc[0].FM.getAltitude() < 2500.0F)) || airc[0] instanceof D3A
								|| airc[0] instanceof MXY_7 || airc[0] instanceof JU_87);
						if (grTask == 4)
						{
							WeWereInGAttack = true;
							boolean bool_42_ = somebodyGAttacks();
							boolean bool_43_ = false;
							for (int i = 0; i < nOfAirc; i++)
							{
								Maneuver maneuver_44_ = (Maneuver)airc[i].FM;
								if (maneuver_44_.gattackCounter >= 7)
									bool_43_ = true;
							}
							if (!bool_42_ || bool_43_ || gTargDestroyed)
							{
								airc[0].FM.AP.way.next();
								w.next();
								setGroupTask(1);
								for (int i = 1; i < nOfAirc; i++)
								{
									Maneuver maneuver_45_ = (Maneuver)airc[i].FM;
									maneuver_45_.push(57);
									maneuver_45_.pop();
								}
								setFormationAndScale((byte)0, 1.0F, true);
								if (bool_43_)
								{
									for (int i = 0; i < nOfAirc; i++)
										((Maneuver)airc[i].FM).gattackCounter = 0;
								}
							}
						}
						else if (grTask == 3)
						{
							switchWayPoint();
							WeWereInGAttack = true;
							if (AirGroupList.length(enemies[0]) != oldEnemyNum)
								setGroupTask(3);
							boolean bool_46_ = somebodyAttacks();
							if (!bool_46_ || aTargDestroyed)
							{
								setGroupTask(1);
								if (!bool_46_)
									timeOutForTaskSwitch = 90;
								for (int i = 1; i < nOfAirc; i++)
								{
									Maneuver maneuver_47_ = (Maneuver)airc[i].FM;
									if (!maneuver_47_.isBusy())
									{
										maneuver_47_.push(57);
										maneuver_47_.pop();
									}
								}
							}
						}
						if (grTask == 1 && w.curr().Action == 3)
						{
							gTargWasFound = false;
							gTargDestroyed = false;
							gTargMode = 0;
							if (bool)
							{
								setFormationAndScale((byte)9, 8.0F, true);
								if (maneuver.AP.getWayPointDistance() < 5000.0F)
								{
									boolean bool_48_ = false;
									if (w.curr().getTarget() != null)
									{
										setGTargMode(w.curr().getTarget());
										if (gTargMode != 0)
										{
											maneuver.target_ground = setGAttackObject(0);
											if (maneuver.target_ground != null && (maneuver.target_ground.distance(airc[0]) < 12000.0))
											{
												setGroupTask(4);
												Voice.speakBeginGattack(airc[0]);
											}
											else if (maneuver.AP.getWayPointDistance() < 1500.0F)
												bool_48_ = true;
										}
										else
											bool_48_ = true;
									}
									else
										bool_48_ = true;
									if (bool_48_)
									{
										Point3d point3d = tmpP3d;
										double d = (double)w.curr().x();
										double d_49_ = (double)w.curr().y();
										Engine.land();
										point3d.set(d, d_49_, (double)Landscape.HQ(w.curr().x(), w.curr().y()));
										setGTargMode(tmpP3d, 800.0F);
										maneuver.target_ground = setGAttackObject(0);
										if (maneuver.target_ground != null)
										{
											setGroupTask(4);
											Voice.speakBeginGattack(airc[0]);
										}
									}
								}
							}
						}
						break;
					}
					case 0 :
					case 2 :
						if (grTask == 2)
						{
							if (enemyFighters)
							{
								int i = AirGroupList.length(enemies[0]);
								for (int i_50_ = 0; i_50_ < i; i_50_++)
								{
									AirGroup airgroup_51_ = AirGroupList.getGroup(enemies[0], i_50_);
									if (airgroup_51_.nOfAirc > 0 && (airgroup_51_.airc[0] instanceof TypeFighter))
									{
										targetGroup = airgroup_51_;
										setGroupTask(3);
										break;
									}
								}
							}
							if (w.Cur() >= w.size() - 1)
							{
								setGroupTask(1);
								setFormationAndScale((byte)0, 1.0F, true);
							}
							if (clientGroup == null || clientGroup.nOfAirc == 0 || clientGroup.w.Cur() >= clientGroup.w.size() - 1 || clientGroup.airc[0].FM.AP.way.isLanding())
							{
								maneuver.AP.way.next();
								w.setCur(maneuver.AP.way.Cur());
								for (int i = 1; i < nOfAirc; i++)
									((Maneuver)airc[i].FM).AP.way.setCur(w.Cur());
								setGroupTask(1);
								setFormationAndScale((byte)0, 1.0F, true);
							}
							switchWayPoint();
						}
						else if (grTask == 3)
						{
							switchWayPoint();
							WeWereInGAttack = true;
							if (AirGroupList.length(enemies[0]) != oldEnemyNum)
								setGroupTask(3);
							boolean bool = somebodyAttacks();
							if (!bool || aTargDestroyed)
							{
								setGroupTask(1);
								setFormationAndScale((byte)0, 1.0F, false);
								if (!bool)
									timeOutForTaskSwitch = 90;
								for (int i = 1; i < nOfAirc; i++)
								{
									Maneuver maneuver_52_ = (Maneuver)airc[i].FM;
									if (!maneuver_52_.isBusy())
									{
										maneuver_52_.push(57);
										maneuver_52_.pop();
									}
								}
							}
						}
						else if (grTask == 4)
						{
							WeWereInGAttack = true;
							boolean bool = somebodyGAttacks();
							boolean bool_53_ = false;
							for (int i = 0; i < nOfAirc; i++)
							{
								Maneuver maneuver_54_ = (Maneuver)airc[i].FM;
								if (maneuver_54_.gattackCounter >= 7)
									bool_53_ = true;
							}
							if (!bool || bool_53_ || gTargDestroyed)
							{
								setGroupTask(1);
								setFormationAndScale((byte)0, 1.0F, true);
								for (int i = 1; i < nOfAirc; i++)
								{
									Maneuver maneuver_55_ = (Maneuver)airc[i].FM;
									maneuver_55_.push(57);
									maneuver_55_.pop();
								}
								if (bool_53_)
								{
									for (int i = 0; i < nOfAirc; i++)
										((Maneuver)airc[i].FM).gattackCounter = 0;
								}
							}
						}
						else if (grTask == 1)
						{
							if (WeWereInGAttack || gTargMode != 0)
							{
								WeWereInGAttack = false;
								gTargMode = 0;
								setFormationAndScale((byte)0, 1.0F, false);
								((Maneuver)airc[0].FM).WeWereInGAttack = true;
							}
							if (WeWereInAttack)
							{
								WeWereInAttack = false;
								setFormationAndScale((byte)0, 1.0F, false);
								((Maneuver)airc[0].FM).WeWereInAttack = true;
							}
							if (w.Cur() > 0 && grAttached == 0 && oldFType == 0)
							{
								w.curr().getP(tmpP);
								tmpV.sub(tmpP, Pos);
								if (tmpV.lengthSquared() < 4000000.0)
									setFormationAndScale((byte)0, 2.5F, true);
								else
									setFormationAndScale((byte)0, 1.0F, true);
							}
							int i = w.Cur();
							w.next();
							if (w.curr().Action == 2 || (w.curr().Action == 3 && ((w.curr().getTarget() != null && !(airc[0] instanceof Scheme4)) || airc[0] instanceof TypeStormovik || airc[0] instanceof JU_87)))
							{
								w.curr().getP(tmpP);
								tmpV.sub(tmpP, Pos);
								float f = (float)tmpV.length();
								if (f < 20000.0F)
									setFormationAndScale((byte)5, 8.0F, true);
							}
							w.setCur(i);
							if (w.curr().getTarget() != null)
							{
								Actor actor = w.curr().getTargetActorRandom();
								if (actor != null && actor instanceof Aircraft)
								{
									tmpV.sub(((Aircraft)actor).FM.Loc, Pos);
									if (tmpV.lengthSquared() < 1.44E8)
									{
										if (actor.getArmy() == airc[0].getArmy())
										{
											if (airc[0] instanceof TypeFighter && !maneuver.hasBombs() && !maneuver.hasRockets())
											{
												if (w.Cur() < w.size() - 2)
												{
													clientGroup = (((Maneuver)((Aircraft)actor).FM).Group);
													setGroupTask(2);
													setFormationAndScale((byte)0, 2.5F, true);
												}
											}
											else
												attachGroup(((Maneuver)((Aircraft)actor).FM).Group);
										}
										else if (airc[0] instanceof TypeFighter || (airc[0] instanceof TypeStormovik))
										{
											targetGroup = ((Maneuver)((Aircraft)actor).FM).Group;
											setGroupTask(3);
										}
									}
								}
							}
							else if (AirGroupList.length(enemies[0]) > 0)
							{
								boolean bool = false;
								if (airc[0] instanceof TypeStormovik)
								{
									int i_56_ = AirGroupList.length(enemies[0]);
									for (int i_57_ = 0; i_57_ < i_56_; i_57_++)
									{
										AirGroup airgroup_58_ = AirGroupList.getGroup(enemies[0], i_57_);
										if (airgroup_58_ != null && airgroup_58_.nOfAirc != 0 && !(airgroup_58_.airc[0] instanceof TypeFighter))
										{
											bool = true;
											targetGroup = airgroup_58_;
											break;
										}
									}
									if (bool)
									{
										if (maneuver.hasBombs())
											bool = false;
										i = w.Cur();
										while (bool && w.Cur() < w.size() - 1)
										{
											if (w.curr().Action == 3)
												bool = false;
											w.next();
										}
										w.setCur(i);
									}
								}
								if (!bool && airc[0] instanceof TypeFighter)
								{
									for (int i_59_ = 0; i_59_ < nOfAirc; i_59_++)
									{
										if (((Maneuver)airc[i_59_].FM).canAttack())
											bool = true;
									}
									if (bool && maneuver.CT.getWeaponMass() > 220.0F)
										bool = false;
								}
								if (bool)
									setGroupTask(3);
							}
							if (rejoinGroup != null)
								rejoinToGroup(rejoinGroup);
						}
						break;
					default :
						grTask = 1;
				}
			}
			oldEnemyNum = AirGroupList.length(enemies[0]);
			if (timeOutForTaskSwitch > 0)
				timeOutForTaskSwitch--;
		}
	}
	
	//TODO: |ZUTI| variables
	//--------------------------------------
	public boolean zutiIsDogfightGroup = false;
	//--------------------------------------
}