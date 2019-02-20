package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.Polares;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class Su_17M extends Sukhoi
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump
{

    public Su_17M()
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
        return "SU17M_";
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(1.5F, 1.5F, 1.5F, 4.8F, 2.0F, 1.5F);
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
        if(thisWeaponsName.endsWith("(Ventral)"))
        {
            hierMesh().chunkVisible("PylonC", false);
            hierMesh().chunkVisible("PylonC2", false);
            hierMesh().chunkVisible("PylonC3", true);
        }
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        computeAL21F_AB();
        computeVarWing();
        super.update(f);
    }

    public void moveCockpitDoor(float f)
    {
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 45F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void computeAL21F_AB()
    {
        if(this.FM.EI.engines[0].getThrustOutput() > 1.001F && this.FM.EI.engines[0].getStage() > 5)
            this.FM.producedAF.x += 26200D;
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

    protected void moveFlap(float f)
    {
        float f1 = 44.5F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap03_D0", 0.0F, -f1 * 0.6F, 0.0F);
        hierMesh().chunkSetAngles("Flap04_D0", 0.0F, -f1 * 0.6F, 0.0F);
    }

    protected void moveVarWing(float f)
    {
        float f1 = -40F * f;
        hierMesh().chunkSetAngles("WingPivotL", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("WingPivotR", 0.0F, f1, 0.0F);
    }

    public void computeVarWing()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        if((double)calculateMach() > 0.90000000000000002D && FM.EI.engines[0].getThrustOutput() < 1.001F)
        {
            this.FM.CT.VarWingControl = 0.0F;
            this.FM.CT.FlapsControl = 0.0F;
            this.FM.CT.bHasFlapsControl = false;
            Reflection.setFloat(FM, "GCenter", Reflection.getFloat(FM, "GCenter") - 0.7F);
            polares.CxMin_0 = 0.021F;
            polares.CyCritH_0 = 0.9F;
            polares.Cy0_0 = 0.007F;
            polares.parabCxCoeff_0 = 0.00113F;
        }
        if((double)calculateMach() > 0.5D && FM.EI.engines[0].getThrustOutput() > 1.001F)
        {
            this.FM.CT.VarWingControl = 0.0F;
            this.FM.CT.FlapsControl = 0.0F;
            this.FM.CT.bHasFlapsControl = false;
            Reflection.setFloat(FM, "GCenter", Reflection.getFloat(FM, "GCenter") - 0.7F);
            polares.CxMin_0 = 0.021F;
            polares.CyCritH_0 = 0.9F;
            polares.Cy0_0 = 0.007F;
            polares.parabCxCoeff_0 = 0.00113F;
        }
        if((double)calculateMach() > 0.45000000000000001D && (double)calculateMach() < 0.90000000000000002D && FM.EI.engines[0].getThrustOutput() < 0.9F)
        {
            this.FM.CT.VarWingControl = 0.35F;
            this.FM.CT.FlapsControl = 0.0F;
            this.FM.CT.bHasFlapsControl = false;
            polares.CxMin_0 = 0.023F;
            polares.CyCritH_0 = 1.0F;
            polares.Cy0_0 = 0.012F;
            polares.parabCxCoeff_0 = 0.00103F;
        }
        if((double)calculateMach() > 0.45000000000000001D && (double)calculateMach() < 0.90000000000000002D && (double)FM.getAOA() >= 10D)
        {
            this.FM.CT.VarWingControl = 0.35F;
            this.FM.CT.FlapsControl = 0.0F;
            this.FM.CT.bHasFlapsControl = false;
            polares.CxMin_0 = 0.023F;
            polares.CyCritH_0 = 1.0F;
            polares.Cy0_0 = 0.012F;
            polares.parabCxCoeff_0 = 0.00103F;
        }
        if((double)calculateMach() > 0.45000000000000001D && (double)calculateMach() < 0.80000000000000004D && FM.EI.engines[0].getThrustOutput() < 0.8F)
        {
            this.FM.CT.VarWingControl = 0.7F;
            polares.CxMin_0 = 0.025F;
            polares.CyCritH_0 = 1.15F;
            polares.Cy0_0 = 0.025F;
            polares.parabCxCoeff_0 = 0.0009F;
        }
        if((double)calculateMach() < 0.65000000000000002D && (double)this.FM.getAOA() >= 10D)
        {
            this.FM.CT.VarWingControl = 0.7F;
            polares.CxMin_0 = 0.025F;
            polares.CyCritH_0 = 1.15F;
            polares.Cy0_0 = 0.025F;
            polares.parabCxCoeff_0 = 0.0009F;
        }
        if((double)calculateMach() < 0.45000000000000001D && FM.EI.engines[0].getStage() >= 6)
        {
            this.FM.CT.VarWingControl = 0.7F;
            this.FM.CT.bHasFlapsControl = true;
            polares.CxMin_0 = 0.025F;
            polares.CyCritH_0 = 1.15F;
            polares.Cy0_0 = 0.025F;
            polares.parabCxCoeff_0 = 0.0009F;
        }
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
        Class class1 = Su_17M.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Su-17");
        Property.set(class1, "meshName", "3DO/Plane/Su-17/hierSU17.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1972F);
        Property.set(class1, "yearExpired", 2020F);
        Property.set(class1, "FlightModel", "FlightModels/Su-17M.fmd:Sukhoi_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSu_7Uc.class, CockpitSu_7Bombardier.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 9, 3, 3, 9, 9, 
            2, 2, 9, 9, 9, 9, 9, 9, 3, 3, 
            9, 9, 9, 9, 2, 2, 9, 9, 9, 9, 
            9, 9, 9, 9, 2, 2, 3, 3, 3, 3, 
            9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 
            2, 2, 3, 3, 3, 3, 9, 9, 9, 9, 
            9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 9, 9, 2, 2, 2, 2, 9, 9, 
            2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_Gun01", "_Gun02", "_ExternalDev01", "_ExternalDev02", "_ExternalTank01", "_ExternalTank02", "_ExternalBomb01", "_ExternalBomb02", "_Dev03", "_Dev04", 
            "_Rock01", "_Rock02", "_Dev05", "_Dev06", "_ExternalDev07", "_ExternalDev08", "_ExternalTank03", "_ExternalTank04", "_ExternalBomb03", "_ExternalBomb04", 
            "_Dev09", "_Dev10", "_Dev11", "_Dev12", "_Rock03", "_Rock04", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", 
            "_Dev17", "_Dev18", "_Dev19", "_Dev20", "_Rock05", "_Rock06", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", 
            "_Dev21", "_Dev22", "_Dev23", "_Dev24", "_Rock07", "_Rock08", "_Rock09", "_Rock10", "_Rock11", "_Rock12", 
            "_Rock13", "_Rock14", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalTank05", "_ExternalTank06", "_Dev25", "_Dev26", 
            "_Dev27", "_Dev28", "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22", 
            "_Rock23", "_Rock24", "_Dev29", "_Dev30", "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_Dev31", "_Dev32", 
            "_Rock29", "_Rock30", "_Rock31", "_Rock32", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", 
            "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalBomb27", "_ExternalBomb28", 
            "_ExternalBomb29", "_ExternalBomb30"
        });
    }
}
