package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Polares;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.FuelTankGun_Tank19;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class SM_12PMU extends Mig_19
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump
{

    public SM_12PMU()
    {
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
        removeChuteTimer = -1L;
        flame = null;
        dust = null;
        trail = null;
        sprite = null;
        turboexhaust = null;
        bOxidiserLeak = false;
        bHasEngine = true;
    }

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
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

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19:
            bHasEngine = false;
            FM.AS.setEngineDies(this, 1);
            return cut(Aircraft.partNames()[i]);

        case 3:
        case 4:
            return false;
        }
        return super.cutFM(i, j, actor);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        droptank();
        guidedMissileUtils.onAircraftLoaded();
        this.FM.Skill = 3;
        this.FM.CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        if(this.thisWeaponsName.endsWith("2xNR-30s"))
        {
            hierMesh().chunkVisible("GunL", true);
            hierMesh().chunkVisible("GunR", true);
        }
        if(Config.isUSE_RENDER())
        {
            flame = Eff3DActor.New(this, findHook("_Engine3EF_01"), null, 1.0F, "3DO/Effects/Aircraft/AfterBurnerF100D.eff", -1F);
            dust = Eff3DActor.New(this, findHook("_Engine3EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109D.eff", -1F);
            trail = Eff3DActor.New(this, findHook("_Engine3EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", -1F);
            sprite = Eff3DActor.New(this, findHook("_Engine3EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", -1F);
            turboexhaust = Eff3DActor.New(this, findHook("_Engine3ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallGND.eff", -1F);
            Eff3DActor.setIntesity(flame, 0.0F);
            Eff3DActor.setIntesity(dust, 0.0F);
            Eff3DActor.setIntesity(trail, 0.0F);
            Eff3DActor.setIntesity(sprite, 0.0F);
            Eff3DActor.setIntesity(turboexhaust, 1.0F);
        }
        if(thisWeaponsName.startsWith("Fighter:"))
        {
            Polares polares = (Polares)Reflection.getValue(FM, "Wing");
            polares.CxMin_0 = 0.0223F;
        }
        if(thisWeaponsName.startsWith("Fighter01:"))
        {
            Polares polares1 = (Polares)Reflection.getValue(FM, "Wing");
            polares1.CxMin_0 = 0.0227F;
        }
        if(thisWeaponsName.startsWith("Fighter02"))
        {
            Polares polares2 = (Polares)Reflection.getValue(FM, "Wing");
            polares2.CxMin_0 = 0.0233F;
        }
    }

    public void destroy()
    {
        if(Actor.isValid(flame))
            flame.destroy();
        if(Actor.isValid(dust))
            dust.destroy();
        if(Actor.isValid(trail))
            trail.destroy();
        if(Actor.isValid(sprite))
            sprite.destroy();
        if(Actor.isValid(turboexhaust))
            turboexhaust.destroy();
        super.destroy();
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(Config.isUSE_RENDER())
        {
            if(oldVwld < 20F && this.FM.getSpeed() > 20F)
            {
                Eff3DActor.finish(turboexhaust);
                turboexhaust = Eff3DActor.New(this, findHook("_Engine3ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallTSPD.eff", -1F);
            }
            if(oldVwld > 20F && this.FM.getSpeed() < 20F)
            {
                Eff3DActor.finish(turboexhaust);
                turboexhaust = Eff3DActor.New(this, findHook("_Engine3ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallGND.eff", -1F);
            }
            oldVwld = this.FM.getSpeed();
        }
        if(flag && bOxidiserLeak)
            if(World.Rnd().nextFloat() < 0.2F)
                this.FM.AS.hitEngine(this, 2, 100);
            else
            if(World.Rnd().nextFloat() < 0.2F)
                this.FM.EI.engines[2].setEngineDies(this);
    }

    private final void doRemovedroptankL()
    {
        if(hierMesh().chunkFindCheck("DroptankL") != -1)
        {
            hierMesh().hideSubTrees("DroptankL");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("DroptankL"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }

    private final void doRemovedroptankR()
    {
        if(hierMesh().chunkFindCheck("DroptankR") != -1)
        {
            hierMesh().hideSubTrees("DroptankR");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("DroptankR"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }

    private void droptank()
    {
        for(int i = 0; i < this.FM.CT.Weapons.length; i++)
            if(this.FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < this.FM.CT.Weapons[i].length; j++)
                    if(this.FM.CT.Weapons[i][j].haveBullets() && (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_Tank19))
                    {
                        havedroptank = true;
                        this.hierMesh().chunkVisible("DroptankL", true);
                        this.hierMesh().chunkVisible("DroptankR", true);
                    }

            }

    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        computeR3M_26();
        computeLimiters();
        computeRocketBooster();
        super.update(f);
        if(havedroptank && !this.FM.CT.Weapons[9][1].haveBullets())
        {
            havedroptank = false;
            doRemovedroptankL();
            doRemovedroptankR();
        }
        if(this.FM.AS.isMaster())
        {
            if(Config.isUSE_RENDER())
                if(this.FM.EI.engines[2].getw() > 0.0F && this.FM.EI.engines[2].getStage() == 6)
                    doSetSootState(2, 8);
                else
                    doSetSootState(2, 7);
            if(prevThtl < 0.85F)
                this.FM.EI.engines[2].setControlThrottle(0.0F);
            else
            if(prevThtl < 0.95F)
                this.FM.EI.engines[2].setControlThrottle(0.85F);
            else
            if(prevThtl < 1.0F)
                this.FM.EI.engines[2].setControlThrottle(0.95F);
            else
                this.FM.EI.engines[2].setControlThrottle(1.0F);
            if(prevThtl != this.FM.CT.PowerControl)
                prevThtl = this.FM.CT.PowerControl;
            if(prevThtl == 0.0F)
            {
                if(!this.FM.Gears.onGround() && (this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode() && this.FM.EI.engines[2].getStage() == 6)
                    this.FM.EI.engines[2].setEngineStops(this);
            } else
            if((this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode() && this.FM.EI.engines[2].getStage() == 0 && this.FM.M.nitro > 0.0F)
                this.FM.EI.engines[2].setStage(this, 6);
        }
        if(this.FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteF86/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.5F);
            ((Actor) (chute)).pos.setRel(new Point3d(-5D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && this.FM.CT.bHasDragChuteControl)
            if(this.FM.CT.DragChuteControl == 1.0F && this.FM.getSpeedKMH() > 600F || this.FM.CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(this.FM.CT.DragChuteControl == 1.0F && this.FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
    }

    protected void moveAirBrake(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("AirbrakeL1_D0", 0.0F, 0.0F, 45F * f);
        hierMesh().chunkSetAngles("AirbrakeL3_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.75F, 10F, -27F));
        hierMesh().chunkSetAngles("AirbrakeR1_D0", 0.0F, 0.0F, 45F * f);
        hierMesh().chunkSetAngles("AirbrakeR3_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.75F, 10F, -27F));
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -0.14F);
        hierMesh().chunkSetLocate("AirbrakeL2_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -0.14F);
        hierMesh().chunkSetLocate("AirbrakeR2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void doSetSootState(int i, int j)
    {
        for(int k = 0; k < 2; k++)
        {
            if(this.FM.AS.astateSootEffects[i][k] != null)
                Eff3DActor.finish(this.FM.AS.astateSootEffects[i][k]);
            this.FM.AS.astateSootEffects[i][k] = null;
        }

        switch(j)
        {
        case 1:
            this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 3:
            this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 2:
            this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 5:
            this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.0F, "3DO/Effects/Aircraft/AfterBurnerF100D.eff", -1F);
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 4:
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 6:
            this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/Full_throttle.eff", -1F);
            this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/Full_throttle.eff", -1F);
            break;

        case 7:
            Eff3DActor.setIntesity(flame, 0.0F);
            Eff3DActor.setIntesity(dust, 0.0F);
            Eff3DActor.setIntesity(trail, 0.0F);
            Eff3DActor.setIntesity(sprite, 0.0F);
            break;

        case 8:
            Eff3DActor.setIntesity(flame, 1.0F);
            Eff3DActor.setIntesity(dust, 1.0F);
            Eff3DActor.setIntesity(trail, 1.0F);
            Eff3DActor.setIntesity(sprite, 1.0F);
            break;
        }
    }

    public void computeR3M_26()
    {
        if(this.FM.EI.engines[0].getThrustOutput() > 1.001F && this.FM.EI.engines[0].getStage() > 5)
            this.FM.producedAF.x += 7200D;
        if(this.FM.EI.engines[1].getThrustOutput() > 1.001F && this.FM.EI.engines[1].getStage() > 5)
            this.FM.producedAF.x += 7200D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6 && FM.EI.engines[1].getThrustOutput() > 1.001F && FM.EI.engines[1].getStage() == 6)
            if((double)f > 16.800000000000001D)
            {
                f1 = 5.5F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                f1 = (0.00823926F * f3 - 0.170425F * f2) + 0.605808F * f;
            }
        FM.producedAF.x -= f1 * 1000F;
    }

    public void computeRocketBooster()
    {
        if(FM.EI.engines[2].getThrustOutput() > 0.5F && this.FM.EI.engines[2].getStage() > 5)
            this.FM.producedAF.x += 30000D;
    }

    public void computeLimiters()
    {
        if(this.FM.getAltitude() > 0.0F && (double)calculateMach() >= 0.96999999999999997D && FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[2].getThrustOutput() < 0.5F && FM.EI.engines[0].getStage() > 5)
            this.FM.Sq.dragParasiteCx += 0.00018F;
    }

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
    public static float FlowRate = 10F;
    public static float FuelReserve = 1500F;
    public boolean bToFire;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    private Eff3DActor flame;
    private Eff3DActor dust;
    private Eff3DActor trail;
    private Eff3DActor sprite;
    private Eff3DActor turboexhaust;
    private float prevThtl;
    private float oldVwld;
    private boolean bOxidiserLeak;
    private boolean havedroptank;
    private boolean bHasEngine;

    static 
    {
        Class class1 = SM_12PMU.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SM-12");
        Property.set(class1, "meshName", "3DO/Plane/SM-12/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/SM-12PMU.fmd:MIG19");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSM_12.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 9, 9, 2, 2, 2, 2, 9, 
            9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 
            3, 9, 9, 9, 9, 3, 3, 9, 9, 9, 
            9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02", "_Rock01", "_Rock02", "_Rock03", "_Rock04", "_Dev03", 
            "_Dev04", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_Rock09", "_Rock10", "_Rock11", "_Rock12", "_Rock13", 
            "_Rock14", "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Dev05", "_Dev06", "_Rock21", 
            "_Rock22", "_Dev07", "_Dev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev11", "_ExternalDev12", "_Dev13", 
            "_Dev14", "_Dev15", "_Dev16", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_Rock27", "_Rock28", "_Rock29", 
            "_Rock30", "_Rock31", "_Rock32", "_Rock33", "_Rock34"
        });
    }
}
