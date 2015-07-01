/*4.10.1 class*/
package com.maddox.il2.ai;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;

public class Way
{
	private ArrayList WList;
	private int Cur;
	private boolean landing;
	private boolean landingOnShip;
	public Airport landingAirport;
	public Airport takeoffAirport;
	private double prevdist2;
	private double prevdistToNextWP2;
	private double curDist;
	private static Vector3d V = new Vector3d();
	
	//TODO: Modified by |ZUTI|: Changed from private to public
	public static Point3d P = new Point3d();
	
	private static Point3d tmpP = new Point3d();
	private static WayPoint defaultWP = new WayPoint();

	public int Cur()
	{
		return Cur;
	}

	public Way()
	{
		WList = new ArrayList();
		prevdist2 = 1.0E8;
		prevdistToNextWP2 = 3.4028234663852886E38;
		WList.clear();
		Cur = 0;
		landing = false;
		landingOnShip = false;
		landingAirport = null;
		takeoffAirport = null;
	}

	public Way(Way way_0_)
	{
		WList = new ArrayList();
		prevdist2 = 1.0E8;
		prevdistToNextWP2 = 3.4028234663852886E38;
		set(way_0_);
	}

	public void set(Way way_1_)
	{
		WList.clear();
		Cur = 0;
		for (int i = 0; i < way_1_.WList.size(); i++)
		{
			WayPoint waypoint = new WayPoint();
			waypoint.set(way_1_.get(i));
			WList.add(waypoint);
			if (waypoint.Action == 2 && waypoint.sTarget != null) landingOnShip = true;
		}
		landing = way_1_.landing;
		landingAirport = way_1_.landingAirport;
		if (takeoffAirport == null) takeoffAirport = way_1_.takeoffAirport;
	}

	public WayPoint first()
	{
		Cur = 0;
		return curr();
	}

	public WayPoint last()
	{
		Cur = Math.max(0, WList.size() - 1);
		return curr();
	}

	public WayPoint next()
	{
		int i = WList.size();
		Cur++;
		if (Cur >= i)
		{
			Cur = Math.max(0, i - 1);
			WayPoint waypoint = curr();
			return waypoint;
		}
		return curr();
	}

	public WayPoint look_at_point(int i)
	{
		int i_2_ = WList.size();
		if (i_2_ == 0) return defaultWP;
		if (i < 0) i = 0;
		if (i > i_2_ - 1) i = i_2_ - 1;
		return (WayPoint) WList.get(i);
	}

	public void setCur(int i)
	{
		if (i < WList.size() && i >= 0) Cur = i;
	}

	public WayPoint prev()
	{
		Cur--;
		if (Cur < 0) Cur = 0;
		return curr();
	}

	public WayPoint curr()
	{
		if (WList.size() == 0) return defaultWP;
		return (WayPoint) WList.get(Cur);
	}

	public WayPoint auto(Point3d point3d)
	{
		if (Cur == 0 || isReached(point3d)) return next();
		return curr();
	}

	public double getCurDist()
	{
		return Math.sqrt(curDist);
	}

	public boolean isReached(Point3d point3d)
	{
		curr().getP(P);
		V.sub(point3d, P);
		if (curr().timeout == -1 && !isLast())
		{
			((WayPoint) WList.get(Cur + 1)).getP(tmpP);
			V.sub(point3d, tmpP);
			curDist = V.x * V.x + V.y * V.y;
			if (curDist < 1.0E8 && curDist > prevdistToNextWP2)
			{
				curr().setTimeout(0);
				prevdistToNextWP2 = 3.4028234663852886E38;
				return true;
			}
			prevdistToNextWP2 = curDist;
		}
		else
		{
			curDist = V.x * V.x + V.y * V.y;
			if (curDist < 1000000.0 && curDist > prevdist2)
			{
				prevdist2 = 1.0E8;
				return true;
			}
			prevdist2 = curDist;
		}
		return false;
	}

	public boolean isLanding()
	{
		return landing;
	}

	public boolean isLandingOnShip()
	{
		return landingOnShip;
	}

	public boolean isLast()
	{
		return Cur == WList.size() - 1;
	}

	public void setLanding(boolean bool)
	{
		landing = bool;
	}

	public void add(WayPoint waypoint)
	{
		WList.add(waypoint);
		if (waypoint.Action == 2 && waypoint.sTarget != null) landingOnShip = true;
	}

	public WayPoint get(int i)
	{
		if (i < 0 || i >= WList.size()) return null;
		return (WayPoint) WList.get(i);
	}

	public void insert(int i, WayPoint waypoint)
	{
		if (i < 0)
			i = 0;
		else if (i > WList.size())
		{
			add(waypoint);
			return;
		}
		WList.add(i, waypoint);
	}

	public int size()
	{
		return WList.size();
	}

	public void load(SectFile sectfile, String string) throws Exception
	{
		int i = sectfile.sectionIndex(string);
		int i_3_ = sectfile.vars(i);
		for (int i_4_ = 0; i_4_ < i_3_; i_4_++)
		{
			String string_5_ = sectfile.var(i, i_4_);
			WayPoint waypoint = new WayPoint();
			if (string_5_.equalsIgnoreCase("TAKEOFF"))
				waypoint.Action = 1;
			else if (string_5_.equalsIgnoreCase("LANDING"))
				waypoint.Action = 2;
			else if (string_5_.equalsIgnoreCase("GATTACK"))
				waypoint.Action = 3;
			else
				waypoint.Action = 0;
			NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.value(i, i_4_));
			P.x = (double) numbertokenizer.next(0.0F, -1000000.0F, 1000000.0F);
			P.y = (double) numbertokenizer.next(0.0F, -1000000.0F, 1000000.0F);
			P.z = ((double) numbertokenizer.next(0.0F, -1000000.0F, 1000000.0F) + World.land().HQ(P.x, P.y));
			float f = numbertokenizer.next(0.0F, 0.0F, 2800.0F);
			waypoint.set(P);
			waypoint.set(f / 3.6F);
			String string_6_ = numbertokenizer.next((String) null);
			if (string_6_ != null)
			{
				if (string_6_.equals("&0"))
				{
					waypoint.bRadioSilence = false;
					string_6_ = null;
				}
				else if (string_6_.equals("&1"))
				{
					waypoint.bRadioSilence = true;
					string_6_ = null;
				}
				else
				{
					numbertokenizer.next(0);
					String string_7_ = numbertokenizer.next((String) null);
					if (string_7_ != null && string_7_.equals("&1")) waypoint.bRadioSilence = true;
				}
			}
			if (string_6_ != null && string_6_.startsWith("Bridge")) string_6_ = " " + string_6_;
			waypoint.setTarget(string_6_);
			add(waypoint);
		}
	}
}