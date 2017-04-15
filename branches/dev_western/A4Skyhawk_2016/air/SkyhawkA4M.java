
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            Skyhawk, TypeTankerDrogue, TypeDockable, Aircraft, 
//            PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, 
//            NetAircraft, Cockpit, TypeGSuit

public class SkyhawkA4M extends SkyhawkFuelReceiver
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGuidedBombCarrier, TypeLaserSpotter
{

    public SkyhawkA4M()
    {
        bChangedPit = false;
        guidedMissileUtils = null;
        bToFire = false;
        trgtPk = 0.0F;
        trgtAI = null;
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        lastCommonThreatActive = 0L;
        intervalCommonThreat = 1000L;
        lastRadarLockThreatActive = 0L;
        intervalRadarLockThreat = 1000L;
        lastMissileLaunchThreatActive = 0L;
        intervalMissileLaunchThreat = 1000L;
        guidedMissileUtils = new GuidedMissileUtils(this);
        bHasLAUcaps = false;
//        DragChuteControl = 0.0F;
        isGuidingBomb = false;
        removeChuteTimer = -1L;
        t1 = 0L;
        tangate = 0.0F;
        azimult = 0.0F;
        tf = 0L;
        bLASER = false;
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 26)
        {
            if(hold && t1 + 200L < Time.current() && bLASER)
            {
                hold = false;
                HUD.log("Lazer Unlock");
                t1 = Time.current();
            }
            if(!hold && t1 + 200L < Time.current() && bLASER)
            {
                hold = true;
                HUD.log("Lazer Lock");
                t1 = Time.current();
            }
        }
    }

    public void typeBomberAdjDistancePlus()
    {
        if(bLASER)
        {
            azimult++;
            tf = Time.current();
        }
    }

    public void typeBomberAdjDistanceMinus()
    {
        if(bLASER)
        {
            azimult--;
            tf = Time.current();
        }
    }

    public void typeBomberAdjSideslipPlus()
    {
        if(bLASER)
        {
            tangate++;
            tf = Time.current();
        }
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(bLASER)
        {
            tangate--;
            tf = Time.current();
        }
    }

    public void updatecontrollaser()
    {
        if(tf + 5L <= Time.current())
        {
            tangate = 0.0F;
            azimult = 0.0F;
        }
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(bLASER)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Engaged");
                ((FlightModelMain) FM).AP.setStabAltitude(2000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Released");
                ((FlightModelMain) FM).AP.setStabAltitude(false);
            }
    }

    private void laser(Point3d point3d)
    {
        point3d.z = World.land().HQ(point3d.x, point3d.y);
        Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(point3d.x, point3d.y, point3d.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
        eff3dactor.postDestroy(Time.current() + 1500L);
    }

    private void LASER()
    {
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if(!(actor instanceof Aircraft) && !(actor instanceof ArtilleryGeneric) && !(actor instanceof CarGeneric) && !(actor instanceof BigshipGeneric) && !(actor instanceof ShipGeneric) && !(actor instanceof TankGeneric) || (actor instanceof StationaryGeneric) || (actor instanceof TypeLaserSpotter) || actor.pos.getAbsPoint().distance(pos.getAbsPoint()) >= 20000D)
                continue;
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            actor.pos.getAbs(point3d, orient);
//            locate.set(point3d, orient);
            Eff3DActor eff3dactor = Eff3DActor.New(actor, null, new Loc(), 1.0F, "effects/Explodes/Air/Zenitka/Germ_88mm/Glow.eff", 1.0F);
            eff3dactor.postDestroy(Time.current() + 1500L);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
            if(actor instanceof Aircraft)
                lightpointactor.light.setEmit(8F, 50F);
            else
            if(!(actor instanceof ArtilleryGeneric))
                lightpointactor.light.setEmit(5F, 30F);
            else
                lightpointactor.light.setEmit(3F, 10F);
            eff3dactor.draw.lightMap().put("light", lightpointactor);
        }

    }

    public boolean typeGuidedBombCisMasterAlive()
    {
        return isMasterAlive;
    }

    public void typeGuidedBombCsetMasterAlive(boolean flag)
    {
        isMasterAlive = flag;
    }

    public boolean typeGuidedBombCgetIsGuiding()
    {
        return isGuidingBomb;
    }

    public void typeGuidedBombCsetIsGuiding(boolean flag)
    {
        isGuidingBomb = flag;
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof Pylon_USTER_gn16)
                        ((Pylon_USTER_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_USMERfw_gn16)
                        ((Pylon_USMERfw_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_gn16)
                        ((Pylon_LAU10_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                        ((Pylon_LAU10_Cap_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU7_gn16)
                        ((Pylon_LAU7_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU118_gn16)
                        ((Pylon_LAU118_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof BombGunCBU24_gn16)
                        ((BombGunCBU24_gn16)FM.CT.Weapons[i][j]).matGray();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk_gn16)
                        ((FuelTankGun_TankSkyhawk_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkNF_gn16)
                        ((FuelTankGun_TankSkyhawkNF_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk400gal_gn16)
                        ((FuelTankGun_TankSkyhawk400gal_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_Mk4HIPEGpod_gn16)
                        ((Pylon_Mk4HIPEGpod_gn16)FM.CT.Weapons[i][j]).matHighvis();

                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                        bHasLAUcaps = true;
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16)
                        bHasLAUcaps = true;
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16)
                        bHasLAUcaps = true;

                    if(FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16)
                        counterFlareList.add(FM.CT.Weapons[i][j]);
                    else if(FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16)
                        counterChaffList.add(FM.CT.Weapons[i][j]);
                }
            }
    }

    private void checkDeleteLAUcaps()
    {
        if(FM.CT.saveWeaponControl[2])
        {
            for(int i = 0; i < FM.CT.Weapons.length; i++)
                if(FM.CT.Weapons[i] != null)
                {
                    for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                        if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                            ((Pylon_LAU10_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                        else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16)
                            ((Pylon_LAU130_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                        else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16)
                            ((Pylon_LAU131_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                }
            bHasLAUcaps = false;
        }
    }

    public void backFire()
    {
        if(counterFlareList.isEmpty())
            hasFlare = false;
        else
        {
            if(Time.current() > lastFlareDeployed + 700L)
            {
                ((RocketGunFlare_gn16)counterFlareList.get(0)).shots(1);
                hasFlare = true;
                lastFlareDeployed = Time.current();
                if(!((RocketGunFlare_gn16)counterFlareList.get(0)).haveBullets())
                    counterFlareList.remove(0);
            }
        }
        if(counterChaffList.isEmpty())
            hasChaff = false;
        else
        {
            if(Time.current() > lastChaffDeployed + 1300L)
            {
                ((RocketGunChaff_gn16)counterChaffList.get(0)).shots(1);
                hasChaff = true;
                lastChaffDeployed = Time.current();
                if(!((RocketGunChaff_gn16)counterChaffList.get(0)).haveBullets())
                    counterChaffList.remove(0);
            }
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

    public void setCommonThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = curTime;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = curTime;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = curTime;
            doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat()
    {
    }

    private void doDealRadarLockThreat()
    {
    }

    private void doDealMissileLaunchThreat()
    {
    }

    public Actor getMissileTarget()
    {
        return guidedMissileUtils.getMissileTarget();
    }

    public Point3f getMissileTargetOffset()
    {
        return guidedMissileUtils.getSelectedActorOffset();
    }

    public int getMissileLockState()
    {
        return guidedMissileUtils.getMissileLockState();
    }

    private float getMissilePk()
    {
        float thePk = 0.0F;
        guidedMissileUtils.setMissileTarget(guidedMissileUtils.lookForGuidedMissileTarget(((Interpolate) (super.FM)).actor, guidedMissileUtils.getMaxPOVfrom(), guidedMissileUtils.getMaxPOVto(), guidedMissileUtils.getPkMaxDist()));
        if(Actor.isValid(guidedMissileUtils.getMissileTarget()))
            thePk = guidedMissileUtils.Pk(((Interpolate) (super.FM)).actor, guidedMissileUtils.getMissileTarget());
        return thePk;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        FM.Sq.dragChuteCx = 3.2F;
        bHasDeployedDragChute = false;
        t1 = Time.current();
        FM.turret[0].bIsAIControlled = false;
        FM.CT.bHasBombSelect = true;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(bLASER)
            LASER();
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
        if(super.backfire)
            backFire();
        if(bLASER)
            laser(spot);
        updatecontrollaser();
        updateDragChute();
    }

    public void missionStarting()
    {
        super.missionStarting();

        checkChangeWeaponColors();
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

    private float llpos;
    public boolean bChangedPit;
    public boolean bToFire;
    private float trgtPk;
    private Actor trgtAI;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive;
    private long intervalCommonThreat;
    private long lastRadarLockThreatActive;
    private long intervalRadarLockThreat;
    private long lastMissileLaunchThreatActive;
    private long intervalMissileLaunchThreat;
    private boolean bHasLAUcaps;
    public float azimult;
    public float tangate;
    public long tf;
    public float v;
    public float h;
    private long t1;
    public boolean hold;
//    public float DragChuteControl;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    private boolean isGuidingBomb;
    private boolean isMasterAlive;
//    private static Loc locate = new Loc();
    public boolean bLASER;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Skyhawk");
        Property.set(class1, "meshName", "3DO/Plane/SkyhawkA4M(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/a4m.fmd:SKYHAWKS");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitSkyhawkA4F.class
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
            "_CANNON01",        "_CANNON02",        "_MGUN01",          "_MGUN02",          "_ExternalDev01",   "_ExternalDev02",   "_ExternalDev03",   "_ExternalDev04",   "_ExternalDev05",   "_ExternalDev01",
            "_ExternalDev02",   "_ExternalDev03",   "_ExternalDev04",   "_ExternalDev05",   "_ExternalBomb02",  "_ExternalBomb03",  "_ExternalBomb04",  "_ExternalBomb05",  "_ExternalBomb01",  "_ExternalBomb06",
            "_ExternalBomb07",  "_ExternalBomb08",  "_ExternalBomb09",  "_ExternalBomb10",  "_ExternalBomb11",  "_ExternalBomb12",  "_Bomb13",          "_ExternalBomb14",  "_Bomb15",          "_ExternalBomb16",
            "_Bomb17",          "_ExternalBomb18",  "_ExternalRock01",  "_ExternalRock01",  "_ExternalRock02",  "_ExternalRock02",  "_ExternalRock03",  "_ExternalRock03",  "_ExternalRock04",  "_ExternalRock04",
            "_ExternalDev06",   "_ExternalDev07",   "_ExternalDev08",   "_ExternalDev09",   "_ExternalDev06",   "_ExternalDev07",   "_ExternalDev08",   "_ExternalDev09",   "_ExternalDev10",   "_ExternalDev11",
            "_ExternalDev12",   "_ExternalDev10",   "_ExternalDev11",   "_ExternalDev12",   "_Rock05",          "_Rock06",          "_Rock07",          "_Rock08",          "_Rock09",          "_Rock10",
            "_Rock11",          "_Rock12",          "_Rock13",          "_Rock14",          "_Rock15",          "_Rock16",          "_Rock17",          "_Rock18",          "_Rock19",          "_Rock20",
            "_Rock21",          "_Rock22",          "_Rock23",          "_Rock24",          "_Rock25",          "_Rock26",          "_Rock27",          "_Rock28",          "_ExternalRock43",  "_ExternalRock43",
            "_ExternalRock29",  "_ExternalRock29",  "_ExternalRock30",  "_ExternalRock30",  "_ExternalRock31",  "_ExternalRock31",  "_ExternalRock32",  "_ExternalRock32",  "_ExternalRock33",  "_ExternalRock33",
            "_ExternalRock34",  "_ExternalRock34",  "_ExternalRock35",  "_ExternalRock35",  "_ExternalRock36",  "_ExternalRock36",  "_ExternalRock37",  "_ExternalRock37",  "_ExternalRock38",  "_ExternalRock38",
            "_ExternalRock39",  "_ExternalRock39",  "_ExternalRock40",  "_ExternalRock40",  "_Rock41",          "_Rock42",          "_Bomb19",          "_Bomb20",          "_ExternalBomb21",  "_ExternalBomb22",
            "_Bomb23",          "_ExternalBomb24",  "_ExternalBomb25",  "_Bomb26",          "_ExternalBomb27",  "_ExternalBomb28",  "_Flare01",         "_Flare02",         "_Chaff01"
        });
        String s = "";
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 119;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x300Dt+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9L+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9L+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82+2xZuni+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82HD+2xZuni+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xCBU24+2xZuni+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xCBU24+2xLAU61+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+2xZuni+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+2xLAU61+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83+2xZuni+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77Napalm+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77Napalm+2xLAU61+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77Napalm+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77Napalm+2xLAU68+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 7);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 7);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk77Napalm+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+2xGBU12+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xZuni+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xZuni+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xLAU61+2xLAU68+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_TC_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_TC_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_Cap_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 7);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 7);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xLAU61+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_TC_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_Cap_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+2xZuni+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+2xZuni+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xCBU24+2xZuni+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk20+2xZuni+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_Cap_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82HD+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xCBU24+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk20+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xMk83+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xMk84+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+2xGBU12+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xGBU10+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77Napalm+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77Napalm+2xMk82+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77Napalm+2xLAU61+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk77Napalm_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xHIPEG700+2xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm700rpm", 750);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm700rpm", 750);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xHIPEG4000+2xAIM9L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xHIPEG700+2xAIM9L+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm700rpm", 750);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm700rpm", 750);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xHIPEG4000+2xAIM9L+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xHIPEG700+2xLAU68+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm700rpm", 750);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm700rpm", 750);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_TC_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_TC_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_Cap_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 7);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 7);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xHIPEG4000+2xLAU68+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_TC_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_TC_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_Cap_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_Cap_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 7);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 7);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xHIPEG4000+2xGBU12+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunUSMk4HIPEG20mm4000rpm", 750);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_Mk4HIPEGpod_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82+2xAIM9L+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82HD+2xAIM9L+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xCBU24+2xAIM9L+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+2xAIM9L+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+2xAIM9L+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+2xAIM9L+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xCBU24+2xAIM9L+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk20+2xAIM9L+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU12+2xAIM9L+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+2xAIM9L+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+2x300Dt+Flare";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+2x300Dt+Flare";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xCBU24+2x300Dt+Flare";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USMERfw_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_SUU25Flare_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunCBU24_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunParaFlareLUU2disp_gn16", 8);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM12C+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunAGM12C_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65B+2xZuni+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(4, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(4, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65B+2xLAU68+1x400Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk400gal_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU131_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 7);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 7);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(4, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(4, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xZuni+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(4, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(4, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xGBU12+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(4, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(4, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+2xZuni+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(4, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(4, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+2xMk20+1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(4, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(4, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xB57nuke5ktpara+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunB57nuke5ktpara_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xB57nuke10ktpara+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunB57nuke10ktpara_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xB61nuke03ktpara+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunColtMk12ki", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunB61nuke03kt_gn16", 1);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - SkyhawkA4M : " + s);
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}