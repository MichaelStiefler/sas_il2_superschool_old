package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombJATO;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class F84F1 extends F84
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeFastJet
{

    public F84F1()
    {
        guidedMissileUtils = new GuidedMissileUtils(this);
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
        APmode1 = false;
        APmode2 = false;
        booster = new Bomb[2];
        bHasBoosters = true;
        boosterFireOutTime = -1L;
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

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -70F), 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.7F, 0.0F, -100F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.1F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.1F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.7F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.7F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.7F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.7F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.025F, 0.0F, -110F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.025F, 0.0F, 110F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    public void moveWheelSink()
    {
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.19075F, 0.0F, 1.0F);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075F * f;
        hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 0.0F, 40F * f);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 0.0F, 40F * f);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 30F * f, 0.0F);
        if(FM.CT.GearControl > 0.5F)
            hierMesh().chunkSetAngles("GearC7_D0", 0.0F, -60F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        float f1 = 55F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, f1);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, f1);
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

    public void destroy()
    {
        doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters()
    {
        Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Rocket/RocketSmokeWhite.eff", 30F);
        Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Rocket/RocketSmokeWhite.eff", 30F);
    }

    public void doCutBoosters()
    {
        for(int i = 0; i < 2; i++)
            if(booster[i] != null)
            {
                booster[i].start();
                booster[i] = null;
            }

    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        if(Config.isUSE_RENDER())
        {
            turbo = Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            turbosmoke = Eff3DActor.New(this, findHook("_Engine1ES_01"), null, 1.0F, "3DO/Effects/Aircraft/GraySmallTSPD.eff", -1F);
            afterburner = Eff3DActor.New(this, findHook("_Engine1EF_02"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurner.eff", -1F);
            Eff3DActor.setIntesity(turbo, 0.0F);
            Eff3DActor.setIntesity(turbosmoke, 0.0F);
            Eff3DActor.setIntesity(afterburner, 0.0F);
        }
        if(thisWeaponsName.endsWith("8xHVAR"))
        {
            hierMesh().chunkVisible("pylon2L_D0", false);
            hierMesh().chunkVisible("pylon2R_D0", false);
        }
        if(thisWeaponsName.endsWith("8xHVAR+JATO"))
        {
            hierMesh().chunkVisible("pylon2L_D0", false);
            hierMesh().chunkVisible("pylon2R_D0", false);
        }
        if(thisWeaponsName.endsWith("16xHVAR"))
        {
            hierMesh().chunkVisible("pylon1L_D0", false);
            hierMesh().chunkVisible("pylon1R_D0", false);
        }
        if(thisWeaponsName.endsWith("16xHVAR+JATO"))
        {
            hierMesh().chunkVisible("pylon1L_D0", false);
            hierMesh().chunkVisible("pylon1R_D0", false);
        }
        if(thisWeaponsName.endsWith("24xHVAR+JATO"))
        {
            hierMesh().chunkVisible("pylon1L_D0", false);
            hierMesh().chunkVisible("pylon1R_D0", false);
            hierMesh().chunkVisible("pylon2L_D0", false);
            hierMesh().chunkVisible("pylon2R_D0", false);
        }
        if(thisWeaponsName.endsWith("24xHVAR"))
        {
            hierMesh().chunkVisible("pylon1L_D0", false);
            hierMesh().chunkVisible("pylon1R_D0", false);
            hierMesh().chunkVisible("pylon2L_D0", false);
            hierMesh().chunkVisible("pylon2R_D0", false);
        }
        if(thisWeaponsName.startsWith("Def"))
        {
            hierMesh().chunkVisible("pylon1L_D0", false);
            hierMesh().chunkVisible("pylon1R_D0", false);
            hierMesh().chunkVisible("pylon2L_D0", false);
            hierMesh().chunkVisible("pylon2R_D0", false);
        }
        for(int i = 0; i < 2; i++)
            if(thisWeaponsName.endsWith("JATO"))
                try
                {
                    booster[i] = new BombJATO();
                    ((Actor) (booster[i])).pos.setBase(this, findHook("_BoosterH" + (i + 1)), false);
                    ((Actor) (booster[i])).pos.resetAsBase();
                    booster[i].drawing(true);
                }
                catch(Exception exception)
                {
                    debugprintln("Structure corrupt - can't hang Starthilferakete..");
                }

    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33:
        case 34:
        case 35:
        case 36:
        case 37:
        case 38:
            doCutBoosters();
            this.FM.AS.setGliderBoostOff();
            bHasBoosters = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        computeSubsonicLimiter();
        super.update(f);
        if((this.FM instanceof Pilot) && bHasBoosters && thisWeaponsName.endsWith("JATO"))
        {
            if(this.FM.getAltitude() > 300F && boosterFireOutTime == -1L && this.FM.Loc.z != 0.0D && World.Rnd().nextFloat() < 0.05F)
            {
                doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                bHasBoosters = false;
            }
            if(bHasBoosters && boosterFireOutTime == -1L && this.FM.Gears.onGround() && this.FM.EI.getPowerOutput() > 0.8F && this.FM.getSpeedKMH() > 20F)
            {
                boosterFireOutTime = Time.current() + 30000L;
                doFireBoosters();
                this.FM.AS.setGliderBoostOn();
            }
            if(bHasBoosters && boosterFireOutTime > 0L)
            {
                if(Time.current() < boosterFireOutTime)
                    this.FM.producedAF.x += 20000D;
                if(Time.current() > boosterFireOutTime + 10000L)
                {
                    doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    bHasBoosters = false;
                }
            }
        }
        if(this.FM.AS.isMaster() && Config.isUSE_RENDER())
        {
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
            setExhaustFlame(Math.round(Aircraft.cvt(this.FM.EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
        }
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
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 0.99F, 0.0F, 0.6F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.45F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("BlisterBar_D0", 0.0F, 0.0F, 90F * f);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log("Autopilot Mode: Altitude ON");
                this.FM.AP.setStabAltitude(1000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log("Autopilot Mode: Altitude OFF");
                this.FM.AP.setStabAltitude(false);
            }
        if(i == 21)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                this.FM.AP.setStabDirection(true);
                this.FM.CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                this.FM.AP.setStabDirection(false);
                this.FM.CT.bHasRudderControl = true;
            }
    }

    public void computeSubsonicLimiter()
    {
        float f = Aircraft.cvt(calculateMach(), 0.5F, 0.9F, 0.0F, 6000F);
        float f1 = Aircraft.cvt(FM.getAltitude(), 0.0F, 11000F, 1.0F, 0.46F);
        if((double)FM.EI.engines[0].getThrustOutput() < 1.0009999999999999D)
            FM.producedAF.x -= f * f1;
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
    private ArrayList rocketsList;
    public boolean bToFire;
    private long tX4Prev;
    public boolean APmode1;
    public boolean APmode2;
    private Bomb booster[];
    protected boolean bHasBoosters;
    protected long boosterFireOutTime;

    static 
    {
        Class class1 = F84F1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F84F");
        Property.set(class1, "meshName", "3DO/Plane/F84F/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946.9F);
        Property.set(class1, "yearExpired", 1955.3F);
        Property.set(class1, "FlightModel", "FlightModels/F84F1.fmd:F84_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitF84F1.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 
            9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 
            3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", 
            "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", 
            "_ExternalBomb05", "_ExternalBomb06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", 
            "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", 
            "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26"
        });
    }
}
