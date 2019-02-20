package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Point3f;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class L_39ZA extends L39A
    implements TypeFighter, TypeStormovik, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit
{

    public L_39ZA()
    {
        guidedMissileUtils = null;
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
        rocketsList = new ArrayList();
        bToFire = false;
        tX4Prev = 0L;
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

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(1.5F, 1.5F, 1.0F, 2.0F, 2.0F, 2.0F);
    }

    public Actor getMissileTarget()
    {
        return guidedMissileUtils.getMissileTarget();
    }

    public Point3f getMissileTargetOffset()
    {
        return new Point3f(guidedMissileUtils.getSelectedActorOffset());
    }

    public boolean hasMissiles()
    {
        return !rocketsList.isEmpty();
    }

    public void shotMissile()
    {
        if(hasMissiles())
            rocketsList.remove(0);
    }

    public int getMissileLockState()
    {
        return guidedMissileUtils.getMissileLockState();
    }

    private float getMissilePk()
    {
        float f = 0.0F;
        guidedMissileUtils.setMissileTarget(guidedMissileUtils.lookForGuidedMissileTarget(((Interpolate) (this.FM)).actor, guidedMissileUtils.getMaxPOVfrom(), guidedMissileUtils.getMaxPOVto(), guidedMissileUtils.getPkMaxDist()));
        if(Actor.isValid(guidedMissileUtils.getMissileTarget()))
            f = guidedMissileUtils.Pk(((Interpolate) (this.FM)).actor, guidedMissileUtils.getMissileTarget());
        return f;
    }

    private void checkAIlaunchMissile()
    {
        if((super.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode() || !(this.FM instanceof Pilot))
            return;
        if(rocketsList.isEmpty())
            return;
        Pilot pilot = (Pilot)this.FM;
        if((pilot.get_maneuver() == 27 || pilot.get_maneuver() == 62 || pilot.get_maneuver() == 63) && ((Maneuver) (pilot)).target != null)
        {
            trgtAI = ((Interpolate) (((Maneuver) (pilot)).target)).actor;
            if(!Actor.isValid(trgtAI) || !(trgtAI instanceof Aircraft))
                return;
            bToFire = false;
            if(trgtPk > 25F && Actor.isValid(guidedMissileUtils.getMissileTarget()) && (guidedMissileUtils.getMissileTarget() instanceof Aircraft) && guidedMissileUtils.getMissileTarget().getArmy() != ((Interpolate) (this.FM)).actor.getArmy() && Time.current() > tX4Prev + 10000L)
            {
                bToFire = true;
                tX4Prev = Time.current();
                shootRocket();
                bToFire = false;
            }
        }
    }

    public void shootRocket()
    {
        if(rocketsList.isEmpty())
        {
            return;
        } else
        {
            ((RocketGun)rocketsList.get(0)).shots(1);
            return;
        }
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(this.thisWeaponsName.startsWith("Obs"))
        {
            hierMesh().chunkVisible("Pilot2_D0", true);
            hierMesh().chunkVisible("Head2_D0", true);
            hierMesh().chunkVisible("HMask2_D0", true);
        }
        if(this.thisWeaponsName.endsWith("armed"))
        {
            hierMesh().chunkVisible("PylonL1", true);
            hierMesh().chunkVisible("PylonR1", true);
            hierMesh().chunkVisible("PylonL2", true);
            hierMesh().chunkVisible("PylonR2", true);
        }
        rocketsList.clear();
        guidedMissileUtils.createMissileList(rocketsList);
        if(Config.isUSE_RENDER())
            turbo = Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
        turbosmoke = Eff3DActor.New(this, findHook("_Engine1ES_01"), null, 1.0F, "3DO/Effects/Aircraft/GraySmallTSPD.eff", -1F);
        afterburner = Eff3DActor.New(this, findHook("_Engine1EF_02"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurner.eff", -1F);
        Eff3DActor.setIntesity(turbo, 0.0F);
        Eff3DActor.setIntesity(turbosmoke, 0.0F);
        Eff3DActor.setIntesity(afterburner, 0.0F);
        guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        super.update(f);
        trgtPk = getMissilePk();
        guidedMissileUtils.checkLockStatus();
        checkAIlaunchMissile();
        if(this.FM.AS.isMaster() && Config.isUSE_RENDER())
            if(this.FM.EI.engines[0].getPowerOutput() > 0.45F && this.FM.EI.engines[0].getStage() == 6)
            {
                if(this.FM.EI.engines[0].getPowerOutput() > 0.65F)
                {
                    if(this.FM.EI.engines[0].getPowerOutput() > 1.001F)
                        this.FM.AS.setSootState(this, 0, 3);
                    else
                        this.FM.AS.setSootState(this, 0, 2);
                } else
                {
                    this.FM.AS.setSootState(this, 0, 1);
                }
            } else
            if(this.FM.EI.engines[0].getPowerOutput() <= 0.45F || this.FM.EI.engines[0].getStage() < 6)
                this.FM.AS.setSootState(this, 0, 0);
    }

    public void doSetSootState(int i, int j)
    {
        switch(j)
        {
        case 0:
            Eff3DActor.setIntesity(turbo, 0.0F);
            Eff3DActor.setIntesity(turbosmoke, 0.0F);
            Eff3DActor.setIntesity(afterburner, 0.0F);
            break;

        case 1:
            Eff3DActor.setIntesity(turbo, 0.0F);
            Eff3DActor.setIntesity(turbosmoke, 1.0F);
            Eff3DActor.setIntesity(afterburner, 0.0F);
            break;

        case 2:
            Eff3DActor.setIntesity(turbo, 1.0F);
            Eff3DActor.setIntesity(turbosmoke, 1.0F);
            Eff3DActor.setIntesity(afterburner, 0.0F);
            break;

        case 3:
            Eff3DActor.setIntesity(turbo, 1.0F);
            Eff3DActor.setIntesity(turbosmoke, 1.0F);
            Eff3DActor.setIntesity(afterburner, 1.0F);
            break;
        }
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
        hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 100F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    private GuidedMissileUtils guidedMissileUtils;
    private float trgtPk;
    private Actor trgtAI;
    private Eff3DActor turbo;
    private Eff3DActor turbosmoke;
    private Eff3DActor afterburner;
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
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    private ArrayList rocketsList;
    public boolean bToFire;
    private long tX4Prev;

    static 
    {
        Class class1 = L_39ZA.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "L-39ZA");
        Property.set(class1, "meshName", "3DO/Plane/L-39C/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/L-39ZA.fmd:L39");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitL39.class, CockpitL39_2.class, CockpitL39Bombardier.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 9, 9, 0, 0, 9, 9, 
            3, 3, 3, 3, 2, 2, 2, 2, 9, 2, 
            2, 2, 2, 9, 2, 2, 2, 2, 2, 2, 
            2, 2, 9, 9, 9, 9, 2, 2, 2, 2, 
            9, 2, 2, 9, 9, 9, 9, 2, 2, 2, 
            2, 9, 9, 9, 9, 9, 9, 3, 3, 3, 
            3, 9, 9, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", 
            "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalRock01", "_ExternalRock02", "_MGUN01", "_MGUN02", "_ExternalRock03", "_ExternalRock04", 
            "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", 
            "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", 
            "_ExternalRock25", "_ExternalRock26", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", 
            "_ExternalDev05", "_ExternalRock31", "_ExternalRock32", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", 
            "_ExternalRock36", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", 
            "_ExternalBomb14", "_ExternalDev16", "_ExternalDev17", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18"
        });
    }
}
