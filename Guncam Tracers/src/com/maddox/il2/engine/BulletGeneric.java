package com.maddox.il2.engine;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.vehicles.artillery.Artillery.Wirbelwind;
import com.maddox.rts.Destroy;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;
import com.maddox.util.HashMapIntEntry;

public class BulletGeneric
    implements Destroy
{
    private static class Lines
    {

        public int color;
        public int n;
        public float coord[];

        private Lines()
        {
            n = 0;
            coord = new float[6];
        }

    }


    public int indx()
    {
        return flags & 0xff;
    }

    public BulletProperties properties()
    {
        return gun.prop.bullet[indx()];
    }

    public Actor owner()
    {
        return owner;
    }

    public GunGeneric gun()
    {
        return gun;
    }

    public Actor gunOwnerBody()
    {
        return gun.interpolater.actor;
    }

    public void destroy()
    {
        owner = gun = null;
        Eff3DActor.finish(effTrail);
        effTrail = null;
    }

    public boolean isDestroyed()
    {
        return gun == null;
    }

    public void move(float f)
    {
        if(gun == null)
        {
            return;
        } else
        {
            Point3d point3d = p1;
            p1 = p0;
            p0 = point3d;
            dspeed.scale((double)(gun.bulletKV[indx()] * f) * speed.length(), speed);
            dspeed.z += gun.bulletAG[indx()] * f;
            speed.add(dspeed);
            p1.scaleAdd(f, speed, p0);
            return;
        }
    }

    public boolean collided(Actor actor, String s, double d)
    {
        return true;
    }

    public void timeOut()
    {
    }

    public BulletGeneric(int i, GunGeneric gungeneric, Loc loc, Vector3d vector3d, long l)
    {
        bMoved = false;
        p0 = new Point3d();
        p1 = new Point3d();
        speed = new Vector3d();
        gun = gungeneric;
        owner = gungeneric.getOwner();
        p0.set(loc.getPoint());
        p1.set(p0);
        speed.set(vector3d);
        timeEnd = l;
        nextBullet = Engine.cur.bulletList;
        Engine.cur.bulletList = this;
        flags = i & 0xff;
    }

    public void preRender()
    {
        if(effTrail != null)
            effTrail.pos.setAbs(p0);
        flags &= 0xffff3fff;
        if((flags & 0x80000000) != 0)
        {
            Point3d point3d = Render.currentCamera().pos.getAbsPoint();
            
            //TODO: +++ Guncam Tracers Mod 4.12.1 +++
            int p0Distance = (int)point3d.distance(p0);
            boolean isWithinDistance = false;
            
            if ((this.owner instanceof Wirbelwind) && (p0Distance < Config.cur.ini.get("Mods", "TracersWirbelwind", 50))) isWithinDistance = true;
            else if ((this.owner == World.getPlayerAircraft()) && (p0Distance < Config.cur.ini.get("Mods", "TracersPlayer", 400))) isWithinDistance = true;
            else if ((this.owner instanceof Aircraft) && (p0Distance < Config.cur.ini.get("Mods", "TracersAircrafts", 300))) isWithinDistance = true;
            else if (p0Distance < Config.cur.ini.get("Mods", "TracersWorld", 100)) isWithinDistance = true;
            
            if(gun._bulletTraceMesh[indx()] != null && isWithinDistance)
//            if(gun._bulletTraceMesh[indx()] != null && point3d.distance(p0) < 500D)
            //TODO: --- CTO Mod 4.12 ---
            {
                if(gun._bulletTraceMesh[indx()].preRender(p0) != 0)
                    flags |= 0x8000;
            } else
            if(gun.prop.bullet[indx()].traceColor != 0)
                flags |= 0xc000;
        }
    }

    public void render()
    {
        if((flags & 0x1000) != 0)
            return;
        int i = indx();
        if((flags & 0x4000) != 0)
        {
            tmpVector.sub(p1, p0);
            tmpVector.normalize();
            tmpVector.scale(gun._bulletTraceMeshLen[i]);
            tmpP.interpolate(p0, p1, Time.tickOffset());
            tmpVector.add(tmpP);
            drawLine(tmpP, tmpVector, gun.prop.bullet[i].traceColor);
        } else
        {
            tmpVector.sub(p1, p0);
            tmpVector.normalize();
            Orient orient = tmpLoc.getOrient();
            orient.setAT0(tmpVector);
            tmpP.interpolate(p0, p1, Time.tickOffset());
            tmpLoc.set(tmpP);
            if(!gun._bulletTraceMesh[i].putToRenderArray(tmpLoc))
            {
                gun._bulletTraceMesh[i].setPos(tmpLoc);
                gun._bulletTraceMesh[i].render();
            }
        }
    }

    public static void preRenderAll()
    {
        for(BulletGeneric bulletgeneric = Engine.cur.bulletList; bulletgeneric != null; bulletgeneric = bulletgeneric.nextBullet)
            if((bulletgeneric.flags & 0x80000000) != 0 || bulletgeneric.effTrail != null)
                bulletgeneric.preRender();

    }

    public static void renderAll()
    {
        for(BulletGeneric bulletgeneric = Engine.cur.bulletList; bulletgeneric != null; bulletgeneric = bulletgeneric.nextBullet)
            if((bulletgeneric.flags & 0x8000) != 0)
                bulletgeneric.render();

        flushLines();
    }

    public static void drawLine(Tuple3d tuple3d, Tuple3d tuple3d1, int i)
    {
        Lines lines = (Lines)mapLines.get(i);
        if(lines == null)
        {
            lines = new Lines();
            lines.color = i;
            mapLines.put(i, lines);
        }
        if(lines.n >= lines.coord.length / 6)
        {
            float af[] = new float[lines.coord.length * 2];
            System.arraycopy(lines.coord, 0, af, 0, lines.coord.length);
            lines.coord = af;
        }
        int j = lines.n * 6;
        lines.coord[j + 0] = (float)tuple3d.x;
        lines.coord[j + 1] = (float)tuple3d.y;
        lines.coord[j + 2] = (float)tuple3d.z;
        lines.coord[j + 0 + 3] = (float)tuple3d1.x;
        lines.coord[j + 1 + 3] = (float)tuple3d1.y;
        lines.coord[j + 2 + 3] = (float)tuple3d1.z;
        lines.n++;
    }

    public static void flushLines()
    {
        HashMapIntEntry hashmapintentry = mapLines.nextEntry(null);
        boolean flag = false;
        for(; hashmapintentry != null; hashmapintentry = mapLines.nextEntry(hashmapintentry))
        {
            Lines lines = (Lines)hashmapintentry.getValue();
            if(lines.n <= 0)
                continue;
            if(!flag)
            {
                Render.drawBeginLines(0);
                flag = true;
            }
            Render.drawLines(lines.coord, lines.n * 2, 1.0F, lines.color, Render.LineFlags | Mat.BLENDADD, 0);
            lines.n = 0;
        }

        if(flag)
            Render.drawEnd();
    }

    public static final int TRACE_EXIST = 0x80000000;
    public static final int ARCADE = 0x40000000;
    public static final int INDEX_MASK = 255;
    public static final int TRACE_VISIBLE = 32768;
    public static final int TRACE_LINE = 16384;
    public static final int ARCADED = 8192;
    public static final int FIRST_TICK = 4096;
    public int flags;
    public BulletGeneric nextBullet;
    protected Actor owner;
    protected GunGeneric gun;
    public Eff3DActor effTrail;
    public long timeEnd;
    public boolean bMoved;
    public Point3d p0;
    public Point3d p1;
    public Vector3d speed;
    protected static Vector3d dspeed = new Vector3d();
    private static Vector3d tmpVector = new Vector3d();
    private static Loc tmpLoc = new Loc();
    private static Point3d tmpP = new Point3d();
    private static HashMapInt mapLines = new HashMapInt();

}
