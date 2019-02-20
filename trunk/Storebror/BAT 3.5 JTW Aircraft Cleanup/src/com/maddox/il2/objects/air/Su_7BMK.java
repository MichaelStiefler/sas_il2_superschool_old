package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Su_7BMK extends Sukhoi
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeAcePlane, TypeFuelDump
{

    public Su_7BMK()
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

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "SU7BMK_";
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
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        computeAL7F1_AB();
        computeSubsonicLimiter();
    }

    public void computeAL7F1_AB()
    {
        if(this.FM.EI.engines[0].getThrustOutput() > 1.001F && this.FM.EI.engines[0].getStage() > 5)
            this.FM.producedAF.x += 24000D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6)
            if(f > 17F)
            {
                f1 = 20F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = ((0.00644628F * f4 - 0.157057F * f3) + 0.92125F * f2) - 0.765843F * f;
            }
        FM.producedAF.x -= f1 * 1000F;
    }

    public void computeSubsonicLimiter()
    {
        float f = Aircraft.cvt(calculateMach(), 0.5F, 1.0F, 0.0F, 25000F);
        float f1 = Aircraft.cvt(FM.getAltitude(), 0.0F, 11000F, 1.0F, 0.205F);
        if(FM.EI.getPowerOutput() < 1.001F)
            FM.producedAF.x -= f * f1;
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

    static 
    {
        Class class1 = Su_7BMK.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Su-7");
        Property.set(class1, "meshName", "3DO/Plane/Su-7B/hierSU7BMK.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1961F);
        Property.set(class1, "yearExpired", 1986F);
        Property.set(class1, "FlightModel", "FlightModels/Su-7BKL.fmd:Sukhoi_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSu_7.class, CockpitSu_7Bombardier.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 9, 3, 3, 9, 9, 
            2, 2, 9, 9, 9, 9, 9, 9, 3, 3, 
            9, 9, 9, 9, 2, 2, 9, 9, 9, 9, 
            9, 9, 9, 9, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_Gun01", "_Gun02", "_ExternalDev01", "_ExternalDev02", "_ExternalTank01", "_ExternalTank02", "_ExternalBomb01", "_ExternalBomb02", "_Dev03", "_Dev04", 
            "_Rocket01", "_Rocket02", "_Dev05", "_Dev06", "_ExternalDev07", "_ExternalDev08", "_ExternalTank03", "_ExternalTank04", "_ExternalBomb03", "_ExternalBomb04", 
            "_Dev09", "_Dev10", "_Dev11", "_Dev12", "_Rocket03", "_Rocket04", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", 
            "_Dev17", "_Dev18", "_ExternalDev19", "_ExternalDev20", "_Rocket05", "_Rocket06"
        });
    }
}
