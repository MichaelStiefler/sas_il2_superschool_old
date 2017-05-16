
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeLaserDesignator;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb, Ballistics

public class BombUSLGB_PavewayII_Generic_gn16 extends Bomb
{

    public BombUSLGB_PavewayII_Generic_gn16()
    {
        popped = false;
        laseron = false;
        bOnceCatchLaser = false;
        t1 = -1L;
        meshopen = (String) null;
    }

    public void start()
    {
        super.start();
        drawing(true);
        t1 = Time.current() + 1000L;
    }

    public void interpolateTick()
    {
        checklaser();
        if(!popped && Time.current() > t1)
        {
            if(meshopen != null)
                setMesh(meshopen);
            popped = true;
        }
        if(Time.current() > t1 + 1000L)
        {
            float tlfs = Time.tickLenFs();
            float speedms = (float)getSpeed(null);
            speedms += (200F - speedms) * 0.1F * tlfs;
            super.pos.getAbs(p, or);
            v.set(1.0D, 0.0D, 0.0D);
            or.transform(v);
            v.scale(speedms);
            if(laseron)
            {
                bOnceCatchLaser = true;
                setSpeed(v);
                p.x += ((Tuple3d) (v)).x * (double)tlfs;
                p.y += ((Tuple3d) (v)).y * (double)tlfs;
                p.z += ((Tuple3d) (v)).z * (double)tlfs - 0.011D;
// commented out by western0221; what necessity ?
//                    if(isNet() && isNetMirror())
//                        super.pos.setAbs(p, or);
                pT.sub(p);
                or.transformInv(pT);
                float f2 = 0.05F;
                if(p.distance(pT) > 0.0D)
                {
                    if(((Tuple3d) (pT)).y > 0.10000000000000001D)
                        deltaAzimuth = -f2;
                    if(((Tuple3d) (pT)).y < -0.10000000000000001D)
                        deltaAzimuth = f2;
                    if(((Tuple3d) (pT)).z < -0.10000000000000001D)
                        deltaTangage = -f2;
                    if(((Tuple3d) (pT)).z > 0.10000000000000001D)
                        deltaTangage = f2;
                    or.increment(70F * f2 * deltaAzimuth, 70F * f2 * deltaTangage, 0.0F);
                    deltaAzimuth = deltaTangage = 0.0F;
                }
                super.pos.setAbs(p, or);
                updateSound();
            }
            else
            {
                if(bOnceCatchLaser)
                {
                    setSpeed(v);
                    p.x += ((Tuple3d) (v)).x * (double)tlfs;
                    p.y += ((Tuple3d) (v)).y * (double)tlfs;
                    p.z += ((Tuple3d) (v)).z * (double)tlfs - 0.011D;
                }
                super.curTm += Time.tickLenFs();
                Ballistics.updateBomb(this, super.M, super.S, super.J, super.DistFromCMtoStab);
                updateSound();
            }
        }
        else
        {
            super.curTm += Time.tickLenFs();
            Ballistics.updateBomb(this, super.M, super.S, super.J, super.DistFromCMtoStab);
            updateSound();
        }
    }

    private void checklaser()
    {
        laseron = false;
        double targetDistance = 0.0D;
        float targetAngle = 0.0F;
        float targetBait = 0.0F;
        float maxTargetBait = 0.0F;
        Aircraft aircraft = (Aircraft) getOwner();
        // superior the Laser spot of this Paveway's owner than others'
        while((aircraft instanceof TypeLaserDesignator) && ((TypeLaserDesignator) aircraft).getLaserOn())
        {
            Point3d point3d = new Point3d();
            point3d = ((TypeLaserDesignator)aircraft).getLaserSpot();
            if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, aircraft.pos.getAbsPoint()) < 1.0F)
                break;
            targetDistance = aircraft.pos.getAbsPoint().distance(point3d);
            if (targetDistance > maxDistance)
                break;
            targetAngle = angleBetween(aircraft, point3d);
            if (targetAngle > maxFOVfrom)
                break;

            pT.set(point3d);
            laseron = true;
            break;
        }
        // seak other Laser designator spots when Paveway's owner doesn't spot Laser
        if(!laseron)
        {
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                if((actor instanceof TypeLaserDesignator) && ((TypeLaserDesignator) actor).getLaserOn() && actor.getArmy() == aircraft.getArmy())
                {
                    Point3d point3d = new Point3d();
                    point3d = ((TypeLaserDesignator)actor).getLaserSpot();
                    // Not target about objects behind of clouds from the Paveway's seaker.
                    if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, aircraft.pos.getAbsPoint()) < 1.0F)
                        continue;
                    targetDistance = aircraft.pos.getAbsPoint().distance(point3d);
                    if (targetDistance > maxDistance)
                        continue;
                    targetAngle = angleBetween(aircraft, point3d);
                    if (targetAngle > maxFOVfrom)
                        continue;

                    targetBait = 1 / targetAngle / (float) (targetDistance * targetDistance);
                    if (targetBait <= maxTargetBait)
                        continue;

                    maxTargetBait = targetBait;
                    pT.set(point3d);
                    laseron = true;
                }
            }
        }
    }

    public static float angleBetween(Actor actorFrom, Point3d pointTo) {
        float angleRetVal = 180.1F;
        double angleDoubleTemp = 0.0D;
        Loc angleActorLoc = new Loc();
        Point3d angleActorPos = new Point3d();
        Vector3d angleTargRayDir = new Vector3d();
        Vector3d angleNoseDir = new Vector3d();
        actorFrom.pos.getAbs(angleActorLoc);
        angleActorLoc.get(angleActorPos);
        angleTargRayDir.sub(pointTo, angleActorPos);
        angleDoubleTemp = angleTargRayDir.length();
        angleTargRayDir.scale(1.0D / angleDoubleTemp);
        angleNoseDir.set(1.0D, 0.0D, 0.0D);
        angleActorLoc.transform(angleNoseDir);
        angleDoubleTemp = angleNoseDir.dot(angleTargRayDir);
        angleRetVal = Geom.RAD2DEG((float) Math.acos(angleDoubleTemp));
        return angleRetVal;
    }

    private Orient or = new Orient();
    private Point3d p = new Point3d();
    private Point3d pT = new Point3d();
    private Vector3d v = new Vector3d();
    private float deltaAzimuth;
    private float deltaTangage;
    private float deltaX;
    private long t1;
    private boolean popped;
    private boolean laseron;
    private boolean bOnceCatchLaser;
    public String meshopen;
    private static float maxFOVfrom = 45.0F;
    private static double maxDistance = 20000D;

}
