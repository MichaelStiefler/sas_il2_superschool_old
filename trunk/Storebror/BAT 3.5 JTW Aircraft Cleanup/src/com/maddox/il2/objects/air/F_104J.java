package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public class F_104J extends F_104
    implements TypeGuidedMissileCarrier, TypeThreatDetector, TypeGSuit, TypeAcePlane
{

    public F_104J()
    {
        guidedMissileUtils = null;
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
        engineSFX = null;
        engineSTimer = 0x98967f;
        outCommand = new NetMsgFiltered();
        myArmy = getArmy();
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "104J_";
    }

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
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

    public static final double Rnd(double d, double d1)
    {
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1)
    {
        return World.Rnd().nextFloat(f, f1);
    }

    private boolean RndB(float f)
    {
        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
    }

    private static final long SecsToTicks(float f)
    {
        long l = (long)(0.5D + (double)(f / Time.tickLenFs()));
        return l < 1L ? 1L : l;
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
        if(thisWeaponsName.startsWith("GAttack:"))
        {
            hierMesh().chunkVisible("PylonL2", false);
            hierMesh().chunkVisible("PylonR2", false);
            FM.Sq.dragParasiteCx += 0.0001F;
        }
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        computeJ79GE11_AB();
        super.update(f);
    }

    public void computeJ79GE11_AB()
    {
        if(this.FM.EI.engines[0].getThrustOutput() > 1.001F && this.FM.EI.engines[0].getStage() > 5)
            this.FM.producedAF.x += 20000D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6)
            if(f > 21F)
            {
                f1 = 10F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                float f5 = f4 * f;
                f1 = (((-0.000273872F * f5 + 0.0131599F * f4) - 0.199571F * f3) + 1.01749F * f2) - 1.49134F * f;
            }
        FM.producedAF.x -= f1 * 1000F;
    }

    private GuidedMissileUtils guidedMissileUtils;
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
    protected SoundFX engineSFX;
    protected int engineSTimer;
    private int myArmy;
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    private static Vector3f n = new Vector3f();
    private static Vector3d tmpv = new Vector3d();
    private NetMsgFiltered outCommand;

    static 
    {
        Class class1 = F_104J.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-104J");
        Property.set(class1, "meshName", "3DO/Plane/F-104/hierF104G.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1966F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/F-104G.fmd:F104FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitF_104G.class, CockpitF_104Bombardier.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 2, 2, 2, 2, 9, 9, 2, 2, 2, 
            2, 9, 9, 2, 2, 2, 2, 9, 9, 9, 
            9, 9, 9, 2, 2, 2, 2, 3, 9, 9, 
            2, 2, 2, 2, 9, 9, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_Rock01", "_Rock02", "_Rock03", "_Rock04", "_ExternalDev01", "_ExternalDev02", "_Rock05", "_Rock06", "_ExternalRock07", 
            "_Rock08", "_ExternalDev03", "_ExternalDev04", "_Rock09", "_Rock10", "_ExternalRock11", "_Rock12", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", 
            "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_Rock13", "_Rock14", "_ExternalRock15", "_Rock16", "_ExternalBomb01", "_ExternalDev11", "_ExternalDev12", 
            "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_Dev13", "_ExternalDev14", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04"
        });
    }
}
