package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.FuelTankGun_PTB490;
import com.maddox.il2.objects.weapons.FuelTankGun_PTB800L;
import com.maddox.il2.objects.weapons.PylonGP9;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class MIG_21UM extends MIG_21
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public MIG_21UM()
    {
        counter = 0;
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
        counter = 0;
        freq = 800;
        Timer1 = Timer2 = freq;
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
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

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        this.bHasSK1Seat = false;
        this.FM.CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        guidedMissileUtils.onAircraftLoaded();
        if((getBulletEmitterByHookName("_ExternalDev01") instanceof FuelTankGun_PTB490) || (getBulletEmitterByHookName("_ExternalDev01") instanceof FuelTankGun_PTB800L))
        {
            pylonOccupied_DT = true;
            this.FM.Sq.dragAirbrakeCx = this.FM.Sq.dragAirbrakeCx / 2.0F;
        }
        if(getBulletEmitterByHookName("_ExternalDev17") instanceof PylonGP9)
        {
            pylonOccupied_Py = true;
            this.FM.Sq.dragAirbrakeCx = this.FM.Sq.dragAirbrakeCx / 2.0F;
        }
        if(pylonOccupied_DT || pylonOccupied_Py)
            pylonOccupied = true;
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        this.k14Mode++;
        if(this.k14Mode > 7)
            this.k14Mode = 0;
        if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
            switch(this.k14Mode)
            {
            case 0:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed");
                break;

            case 1:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + Gsh-23 AA");
                break;

            case 2:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + GSh-23 AG");
                break;

            case 3:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + S-5 AG");
                break;

            case 4:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + S-24 AG");
                break;

            case 5:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + Bomb AG");
                break;

            case 6:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Missile");
                break;

            case 7:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Off");
                break;
            }
        return true;
    }

    public void update(float f)
    {
        super.update(f);
        computeR13_300_AB();
        if(pylonOccupied && (double)airBrake_State < 0.0015D && pylonOccupied_DT && !getBulletEmitterByHookName("_ExternalDev01").haveBullets())
        {
            pylonOccupied = false;
            this.FM.Sq.dragAirbrakeCx = this.FM.Sq.dragAirbrakeCx * 2.0F;
        }
        if(this.FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteMiG21bis/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.5F);
            ((Actor) (chute)).pos.setRel(new Point3d(-7D, 0.0D, 0.59999999999999998D), new Orient(0.0F, 90F, 0.0F));
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
        if(removeChuteTimer > 0L && !this.FM.CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
        guidedMissileUtils.update();
    }

    private void setSubsonicLimiter()
    {
        if(this.FM.getAltitude() > 0.0F && (double)calculateMach() >= 0.96999999999999997D && this.FM.EI.engines[0].getThrustOutput() < 1.001F && this.FM.EI.engines[0].getStage() > 5)
            this.FM.Sq.dragParasiteCx += 0.0002F;
    }

    public void computeR13_300_AB()
    {
        if(this.FM.EI.engines[0].getThrustOutput() > 1.001F && this.FM.EI.engines[0].getStage() > 5)
            this.FM.producedAF.x += 19530D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6)
            if((double)f > 19.5D)
            {
                f1 = 26F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = (0.00982201F * f3 - 0.148685F * f2) + 0.497876F * f;
            }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("AirbrakeL", -35F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("AirbrakeR", 35F * f, 0.0F, 0.0F);
        if(!pylonOccupied)
        {
            hierMesh().chunkSetAngles("AirbrakeRear", 0.0F, 40F * f, 0.0F);
            hierMesh().chunkSetAngles("AirbrakeTelescope", 0.0F, -40F * f, 0.0F);
        }
        airBrake_State = f;
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, -70F * f, 0.0F);
        hierMesh().chunkSetAngles("Blister2_D0", 0.0F, -70F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    private float airBrake_State;
    private boolean pylonOccupied;
    private boolean pylonOccupied_DT;
    private boolean pylonOccupied_Py;
    public float Timer1;
    public float Timer2;
    private int freq;
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
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    private int counter;
    protected boolean bHasBoosters;
    protected long boosterFireOutTime;
    private SoundFX fxSPO10;
    private Sample smplSPO10;
    private boolean SPO10SoundPlaying;
    private boolean bRadarWarning;
    private static Point3d p = new Point3d();

    static 
    {
        Class class1 = MIG_21UM.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG21");
        Property.set(class1, "meshName", "3DO/Plane/MiG-21U/hier21UM.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944.9F);
        Property.set(class1, "yearExpired", 1948.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-21UM.fmd:MIG21");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitMIG21UM.class, CockpitMIG_21UTI.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 9, 9, 2, 2, 2, 
            2, 2, 2, 2, 2, 9, 9, 3, 3, 9, 
            9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_Rock01", "_Rock01", "_Rock02", 
            "_Rock02", "_Rock03", "_Rock03", "_Rock04", "_Rock04", "_ExternalDev06", "_ExternalDev07", "_ExternalBomb01", "_ExternalBomb02", "_Dev08", 
            "_Dev09", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_Rock09", "_Rock10", "_Rock11", "_Rock12", "_Rock13", 
            "_Rock14", "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22", "_Rock23", 
            "_Rock24", "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_Rock29", "_Rock30", "_Rock31", "_Rock32", "_Rock33", 
            "_Rock34", "_Rock35", "_Rock36"
        });
    }
}
