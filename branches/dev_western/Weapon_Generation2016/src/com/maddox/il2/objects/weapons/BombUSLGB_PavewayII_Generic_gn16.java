
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.air.TypeLaserSpotter;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb, Ballistics

public class BombUSLGB_PavewayII_Generic_gn16 extends Bomb
{

    public BombUSLGB_PavewayII_Generic_gn16()
    {
        victim = null;
        lastdist = 20000D;
        evade = false;
        range = 0.0D;
        popped = false;
        laseron = false;
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
        if(!popped && Time.current() > t1)
        {
            if(meshopen != null)
                setMesh(meshopen);
            popped = true;
        }
        if(Time.current() > t1 + 1000L)
        {
            float f = Time.tickLenFs();
            float f1 = (float)getSpeed(null);
            f1 += (200F - f1) * 0.1F * f;
            super.pos.getAbs(p, or);
            v.set(1.0D, 0.0D, 0.0D);
            or.transform(v);
            v.scale(f1);
            setSpeed(v);
            p.x += ((Tuple3d) (v)).x * (double)f;
            p.y += ((Tuple3d) (v)).y * (double)f;
            p.z += ((Tuple3d) (v)).z * (double)f - 0.01D;
            if(isNet() && isNetMirror())
                super.pos.setAbs(p, or);
            if(Time.current() > tStart + 350L)
            {
                checklaser();
                if(!laseron)
                {
                    super.curTm += Time.tickLenFs();
                    Ballistics.updateBomb(this, super.M, super.S, super.J, super.DistFromCMtoStab);
                    updateSound();
                } else
                {
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
                }
                super.pos.setAbs(p, or);
            }
        } else
        {
            super.curTm += Time.tickLenFs();
            Ballistics.updateBomb(this, super.M, super.S, super.J, super.DistFromCMtoStab);
            updateSound();
        }
    }

    private void checklaser()
    {
        laseron = false;
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if((actor instanceof TypeLaserSpotter) && actor.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 20000D && actor == World.getPlayerAircraft())
            {
                Point3d point3d = new Point3d();
                point3d = ((TypeLaserSpotter)actor).spot;
                if(super.pos.getAbsPoint().distance(point3d) < 15000D);
                pT.set(point3d);
                laseron = true;
            }
        }

    }

    private Orient or = new Orient();
    private Point3d p = new Point3d();
    private Point3d pT = new Point3d();
    private Vector3d v = new Vector3d();
    private long tStart;
    private float deltaAzimuth;
    private float deltaTangage;
    private float deltaX;
    private Actor victim;
    private AirGroup airgroup = null;
    private Pilot pilot = null;
    private double lastdist;
    private boolean evade;
    private double range;
    private long t1;
    private boolean popped;
    private boolean laseron;
    public String meshopen;

}
