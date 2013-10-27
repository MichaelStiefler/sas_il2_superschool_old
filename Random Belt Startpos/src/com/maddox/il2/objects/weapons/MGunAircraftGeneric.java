package com.maddox.il2.objects.weapons;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletAimer;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.BulletGeneric;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Time;

public class MGunAircraftGeneric extends Gun
    implements BulletEmitter, BulletAimer
{

	//TODO: +++ Additional properties and methods required for random belting +++
	private static Random theRandom = null;
	private void initRandom() {
		long lTime = System.currentTimeMillis();
		SecureRandom secRandom = new SecureRandom();
		secRandom.setSeed(lTime);
		long lSeed1 = (long) secRandom.nextInt();
		long lSeed2 = (long) secRandom.nextInt();
		long lSeed = (lSeed1 << 32) + lSeed2;
		theRandom = new RangeRandom(lSeed);
	}
	//TODO: --- Additional properties and methods required for random belting ---

	public MGunAircraftGeneric()
    {
        _index = -1;
    }

    public String getDayProperties(String s)
    {
        if(s == null)
            return null;
        String s1 = "3DO/Effects/GunFire/";
        if(s.regionMatches(true, 0, s1, 0, s1.length()))
            return "3DO/Effects/GunFireDay/" + s.substring(s1.length());
        else
            return s;
    }

    public void createdProperties()
    {
        if(prop.fireMesh != null && prop.fireMeshDay == null)
            prop.fireMeshDay = getDayProperties(prop.fireMesh);
        if(prop.fire != null && prop.fireDay == null)
            prop.fireDay = getDayProperties(prop.fire);
        if(prop.sprite != null && prop.spriteDay == null)
            prop.spriteDay = getDayProperties(prop.sprite);
        super.createdProperties();
    }

    public String prop_fireMesh()
    {
        if(World.Sun().ToSun.z >= -0.22F)
            return prop.fireMeshDay;
        else
            return prop.fireMesh;
    }

    public String prop_fire()
    {
        if(World.Sun().ToSun.z >= -0.22F)
            return prop.fireDay;
        else
            return prop.fire;
    }

    public String prop_sprite()
    {
        if(World.Sun().ToSun.z >= -0.22F)
            return prop.spriteDay;
        else
            return prop.sprite;
    }

    public void setConvDistance(float f, float f1)
    {
        Point3d point3d = pos.getRelPoint();
        Orient orient = new Orient();
        orient.set(pos.getRelOrient());
        float f2 = (float)Math.sqrt(point3d.y * point3d.y + (double)(f * f));
        float f3 = (float)Math.toDegrees(Math.atan(-point3d.y / (double)f));
        if(!prop.bUseHookAsRel)
        {
            f3 = 0.0F;
            f2 = f;
        }
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = prop.bullet[0].speed;
        float f7 = 0.0F;
        while(f4 < f2) 
        {
            f6 += (bulletKV[0] * Time.tickConstLenFs() * 1.0F * BulletAircraftGeneric.fv(f6)) / f6;
            f7 -= bulletAG[0] * Time.tickConstLenFs();
            f4 += f6 * Time.tickConstLenFs();
            f5 += f7 * Time.tickConstLenFs();
        }
        f5 += f1;
        f5 = (float)((double)f5 - point3d.z);
        float f8 = (float)Math.toDegrees(Math.atan(f5 / f2));
        orient.setYPR(orient.azimut() + f3, orient.tangage() + f8, orient.kren());
        pos.setRel(orient);
    }

    public void init()
    {
        int i = prop.bullet.length;
        bulletAG = new float[i];
        bulletKV = new float[i];
        initRealisticGunnery();
    	//TODO: +++ Initialize random belt start position +++
		if (theRandom == null) this.initRandom(); // Check if randomizer has been initialized and if not, do it now
		if (this.prop.bullet.length < 2) { // Belt filled with same bullets all over? Then we've got nothing to choose from.
			this.bulletNum = 0;
			_index = -1;
			return;
		}
		if (!lastGun.equals(this.getClass().getName()) || remainingBeltStartPos.size() == 0) { // obviously we have a different gun than before, so the belt can start on any position
			remainingBeltStartPos = new ArrayList(this.prop.bullet.length); // Generate Array of available belt start positions.
			for (int beltPos=0; beltPos<this.prop.bullet.length; beltPos++) remainingBeltStartPos.add(new Integer(beltPos)); // Fill Array of available belt start positions.
			this.bulletNum = theRandom.nextInt(this.prop.bullet.length); // set random belt bullet number for tracers
			remainingBeltStartPos.remove(this.bulletNum);
			lastGun = this.getClass().getName();
		} else { // this gun is of the same type as the previous one, make sure not to use the same belt start pos again
			this.bulletNum = ((Integer)(remainingBeltStartPos.remove(theRandom.nextInt(remainingBeltStartPos.size())))).intValue(); // set random belt bullet number for tracers
		}
//		System.out.println(this.getClass().getName() + " Belt Start Pos = " + (this.bulletNum + 1) + " of " + this.prop.bullet.length);
		_index = this.bulletNum - 1; // set random belt bullet number for bullet properties, decrement by one since method "nextIndexBulletType()" increments this value on the beginning of each step
    	//TODO: --- Initialize random belt start position ---
    }

    public void initRealisticGunnery(boolean flag)
    {
        int i = prop.bullet.length;
        for(int j = 0; j < i; j++)
            if(flag)
            {
                bulletAG[j] = -10F;
                bulletKV[j] = -((1000F * prop.bullet[j].kalibr) / prop.bullet[j].massa);
            } else
            {
                bulletAG[j] = 0.0F;
                bulletKV[j] = 0.0F;
            }

    }

    public Bullet createNextBullet(Vector3d vector3d, int i, GunGeneric gungeneric, Loc loc, Vector3d vector3d1, long l)
    {
        bullet = new BulletAircraftGeneric(vector3d, i, gungeneric, loc, vector3d1, l);
        if(!World.cur().diffCur.Realistic_Gunnery)
            if(!isContainOwner(World.getPlayerAircraft()));
        return bullet;
    }

    public void loadBullets(int i)
    {
        super.loadBullets(i);
        resetGuard();
    }

    public void _loadBullets(int i)
    {
        super._loadBullets(i);
        resetGuard();
    }

    private void resetGuard()
    {
        guardBullet = null;
        for(BulletGeneric bulletgeneric = Engine.cur.bulletList; bulletgeneric != null; bulletgeneric = bulletgeneric.nextBullet)
            if((bulletgeneric instanceof BulletAircraftGeneric) && bulletgeneric.gun() == this)
                ((BulletAircraftGeneric)bulletgeneric).bulletss = bulletgeneric.hashCode();

    }

    public int nextIndexBulletType()
    {
        _index++;
        if(_index >= prop.bullet.length)
            _index = 0;
        return _index;
    }

    protected BulletAircraftGeneric guardBullet;
    private static Bullet bullet;
    private int _index;
	//TODO: +++ Additional properties and methods required for random belting +++
    private static String lastGun = "";
    private static ArrayList remainingBeltStartPos = null;
	//TODO: --- Additional properties and methods required for random belting ---
}
