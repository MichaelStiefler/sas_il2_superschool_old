package com.maddox.il2.builder;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;

public class PAir extends PPoint
{

    public void setType(int i)
    {
        type = i;
        setIcon();
    }

    public int type()
    {
        return type;
    }

    public Actor getTarget()
    {
        if(Actor.isValid(target))
            return target;
        if(sTarget != null)
        {
            Actor actor = Actor.getByName(sTarget);
            if(Actor.isValid(actor))
            {
                if(actor instanceof Path)
                {
                    Path path = (Path)actor;
                    if(iTarget >= path.points())
                        iTarget = path.points() - 1;
                    if(iTarget < 0)
                    {
                        iTarget = 0;
                        sTarget = null;
                        target = null;
                    } else
                    {
                        PPoint ppoint = path.point(iTarget);
                        target = ppoint;
                    }
                } else
                {
                    target = actor;
                }
                return target;
            }
            sTarget = null;
        }
        target = null;
        return null;
    }

    public void setTarget(Actor actor)
    {
        target = actor;
        if(target != null)
            if(type != 1 && type != 2)
            {
                if(target instanceof PAir)
                    setType(0);
                else
                    setType(3);
            } else
            if(target instanceof PPoint)
            {
                if(target.getOwner() instanceof PathChief)
                {
                    PathChief pathchief = (PathChief)target.getOwner();
                    if(!PlMisChief.isAirport(pathchief._iType, pathchief._iItem))
                        target = null;
                } else
                {
                    target = null;
                }
            } else
            if(!"true".equals(Property.stringValue(target.getClass(), "IsAirport", (String)null)))
                target = null;
    }

    public void destroy()
    {
        target = null;
        sTarget = null;
        Path path = (Path)getOwner();
        super.destroy();
        if(path != null)
            path.computeTimes();
    }

    public PAir(Path path, PPoint ppoint, Mat mat, Point3d point3d, int i, double d, 
            double d1)
    {
        super(path, ppoint, mat, point3d);
        target = null;
        sTarget = null;
        iTarget = 0;
        bRadioSilence = false;
        setType(i);
        height = d;
        speed = d1;
        path.computeTimes();
    }

    public PAir(Path path, PPoint ppoint, Point3d point3d, int i, double d, double d1)
    {
        this(path, ppoint, (Mat)null, point3d, i, d, d1);
    }

    public PAir(Path path, PPoint ppoint, String s, Point3d point3d, int i, double d, 
            double d1)
    {
        this(path, ppoint, IconDraw.get(s), point3d, i, d, d1);
    }

    private void setIcon()
    {
        String s = null;
        switch(type)
        {
        case 0:
            s = "normfly";
            break;

        case 1:
            s = "takeoff";
            break;

        case 2:
            s = "landing";
            break;

        case 3:
            s = "gattack";
            break;

        default:
            return;
        }
        icon = IconDraw.get("icons/" + s + ".mat");
    }

    public static final int NORMFLY = 0;
    public static final int TAKEOFF = 1;
    public static final int LANDING = 2;
    public static final int GATTACK = 3;
    public static final String types[] = {
        "NORMFLY", "TAKEOFF", "LANDING", "GATTACK"
    };
    private int type;
    public double height;
    public double speed;
    private Actor target;
    public String sTarget;
    public int iTarget;
    public boolean bRadioSilence;

}
