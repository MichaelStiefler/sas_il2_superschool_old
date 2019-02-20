package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGunAAM3;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;

public class FFR_41MR extends FFR_00
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane
{

    public FFR_41MR()
    {
        upperAngle = 0.0F;
        atackAngle = 0.0F;
        windFoldValue = 0.0F;
        bulletEmitters = null;
        bJettisonPylons = true;
        guidedMissileUtils = null;
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
        long l = Time.current();
        if(l - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = l;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long l = Time.current();
        if(l - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = l;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long l = Time.current();
        if(l - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = l;
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

    private boolean sirenaWarning()
    {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if(World.getPlayerAircraft() == null)
            return false;
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        Aircraft aircraft1 = War.getNearestEnemy(this, 4000F);
        if((aircraft1 instanceof Aircraft) && aircraft1.getArmy() != World.getPlayerArmy() && (aircraft1 instanceof TypeFighterAceMaker) && (aircraft1 instanceof TypeRadarGunsight) && aircraft1 != World.getPlayerAircraft() && aircraft1.getSpeed(vector3d) > 20D)
        {
            this.pos.getAbs(point3d);
            double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y;
            double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z;
            new String();
            new String();
            double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
            new String();
            double d7 = d3 - d;
            double d8 = d4 - d1;
            float f = 57.32484F * (float)Math.atan2(d8, -d7);
            int k = (int)(Math.floor((int)f) - 90D);
            if(k < 0)
                k += 360;
            int l = k - i;
            double d9 = d - d3;
            double d10 = d1 - d4;
            double d11 = Math.sqrt(d6 * d6);
            int i1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
            float f1 = 57.32484F * (float)Math.atan2(i1, d11);
            int j1 = (int)(Math.floor((int)f1) - 90D);
            if(j1 < 0)
                j1 += 360;
            int k1 = j1 - j;
            int l1 = (int)(Math.ceil(((double)i1 * 3.2808399000000001D) / 100D) * 100D);
            if(l1 >= 5280)
                l1 = (int)Math.floor(l1 / 5280);
            bRadarWarning = (double)i1 <= 3000D && (double)i1 >= 50D && k1 >= 195 && k1 <= 345 && Math.sqrt(l * l) >= 120D;
            playSirenaWarning(bRadarWarning);
        } else
        {
            bRadarWarning = false;
            playSirenaWarning(bRadarWarning);
        }
        return true;
    }

    public void playSirenaWarning(boolean flag)
    {
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(5F, 5F, 7F, 7.8F, 9F, 8F);
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        this.FM.Skill = 3;
        this.FM.CT.bHasDragChuteControl = true;
        if(this.thisWeaponsName.startsWith("FULL"))
        {
            hierMesh().chunkVisible("Pyron1_D0", true);
            hierMesh().chunkVisible("Pyron2_D0", true);
            hierMesh().chunkVisible("Pyron3_D0", true);
            hierMesh().chunkVisible("Pyron4_D0", true);
            hierMesh().chunkVisible("Pyron5_D0", true);
            hierMesh().chunkVisible("Pyron6_D0", true);
            hierMesh().chunkVisible("Pyron7_D0", true);
            hierMesh().chunkVisible("Pyron8_D0", true);
            hierMesh().chunkVisible("SubPyron1_D0", true);
            hierMesh().chunkVisible("SubPyron2_D0", true);
            hierMesh().chunkVisible("SubPyron3_D0", true);
            hierMesh().chunkVisible("SubPyron4_D0", true);
            hierMesh().chunkVisible("SubPyron6_D0", false);
            hierMesh().chunkVisible("SubPyron7_D0", false);
        } else
        if(this.thisWeaponsName.startsWith("CA2"))
        {
            hierMesh().chunkVisible("Pyron1_D0", false);
            hierMesh().chunkVisible("Pyron2_D0", false);
            hierMesh().chunkVisible("Pyron3_D0", false);
            hierMesh().chunkVisible("Pyron4_D0", false);
            hierMesh().chunkVisible("Pyron5_D0", true);
            hierMesh().chunkVisible("Pyron6_D0", true);
            hierMesh().chunkVisible("Pyron7_D0", false);
            hierMesh().chunkVisible("Pyron8_D0", false);
            hierMesh().chunkVisible("SubPyron1_D0", false);
            hierMesh().chunkVisible("SubPyron2_D0", false);
            hierMesh().chunkVisible("SubPyron3_D0", false);
            hierMesh().chunkVisible("SubPyron4_D0", false);
        } else
        if(this.thisWeaponsName.startsWith("CA4"))
        {
            hierMesh().chunkVisible("Pyron1_D0", false);
            hierMesh().chunkVisible("Pyron2_D0", false);
            hierMesh().chunkVisible("Pyron3_D0", false);
            hierMesh().chunkVisible("Pyron4_D0", false);
            hierMesh().chunkVisible("Pyron5_D0", true);
            hierMesh().chunkVisible("Pyron6_D0", true);
            hierMesh().chunkVisible("Pyron7_D0", true);
            hierMesh().chunkVisible("Pyron8_D0", true);
            hierMesh().chunkVisible("SubPyron1_D0", false);
            hierMesh().chunkVisible("SubPyron2_D0", false);
            hierMesh().chunkVisible("SubPyron3_D0", false);
            hierMesh().chunkVisible("SubPyron4_D0", false);
        } else
        if(this.thisWeaponsName.startsWith("SA2_2"))
        {
            hierMesh().chunkVisible("Pyron1_D0", true);
            hierMesh().chunkVisible("Pyron2_D0", true);
            hierMesh().chunkVisible("Pyron3_D0", true);
            hierMesh().chunkVisible("Pyron4_D0", true);
            hierMesh().chunkVisible("SubPyron1_D0", false);
            hierMesh().chunkVisible("SubPyron2_D0", false);
            hierMesh().chunkVisible("SubPyron3_D0", false);
            hierMesh().chunkVisible("SubPyron4_D0", false);
        } else
        if(this.thisWeaponsName.startsWith("SA2"))
        {
            hierMesh().chunkVisible("Pyron1_D0", true);
            hierMesh().chunkVisible("Pyron2_D0", true);
            hierMesh().chunkVisible("Pyron3_D0", false);
            hierMesh().chunkVisible("Pyron4_D0", false);
            hierMesh().chunkVisible("SubPyron1_D0", false);
            hierMesh().chunkVisible("SubPyron2_D0", false);
            hierMesh().chunkVisible("SubPyron3_D0", false);
            hierMesh().chunkVisible("SubPyron4_D0", false);
        } else
        if(this.thisWeaponsName.startsWith("SA4"))
        {
            hierMesh().chunkVisible("Pyron1_D0", true);
            hierMesh().chunkVisible("Pyron2_D0", true);
            hierMesh().chunkVisible("Pyron3_D0", false);
            hierMesh().chunkVisible("Pyron4_D0", false);
            hierMesh().chunkVisible("SubPyron1_D0", true);
            hierMesh().chunkVisible("SubPyron2_D0", true);
            hierMesh().chunkVisible("SubPyron3_D0", false);
            hierMesh().chunkVisible("SubPyron4_D0", false);
        } else
        if(this.thisWeaponsName.startsWith("SA8"))
        {
            hierMesh().chunkVisible("Pyron1_D0", true);
            hierMesh().chunkVisible("Pyron2_D0", true);
            hierMesh().chunkVisible("Pyron3_D0", true);
            hierMesh().chunkVisible("Pyron4_D0", true);
            hierMesh().chunkVisible("SubPyron1_D0", true);
            hierMesh().chunkVisible("SubPyron2_D0", true);
            hierMesh().chunkVisible("SubPyron3_D0", true);
            hierMesh().chunkVisible("SubPyron4_D0", true);
        } else
        {
            hierMesh().chunkVisible("Pyron1_D0", false);
            hierMesh().chunkVisible("Pyron2_D0", false);
            hierMesh().chunkVisible("Pyron3_D0", false);
            hierMesh().chunkVisible("Pyron4_D0", false);
            hierMesh().chunkVisible("SubPyron1_D0", false);
            hierMesh().chunkVisible("SubPyron2_D0", false);
            hierMesh().chunkVisible("SubPyron3_D0", false);
            hierMesh().chunkVisible("SubPyron4_D0", false);
        }
        bulletEmitters = new BulletEmitter[weponHookArray.length];
        for(int i = 0; i < weponHookArray.length; i++)
            bulletEmitters[i] = getBulletEmitterByHookName(weponHookArray[i]);

    }

    public void auxPressed(int i)
    {
        if(i == 0 && !bJettisonPylons)
        {
            this.FM.CT.dropExternalStores(true);
            bJettisonPylons = true;
        }
        super.auxPressed(i);
    }

    public boolean dropExternalStores(boolean flag)
    {
        return false;
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        sirenaWarning();
        if(this.FM.CT.getArrestor() > 0.2F)
            if(this.FM.Gears.arrestorVAngle != 0.0F)
            {
                float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
                arrestor = 0.8F * arrestor + 0.2F * f1;
                moveArrestorHook(arrestor);
            } else
            {
                float f2 = (-33F * this.FM.Gears.arrestorVSink) / 57F;
                if(f2 < 0.0F && this.FM.getSpeedKMH() > 60F)
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                if(f2 > 0.0F && this.FM.CT.getArrestor() < 0.95F)
                    f2 = 0.0F;
                if(f2 > 0.2F)
                    f2 = 0.2F;
                if(f2 > 0.0F)
                    arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
                else
                    arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
                if(arrestor < 0.0F)
                    arrestor = 0.0F;
                else
                if(arrestor > 1.0F)
                    arrestor = 1.0F;
                moveArrestorHook(arrestor);
            }
        super.update(f);
    }

    public void updateHook()
    {
        for(int i = 0; i < weponHookArray.length; i++)
            try
            {
                if(bulletEmitters[i] instanceof RocketGunAAM3)
                    ((RocketGunAAM3)bulletEmitters[i]).updateHook(weponHookArray[i]);
            }
            catch(Exception exception) { }

    }

    public void hideWepon(boolean flag)
    {
        for(int i = 0; i < weponHookArray.length; i++)
            try
            {
                if(bulletEmitters[i] instanceof RocketGunAAM3)
                    ((RocketGunAAM3)bulletEmitters[i]).hide(flag);
            }
            catch(Exception exception) { }

    }

    public boolean haveWingWepon()
    {
        for(int i = 0; i < weponHookArray.length; i++)
            try
            {
                if((bulletEmitters[i] instanceof RocketGunAAM3) && i > 0 && i < 25 && ((RocketGunAAM3)bulletEmitters[i]).countBullets() > 0)
                    return true;
            }
            catch(Exception exception) { }

        return false;
    }

    public void changeSupersonic(int i)
    {
        if(!this.FM.isPlayers())
            return;
        if(i == 0)
        {
            super.changeSupersonic(i);
            if(atackAngle != 0.0F)
            {
                if(atackAngle > 0.0F)
                    atackAngle--;
                if(atackAngle < 0.0F)
                    atackAngle++;
                if(Math.abs(atackAngle - 0.0F) < 1.0F)
                    atackAngle = 0.0F;
                hierMesh().chunkSetAngles("WingLOut_D0", 0.0F, 0.0F, atackAngle);
                hierMesh().chunkSetAngles("WingROut_D0", 0.0F, 0.0F, atackAngle);
                this.needUpdateHook = true;
            } else
            if(upperAngle != 0.0F)
            {
                if(upperAngle > 0.0F)
                    upperAngle--;
                if(upperAngle < 0.0F)
                    upperAngle++;
                if(Math.abs(upperAngle - 0.0F) < 1.0F)
                    upperAngle = 0.0F;
                hierMesh().chunkSetAngles("WingLMid_D0", 0.0F, upperAngle, 0.0F);
                hierMesh().chunkSetAngles("WingRMid_D0", 0.0F, -upperAngle, 0.0F);
                this.needUpdateHook = true;
            }
        } else
        {
            super.changeSupersonic(i);
            if(upperAngle != 40F)
            {
                if(upperAngle > 40F)
                    upperAngle--;
                if(upperAngle < 40F)
                    upperAngle++;
                if(Math.abs(upperAngle - 40F) < 1.0F)
                    upperAngle = 40F;
                hierMesh().chunkSetAngles("WingLMid_D0", 0.0F, upperAngle, 0.0F);
                hierMesh().chunkSetAngles("WingRMid_D0", 0.0F, -upperAngle, 0.0F);
                this.needUpdateHook = true;
            } else
            if(atackAngle != 180F)
            {
                if(atackAngle > 180F)
                    atackAngle--;
                if(atackAngle < 180F)
                    atackAngle++;
                if(Math.abs(atackAngle - 180F) < 1.0F)
                    atackAngle = 180F;
                hierMesh().chunkSetAngles("WingLOut_D0", 0.0F, 0.0F, atackAngle);
                hierMesh().chunkSetAngles("WingROut_D0", 0.0F, 0.0F, atackAngle);
                this.needUpdateHook = true;
            }
        }
    }

    public void moveWingFold(float f)
    {
        if((double)f < 0.10000000000000001D)
            f = 0.0F;
        if((double)f > 0.90000000000000002D)
            f = 1.0F;
        super.moveWingFold(f);
        if(windFoldValue != f)
        {
            windFoldValue = f;
            this.needUpdateHook = true;
        }
    }

    public void moveFan(float f)
    {
        super.moveFan(f);
    }

    protected static void weaponsRegister(Class class1, String s, String as[])
    {
        try
        {
            int ai[] = Aircraft.getWeaponTriggersRegistered(class1);
            int i = ai.length;
            int j = as.length;
            ArrayList arraylist = (ArrayList)Property.value(class1, "weaponsList");
            if(arraylist == null)
            {
                arraylist = new ArrayList();
                Property.set(class1, "weaponsList", arraylist);
            }
            HashMapInt hashmapint = (HashMapInt)Property.value(class1, "weaponsMap");
            if(hashmapint == null)
            {
                hashmapint = new HashMapInt();
                Property.set(class1, "weaponsMap", hashmapint);
            }
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[i];
            for(int k = 0; k < j; k++)
            {
                String s1 = as[k];
                int i1 = 1;
                if(s1 != null)
                {
                    for(int j1 = s1.length() - 1; j1 > 0; j1--)
                    {
                        if(s1.charAt(j1) != ' ')
                            continue;
                        try
                        {
                            i1 = Integer.parseInt(s1.substring(j1 + 1));
                            s1 = s1.substring(0, j1);
                        }
                        catch(Exception exception1) { }
                        break;
                    }

                    a_lweaponslot[k] = new Aircraft._WeaponSlot(ai[k], s1, i1);
                } else
                {
                    a_lweaponslot[k] = null;
                }
            }

            for(int l = j; l < i; l++)
                a_lweaponslot[l] = null;

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    float upperAngle;
    float atackAngle;
    float windFoldValue;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean bRadarWarning;
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
    public boolean bToFire;
    private float arrestor;
    private boolean bJettisonPylons;
    static String weponHookArray[] = {
        "_CANNON01", "_ExternalDev01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev02", "_ExternalDev03", "_ExternalDev03", "_ExternalDev04", "_ExternalDev04", "_ExternalDev05", 
        "_ExternalDev05", "_ExternalDev06", "_ExternalDev06", "_ExternalDev07", "_ExternalDev07", "_ExternalDev08", "_ExternalDev08", "_ExternalDev09", "_ExternalDev09", "_ExternalDev10", 
        "_ExternalDev10", "_ExternalDev11", "_ExternalDev11", "_ExternalDev12", "_ExternalDev12", "_ExternalDev13", "_ExternalDev13", "_ExternalDev14", "_ExternalDev14", "_ExternalDev15", 
        "_ExternalDev15", "_ExternalDev16", "_ExternalDev16"
    };
    BulletEmitter bulletEmitters[];

    static 
    {
        Class class1 = FFR_41MR.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "YUKIKAZE");
        Property.set(class1, "meshName", "3DO/Plane/FFR-41MR/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/FFR-41MR.fmd:FAF");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitFFR_41MR.class, CockpitFFR_41MR.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, weponHookArray);
    }
}
