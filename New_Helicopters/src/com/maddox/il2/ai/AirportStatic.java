package com.maddox.il2.ai;

import com.maddox.JGP.Point2f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.ActorPosStatic;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.il2.objects.air.TypeHelicopter;
import com.maddox.rts.Time;

import java.util.ArrayList;

public abstract class AirportStatic
  extends Airport
{
  private ArrayList runway;
  public static final int PT_RUNWAY = 1;
  public static final int PT_TAXI = 2;
  public static final int PT_STAY = 4;
  
  private static class Runway
  {
    public Loc loc;
    public float[] planeShift;
    public Aircraft[] planes;
    public int curPlaneShift;
    public int oldTickCounter;
    
    public Runway(Loc paramLoc)
    {
      this.loc = new Loc();
      this.planeShift = new float[32];
      this.planes = new Aircraft[32];
      this.curPlaneShift = 0;
      this.oldTickCounter = 0;
      this.loc.set(paramLoc.getX(), paramLoc.getY(), World.land().HQ(paramLoc.getX(), paramLoc.getY()), paramLoc.getAzimut(), 0.0F, 0.0F);
    }
  }
  
  public AirportStatic()
  {
    this.runway = new ArrayList();
  }
  
  public static void make(ArrayList paramArrayList, Point2f[][] paramArrayOfPoint2f1, Point2f[][] paramArrayOfPoint2f2, Point2f[][] paramArrayOfPoint2f3)
  {
    if (paramArrayList == null) {
      return;
    }
    ArrayList localArrayList = new ArrayList();
    while (paramArrayList.size() > 0)
    {
      Loc localLoc = (Loc)paramArrayList.remove(0);
      int i = 0;
      Object localObject = null;
      int j = 0;
      while (j < localArrayList.size())
      {
        localObject = (AirportStatic)localArrayList.get(j);
        if (((AirportStatic)localObject).oppositeRunway(localLoc) != null)
        {
          i = 1;
          break;
        }
        j++;
      }
      if (i != 0)
      {
        ((AirportStatic)localObject).runway.add(new Runway(localLoc));
        int k = ((AirportStatic)localObject).runway.size();
        p3d.set(0.0D, 0.0D, 0.0D);
        for (int m = 0; m < k; m++)
        {
          localLoc = ((Runway)((AirportStatic)localObject).runway.get(m)).loc;
          p3d.x += localLoc.getPoint().x;
          p3d.y += localLoc.getPoint().y;
          p3d.z += localLoc.getPoint().z;
        }
        p3d.x /= k;
        p3d.y /= k;
        p3d.z /= k;
        ((AirportStatic)localObject).pos.setAbs(p3d);
      }
      else
      {
        if (Engine.cur.land.isWater(localLoc.getPoint().x, localLoc.getPoint().y)) {
          localObject = new AirportMaritime();
        } else {
          localObject = new AirportGround();
        }
        ((AirportStatic)localObject).pos = new ActorPosStatic((Actor)localObject, localLoc);
        ((AirportStatic)localObject).runway.add(new Runway(localLoc));
        localArrayList.add(localObject);
      }
    }
  }
  
  public boolean landWay(FlightModel paramFlightModel)
  {
    paramFlightModel.AP.way.curr().getP(pWay);
    Runway localRunway = nearestRunway(pWay);
    if (localRunway == null) {
      return false;
    }
    Way localWay1 = new Way();
    
    Way localWay2 = new Way();
    float f1 = (float)Engine.land().HQ_Air(localRunway.loc.getX(), localRunway.loc.getY());
    float f2 = paramFlightModel.M.massEmpty / 3000.0F;
    if (f2 > 1.0F) {
      f2 = 1.0F;
    }
    if (paramFlightModel.EI.engines[0].getType() > 1) {
      f2 = 1.0F;
    }
    if (paramFlightModel.EI.engines[0].getType() == 2) {
      f2 = 1.25F;
    } else if (paramFlightModel.EI.engines[0].getType() == 3) {
      f2 = 1.5F;
    }
    float f3 = f2;
    if ((paramFlightModel.actor instanceof TypeHelicopter)) {
    	f2 = 0.4F;
    	f3 = 0.4F;
    }
    for (int i = x.length - 1; i >= 0; i--)
    {
      WayPoint localWayPoint1 = new WayPoint();
      WayPoint localWayPoint2 = new WayPoint();
      int j = paramFlightModel.AP.way.curr().waypointType - 101 + 1;
      if (j < 0) {
        j = 0;
      }
      int k;
      switch (j)
      {
      case 0: 
      default: 
        if ((paramFlightModel.actor instanceof TypeFastJet)) {
          pd.set(xFJ[i], yFJ[i], zFJ[i]);
        } else {
          pd.set(x[i] * f2, y[i] * f2, z[i] * f3);
        }
        pdRs.set(pd);
        break;
      case 1: 
        if ((paramFlightModel.actor instanceof TypeFastJet)) {
          pd.set(xFJ[i], ryFJ[i], zFJ[i]);
        } else {
          pd.set(x[i] * f2, ry[i] * f2, z[i] * f3);
        }
        pdRs.set(pd);
        break;
      case 2: 
        if ((paramFlightModel.actor instanceof TypeFastJet))
        {
          if (paramFlightModel.AILandingWP360OverHeadApr == 1)
          {
            if (((paramFlightModel instanceof Maneuver)) && (((Maneuver)paramFlightModel).Group != null))
            {
              k = ((Maneuver)paramFlightModel).Group.numInGroup((Aircraft)paramFlightModel.actor) % 4;
              pd.set(ohaafxFJ[k][i], ohaafyFJ[i], ohaafzFJ[i]);
            }
            else
            {
              pd.set(ohaafxFJ[0][i], ohaafyFJ[i], ohaafzFJ[i]);
            }
            pdRs.set(ohaafxFJ[0][i], ohaafyFJ[i], ohaafzFJ[i]);
            
            paramFlightModel.bOnGoingOverHeadApproach = true;
          }
          else if (paramFlightModel.AILandingWP360OverHeadApr == 2)
          {
            if (((paramFlightModel instanceof Maneuver)) && (((Maneuver)paramFlightModel).Group != null))
            {
              k = ((Maneuver)paramFlightModel).Group.numInGroup((Aircraft)paramFlightModel.actor) % 4;
              pd.set(ohanvxFJ[k][i], ohanvyFJ[i], ohanvzFJ[i]);
            }
            else
            {
              pd.set(ohanvxFJ[0][i], ohanvyFJ[i], ohanvzFJ[i]);
            }
            pdRs.set(ohanvxFJ[0][i], ohanvyFJ[i], ohanvzFJ[i]);
            
            paramFlightModel.bOnGoingOverHeadApproach = true;
          }
          else
          {
            pd.set(xFJ[i], lsyFJ[i], zFJ[i]);
            pdRs.set(pd);
          }
        }
        else
        {
          pd.set(x[i] * f2, lsy[i] * f2, z[i] * f3);
          pdRs.set(pd);
        }
        break;
      case 3: 
        if ((paramFlightModel.actor instanceof TypeFastJet))
        {
          if (paramFlightModel.AILandingWP360OverHeadApr == 1)
          {
            if (((paramFlightModel instanceof Maneuver)) && (((Maneuver)paramFlightModel).Group != null))
            {
              k = ((Maneuver)paramFlightModel).Group.numInGroup((Aircraft)paramFlightModel.actor) % 4;
              pd.set(ohaafxFJ[k][i], ohaafryFJ[i], ohaafzFJ[i]);
            }
            else
            {
              pd.set(ohaafxFJ[0][i], ohaafryFJ[i], ohaafzFJ[i]);
            }
            pdRs.set(ohaafxFJ[0][i], ohaafryFJ[i], ohaafzFJ[i]);
            
            paramFlightModel.bOnGoingOverHeadApproach = true;
          }
          else if (paramFlightModel.AILandingWP360OverHeadApr == 2)
          {
            if (((paramFlightModel instanceof Maneuver)) && (((Maneuver)paramFlightModel).Group != null))
            {
              k = ((Maneuver)paramFlightModel).Group.numInGroup((Aircraft)paramFlightModel.actor) % 4;
              pd.set(ohanvxFJ[k][i], ohanvryFJ[i], ohanvzFJ[i]);
            }
            else
            {
              pd.set(ohanvxFJ[0][i], ohanvryFJ[i], ohanvzFJ[i]);
            }
            pdRs.set(ohanvxFJ[0][i], ohanvryFJ[i], ohanvzFJ[i]);
            
            paramFlightModel.bOnGoingOverHeadApproach = true;
          }
          else
          {
            pd.set(xFJ[i], rsyFJ[i], zFJ[i]);
            pdRs.set(pd);
          }
        }
        else
        {
          pd.set(x[i] * f2, rsy[i] * f2, z[i] * f3);
          pdRs.set(pd);
        }
        break;
      case 4: 
        if ((paramFlightModel.actor instanceof TypeFastJet))
        {
          pd.set(sixFJ[i], siyFJ[i], sizFJ[i]);
          pdRs.set(xFJ[i], yFJ[i], zFJ[i]);
        }
        else
        {
          pd.set(six[i] * f2, siy[i] * f2, siz[i] * f3);
          pdRs.set(x[i] * f2, y[i] * f2, z[i] * f3);
        }
        break;
      }
      if ((paramFlightModel.actor instanceof TypeFastJet))
      {
        float f4;
        if (i < 3)
        {
          if ((paramFlightModel.VminFLAPS < 50.04F) && (paramFlightModel.Vmin < 63.94F) && (paramFlightModel.Vlanding < 50.04F)) {
            f4 = paramFlightModel.VminFLAPS * 1.2F;
          } else {
            f4 = Math.max(vFJ[i] * 0.278F, paramFlightModel.VminFLAPS * 1.2F);
          }
        }
        else if (i == 3)
        {
          if ((paramFlightModel.VminFLAPS < 50.04F) && (paramFlightModel.Vmin < 63.94F) && (paramFlightModel.Vlanding < 50.04F)) {
            f4 = paramFlightModel.Vmin * 1.2F;
          } else {
            f4 = Math.max(vFJ[i] * 0.278F, paramFlightModel.Vmin * 1.2F);
          }
        }
        else {
          f4 = Math.max(vFJ[i] * 0.278F, paramFlightModel.Vmin * 1.2F);
        }
        if ((paramFlightModel.VminAI > 0.0F) && (f4 < paramFlightModel.VminAI)) {
          f4 = paramFlightModel.VminAI;
        }
        localWayPoint1.set(Math.min(f4, 120.0F));
        localWayPoint2.set(Math.min(f4, 120.0F));
      }
      else
      {
        localWayPoint1.set(Math.min(v[i] * 0.278F, paramFlightModel.Vmax * 0.7F));
        localWayPoint2.set(Math.min(v[i] * 0.278F, paramFlightModel.Vmax * 0.7F));
      }
      localWayPoint1.Action = 2;
      localRunway.loc.transform(pd);
      float f5 = (float)Engine.land().HQ_Air(pd.x, pd.y);
      pd.z -= f5 - f1;
      pdRs.z -= f5 - f1;
      pf.set(pd);
      pfRs.set(pdRs);
      localWayPoint1.set(pf);
      localWayPoint2.set(pf);
      localWay1.add(localWayPoint1);
      localWay2.add(localWayPoint2);
    }
    localWay1.setLanding(true);
    localWay2.setLanding(true);
    if (paramFlightModel.AP.way.curr().waypointType == 104) {
      localWay1.setLanding2(true);
    }
    paramFlightModel.AP.way = localWay1;
    paramFlightModel.APreserve.way = localWay2;
    return true;
  }
  
  private boolean setStationaryPlaneTakeoff(Aircraft paramAircraft, Runway paramRunway)
  {
    Actor localActor = Actor.getByName(paramAircraft.spawnActorName);
    if (!localActor.isAlive())
    {
      paramAircraft.destroy();
      return false;
    }
    Point3d localPoint3d = paramAircraft.spawnLocSingleCoop.getPoint();
    Orient localOrient = paramAircraft.spawnLocSingleCoop.getOrient();
    localPoint3d.z = (World.land().HQ(localPoint3d.x, localPoint3d.y) + paramAircraft.FM.Gears.H);
    Engine.land().N(localPoint3d.x, localPoint3d.y, v1);
    localOrient.orient(v1);
    localOrient.increment(0.0F, paramAircraft.FM.Gears.Pitch, 0.0F);
    paramAircraft.setOnGround(localPoint3d, localOrient, zeroSpeed);
    if ((paramAircraft.FM instanceof Maneuver))
    {
      ((Maneuver)paramAircraft.FM).direction = paramAircraft.pos.getAbsOrient().getAzimut();
      ((Maneuver)paramAircraft.FM).rwLoc = paramRunway.loc;
    }
    paramAircraft.FM.AP.way.takeoffAirport = this;
    if ((Actor.isValid(paramAircraft)) && ((paramAircraft.FM instanceof Maneuver)))
    {
      Maneuver localManeuver = (Maneuver)paramAircraft.FM;
      if ((localManeuver.Group != null) && (localManeuver.Group.w != null)) {
        localManeuver.Group.w.takeoffAirport = this;
      }
    }
    paramAircraft.spawnLocSingleCoop.set(paramAircraft.pos.getAbs());
    if ((!Mission.isCoop()) || (paramAircraft.getWing().bOnlyAI)) {
      localActor.destroy();
    }
    paramAircraft.stationarySpawnLocSet = true;
    return true;
  }
  
	public void setTakeoff(Point3d point3d, Aircraft aaircraft[]) {
		Runway runway1 = nearestRunway(point3d);
		if (runway1 == null) return;
		Runway runway2 = oppositeRunway(runway1.loc);
		double d = 1000D;
		if (runway2 != null) d = runway1.loc.getPoint().distance(runway2.loc.getPoint());
		if (Time.tickCounter() != runway1.oldTickCounter) {
			runway1.oldTickCounter = Time.tickCounter();
			runway1.curPlaneShift = 0;
		}
		for (int i = 0; i < aaircraft.length; i++) {
			if (!Actor.isValid(aaircraft[i])) continue;
			if (aaircraft[i].spawnLocSingleCoop != null) {
				boolean flag = setStationaryPlaneTakeoff(aaircraft[i], runway1);
				if (!flag || !Mission.isCoop() || aaircraft[i].getWing().bOnlyAI) continue;
			}
			float f = aaircraft[i].collisionR() * 2.0F + 20F;
			for (int j = runway1.curPlaneShift; j > 0; j--) {
				runway1.planeShift[j] = runway1.planeShift[j - 1] + f;
				runway1.planes[j] = runway1.planes[j - 1];
			}

			runway1.planeShift[0] = 0.0F;
			runway1.planes[0] = aaircraft[i];
			runway1.curPlaneShift++;
			if (runway1.curPlaneShift > 31) throw new RuntimeException("Too many planes on airdrome");
			for (int k = 0; k < runway1.curPlaneShift; k++) {
				if (!Actor.isValid(runway1.planes[k])) continue;
				tmpLoc.set((double) runway1.planeShift[k] - d, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
				tmpLoc.add(runway1.loc);
				Point3d point3d1 = tmpLoc.getPoint();
				Orient orient = tmpLoc.getOrient();
				point3d1.z = World.land().HQ(point3d1.x, point3d1.y) + (double) runway1.planes[k].FM.Gears.H;
				Engine.land().N(point3d1.x, point3d1.y, v1);
				orient.orient(v1);
				orient.increment(0.0F, runway1.planes[k].FM.Gears.Pitch, 0.0F);
				runway1.planes[k].setOnGround(point3d1, orient, zeroSpeed);
				if (runway1.planes[k].FM instanceof Maneuver) {
					((Maneuver) runway1.planes[k].FM).direction = runway1.planes[k].pos.getAbsOrient().getAzimut();
					((Maneuver) runway1.planes[k].FM).rwLoc = runway1.loc;
				}
				runway1.planes[k].FM.AP.way.takeoffAirport = this;
			}

		}

		if (Actor.isValid(aaircraft[0]) && (aaircraft[0].FM instanceof Maneuver)) {
			Maneuver maneuver = (Maneuver) aaircraft[0].FM;
			if (maneuver.Group != null && maneuver.Group.w != null) maneuver.Group.w.takeoffAirport = this;
		}
	}
  
  public void setAsHomeAirport(Aircraft[] paramArrayOfAircraft)
  {
    for (int i = 0; i < paramArrayOfAircraft.length; i++) {
      if (Actor.isValid(paramArrayOfAircraft[i])) {
        paramArrayOfAircraft[i].FM.AP.way.takeoffAirport = this;
      }
    }
  }
  
  public double shiftFromLine(FlightModel paramFlightModel)
  {
    tmpLoc.set(paramFlightModel.Loc);
    if ((paramFlightModel instanceof Maneuver))
    {
      Maneuver localManeuver = (Maneuver)paramFlightModel;
      if (localManeuver.rwLoc != null)
      {
        tmpLoc.sub(localManeuver.rwLoc);
        return tmpLoc.getY();
      }
    }
    return 0.0D;
  }
  
  public boolean nearestRunway(Point3d paramPoint3d, Loc paramLoc)
  {
    Runway localRunway = nearestRunway(paramPoint3d);
    if (localRunway != null)
    {
      paramLoc.set(localRunway.loc);
      return true;
    }
    return false;
  }
  
  private Runway nearestRunway(Point3d paramPoint3d)
  {
    Object localObject = null;
    double d1 = 0.0D;
    np.set(paramPoint3d);
    int i = this.runway.size();
    for (int j = 0; j < i; j++)
    {
      Runway localRunway = (Runway)this.runway.get(j);
      np.z = localRunway.loc.getPoint().z;
      double d2 = localRunway.loc.getPoint().distanceSquared(np);
      if ((localObject == null) || (d2 < d1))
      {
        localObject = localRunway;
        d1 = d2;
      }
    }
    if (d1 > 2.25E8D) {
      localObject = null;
    }
    return (Runway)localObject;
  }
  
  private Runway oppositeRunway(Loc paramLoc)
  {
    int i = this.runway.size();
    for (int j = 0; j < i; j++)
    {
      Runway localRunway = (Runway)this.runway.get(j);
      pcur.set(localRunway.loc.getPoint());
      paramLoc.transformInv(pcur);
      if ((Math.abs(pcur.y) < 15.0D) && (pcur.x < -800.0D) && (pcur.x > -2500.0D))
      {
        p1.set(1.0D, 0.0D, 0.0D);
        p2.set(1.0D, 0.0D, 0.0D);
        localRunway.loc.getOrient().transform(p1);
        paramLoc.getOrient().transform(p2);
        if (p1.dot(p2) < -0.9D) {
          return localRunway;
        }
      }
    }
    return null;
  }
  
  private static Point3d p3d = new Point3d();
  private static float[] x = { -500.0F, 0.0F, 220.0F, 2000.0F, 4000.0F, 5000.0F, 4000.0F, 0.0F, -2000.0F };
  private static float[] xFJ = { -1000.0F, -0.0F, 220.0F, 4000.0F, 8000.0F, 10000.0F, 8000.0F, 0.0F, -4000.0F };
  private static float[] y = { 0.0F, 0.0F, 0.0F, 0.0F, -500.0F, -2000.0F, -4000.0F, -4000.0F, -4000.0F };
  private static float[] yFJ = { 0.0F, 0.0F, 0.0F, 0.0F, -1000.0F, -4000.0F, -8000.0F, -8000.0F, -8000.0F };
  private static float[] z = { 0.0F, 6.0F, 20.0F, 160.0F, 500.0F, 600.0F, 700.0F, 700.0F, 700.0F };
  private static float[] zFJ = { 0.0F, 6.0F, 20.0F, 270.0F, 600.0F, 880.0F, 1100.0F, 1100.0F, 1100.0F };
  private static float[] v = { 0.0F, 180.0F, 220.0F, 240.0F, 270.0F, 280.0F, 300.0F, 300.0F, 300.0F };
  private static float[] vFJ = { 0.0F, 250.0F, 280.0F, 300.0F, 330.0F, 350.0F, 370.0F, 390.0F, 400.0F };
  private static float[] ry = { 0.0F, 0.0F, 0.0F, 0.0F, 500.0F, 2000.0F, 4000.0F, 4000.0F, 4000.0F };
  private static float[] ryFJ = { 0.0F, 0.0F, 0.0F, 0.0F, 1000.0F, 4000.0F, 8000.0F, 8000.0F, 8000.0F };
  private static float[] lsy = { 0.0F, 0.0F, 0.0F, 0.0F, -250.0F, -1000.0F, -2000.0F, -2000.0F, -2000.0F };
  private static float[] lsyFJ = { 0.0F, 0.0F, 0.0F, 0.0F, -500.0F, -2000.0F, -4000.0F, -4000.0F, -4000.0F };
  private static float[] rsy = { 0.0F, 0.0F, 0.0F, 0.0F, 250.0F, 1000.0F, 2000.0F, 2000.0F, 2000.0F };
  private static float[] rsyFJ = { 0.0F, 0.0F, 0.0F, 0.0F, 500.0F, 2000.0F, 4000.0F, 4000.0F, 4000.0F };
  private static float[] six = { -500.0F, 0.0F, 220.0F, 2000.0F, 3000.0F, 4000.0F, 5000.0F, 5500.0F, 6000.0F };
  private static float[] sixFJ = { -1000.0F, 0.0F, 220.0F, 4000.0F, 6000.0F, 8000.0F, 10000.0F, 11000.0F, 12000.0F };
  private static float[] siy = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };
  private static float[] siyFJ = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };
  private static float[] siz = { 0.0F, 6.0F, 20.0F, 160.0F, 500.0F, 600.0F, 700.0F, 700.0F, 700.0F };
  private static float[] sizFJ = { 0.0F, 6.0F, 20.0F, 270.0F, 380.0F, 500.0F, 590.0F, 610.0F, 630.0F };
  private static float[][] ohaafxFJ = { { -1000.0F, -10.0F, 212.0F, 2220.0F, 3500.0F, 3500.0F, 2000.0F, -2000.0F, -4000.0F }, { -1000.0F, -5.0F, 216.0F, 2220.0F, 3500.0F, 3500.0F, 2000.0F, -3000.0F, -5000.0F }, { -1000.0F, 0.0F, 220.0F, 2220.0F, 3500.0F, 3500.0F, 2000.0F, -4000.0F, -6000.0F }, { -1000.0F, 5.0F, 224.0F, 2220.0F, 3500.0F, 3500.0F, 2000.0F, -5000.0F, -7000.0F } };
  private static float[] ohaafyFJ = { 0.0F, 0.0F, 0.0F, 0.0F, -1230.0F, -2460.0F, -3700.0F, -3700.0F, -1000.0F };
  private static float[] ohaafryFJ = { 0.0F, 0.0F, 0.0F, 0.0F, 1230.0F, 2460.0F, 3700.0F, 3700.0F, 1000.0F };
  private static float[] ohaafzFJ = { 0.0F, 6.0F, 20.0F, 150.0F, 280.0F, 390.0F, 560.0F, 560.0F, 650.0F };
  private static float[][] ohanvxFJ = { { -1000.0F, -10.0F, 212.0F, 2200.0F, 3000.0F, 3000.0F, 2000.0F, -2000.0F, -4000.0F }, { -1000.0F, -5.0F, 216.0F, 2200.0F, 3000.0F, 3000.0F, 2000.0F, -3000.0F, -5000.0F }, { -1000.0F, 0.0F, 220.0F, 2200.0F, 3000.0F, 3000.0F, 2000.0F, -4000.0F, -6000.0F }, { -1000.0F, 5.0F, 224.0F, 2200.0F, 3000.0F, 3000.0F, 2000.0F, -5000.0F, -7000.0F } };
  private static float[] ohanvyFJ = { 0.0F, 0.0F, 0.0F, 0.0F, -800.0F, -1600.0F, -2500.0F, -2500.0F, -400.0F };
  private static float[] ohanvryFJ = { 0.0F, 0.0F, 0.0F, 0.0F, 800.0F, 1600.0F, 2500.0F, 2500.0F, 400.0F };
  private static float[] ohanvzFJ = { 0.0F, 6.0F, 20.0F, 150.0F, 180.0F, 220.0F, 260.0F, 260.0F, 300.0F };
  private static Point3d pWay = new Point3d();
  private static Point3d pd = new Point3d();
  private static Point3f pf = new Point3f();
  private static Point3d pdRs = new Point3d();
  private static Point3f pfRs = new Point3f();
  private static Vector3d v1 = new Vector3d();
  private static Vector3d zeroSpeed = new Vector3d();
  private static Loc tmpLoc = new Loc();
  private static Point3d pcur = new Point3d();
  private static Point3d np = new Point3d();
  private static Vector3d p1 = new Vector3d();
  private static Vector3d p2 = new Vector3d();
}
