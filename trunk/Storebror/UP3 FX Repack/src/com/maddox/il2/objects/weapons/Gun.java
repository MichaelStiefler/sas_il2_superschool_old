package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetGunner;
import com.maddox.rts.Time;

public class Gun extends GunGeneric implements BulletEmitter {

	public Gun() {
	}

	public void initRealisticGunnery() {
		boolean flag = !isContainOwner(World.getPlayerAircraft()) || World.cur().diffCur.Realistic_Gunnery;
		initRealisticGunnery(flag);
		//TODO: +++ FX Repack Mod +++
        bNoTrace = Config.cur.ini.get("Mods", "PALNoTracers", false);
        bNoSmoke = Config.cur.ini.get("Mods", "PALNoSmoketrails", false);
		//TODO: --- FX Repack Mod ---
	}

	public int nextIndexBulletType() {
		return 0;
	}

	public Bullet createNextBullet(Vector3d vector3d, int i, GunGeneric gungeneric, Loc loc1, Vector3d vector3d1, long l) {
		return new Bullet(vector3d, i, gungeneric, loc1, vector3d1, l);
	}

	public void doStartBullet(double d) {
		int i = nextIndexBulletType();
		long l = Time.tick() + (long)((double)Time.tickLenFms() * d);
		pos.getTime(l, loc);
		Loc loc1 = loc;
		Orient orient;
		if (prop.maxDeltaAngle > 0.0F) {
			orient = loc.getOrient();
			float f = World.Rnd().nextFloat(-prop.maxDeltaAngle, prop.maxDeltaAngle);
			float f1 = World.Rnd().nextFloat(-prop.maxDeltaAngle, prop.maxDeltaAngle);
			orient.increment(f, f1, 0.0F);
		} else {
			orient = loc1.getOrient();
		}
		v1.set(1.0D, 0.0D, 0.0D);
		vWind.set(0.0D, 0.0D, 0.0D);
		orient.transform(v1);
		v1.scale(prop.bullet[i].speed);
		Actor actor = getOwner();
		if (actor instanceof Aircraft) {
			v2.set(v1);
			v2.scale(prop.bullet[i].massa * (float)prop.bulletsCluster);
			((Aircraft)actor).FM.gunPulse(v2);
			actor.getSpeed(v);
			v1.add(v);
			if (World.cur().diffCur.Wind_N_Turbulence) {
				Point3d point3d = new Point3d();
				pos.getAbs(point3d);
				World.wind().getVectorWeapon(point3d, vWind);
				v1.add(-vWind.x, -vWind.y, 0.0D);
			}
			if (actor == World.getPlayerAircraft()) {
				World.cur().scoreCounter.bulletsFire += prop.bulletsCluster;
				if (World.cur().diffCur.Realistic_Gunnery && (((Aircraft)actor).FM instanceof RealFlightModel)) {
					Loc loc2 = pos.getRel();
					if (Math.abs(loc2.getPoint().y) < 0.5D) {
						double d1 = prop.bullet[i].massa * prop.bullet[i].speed;
						v.x = World.Rnd().nextDouble(-20D, 20D) * d1;
						v.y = World.Rnd().nextDouble(-100D, 200D) * d1;
						v.z = World.Rnd().nextDouble(-200D, 200D) * d1;
						v.scale(0.29999999999999999D);
						((RealFlightModel)((Aircraft)actor).FM).gunMomentum(v, false);
					} else {
						double d2 = prop.bullet[i].massa * (float)prop.bulletsCluster * prop.shotFreq;
						v2.set(-1D, 0.0D, 0.0D);
						loc2.transform(v2);
						double d4 = 0.45000000000000001D * Math.sqrt(Math.sqrt(prop.bullet[i].massa));
						d4 = 64D * World.Rnd().nextDouble(1.0D - d4, 1.0D + d4);
						v2.scale(d4 * v1.length() * d2);
						v.cross(loc2.getPoint(), v2);
						v.y *= 0.10000000149011612D;
						v.z *= 0.5D;
						v.scale(0.29999999999999999D);
						((RealFlightModel)((Aircraft)actor).FM).gunMomentum(v, true);
					}
				}
			}
		} else if (getSpeed(v) > 0.0D)
			v1.add(v);
		if ((actor instanceof NetGunner) && World.isPlayerGunner())
			World.cur().scoreCounter.bulletsFire += prop.bulletsCluster;
		Bullet bullet = createNextBullet(vWind, i, this, loc1, v1, Time.current()
				+ (long)(int)(prop.bullet[i].timeLife * 1000F));
		bullet.move((float)((1.0D - d) * (double)Time.tickLenFs()));
		bullet.bMoved = true;
		bullet.flags |= 0x1000;
		if (Config.isUSE_RENDER() && bulletNum % prop.traceFreq == 0) {
			//TODO: +++ FX Repack Mod +++
            if(!bNoTrace)
			//TODO: --- FX Repack Mod ---
			bullet.flags |= 0x80000000;
			//TODO: +++ FX Repack Mod +++
			if (!bNoSmoke && prop.bullet[i].traceTrail != null) {
//			if (prop.bullet[i].traceTrail != null) {
			//TODO: --- FX Repack Mod ---
				com.maddox.il2.engine.Camera3D camera3d = Main3D.cur3D().camera3D;
				if (Actor.isValid(camera3d)) {
					double d3 = 1000000D;
					Point3d point3d1 = loc1.getPoint();
					v1.scale(prop.bullet[i].timeLife);
					p1.add(point3d1, v1);
					Point3d point3d2 = ((Actor)(camera3d)).pos.getAbsPoint();
					double d5 = p1.x - point3d1.x;
					double d6 = p1.y - point3d1.y;
					double d7 = p1.z - point3d1.z;
					double d8 = d5 * d5 + d6 * d6 + d7 * d7;
					double d9 = ((point3d2.x - point3d1.x) * d5 + (point3d2.y - point3d1.y) * d6 + (point3d2.z - point3d1.z)
							* d7)
							/ d8;
					if (d9 > 0.0D && d9 < 1.0D) {
						double d10 = point3d1.x + d9 * d5;
						double d12 = point3d1.y + d9 * d6;
						double d14 = point3d1.z + d9 * d7;
						double d15 = (d10 - point3d2.x) * (d10 - point3d2.x) + (d12 - point3d2.y) * (d12 - point3d2.y)
								+ (d14 - point3d2.z) * (d14 - point3d2.z);
						if (d15 > d3)
							return;
					} else {
						double d11 = (p1.x - point3d2.x) * (p1.x - point3d2.x) + (p1.y - point3d2.y) * (p1.y - point3d2.y)
								+ (p1.z - point3d2.z) * (p1.z - point3d2.z);
						double d13 = (point3d1.x - point3d2.x) * (point3d1.x - point3d2.x) + (point3d1.y - point3d2.y)
								* (point3d1.y - point3d2.y) + (point3d1.z - point3d2.z) * (point3d1.z - point3d2.z);
						if (d13 > d3 && d11 > d3)
							return;
					}
					//TODO: +++ FX Repack Mod +++
//					bullet.effTrail = Eff3DActor.NewPosMove(pos.getAbs(), 1.0F, prop.bullet[i].traceTrail, -1F);
                    boolean TrailNow = World.Rnd().nextInt(100) < 17;
                    int TrailType = 0;
                    String TraceTrail = prop.bullet[i].traceTrail;
                    if(actor instanceof Aircraft)
                    {
                        if(actor != World.getPlayerAircraft())
                        {
                            if(pos.getAbs().getZ() > 1000D)
                            {
                                if(!TrailNow)
                                    TrailType = 2;
                                else
                                    TrailType = 3;
                            } else
                            if(pos.getAbs().getZ() > 300D)
                                TrailType = 3;
                            else
                            if(TrailNow)
                                TrailType = 3;
                        } else
                        if(pos.getAbs().getZ() > 1000D)
                            TrailType = 1;
                        else
                        if(pos.getAbs().getZ() > 300D)
                        {
                            if(!TrailNow)
                                TrailType = 2;
                            else
                                TrailType = 1;
                        } else
                        if(TrailNow)
                            TrailType = 2;
                        else
                            TrailType = 3;
                    } else
                    if(TrailNow)
                        TrailType = 3;
                    switch(TrailType)
                    {
                    case 1: // '\001'
                        bullet.effTrail = Eff3DActor.NewPosMove(pos.getAbs(), 1.0F, TraceTrail, -1F);
                        break;

                    case 2: // '\002'
                        if(TraceTrail.equalsIgnoreCase("Effects/Smokes/SmokeBlack_BuletteTrail.eff"))
                            TraceTrail = "Effects/Smokes/SmokeBlack_BuletteTrailMed.eff";
                        else
                        if(TraceTrail.equalsIgnoreCase("3DO/Effects/Tracers/TrailCurved.eff"))
                            TraceTrail = "3DO/Effects/Tracers/TrailCurvedMed.eff";
                        else
                            TraceTrail = "3DO/Effects/Tracers/TrailThinMed.eff";
                        bullet.effTrail = Eff3DActor.NewPosMove(pos.getAbs(), 1.0F, TraceTrail, -1F);
                        break;

                    default:
                        if(TraceTrail.equalsIgnoreCase("Effects/Smokes/SmokeBlack_BuletteTrail.eff"))
                            TraceTrail = "Effects/Smokes/SmokeBlack_BuletteTrailLow.eff";
                        else
                        if(TraceTrail.equalsIgnoreCase("3DO/Effects/Tracers/TrailCurved.eff"))
                            TraceTrail = "3DO/Effects/Tracers/TrailCurvedLow.eff";
                        else
                            TraceTrail = "3DO/Effects/Tracers/TrailThinLow.eff";
                        bullet.effTrail = Eff3DActor.NewPosMove(pos.getAbs(), 1.0F, TraceTrail, -1F);
                        break;
                    }
					//TODO: --- FX Repack Mod ---
				}
			}
		}
	}

