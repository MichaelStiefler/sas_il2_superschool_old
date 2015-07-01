/* 4.10.1 class */
package com.maddox.il2.ai;

import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.AirGroupList;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeTransport;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Time;

public class War
{
	public static final int			TICK_DIV4	= 4;
	public static final int			TICK_DIV8	= 8;
	public static final int			TICK_DIV16	= 16;
	public static final int			TICK_DIV32	= 32;
	public static final int			ARMY_NUM	= 2;
	public static AirGroupList[]	Groups		= new AirGroupList[2];
	private static int				curArmy		= 0;
	private static int				curGroup	= 0;
	private static Vector3d			tmpV		= new Vector3d();
	private static Vector3d			Ve			= new Vector3d();
	private static Vector3d			Vtarg		= new Vector3d();

	public static War cur()
	{
		return World.cur().war;
	}

	public War()
	{
		//TODO: Lutz mod
		ZutiSupportMethods_AI.bIniTakeOff = false;
	}

	public boolean isActive()
	{
		if (!Mission.isPlaying())
			return false;
		if (NetMissionTrack.isPlaying())
			return false;
		if (Mission.isSingle())
			return true;

		//TODO: Edit by |ZUTI|
		//if (Mission.isServer() && Mission.isCoop())
		if (Mission.isServer() && (Mission.isCoop() || Mission.isDogfight()))
			return true;

		return false;
	}

	public void onActorDied(Actor actor, Actor actor_0_)
	{
		if (isActive())
		{
			if (actor instanceof Aircraft && ((Aircraft) actor).FM instanceof Maneuver)
			{
				Maneuver maneuver = (Maneuver) ((Aircraft) actor).FM;
				if (maneuver.Group != null)
				{
					maneuver.Group.delAircraft((Aircraft) actor);
					maneuver.Group = null;
				}
			}
		}
	}

	public void missionLoaded()
	{
		/* empty */
	}

	public void resetGameCreate()
	{
		curArmy = 0;
		curGroup = 0;

		//TODO: Lutz Mod
		ZutiSupportMethods_AI.bIniTakeOff = false;
	}

	public void resetGameClear()
	{
		for (int i = 0; i < 2; i++)
		{
			while (Groups[i] != null)
			{
				//TODO: Edit by |ZUTI|
				//Pinpointed to here. When errors are caught, all is fine!
				try
				{
					Groups[i].G.release();
				}
				catch (Exception ex)
				{
				}
				try
				{
					AirGroupList.delAirGroup(Groups, i, Groups[i].G);
				}
				catch (Exception ex)
				{
				}
			}
		}

		//TODO: Lutz mod
		ZutiSupportMethods_AI.bIniTakeOff = false;
	}

