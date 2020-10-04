// Source File Name:   RocketGun.java
// 03rd/Oct./2020 : Bomb hook position tweak in _Hardpoint refering

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket, GunEmpty

public class RocketGun extends Interpolate
    implements BulletEmitter
{

    public RocketGun()
    {
        ready = true;
        bHide = false;
        bRocketPosRel = true;
        bulletClass = null;
        bCassette = false;
        timeLife = -1F;
        spread = 0;
        plusPitch = 0.0F;
        plusYaw = 0.0F;
        bulletMassa = 0.048F;
    }

    public void doDestroy()
    {
        ready = false;
        if(rocket != null)
        {
            rocket.destroy();
            rocket = null;
        }
    }

    private boolean nameEQ(HierMesh hiermesh, int i, int j)
    {
        if(hiermesh == null)
            return false;
        hiermesh.setCurChunk(i);
        String s = hiermesh.chunkName();
        hiermesh.setCurChunk(j);
        String s1 = hiermesh.chunkName();
        int l = Math.min(s.length(), s1.length());
        for(int k = 0; k < l; k++)
        {
            char c = s.charAt(k);
            if(c == '_')
                return true;
            if(c != s1.charAt(k))
                return false;
        }

        return true;
    }

    public BulletEmitter detach(HierMesh hiermesh, int i)
    {
        if(!ready)
            return GunEmpty.get();
        if(i == -1 || nameEQ(hiermesh, i, hook.chunkNum()))
        {
            bExecuted = true;
            ready = false;
            return GunEmpty.get();
        } else
        {
            return this;
        }
    }

    protected int bullets()
    {
        return actor == null ? 0 : bulletss - actor.hashCode();
    }

    protected void bullets(int i)
    {
        if(actor != null)
            bulletss = i + actor.hashCode();
        else
            bulletss = 0;
    }

    public boolean isEnablePause()
    {
        return false;
    }

    public boolean isPause()
    {
        return false;
    }

    public void setPause(boolean flag)
    {
    }

    public void hide(boolean flag)
    {
        bHide = flag;
        if(bHide)
        {
            if(Actor.isValid(rocket))
                rocket.drawing(false);
        } else
        if(Actor.isValid(rocket))
            rocket.drawing(true);
    }

    public boolean isHide()
    {
        return bHide;
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = f;
    }

    public float getRocketTimeLife()
    {
        return timeLife;
    }

    public boolean isCassette()
    {
        return bCassette;
    }

    public float bulletMassa()
    {
        return bulletMassa;
    }

    public int countBullets()
    {
        return bullets();
    }

    public boolean haveBullets()
    {
        return bullets() != 0;
    }

    public void loadBullets()
    {
        loadBullets(bulletsFull);
    }

    public void _loadBullets(int i)
    {
        loadBullets(i);
    }

    public void loadBullets(int i)
    {
        bullets(i);
        if(bullets() != 0)
        {
            if(!Actor.isValid(rocket))
                newRocket();
        } else
        if(Actor.isValid(rocket))
        {
            rocket.destroy();
            rocket = null;
        }
    }

    public Class bulletClass()
    {
        return bulletClass;
    }

    public void setBulletClass(Class class1)
    {
        bulletClass = class1;
        bulletMassa = Property.floatValue(bulletClass, "massa", bulletMassa);
    }

    public boolean isShots()
    {
        return bExecuted;
    }

    public void shots(int i, float f)
    {
        shots(i);
    }

    public void shots(int i)
    {
        if(isHide())
            return;
        if(isCassette() && i != 0 && bullets() != -1)
            i = bullets();
        if(!bExecuted && i != 0)
        {
            if(bullets() == 0)
                return;
            curShotStep = 0;
            curShots = i;
            bExecuted = true;
        } else
        if(bExecuted && i != 0)
            curShots = i;
        else
        if(bExecuted && i == 0)
            bExecuted = false;
    }

    protected void interpolateStep()
    {
        bTickShot = false;
        if(curShotStep == 0)
        {
            if(bullets() == 0 || curShots == 0 || !Actor.isValid(actor))
            {
                shots(0);
                return;
            }
            bTickShot = true;
            if(rocket != null)
            {
                rocket.pos.setUpdateEnable(true);
                if(plusPitch != 0.0F || plusYaw != 0.0F)
                {
                    rocket.pos.getAbs(_tmpOr0);
                    _tmpOr1.setYPR(plusYaw, plusPitch, 0.0F);
                    _tmpOr1.add(_tmpOr0);
                    rocket.pos.setAbs(_tmpOr1);
                }
                rocket.pos.resetAsBase();
                rocket.start(timeLife, spread);
                if(Actor.isValid(rocket))
                {
                    String s = Property.stringValue(getClass(), "sound", null);
                    if(s != null)
                        rocket.newSound(s, true);
                }
                rocket = null;
            }
            if(curShots > 0)
                curShots--;
            if(bullets() > 0)
                bullets(bullets() - 1);
            if(bullets() != 0)
                newRocket();
            curShotStep = shotStep;
        }
        curShotStep--;
    }

    public boolean tick()
    {
        interpolateStep();
        return ready;
    }

    private void newRocket()
    {
        try
        {
            rocket = (Rocket)bulletClass.newInstance();
            // +++ western: Engine mod 2.9.x Bomb hook position in _Hardpoint refering
            HookNamed hookatc = null;
            try {
                hookatc = (HookNamed)rocket.findHook("_Hardpoint");
            } catch(Exception exception) { }
            if(hookatc == null)
                rocket.pos.setBase(actor, hook, false);
            else
                rocket.pos.setBase(actor, hook, false, hookatc);
            // ---
            if(bRocketPosRel)
                rocket.pos.changeHookToRel();
            rocket.pos.resetAsBase();
            rocket.visibilityAsBase(true);
            if(bRocketPosRel)
                rocket.pos.setUpdateEnable(false);
        }
        catch(Exception exception) { }
    }

    public void setHookToRel(boolean flag)
    {
        if(bRocketPosRel == flag)
            return;
        if(Actor.isValid(rocket))
            if(flag)
            {
                rocket.pos.changeHookToRel();
                rocket.pos.setUpdateEnable(false);
            } else
            {
                rocket.pos.setRel(nullLoc);
                // +++ western: Engine mod 2.9.x Bomb hook position in _Hardpoint refering
                HookNamed hookatc = null;
                try {
                    hookatc = (HookNamed)rocket.findHook("_Hardpoint");
                } catch(Exception exception) { }
                if(hookatc == null)
                    rocket.pos.setBase(rocket.pos.base(), hook, false);
                else
                    rocket.pos.setBase(rocket.pos.base(), hook, false, hookatc);
                // ---
                rocket.pos.setUpdateEnable(true);
            }
        bRocketPosRel = flag;
    }

    public String getHookName()
    {
        return hook.name();
    }

    public void set(Actor actor, String s, Loc loc)
    {
        set(actor, s);
    }

    public void set(Actor actor, String s, String s1)
    {
        set(actor, s);
    }

    public void set(Actor actor, String s)
    {
        this.actor = actor;
        Class class1 = getClass();
        bCassette = Property.containsValue(class1, "cassette");
        bulletClass = (Class)Property.value(class1, "bulletClass", null);
        bullets(Property.intValue(class1, "bullets", 1));
        bulletsFull = bullets();
        setBulletClass(bulletClass);
        float f = Property.floatValue(class1, "shotFreq", 0.5F);
        if(f < 0.001F)
            f = 0.001F;
        shotStep = (int)((1.0F / f + Time.tickConstLenFs() / 2.0F) / Time.tickConstLenFs());
        if(shotStep <= 0)
            shotStep = 1;
        hook = (HookNamed)actor.findHook(s);
        newRocket();
        this.actor.interpPut(this, null, -1L, null);
    }

    public void setConvDistance(float f, float f1)
    {
        if(!Actor.isValid(rocket))
        {
            return;
        } else
        {
            Point3d point3d = rocket.pos.getRelPoint();
            Orient orient = new Orient();
            orient.set(rocket.pos.getRelOrient());
            float f2 = (float)Math.sqrt(point3d.y * point3d.y + (double)(f * f));
            plusYaw = (float)Math.toDegrees(Math.atan(-point3d.y / (double)f));
            plusPitch = f1;
            return;
        }
    }

    public void setSpreadRnd(int i)
    {
        spread = i;
    }

    protected boolean ready;
    protected Rocket rocket;
    protected HookNamed hook;
    protected boolean bHide;
    protected boolean bRocketPosRel;
    protected Class bulletClass;
    protected int bulletsFull;
    protected int bulletss;
    protected int shotStep;
    private boolean bCassette;
    protected float timeLife;
    protected int spread;
    private float plusPitch;
    private float plusYaw;
    protected float bulletMassa;
    private int curShotStep;
    private int curShots;
    protected boolean bTickShot;
    private static Orient _tmpOr0 = new Orient();
    private static Orient _tmpOr1 = new Orient();
    private static Loc nullLoc = new Loc();
    private static final boolean DEBUG = false;

}