	private boolean nameEQ(HierMesh hiermesh, int i, int j) {
		if (hiermesh == null)
			return false;
		hiermesh.setCurChunk(i);
		String s = hiermesh.chunkName();
		hiermesh.setCurChunk(j);
		String s1 = hiermesh.chunkName();
		int l = Math.min(s.length(), s1.length());
		for (int k = 0; k < l; k++) {
			char c = s.charAt(k);
			if (c == '_')
				return true;
			if (c != s1.charAt(k))
				return false;
		}

		return true;
	}

	public BulletEmitter detach(HierMesh hiermesh, int i) {
		if (isDestroyed())
			return GunEmpty.get();
		if (i == -1 || nameEQ(hiermesh, i, chunkIndx)) {
			destroy();
			return GunEmpty.get();
		} else {
			return this;
		}
	}

	public float TravelTime(Point3d point3d, Point3d point3d1) {
		float f = (float)point3d.distance(point3d1);
		if (f < prop.aimMinDist || f > prop.aimMaxDist)
			return -1F;
		else
			return f / prop.bullet[0].speed;
	}

	public boolean FireDirection(Point3d point3d, Point3d point3d1, Vector3d vector3d) {
		float f = (float)point3d.distance(point3d1);
		if (f < prop.aimMinDist || f > prop.aimMaxDist) {
			return false;
		} else {
			vector3d.set(point3d1);
			vector3d.sub(point3d);
			vector3d.scale(1.0F / f);
			return true;
		}
	}

	//TODO: +++ FX Repack Mod +++
    private static boolean bNoTrace = false;
    private static boolean bNoSmoke = false;
	//TODO: --- FX Repack Mod ---
	private static Loc loc = new Loc();
	private static Vector3d v = new Vector3d();
	private static Vector3d v1 = new Vector3d();
	private static Vector3d v2 = new Vector3d();
	private static Point3d p1 = new Point3d();
	private static Vector3d vWind = new Vector3d();

}
