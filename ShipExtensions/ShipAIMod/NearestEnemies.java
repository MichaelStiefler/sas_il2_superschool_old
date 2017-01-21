
package com.maddox.il2.ai.ground;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.il2.objects.ships.Ship;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.TransparentTestRunway;
import com.maddox.il2.objects.vehicles.planes.Plane;
import java.util.List;

// Referenced classes of package com.maddox.il2.ai.ground:
//            Prey

public class NearestEnemies
    implements Accumulator
{

    public NearestEnemies()
    {
    }

    public static NearestEnemies enemies()
    {
        return enemies;
    }

    public static void set(int i)
    {
        set(i, 0.0F, 0.0F);
        useSpeed = false;
        useHeight = false;
    }

    public static void set(int i, float f, float f1)
    {
        set(i, f, f1, 0.0F, 0.0F);
        useSpeed = true;
        useHeight = false;
    }

    public static void set(int i, float f, float f1, float f2, float f3)
    {
        enemies.clear();
        usedWeaponsMask = i;
        minSpeed = f;
        maxSpeed = f1;
        minHeight = f2;
        maxHeight = f3;
        useSpeed = true;
        useHeight = true;
    }

    public static void resetGameClear()
    {
        for(int i = 0; i < nearAct.length; i++)
            nearAct[i] = null;

    }

    public void clear()
    {
        nearNUsed = 0;
    }

    public boolean add(Actor actor, double d)
    {
        if(!(actor instanceof Prey) || (((Prey)actor).HitbyMask() & usedWeaponsMask) == 0)
            return true;
        if(useSpeed)
        {
            float f = (float)actor.getSpeed(null);
            if(f < minSpeed || f > maxSpeed)
                return true;
        }
        if(useHeight)
        {
            float f1 = (float)actor.pos.getAbsPoint().z;
            if(f1 < minHeight || f1 > maxHeight)
                return true;
        }
        int i;
        for(i = nearNUsed - 1; i >= 0 && d < nearDSq[i]; i--);
        if(++i >= nearNUsed)
        {
            if(nearNUsed < 3)
            {
                nearAct[nearNUsed] = actor;
                nearDSq[nearNUsed] = d;
                nearNUsed++;
            }
        } else
        {
            int j;
            if(nearNUsed < 3)
            {
                j = nearNUsed - 1;
                nearNUsed++;
            } else
            {
                j = nearNUsed - 2;
            }
            for(; j >= i; j--)
            {
                nearAct[j + 1] = nearAct[j];
                nearDSq[j + 1] = nearDSq[j];
            }

            nearAct[i] = actor;
            nearDSq[i] = d;
        }
        return true;
    }

    public static Actor getAFoundEnemy()
    {
        if(nearNUsed <= 0)
            return null;
        else
            return nearAct[nearNUsed != 1 ? World.Rnd().nextInt(nearNUsed) : 0];
    }

    public static Actor getAFoundEnemy(Point3d point3d, double d, int i)
    {
        return getAFoundEnemy(point3d, d, i, 5F);
    }

    public static Actor getAFoundEnemy(Point3d point3d, double d, int i, float ff)
    {
        double d1 = d * d;
        List list = Engine.targets();
        int j = list.size();
        boolean flag = false;
        for(int k = 0; k < j; k++)
        {
            Actor actor = (Actor)list.get(k);
            if((actor instanceof TransparentTestRunway) || (actor instanceof com.maddox.il2.objects.vehicles.planes.Plane.GenericSpawnPointPlane) || (((Prey)actor).HitbyMask() & usedWeaponsMask) == 0)
                continue;
            int i1 = actor.getArmy();
            if(i1 == 0 || i1 == i)
                continue;
            Point3d point3d1 = actor.pos.getAbsPoint();
            double d2 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y);
            if(d2 > d1)
                continue;
            if(useSpeed)
            {
                float f = (float)actor.getSpeed(null);
                if(f < minSpeed || f > maxSpeed)
                    continue;
            }
            if(useHeight)
            {
                float f1 = (float)actor.pos.getAbsPoint().z;
                if(f1 < minHeight || f1 > maxHeight)
                    continue;
            }
            if(actor instanceof Paratrooper)
            {
                d2 += 1.0D + d1;
                flag = true;
            }
            if(actor instanceof Aircraft)
            {
                tempPos.set(point3d);
                tempPos.z += ff;
                if(!VisCheck.visCheck(tempPos, point3d1, null, (Aircraft)actor))
                    continue;
            }
            int j1;
            for(j1 = nearNUsed - 1; j1 >= 0 && d2 < nearDSq[j1]; j1--);
            if(++j1 >= nearNUsed)
            {
                if(nearNUsed < 3)
                {
                    nearAct[nearNUsed] = actor;
                    nearDSq[nearNUsed] = d2;
                    nearNUsed++;
                }
                continue;
            }
            int k1;
            if(nearNUsed < 3)
            {
                k1 = nearNUsed - 1;
                nearNUsed++;
            } else
            {
                k1 = nearNUsed - 2;
            }
            for(; k1 >= j1; k1--)
            {
                nearAct[k1 + 1] = nearAct[k1];
                nearDSq[k1 + 1] = nearDSq[k1];
            }

            nearAct[j1] = actor;
            nearDSq[j1] = d2;
        }

        if(nearNUsed <= 0)
            return null;
        if(!flag || (nearAct[0] instanceof Paratrooper))
            return nearAct[nearNUsed != 1 ? World.Rnd().nextInt(nearNUsed) : 0];
        int l;
        for(l = 1; l < nearNUsed && !(nearAct[l] instanceof Paratrooper); l++);
        return nearAct[l != 1 ? World.Rnd().nextInt(l) : 0];
    }

    public static Actor getAFoundEnemyInShoot(Point3d point3d, double d, int i, 
             Orient orient, AnglesRange anglesrange, AnglesRange anglesrange1, boolean nfflag)
    {
        return getAFoundEnemyInShoot( point3d, d, i, 5F,
                   orient, anglesrange, anglesrange1, nfflag);
    }

    public static Actor getAFoundEnemyInShoot(Point3d point3d, double d, int i, float ff,
             Orient orient, AnglesRange anglesrange, AnglesRange anglesrange1, boolean nfflag)
    {
        Vector3d vector3d = new Vector3d();
        Object obj = new Vector3d();
        Orient orient1 = new Orient();

        double d1 = d * d;
        List list = Engine.targets();
        int j = list.size();
        boolean flag = false;
        for(int k = 0; k < j; k++)
        {
            Actor actor = (Actor)list.get(k);
            if((actor instanceof com.maddox.il2.objects.ships.Ship.RwyTransp) || (actor instanceof com.maddox.il2.objects.ships.Ship.RwyTranspWide) || (actor instanceof com.maddox.il2.objects.ships.Ship.RwyTranspSqr) || (((Prey)actor).HitbyMask() & usedWeaponsMask) == 0)
                continue;
            int i1 = actor.getArmy();
            if(i1 == 0 || i1 == i)
                continue;
            Point3d point3d1 = actor.pos.getAbsPoint();
            double d2 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y);
            if(d2 > d1)
                continue;
            if(useSpeed)
            {
                float f = (float)actor.getSpeed(null);
                if(f < minSpeed || f > maxSpeed)
                    continue;
            }
            if(useHeight)
            {
                float f1 = (float)actor.pos.getAbsPoint().z;
                if(f1 < minHeight || f1 > maxHeight)
                    continue;
            }
            if(actor instanceof Paratrooper)
            {
                d2 += 1.0D + d1;
                flag = true;
            }
            if(actor instanceof Aircraft)
            {
                tempPos.set(point3d);
                tempPos.z += ff;
                if(!VisCheck.visCheck(tempPos, point3d1, null, (Aircraft)actor))
                    continue;
            }

            vector3d.x = point3d1.x - point3d.x;
            vector3d.y = point3d1.y - point3d.y;
            vector3d.z = point3d1.z - point3d.z;
            orient.transformInv(vector3d, ((com.maddox.JGP.Tuple3d) (obj)));
            orient1.setAT0(((Vector3d) (obj)));
            float f9 = anglesrange.transformIntoRangeSpace(orient1.getYaw());
            float f221 = anglesrange1.transformIntoRangeSpace(orient1.getYaw());
            if(!anglesrange.isInside(f9) || (anglesrange1.isInside(f221) && nfflag))
                continue;
          
            int j1;
            for(j1 = nearNUsed - 1; j1 >= 0 && d2 < nearDSq[j1]; j1--);
            if(++j1 >= nearNUsed)
            {
                if(nearNUsed < 3)
                {
                    nearAct[nearNUsed] = actor;
                    nearDSq[nearNUsed] = d2;
                    nearNUsed++;
                }
                continue;
            }
            int k1;
            if(nearNUsed < 3)
            {
                k1 = nearNUsed - 1;
                nearNUsed++;
            } else
            {
                k1 = nearNUsed - 2;
            }
            for(; k1 >= j1; k1--)
            {
                nearAct[k1 + 1] = nearAct[k1];
                nearDSq[k1 + 1] = nearDSq[k1];
            }

            nearAct[j1] = actor;
            nearDSq[j1] = d2;
        }

        if(nearNUsed <= 0)
            return null;
        if(!flag || (nearAct[0] instanceof Paratrooper))
            return nearAct[nearNUsed != 1 ? World.Rnd().nextInt(nearNUsed) : 0];
        int l;
        for(l = 1; l < nearNUsed && !(nearAct[l] instanceof Paratrooper); l++);
        return nearAct[l != 1 ? World.Rnd().nextInt(l) : 0];
    }

    public static Actor getAFoundEnemyInHeading(Point3d point3d, double d, int i,
             Orient orient, AnglesRange anglesrange, AnglesRange anglesrange1, boolean nfflag,
             double dmin)
    {
        return getAFoundEnemyInHeading( point3d, d, i, 5F,
                   orient, anglesrange, anglesrange1, nfflag,
                   dmin);
    }

    public static Actor getAFoundEnemyInHeading(Point3d point3d, double d, int i, float ff,
             Orient orient, AnglesRange anglesrange, AnglesRange anglesrange1, boolean nfflag,
             double dmin)
    {
        Vector3d vector3d = new Vector3d();
        Object obj = new Vector3d();
        Orient orient1 = new Orient();

        double d1 = d * d;
        double dmin2 = dmin * dmin;
        List list = Engine.targets();
        int j = list.size();

        if( bLogDetail )
        {
            System.out.println("NearestEnemies: getAFoundEnemyInHeading , Engines.targets list size =" + j);
        }

        boolean flag = false;
        for(int k = 0; k < j; k++)
        {
            if( bLogDetail )
                System.out.println("NearestEnemies: for(int k = 0; k < j; k++) , k == " + k + ".");
            Actor actor = (Actor)list.get(k);
            if((actor instanceof TransparentTestRunway) || (actor instanceof com.maddox.il2.objects.vehicles.planes.Plane.GenericSpawnPointPlane) || (((Prey)actor).HitbyMask() & usedWeaponsMask) == 0)
            {
                if( bLogDetail )
                {
                    System.out.println("actor == RwyTransp, or (((Prey)actor).HitbyMask() & usedWeaponsMask) == 0");
                    System.out.println("(((Prey)actor).HitbyMask() == " + (((Prey)actor).HitbyMask()) + " " +(((((Prey)actor).HitbyMask())<0)? 1 : 0) + ((((Prey)actor).HitbyMask())&18384)/18384 + ((((Prey)actor).HitbyMask())&9192)/9192 + ((((Prey)actor).HitbyMask())&4096)/4096 + ((((Prey)actor).HitbyMask())&2048)/2048 + ((((Prey)actor).HitbyMask())&1024)/1024 + ((((Prey)actor).HitbyMask())&512)/512 + ((((Prey)actor).HitbyMask())&256)/256 + ((((Prey)actor).HitbyMask())&128)/128 + ((((Prey)actor).HitbyMask())&64)/64 + ((((Prey)actor).HitbyMask())&32)/32 + ((((Prey)actor).HitbyMask())&16)/16 + ((((Prey)actor).HitbyMask())&8)/8 + ((((Prey)actor).HitbyMask())&4)/4 + ((((Prey)actor).HitbyMask())&2)/2 + ((((Prey)actor).HitbyMask())&1) + ", usedWeaponsMask == " + usedWeaponsMask + ".");
                }
                continue;
            }
            int i1 = actor.getArmy();
            if(i1 == 0 || i1 == i)
            {
                if( bLogDetail )
                    System.out.println("NearestEnemies: getAFoundEnemyInHeading , actor.getArmy() == i1 == " + i1 + " , k == " + k);
                continue;
            }
            Point3d point3d1 = actor.pos.getAbsPoint();
            double d2 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y) + (point3d.z - point3d1.z) * (point3d.z - point3d1.z);
            if(d2 > d1)
            {
                if( bLogDetail )
                    System.out.println("NearestEnemies: getAFoundEnemyInHeading , target No." + k + " is too far.");
                continue;
            }
            if(d2 < dmin2 )
            {
                if( bLogDetail )
                    System.out.println("NearestEnemies: getAFoundEnemyInHeading , target No." + k + " is too near.");
                continue;
            }
            if(useSpeed)
            {
                float f = (float)actor.getSpeed(null);
                if(f < minSpeed || f > maxSpeed)
                {
                    if( bLogDetail )
                        System.out.println("NearestEnemies: getAFoundEnemyInHeading , target No." + k + " is out of speed range.");
                    continue;
                }
            }
            if(useHeight)
            {
                float f1 = (float)actor.pos.getAbsPoint().z;
                if(f1 < minHeight || f1 > maxHeight)
                {
                    if( bLogDetail )
                        System.out.println("NearestEnemies: getAFoundEnemyInHeading , target No." + k + " is out of height range.");
                    continue;
                }
            }
            if(actor instanceof Paratrooper)
            {
                d2 += 1.0D + d1;
                flag = true;
            }
            if(actor instanceof Aircraft)
            {
                tempPos.set(point3d);
                tempPos.z += ff;
                if(!VisCheck.visCheck(tempPos, point3d1, null, (Aircraft)actor))
                    continue;
            }

            vector3d.x = point3d1.x - point3d.x;
            vector3d.y = point3d1.y - point3d.y;
            vector3d.z = point3d1.z - point3d.z;
            orient.transformInv(vector3d, ((com.maddox.JGP.Tuple3d) (obj)));
            orient1.setAT0(((Vector3d) (obj)));
            float f9 = anglesrange.transformIntoRangeSpace(orient1.getYaw());
            float f221 = anglesrange1.transformIntoRangeSpace(orient1.getYaw());
            if(!anglesrange.isInside(f9) || (anglesrange1.isInside(f221) && nfflag))
            {
                if( bLogDetail )
                    System.out.println("NearestEnemies: getAFoundEnemyInHeading , target No." + k + " is out of heading range.");
                continue;
            }
          
            int j1;
            for(j1 = nearNUsed - 1; j1 >= 0 && d2 < nearDSq[j1]; j1--);

            if( bLogDetail )
                System.out.println("nearNUsed == " + nearNUsed + " , j1 == " + j1 + " .");
            if(++j1 >= nearNUsed)
            {
                if( bLogDetail )
                    System.out.println("++j1 >= nearNUsed .");
                if(nearNUsed < 3)
                {
                    if( bLogDetail )
                        System.out.println("nearNUsed < 3.");
                    nearAct[nearNUsed] = actor;
                    nearDSq[nearNUsed] = d2;
                    nearNUsed++;
                }
                continue;
            }
            int k1;
            if(nearNUsed < 3)
            {
                k1 = nearNUsed - 1;
                nearNUsed++;
            } else
            {
                k1 = nearNUsed - 2;
            }
            for(; k1 >= j1; k1--)
            {
                nearAct[k1 + 1] = nearAct[k1];
                nearDSq[k1 + 1] = nearDSq[k1];
            }

            nearAct[j1] = actor;
            nearDSq[j1] = d2;
            if( bLogDetail )
                System.out.println("set turget No." + k + " as a new nearest enemy.");
        }

        if(nearNUsed <= 0)
        {
            if( bLogDetail )
                System.out.println("NearestEnemies: getAFoundEnemyInHeading , no target in range, return null.");
            return null;
        }
        if(!flag || (nearAct[0] instanceof Paratrooper))
            return nearAct[nearNUsed != 1 ? World.Rnd().nextInt(nearNUsed) : 0];
        int l;
        for(l = 1; l < nearNUsed && !(nearAct[l] instanceof Paratrooper); l++);
        return nearAct[l != 1 ? World.Rnd().nextInt(l) : 0];
    }

    public static Actor getAFoundEnemyShipInHeading(Point3d point3d, double d, int i,
             Orient orient, AnglesRange anglesrange, AnglesRange anglesrange1, boolean nfflag,
             double dmin)
    {
        Vector3d vector3d = new Vector3d();
        Object obj = new Vector3d();
        Orient orient1 = new Orient();

        double d1 = d * d;
        double dmin2 = dmin * dmin;
        List list = Engine.targets();
        int j = list.size();
        boolean flag = false;
        for(int k = 0; k < j; k++)
        {
            Actor actor = (Actor)list.get(k);
            if(!(actor instanceof BigshipGeneric) && !(actor instanceof ShipGeneric))
                continue;
            if((((Prey)actor).HitbyMask() & usedWeaponsMask) == 0)
                continue;
            int i1 = actor.getArmy();
            if(i1 == 0 || i1 == i)
                continue;
            Point3d point3d1 = actor.pos.getAbsPoint();
            double d2 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y);
            if(d2 > d1)
                continue;
            if(d2 < dmin2 )
                continue;
            if(useSpeed)
            {
                float f = (float)actor.getSpeed(null);
                if(f < minSpeed || f > maxSpeed)
                    continue;
            }
            if(useHeight)
            {
                float f1 = (float)actor.pos.getAbsPoint().z;
                if(f1 < minHeight || f1 > maxHeight)
                    continue;
            }
            if(actor instanceof Paratrooper)
            {
                d2 += 1.0D + d1;
                flag = true;
            }

            vector3d.x = point3d1.x - point3d.x;
            vector3d.y = point3d1.y - point3d.y;
            vector3d.z = point3d1.z - point3d.z;
            orient.transformInv(vector3d, ((com.maddox.JGP.Tuple3d) (obj)));
            orient1.setAT0(((Vector3d) (obj)));
            float f9 = anglesrange.transformIntoRangeSpace(orient1.getYaw());
            float f221 = anglesrange1.transformIntoRangeSpace(orient1.getYaw());
            if(!anglesrange.isInside(f9) || (anglesrange1.isInside(f221) && nfflag))
                continue;
          
            int j1;
            for(j1 = nearNUsed - 1; j1 >= 0 && d2 < nearDSq[j1]; j1--);
            if(++j1 >= nearNUsed)
            {
                if(nearNUsed < 3)
                {
                    nearAct[nearNUsed] = actor;
                    nearDSq[nearNUsed] = d2;
                    nearNUsed++;
                }
                continue;
            }
            int k1;
            if(nearNUsed < 3)
            {
                k1 = nearNUsed - 1;
                nearNUsed++;
            } else
            {
                k1 = nearNUsed - 2;
            }
            for(; k1 >= j1; k1--)
            {
                nearAct[k1 + 1] = nearAct[k1];
                nearDSq[k1 + 1] = nearDSq[k1];
            }

            nearAct[j1] = actor;
            nearDSq[j1] = d2;
        }

        if(nearNUsed <= 0)
            return null;
        if(!flag || (nearAct[0] instanceof Paratrooper))
            return nearAct[nearNUsed != 1 ? World.Rnd().nextInt(nearNUsed) : 0];
        int l;
        for(l = 1; l < nearNUsed && !(nearAct[l] instanceof Paratrooper); l++);
        return nearAct[l != 1 ? World.Rnd().nextInt(l) : 0];
    }

    public static Actor getAFoundFlyingPlane(Point3d point3d, double d, int i, float f)
    {
        double d1 = d * d;
        List list = Engine.targets();
        int j = list.size();
        for(int k = 0; k < j; k++)
        {
            Actor actor = (Actor)list.get(k);
            if(!(actor instanceof Aircraft))
                continue;
            if(useHeight)
            {
                float f1 = (float)actor.pos.getAbsPoint().z;
                if(f1 < minHeight || f1 > maxHeight)
                    continue;
            }
            int l = actor.getArmy();
            if(l == 0 || l == i || (((Prey)actor).HitbyMask() & usedWeaponsMask) == 0)
                continue;
            Point3d point3d1 = actor.pos.getAbsPoint();
            double d2 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y) + (point3d.z - point3d1.z) * (point3d.z - point3d1.z);
            if(d2 > d1 || actor.getSpeed(null) < 10D || point3d1.z - World.land().HQ(point3d1.x, point3d1.y) < (double)f)
                continue;
            int i1;
            for(i1 = nearNUsed - 1; i1 >= 0 && d2 < nearDSq[i1]; i1--);
            if(++i1 >= nearNUsed)
            {
                if(nearNUsed < 3)
                {
                    nearAct[nearNUsed] = actor;
                    nearDSq[nearNUsed] = d2;
                    nearNUsed++;
                }
                continue;
            }
            int j1;
            if(nearNUsed < 3)
            {
                j1 = nearNUsed - 1;
                nearNUsed++;
            } else
            {
                j1 = nearNUsed - 2;
            }
            for(; j1 >= i1; j1--)
            {
                nearAct[j1 + 1] = nearAct[j1];
                nearDSq[j1 + 1] = nearDSq[j1];
            }

            nearAct[i1] = actor;
            nearDSq[i1] = d2;
        }

        if(nearNUsed <= 0)
            return null;
        else
            return nearAct[nearNUsed != 1 ? World.Rnd().nextInt(nearNUsed) : 0];
    }

    private static final int MAX_OBJECTS = 3;
    private static Actor nearAct[] = new Actor[3];
    private static double nearDSq[] = new double[3];
    private static int nearNUsed;
    private static NearestEnemies enemies = new NearestEnemies();
    private static int usedWeaponsMask;
    private static boolean useSpeed = false;
    private static float minSpeed;
    private static float maxSpeed;
    private static boolean useHeight = false;
    private static float minHeight;
    private static float maxHeight;
    private static Point3d tempPos = new Point3d();

    private static boolean bLogDetail = false;
}