	public void interpolateTick()
	{
		if (isActive())
		{
			try
			{
				if (Time.tickCounter() % 4 == 0)
				{
					checkCollisionForAircraft();
					if (Time.tickCounter() % 32 == 0)
					{
						checkGroupsContact();
						if (Time.tickCounter() % 64 == 0)
							delEmptyGroups();
					}
					upgradeGroups();
				}
				//TODO: Lutz mod
				//---------------------------------------------------
				else if (com.maddox.rts.Time.tickCounter() % 81 == 0)
					ZutiSupportMethods_AI.checkGroupTakeOff(Groups);
				//---------------------------------------------------
				
				formationUpdate();
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}
	}

	private void upgradeGroups()
	{
		int i = AirGroupList.length(Groups[curArmy]);
		if (i > curGroup)
			AirGroupList.getGroup(Groups[curArmy], curGroup).update();
		else
		{
			curArmy++;
			if (curArmy > 1)
				curArmy = 0;
			curGroup = 0;
			return;
		}
		curGroup++;
	}

	private void formationUpdate()
	{
		for (int i = 0; i < 2; i++)
		{
			if (Groups[i] != null)
			{
				int i_1_ = AirGroupList.length(Groups[i]);
				for (int i_2_ = 0; i_2_ < i_1_; i_2_++)
					AirGroupList.getGroup(Groups[i], i_2_).formationUpdate();
			}
		}
	}

	private void checkGroupsContact()
	{
		int i = AirGroupList.length(Groups[0]);
		int i_3_ = AirGroupList.length(Groups[1]);
		for (int i_4_ = 0; i_4_ < i; i_4_++)
		{
			AirGroup airgroup = AirGroupList.getGroup(Groups[0], i_4_);
			if (airgroup != null && airgroup.Pos != null)
			{
				for (int i_5_ = 0; i_5_ < i_3_; i_5_++)
				{
					AirGroup airgroup_6_ = AirGroupList.getGroup(Groups[1], i_5_);
					if (airgroup_6_ != null && airgroup_6_.Pos != null)
					{
						tmpV.sub(airgroup.Pos, airgroup_6_.Pos);
						if (tmpV.lengthSquared() < 4.0E8 && airgroup.groupsInContact(airgroup_6_))
						{
							if (!AirGroupList.groupInList(airgroup.enemies[0], airgroup_6_))
							{
								AirGroupList.addAirGroup(airgroup.enemies, 0, airgroup_6_);
								if (airgroup.airc[0] != null && airgroup_6_.airc[0] != null)
									Voice.speakEnemyDetected(airgroup.airc[0], airgroup_6_.airc[0]);
								airgroup.setEnemyFighters();
							}
							if (!AirGroupList.groupInList(airgroup_6_.enemies[0], airgroup))
							{
								AirGroupList.addAirGroup(airgroup_6_.enemies, 0, airgroup);
								if (airgroup.airc[0] != null && airgroup_6_.airc[0] != null)
									Voice.speakEnemyDetected(airgroup_6_.airc[0], airgroup.airc[0]);
								airgroup_6_.setEnemyFighters();
							}
						}
						else
						{
							if (AirGroupList.groupInList(airgroup.enemies[0], airgroup_6_))
							{
								AirGroupList.delAirGroup(airgroup.enemies, 0, airgroup_6_);
								airgroup.setEnemyFighters();
							}
							if (AirGroupList.groupInList(airgroup_6_.enemies[0], airgroup))
							{
								AirGroupList.delAirGroup(airgroup_6_.enemies, 0, airgroup);
								airgroup_6_.setEnemyFighters();
							}
						}
					}
				}
			}
		}
	}

	private void delEmptyGroups()
	{
		int i = AirGroupList.length(Groups[0]);
		int i_7_ = AirGroupList.length(Groups[1]);
		for (int i_8_ = 0; i_8_ < i; i_8_++)
		{
			AirGroup airgroup = AirGroupList.getGroup(Groups[0], i_8_);
			if (airgroup != null && airgroup.nOfAirc == 0)
			{
				airgroup.release();
				AirGroupList.delAirGroup(Groups, 0, airgroup);
				//System.out.println("AirGroup released 0");
			}
		}
		for (int i_9_ = 0; i_9_ < i_7_; i_9_++)
		{
			AirGroup airgroup = AirGroupList.getGroup(Groups[1], i_9_);
			if (airgroup != null && airgroup.nOfAirc == 0)
			{
				airgroup.release();
				AirGroupList.delAirGroup(Groups, 1, airgroup);
				//System.out.println("AirGroup released 1");
			}
		}
	}

	private void checkCollisionForAircraft()
	{
		List list = Engine.targets();
		int i = list.size();
		for (int i_10_ = 0; i_10_ < i; i_10_++)
		{
			Actor actor = (Actor) list.get(i_10_);
			if (actor instanceof Aircraft)
			{
				FlightModel flightmodel = ((Aircraft) actor).FM;
				for (int i_11_ = i_10_ + 1; i_11_ < i; i_11_++)
				{
					Actor actor_12_ = (Actor) list.get(i_11_);
					if (i_10_ != i_11_ && actor_12_ instanceof Aircraft)
					{
						FlightModel flightmodel_13_ = ((Aircraft) actor_12_).FM;
						if (flightmodel instanceof Pilot && flightmodel_13_ instanceof Pilot)
						{
							float f = (float) (flightmodel.Loc.distanceSquared(flightmodel_13_.Loc));
							if (!(f > 1.0E7F))
							{
								if (flightmodel.actor.getArmy() != flightmodel_13_.actor.getArmy())
								{
									if (flightmodel instanceof RealFlightModel)
										testAsDanger(flightmodel, flightmodel_13_);
									if (flightmodel_13_ instanceof RealFlightModel)
										testAsDanger(flightmodel_13_, flightmodel);
								}
								Ve.sub(flightmodel.Loc, flightmodel_13_.Loc);
								float f_14_ = (float) Ve.length();
								Ve.normalize();
								if (flightmodel.actor.getArmy() == flightmodel_13_.actor.getArmy())
								{
									tmpV.set(Ve);
									flightmodel_13_.Or.transformInv(tmpV);
									if (tmpV.x > 0.0 && tmpV.y > -0.1 && tmpV.y < 0.1 && tmpV.z > -0.1 && tmpV.z < 0.1)
										((Maneuver) flightmodel_13_).setShotAtFriend(f_14_);
									tmpV.set(Ve);
									tmpV.scale(-1.0);
									flightmodel.Or.transformInv(tmpV);
									if (tmpV.x > 0.0 && tmpV.y > -0.1 && tmpV.y < 0.1 && tmpV.z > -0.1 && tmpV.z < 0.1)
										((Maneuver) flightmodel).setShotAtFriend(f_14_);
								}
								if (!(f > 20000.0F))
								{
									float f_15_ = ((flightmodel.actor.collisionR() + flightmodel_13_.actor.collisionR()) * 1.5F);
									f_14_ -= f_15_;
									Vtarg.sub(flightmodel_13_.Vwld, flightmodel.Vwld);
									Vtarg.scale(1.5);
									float f_16_ = (float) Vtarg.length();
									if (!(f_16_ < f_14_))
									{
										Vtarg.normalize();
										Vtarg.scale((double) f_14_);
										Ve.scale(Vtarg.dot(Ve));
										Vtarg.sub(Ve);
										if (Vtarg.length() < (double) f_15_ || f_14_ < 0.0F)
										{
											if (((Aircraft) actor).FM instanceof Pilot)
												((Maneuver) ((Aircraft) actor).FM).setStrikeEmer(flightmodel_13_);
											if (((Aircraft) actor_12_).FM instanceof Pilot)
												((Maneuver) ((Aircraft) actor_12_).FM).setStrikeEmer(flightmodel);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public static void testAsDanger(FlightModel flightmodel, FlightModel flightmodel_17_)
	{
		if (!(flightmodel.actor instanceof TypeTransport))
		{
			Ve.sub(flightmodel_17_.Loc, flightmodel.Loc);
			flightmodel.Or.transformInv(Ve);
			if (Ve.x > 0.0)
			{
				float f = (float) Ve.length();
				Ve.normalize();
				((Maneuver) flightmodel_17_).incDangerAggressiveness(4, (float) Ve.x, f, flightmodel);
			}
		}
	}

	public static Aircraft getNearestFriend(Aircraft aircraft)
	{
		return getNearestFriend(aircraft, 10000.0F);
	}

	public static Aircraft getNearestFriend(Aircraft aircraft, float f)
	{
		Point3d point3d = aircraft.pos.getAbsPoint();
		double d = (double) (f * f);
		int i = aircraft.getArmy();
		Aircraft aircraft_18_ = null;
		List list = Engine.targets();
		int i_19_ = list.size();
		for (int i_20_ = 0; i_20_ < i_19_; i_20_++)
		{
			Actor actor = (Actor) list.get(i_20_);
			if (actor instanceof Aircraft && actor != aircraft && actor.getArmy() == i)
			{
				Point3d point3d_21_ = actor.pos.getAbsPoint();
				double d_22_ = ((point3d.x - point3d_21_.x) * (point3d.x - point3d_21_.x) + (point3d.y - point3d_21_.y) * (point3d.y - point3d_21_.y) + (point3d.z - point3d_21_.z)
						* (point3d.z - point3d_21_.z));
				if (d_22_ < d)
				{
					aircraft_18_ = (Aircraft) actor;
					d = d_22_;
				}
			}
		}
		return aircraft_18_;
	}

	public static Aircraft getNearestFriendAtPoint(Point3d point3d, Aircraft aircraft, float f)
	{
		double d = (double) (f * f);
		int i = aircraft.getArmy();
		Aircraft aircraft_23_ = null;
		List list = Engine.targets();
		int i_24_ = list.size();
		for (int i_25_ = 0; i_25_ < i_24_; i_25_++)
		{
			Actor actor = (Actor) list.get(i_25_);
			if (actor instanceof Aircraft && actor.getArmy() == i)
			{
				Point3d point3d_26_ = actor.pos.getAbsPoint();
				double d_27_ = ((point3d.x - point3d_26_.x) * (point3d.x - point3d_26_.x) + (point3d.y - point3d_26_.y) * (point3d.y - point3d_26_.y) + (point3d.z - point3d_26_.z)
						* (point3d.z - point3d_26_.z));
				if (d_27_ < d)
				{
					aircraft_23_ = (Aircraft) actor;
					d = d_27_;
				}
			}
		}
		return aircraft_23_;
	}

	public static Aircraft getNearestFriendlyFighter(Aircraft aircraft, float f)
	{
		double d = (double) (f * f);
		Point3d point3d = aircraft.pos.getAbsPoint();
		int i = aircraft.getArmy();
		Aircraft aircraft_28_ = null;
		List list = Engine.targets();
		int i_29_ = list.size();
		for (int i_30_ = 0; i_30_ < i_29_; i_30_++)
		{
			Actor actor = (Actor) list.get(i_30_);
			if (actor instanceof Aircraft)
			{
				Aircraft aircraft_31_ = (Aircraft) actor;
				if (aircraft_31_ != aircraft && aircraft_31_.getArmy() == i && aircraft_31_.getWing() != aircraft.getWing() && aircraft_31_ instanceof TypeFighter)
				{
					Point3d point3d_32_ = aircraft_31_.pos.getAbsPoint();
					double d_33_ = ((point3d.x - point3d_32_.x) * (point3d.x - point3d_32_.x) + (point3d.y - point3d_32_.y) * (point3d.y - point3d_32_.y) + (point3d.z - point3d_32_.z)
							* (point3d.z - point3d_32_.z));
					if (d_33_ < d)
					{
						aircraft_28_ = aircraft_31_;
						d = d_33_;
					}
				}
			}
		}
		return aircraft_28_;
	}

	public static Aircraft getNearestEnemy(Aircraft aircraft, float f)
	{
		double d = (double) (f * f);
		Point3d point3d = aircraft.pos.getAbsPoint();
		int i = aircraft.getArmy();
		Aircraft aircraft_34_ = null;
		List list = Engine.targets();
		int i_35_ = list.size();
		for (int i_36_ = 0; i_36_ < i_35_; i_36_++)
		{
			Actor actor = (Actor) list.get(i_36_);
			if (actor instanceof Aircraft && actor.getArmy() != i)
			{
				Point3d point3d_37_ = actor.pos.getAbsPoint();
				double d_38_ = ((point3d.x - point3d_37_.x) * (point3d.x - point3d_37_.x) + (point3d.y - point3d_37_.y) * (point3d.y - point3d_37_.y) + (point3d.z - point3d_37_.z)
						* (point3d.z - point3d_37_.z));
				if (d_38_ < d)
				{
					aircraft_34_ = (Aircraft) actor;
					d = d_38_;
				}
			}
		}
		return aircraft_34_;
	}

	public static Actor GetNearestEnemy(Actor actor, int i, float f)
	{
		return NearestTargets.getEnemy(0, i, actor.pos.getAbsPoint(), (double) f, actor.getArmy());
	}

	public static Actor GetNearestEnemy(Actor actor, int i, float f, int i_39_)
	{
		return NearestTargets.getEnemy(i_39_, i, actor.pos.getAbsPoint(), (double) f, actor.getArmy());
	}

	public static Actor GetNearestEnemy(Actor actor, int i, float f, Point3d point3d)
	{
		return NearestTargets.getEnemy(0, i, point3d, (double) f, actor.getArmy());
	}

	public static Actor GetNearestEnemy(Actor actor, int i, Point3d point3d, float f)
	{
		return NearestTargets.getEnemy(i, 16, point3d, (double) f, actor.getArmy());
	}

	public static Actor GetNearestEnemy(Actor actor, Point3d point3d, float f)
	{
		return NearestTargets.getEnemy(0, 16, point3d, (double) f, actor.getArmy());
	}

	public static Actor GetNearestFromChief(Actor actor, Actor actor_40_)
	{
		if (!Actor.isAlive(actor_40_))
			return null;
		Actor actor_41_ = null;
		if (actor_40_ instanceof Chief || actor_40_ instanceof Bridge)
		{
			int i = actor_40_.getOwnerAttachedCount();
			if (i < 1)
				return null;
			actor_41_ = (Actor) actor_40_.getOwnerAttached(0);
			double d = actor.pos.getAbsPoint().distance(actor_41_.pos.getAbsPoint());
			for (int i_42_ = 1; i_42_ < i; i_42_++)
			{
				Actor actor_43_ = (Actor) actor_40_.getOwnerAttached(i_42_);
				double d_44_ = actor.pos.getAbsPoint().distance(actor_43_.pos.getAbsPoint());
				if (d_44_ < d)
				{
					d_44_ = d;
					actor_41_ = actor_43_;
				}
			}
		}
		return actor_41_;
	}

	public static Actor GetRandomFromChief(Actor actor, Actor actor_45_)
	{
		if (!Actor.isAlive(actor_45_))
			return null;
		if (actor_45_ instanceof Chief || actor_45_ instanceof Bridge)
		{
			int i = actor_45_.getOwnerAttachedCount();
			if (i < 1)
				return null;
			for (int i_46_ = 0; i_46_ < i; i_46_++)
			{
				Actor actor_47_ = ((Actor) actor_45_.getOwnerAttached(World.Rnd().nextInt(0, i - 1)));
				if (Actor.isValid(actor_47_) && actor_47_.isAlive())
					return actor_47_;
			}
			for (int i_48_ = 0; i_48_ < i; i_48_++)
			{
				Actor actor_49_ = (Actor) actor_45_.getOwnerAttached(i_48_);
				if (Actor.isValid(actor_49_) && actor_49_.isAlive())
					return actor_49_;
			}
		}
		return actor_45_;
	}

	public static Aircraft GetNearestEnemyAircraft(Actor actor, float f, int i)
	{

		Actor actor_50_ = GetNearestEnemy(actor, -1, f, i);
		if (actor_50_ != null)
			return (Aircraft) actor_50_;
		actor_50_ = GetNearestEnemy(actor, -1, f, 9);
		return (Aircraft) actor_50_;
	}
}