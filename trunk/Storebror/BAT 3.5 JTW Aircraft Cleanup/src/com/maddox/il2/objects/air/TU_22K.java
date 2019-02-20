package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class TU_22K extends TU_22fuelReceiver
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeFuelDump
{

    public TU_22K()
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
        bDynamoOperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        g1 = null;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
    }

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 0.0F, 70F * f);
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 85F), 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -85F), 0.0F);
    }

    public void moveWingFold(float f)
    {
        if(f < 0.001F)
        {
            setGunPodsOn(true);
            hideWingWeapons(false);
        } else
        {
            setGunPodsOn(false);
            this.FM.CT.WeaponControl[0] = false;
            hideWingWeapons(false);
        }
        moveWingFold(hierMesh(), f);
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

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        FM.Skill = 3;
        this.FM.CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        if(this.FM.CT.Weapons[0] != null)
            g1 = this.FM.CT.Weapons[0][0];
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        if(FM.CT.getArrestor() > 0.2F)
            if(FM.Gears.arrestorVAngle != 0.0F)
            {
                float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
                arrestor = 0.8F * arrestor + 0.2F * f1;
                moveArrestorHook(arrestor);
            } else
            {
                float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
                if(f2 < 0.0F && FM.getSpeedKMH() > 60F)
                    Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                if(f2 > 0.0F && FM.CT.getArrestor() < 0.95F)
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
        if(this.FM.CT.FlapsControl > 0.2F)
            this.FM.CT.BlownFlapsControl = 1.0F;
        else
            this.FM.CT.BlownFlapsControl = 0.0F;
        super.update(f);
        if(this.FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute1 = new Chute(this);
            chute2 = new Chute(this);
            chute1.setMesh("3do/plane/ChuteSu_25/mono.sim");
            chute2.setMesh("3do/plane/ChuteSu_25/mono.sim");
            chute1.mesh().setScale(0.5F);
            chute2.mesh().setScale(0.5F);
            ((Actor) (chute1)).pos.setRel(new Point3d(-18D, 0.0D, 0.59999999999999998D), new Orient(20F, 90F, 0.0F));
            ((Actor) (chute2)).pos.setRel(new Point3d(-18D, 0.0D, 0.59999999999999998D), new Orient(-20F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && this.FM.CT.bHasDragChuteControl)
            if(this.FM.CT.DragChuteControl == 1.0F && this.FM.getSpeedKMH() > 600F || this.FM.CT.DragChuteControl < 1.0F)
            {
                if(chute1 != null && chute2 != null)
                {
                    chute1.tangleChute(this);
                    chute2.tangleChute(this);
                    ((Actor) (chute1)).pos.setRel(new Point3d(-15D, 0.0D, 1.0D), new Orient(20F, 90F, 0.0F));
                    ((Actor) (chute2)).pos.setRel(new Point3d(-15D, 0.0D, 1.0D), new Orient(-20F, 90F, 0.0F));
                }
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(this.FM.CT.DragChuteControl == 1.0F && this.FM.getSpeedKMH() < 20F)
            {
                if(chute1 != null && chute2 != null)
                {
                    chute1.tangleChute(this);
                    chute2.tangleChute(this);
                }
                ((Actor) (chute1)).pos.setRel(new Orient(10F, 100F, 0.0F));
                ((Actor) (chute2)).pos.setRel(new Orient(-10F, 100F, 0.0F));
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !this.FM.CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
        {
            chute1.destroy();
            chute2.destroy();
        }
    }

    public void moveFan(float f)
    {
        if(g1 != null && g1.isShots() && oldbullets != g1.countBullets())
        {
            oldbullets = g1.countBullets();
            if(dynamoOrient == 360F)
                dynamoOrient = 0.0F;
            dynamoOrient = dynamoOrient + 30F;
            hierMesh().chunkSetAngles("6x20mm_C", 0.0F, dynamoOrient, 0.0F);
            hierMesh().chunkSetAngles("6x20mm_L", 0.0F, dynamoOrient, 0.0F);
            hierMesh().chunkSetAngles("6x20mm_R", 0.0F, dynamoOrient, 0.0F);
        }
        super.moveFan(f);
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "22K_";
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
    private float arrestor;
    private BulletEmitter g1;
    private int oldbullets;
    private boolean bDynamoOperational;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int pk;
    private float deltaAzimuth;
    private float deltaTangage;
    private Chute chute1;
    private Chute chute2;

    static 
    {
        Class class1 = TU_22K.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Tu-22");
        Property.set(class1, "meshName", "3DO/Plane/Tu-22/hierTu22K.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/Tu-22K.fmd:TU22FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitTU_22.class, CockpitTU_22_Bombardier.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 10, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_InternalBomb01", "_InternalBomb02", "_InternalBomb03", "_InternalBomb04", "_InternalBomb05", "_InternalBomb06", "_InternalBomb07", "_InternalBomb08", 
            "_InternalBomb09", "_InternalBomb10", "_InternalBomb11", "_InternalBomb12", "_InternalBomb13", "_InternalBomb14", "_InternalBomb15", "_InternalBomb16", "_InternalBomb17", "_InternalBomb18", 
            "_InternalBomb19", "_InternalBomb20", "_InternalBomb21", "_InternalBomb22", "_InternalBomb23", "_InternalBomb24", "_ExternalRock01", "_ExternalRock02"
        });
    }
}
