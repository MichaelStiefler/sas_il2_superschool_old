package com.maddox.il2.objects.air;

import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.BombGunCBU24_gn16;
import com.maddox.il2.objects.weapons.BombGunGBU10_Mk84LGB_gn16;
import com.maddox.il2.objects.weapons.BombGunGBU12_Mk82LGB_gn16;
import com.maddox.il2.objects.weapons.BombGunGBU16_Mk83LGB_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawk400gal_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawkNF_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawk_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU10_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU10_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU118_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU130_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU131_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU7_gn16;
import com.maddox.il2.objects.weapons.Pylon_Mk4HIPEGpod_gn16;
import com.maddox.il2.objects.weapons.Pylon_USMERfw_gn16;
import com.maddox.il2.objects.weapons.Pylon_USMERmd_gn16;
import com.maddox.il2.objects.weapons.Pylon_USTER_gn16;
import com.maddox.il2.objects.weapons.RocketGunChaff_gn16;
import com.maddox.il2.objects.weapons.RocketGunFlare_gn16;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class SkyhawkA4M extends SkyhawkFuelReceiver
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeLaserDesignator
{

    public SkyhawkA4M()
    {
        LaserLoc1 = new Loc();
        LaserP1 = new Point3d();
        LaserP2 = new Point3d();
        LaserPL = new Point3d();
        bLaserOn = false;
        bLGBengaged = false;
        bHasPaveway = false;
        tLastLaserUpdate = -1L;
        tLastLGBcheck = -1L;
        bChangedPit = false;
        guidedMissileUtils = null;
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
        guidedMissileUtils = new GuidedMissileUtils(this);
        bHasLAUcaps = false;
        removeChuteTimer = -1L;
        tLastLaserLockKeyInput = 0L;
        tangateLaserHead = 0;
        azimultLaserHead = 0;
        tangateLaserHeadOffset = 0;
        azimultLaserHeadOffset = 0;
        laserSpotPos = new Point3d();
        laserSpotPosSaveHold = new Point3d();
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 26 && getLaserOn())
        {
            if(holdLaser && tLastLaserLockKeyInput + 200L < Time.current())
            {
                holdLaser = false;
                holdFollowLaser = false;
                actorFollowing = null;
                tangateLaserHeadOffset = 0;
                azimultLaserHeadOffset = 0;
                HUD.log("Laser Pos Unlock");
                tLastLaserLockKeyInput = Time.current();
            }
            if(!holdLaser && tLastLaserLockKeyInput + 200L < Time.current())
            {
                holdLaser = true;
                holdFollowLaser = false;
                actorFollowing = null;
                laserSpotPosSaveHold.set(getLaserSpot());
                HUD.log("Laser Pos Lock");
                tLastLaserLockKeyInput = Time.current();
            }
        }
        if(i == 29)
            setLaserOn(!bLaserOn);
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(bLaserOn)
            if(holdLaser)
            {
                tangateLaserHeadOffset = 4;
            } else
            {
                tangateLaserHead += 5;
                if(tangateLaserHead > 500)
                    tangateLaserHead = 500;
            }
    }

    public void typeBomberAdjAltitudeMinus()
    {
        if(bLaserOn)
            if(holdLaser)
            {
                tangateLaserHeadOffset = -4;
            } else
            {
                tangateLaserHead -= 5;
                if(tangateLaserHead < -1000)
                    tangateLaserHead = -1000;
            }
    }

    public void typeBomberAdjSideslipPlus()
    {
        if(bLaserOn)
            if(holdLaser)
            {
                azimultLaserHeadOffset = 4;
            } else
            {
                azimultLaserHead += 5;
                if(azimultLaserHead > 1000)
                    azimultLaserHead = 1000;
            }
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(bLaserOn)
            if(holdLaser)
            {
                azimultLaserHeadOffset = -4;
            } else
            {
                azimultLaserHead -= 5;
                if(azimultLaserHead < -1000)
                    azimultLaserHead = -1000;
            }
    }

    public void laserUpdate()
    {
        if(Time.current() == tLastLaserUpdate)
            return;
        tLastLaserUpdate = Time.current();
        Orient orient = new Orient();
        Point3d point3d = new Point3d();
        pos.getAbs(point3d, orient);
        float f = orient.getRoll();
        if(f > 180F)
            f -= 360F;
        if(f < -180F)
            f += 360F;
        float f1 = cvt(f, -50F, 50F, 50F, -50F);
        if(holdLaser)
        {
            float f2 = orient.getPitch();
            float f3 = 0.0F;
            if(f2 > 90F)
                f3 = f2 - 360F;
            else
                f3 = f2;
            Point3d point3d1 = new Point3d();
            point3d1.set(laserSpotPosSaveHold);
            point3d1.sub(point3d);
            double d = point3d1.x;
            double d1 = point3d1.y;
            double d2 = point3d1.z;
            double d3 = Math.abs(Math.sqrt(d * d + d1 * d1));
            float f4 = (float)Math.toDegrees(Math.atan(d2 / d3)) - f3;
            float f5 = 0.0F;
            if(d > 0.0D)
                f5 = (float)Math.toDegrees(Math.atan(d1 / d)) - orient.getYaw();
            else
                f5 = (180F + (float)Math.toDegrees(Math.atan(d1 / d))) - orient.getYaw();
            if(f5 > 180F)
                f5 -= 360F;
            if(f5 < -180F)
                f5 += 360F;
            azimultLaserHead = (int)(f5 / 0.03F) + azimultLaserHeadOffset;
            if(azimultLaserHead > 1000)
                azimultLaserHead = 1000;
            if(azimultLaserHead < -1000)
                azimultLaserHead = -1000;
            tangateLaserHead = (int)(f4 / 0.03F) + tangateLaserHeadOffset;
            if(tangateLaserHead > 500)
                tangateLaserHead = 500;
            if(tangateLaserHead < -1000)
                tangateLaserHead = -1000;
        }
        hierMesh().chunkSetAngles("LaserMshRoll_D0", 0.0F, 0.0F, f1);
        hierMesh().chunkSetAngles("LaserMsh_D0", -(float)tangateLaserHead * 0.03F, -(float)azimultLaserHead * 0.03F, 0.0F);
        this.pos.setUpdateEnable(true);
        this.pos.getRender(Actor._tmpLoc);
        LaserHook = new HookNamed(this, "_Laser1");
        LaserLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        LaserHook.computePos(this, Actor._tmpLoc, LaserLoc1);
        LaserLoc1.get(LaserP1);
        LaserLoc1.set(30000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        LaserHook.computePos(this, Actor._tmpLoc, LaserLoc1);
        LaserLoc1.get(LaserP2);
        if(Landscape.rayHitHQ(LaserP1, LaserP2, LaserPL))
        {
            LaserPL.z -= 0.94999999999999996D;
            LaserP2.interpolate(LaserP1, LaserPL, 1.0F);
            setLaserSpot(LaserP2);
            Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(LaserP2.x, LaserP2.y, LaserP2.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
            eff3dactor.postDestroy(Time.current() + 1500L);
        }
        if(azimultLaserHeadOffset != 0 || tangateLaserHeadOffset != 0)
        {
            azimultLaserHeadOffset = 0;
            tangateLaserHeadOffset = 0;
            laserSpotPosSaveHold.set(LaserP2);
        }
    }

    private void checkgroundlaser()
    {
        boolean flag = false;
        double d = 0.0D;
        float f = 0.0F;
        float f3 = 0.0F;
        float f5 = 0.0F;
        if(getLaserOn())
        {
            Point3d point3d = new Point3d();
            point3d = getLaserSpot();
            if(Main.cur().clouds == null || Main.cur().clouds.getVisibility(point3d, pos.getAbsPoint()) >= 1.0F)
            {
                double d1 = pos.getAbsPoint().distance(point3d);
                if(d1 <= maxPavewayDistance)
                {
                    float f1 = angleBetween(this, point3d);
                    if(f1 <= maxPavewayFOVfrom)
                        flag = true;
                }
            }
        }
        if(!flag)
        {
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                if(!(actor instanceof TypeLaserDesignator) || !((TypeLaserDesignator)actor).getLaserOn() || actor.getArmy() != getArmy())
                    continue;
                Point3d point3d1 = new Point3d();
                point3d1 = ((TypeLaserDesignator)actor).getLaserSpot();
                if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d1, pos.getAbsPoint()) < 1.0F)
                    continue;
                double d2 = pos.getAbsPoint().distance(point3d1);
                if(d2 > maxPavewayDistance)
                    continue;
                float f2 = angleBetween(this, point3d1);
                if(f2 > maxPavewayFOVfrom)
                    continue;
                float f4 = 1.0F / f2 / (float)(d2 * d2);
                if(f4 > f5)
                {
                    f5 = f4;
                    flag = true;
                }
            }

        }
        setLaserArmEngaged(flag);
    }

    private static float angleBetween(Actor actor, Point3d point3d)
    {
        float f = 180.1F;
        double d = 0.0D;
        Loc loc = new Loc();
        Point3d point3d1 = new Point3d();
        Vector3d vector3d = new Vector3d();
        Vector3d vector3d1 = new Vector3d();
        actor.pos.getAbs(loc);
        loc.get(point3d1);
        vector3d.sub(point3d, point3d1);
        d = vector3d.length();
        vector3d.scale(1.0D / d);
        vector3d1.set(1.0D, 0.0D, 0.0D);
        loc.transform(vector3d1);
        d = vector3d1.dot(vector3d);
        f = Geom.RAD2DEG((float)Math.acos(d));
        return f;
    }

    public Point3d getLaserSpot()
    {
        return laserSpotPos;
    }

    public boolean setLaserSpot(Point3d point3d)
    {
        laserSpotPos.set(point3d);
        return true;
    }

    public boolean getLaserOn()
    {
        return bLaserOn;
    }

    public boolean setLaserOn(boolean flag)
    {
        if(bLaserOn != flag)
            if(!bLaserOn)
            {
                if(this == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: ON");
                holdLaser = false;
                holdFollowLaser = false;
                actorFollowing = null;
                tangateLaserHead = 0;
                azimultLaserHead = 0;
                tangateLaserHeadOffset = 0;
                azimultLaserHeadOffset = 0;
            } else
            {
                if(this == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: OFF");
                holdLaser = false;
                holdFollowLaser = false;
                actorFollowing = null;
                tangateLaserHead = 0;
                azimultLaserHead = 0;
                tangateLaserHeadOffset = 0;
                azimultLaserHeadOffset = 0;
            }
        return bLaserOn = flag;
    }

    public boolean getLaserArmEngaged()
    {
        return bLGBengaged;
    }

    public boolean setLaserArmEngaged(boolean flag)
    {
        if(bLGBengaged != flag)
            if(!bLGBengaged)
            {
                if(this == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser Bomb: Engaged");
            } else
            if(this == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser Bomb: Disengaged");
        return bLGBengaged = flag;
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
        {
            if(FM.CT.Weapons[i] == null)
                continue;
            for(int j = 0; j < FM.CT.Weapons[i].length; j++)
            {
                if(FM.CT.Weapons[i][j] instanceof Pylon_USTER_gn16)
                    ((Pylon_USTER_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_USMERfw_gn16)
                    ((Pylon_USMERfw_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_USMERmd_gn16)
                    ((Pylon_USMERmd_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_gn16)
                    ((Pylon_LAU10_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                    ((Pylon_LAU10_Cap_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU7_gn16)
                    ((Pylon_LAU7_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU118_gn16)
                    ((Pylon_LAU118_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof BombGunCBU24_gn16)
                    ((BombGunCBU24_gn16)FM.CT.Weapons[i][j]).matGray();
                else
                if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk_gn16)
                    ((FuelTankGun_TankSkyhawk_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkNF_gn16)
                    ((FuelTankGun_TankSkyhawkNF_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk400gal_gn16)
                    ((FuelTankGun_TankSkyhawk400gal_gn16)FM.CT.Weapons[i][j]).matHighvis();
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_Mk4HIPEGpod_gn16)
                    ((Pylon_Mk4HIPEGpod_gn16)FM.CT.Weapons[i][j]).matHighvis();
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                    bHasLAUcaps = true;
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16)
                    bHasLAUcaps = true;
                else
                if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16)
                    bHasLAUcaps = true;
                if(FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16)
                {
                    counterFlareList.add(FM.CT.Weapons[i][j]);
                    continue;
                }
                if(FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16)
                {
                    counterChaffList.add(FM.CT.Weapons[i][j]);
                    continue;
                }
                if((FM.CT.Weapons[i][j] instanceof BombGunGBU10_Mk84LGB_gn16) || (FM.CT.Weapons[i][j] instanceof BombGunGBU12_Mk82LGB_gn16) || (FM.CT.Weapons[i][j] instanceof BombGunGBU16_Mk83LGB_gn16))
                {
                    bHasPaveway = true;
                    FM.bNoDiveBombing = true;
                }
            }

        }

    }

    private void checkDeleteLAUcaps()
    {
        if(FM.CT.saveWeaponControl[2])
        {
            for(int i = 0; i < FM.CT.Weapons.length; i++)
            {
                if(FM.CT.Weapons[i] == null)
                    continue;
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                    {
                        ((Pylon_LAU10_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                        continue;
                    }
                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16)
                    {
                        ((Pylon_LAU130_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                        continue;
                    }
                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16)
                        ((Pylon_LAU131_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                }

            }

            bHasLAUcaps = false;
        }
    }

    public void backFire()
    {
        if(counterFlareList.isEmpty())
            hasFlare = false;
        else
        if(Time.current() > lastFlareDeployed + 700L)
        {
            ((RocketGunFlare_gn16)counterFlareList.get(0)).shots(1);
            hasFlare = true;
            lastFlareDeployed = Time.current();
            if(!((RocketGunFlare_gn16)counterFlareList.get(0)).haveBullets())
                counterFlareList.remove(0);
        }
        if(counterChaffList.isEmpty())
            hasChaff = false;
        else
        if(Time.current() > lastChaffDeployed + 1300L)
        {
            ((RocketGunChaff_gn16)counterChaffList.get(0)).shots(1);
            hasChaff = true;
            lastChaffDeployed = Time.current();
            if(!((RocketGunChaff_gn16)counterChaffList.get(0)).haveBullets())
                counterChaffList.remove(0);
        }
    }

    public long getChaffDeployed()
    {
        if(hasChaff)
            return lastChaffDeployed;
        else
            return 0L;
    }

    public long getFlareDeployed()
    {
        if(hasFlare)
            return lastFlareDeployed;
        else
            return 0L;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        FM.Sq.dragChuteCx = 3.2F;
        bHasDeployedDragChute = false;
        FM.CT.bHasBombSelect = true;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(bHasPaveway && FM.bNoDiveBombing && Time.current() - tLastLGBcheck > 30000L && (FM instanceof Maneuver))
        {
            if(!((Maneuver)FM).hasBombs())
            {
                bHasPaveway = false;
                FM.bNoDiveBombing = false;
            }
            tLastLGBcheck = Time.current();
        }
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void update(float f)
    {
        if(bHasLAUcaps)
            checkDeleteLAUcaps();
        super.update(f);
        guidedMissileUtils.update();
        if(this.backfire)
            backFire();
        if(bLaserOn)
            laserUpdate();
        if(bHasPaveway)
            checkgroundlaser();
        updateDragChute();
        if((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode()) && (FM instanceof Maneuver) && bHasPaveway && FM.AP.way.curr().Action == 3 && ((Maneuver)FM).hasBombs() && getLaserArmEngaged())
        {
            ((Maneuver)FM).bombsOut = true;
            FM.CT.WeaponControl[3] = true;
            Voice.speakAttackByBombs(this);
        }
    }

    public void missionStarting()
    {
        super.missionStarting();
        checkChangeWeaponColors();
        bLaserOn = false;
        tLastLaserLockKeyInput = 0L;
        tangateLaserHead = 0;
        azimultLaserHead = 0;
        tangateLaserHeadOffset = 0;
        azimultLaserHeadOffset = 0;
        tLastLaserUpdate = -1L;
    }

    private void updateDragChute()
    {
        if(FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteA4M_US/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.8F);
            ((Actor) (chute)).pos.setRel(new Point3d(-8D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && FM.CT.bHasDragChuteControl)
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() > 600F || FM.CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !FM.CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
    }

    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(byte byte0)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
        }
        catch(Exception exception)
        {
            System.out.println("Weapon register error - SkyhawkA4M : Default loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    public boolean bChangedPit;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;
    private boolean bHasLAUcaps;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    private Hook LaserHook;
    private Loc LaserLoc1;
    private Point3d LaserP1;
    private Point3d LaserP2;
    private Point3d LaserPL;
    private long tLastLaserLockKeyInput;
    public int azimultLaserHead;
    public int tangateLaserHead;
    public int azimultLaserHeadOffset;
    public int tangateLaserHeadOffset;
    public boolean holdLaser;
    public boolean holdFollowLaser;
    public Actor actorFollowing;
    private Point3d laserSpotPos;
    private Point3d laserSpotPosSaveHold;
    private boolean bLaserOn;
    private boolean bLGBengaged;
    public boolean bHasPaveway;
    private long tLastLaserUpdate;
    private static float maxPavewayFOVfrom = 45F;
    private static double maxPavewayDistance = 20000D;
    private long tLastLGBcheck;

    static 
    {
        Class class1 = SkyhawkA4M.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Skyhawk");
        Property.set(class1, "meshName", "3DO/Plane/SkyhawkA4M(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/a4m.fmd:SKYHAWKS");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSkyhawkA4F.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 
            9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 
            9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 7, 7, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev01", 
            "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb01", "_ExternalBomb06", 
            "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_Bomb13", "_ExternalBomb14", "_Bomb15", "_ExternalBomb16", 
            "_Bomb17", "_ExternalBomb18", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", 
            "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", 
            "_ExternalDev12", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_Rock09", "_Rock10", 
            "_Rock11", "_Rock12", "_Rock13", "_Rock14", "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", 
            "_Rock21", "_Rock22", "_Rock23", "_Rock24", "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_ExternalRock43", "_ExternalRock43", 
            "_ExternalRock29", "_ExternalRock29", "_ExternalRock30", "_ExternalRock30", "_ExternalRock31", "_ExternalRock31", "_ExternalRock32", "_ExternalRock32", "_ExternalRock33", "_ExternalRock33", 
            "_ExternalRock34", "_ExternalRock34", "_ExternalRock35", "_ExternalRock35", "_ExternalRock36", "_ExternalRock36", "_ExternalRock37", "_ExternalRock37", "_ExternalRock38", "_ExternalRock38", 
            "_ExternalRock39", "_ExternalRock39", "_ExternalRock40", "_ExternalRock40", "_Rock41", "_Rock42", "_Bomb19", "_Bomb20", "_ExternalBomb21", "_ExternalBomb22", 
            "_Bomb23", "_ExternalBomb24", "_ExternalBomb25", "_Bomb26", "_ExternalBomb27", "_ExternalBomb28", "_Flare01", "_Flare02", "_Chaff01"
        });
    }
}
