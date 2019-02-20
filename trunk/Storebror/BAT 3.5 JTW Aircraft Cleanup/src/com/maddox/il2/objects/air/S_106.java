package com.maddox.il2.objects.air;

import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.War;
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
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class S_106 extends MIG_21
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public S_106()
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
        freq = 800;
        Timer1 = Timer2 = freq;
        pylonOccupied = false;
        pylonOccupied_DT = false;
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "S106_";
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
        this.FM.CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        guidedMissileUtils.onAircraftLoaded();
        if((getBulletEmitterByHookName("_ExternalDev03") instanceof FuelTankGun_PTB490) || (getBulletEmitterByHookName("_ExternalDev03") instanceof FuelTankGun_PTB800L))
        {
            pylonOccupied_DT = true;
            this.FM.Sq.dragAirbrakeCx = this.FM.Sq.dragAirbrakeCx / 2.0F;
        }
        if(pylonOccupied_DT)
            pylonOccupied = true;
    }

    public void typeFighterAceMakerRangeFinder()
    {
        if(this.k14Mode == 0)
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
            hunted = War.GetNearestEnemyAircraft(((Interpolate) (this.FM)).actor, 2000F, 9);
        if(hunted != null)
        {
            this.k14Distance = (float)((Interpolate) (this.FM)).actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
            if(this.k14Distance > 1700F)
                this.k14Distance = 1700F;
            else
            if(this.k14Distance < 200F)
                this.k14Distance = 200F;
        }
    }

    protected void moveFlap(float f)
    {
        super.moveFlap(f);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.3F);
        hierMesh().chunkSetLocate("Flap01a_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Flap02a_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        this.k14Mode++;
        if(this.k14Mode > 1)
            this.k14Mode = 0;
        if(this.k14Mode == 0)
        {
            if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "PKI Sight: On");
        } else
        if(this.k14Mode == 1 && ((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "PKI Sight: Off");
        return true;
    }

    public void update(float f)
    {
        super.update(f);
        if(pylonOccupied && (double)airBrake_State < 0.0015D && pylonOccupied_DT && !getBulletEmitterByHookName("_ExternalDev03").haveBullets())
        {
            pylonOccupied = false;
            this.FM.Sq.dragAirbrakeCx = this.FM.Sq.dragAirbrakeCx * 2.0F;
        }
        computeR11_300F_AB();
        if(Config.isUSE_RENDER())
            if(!this.FM.AS.bIsAboutToBailout)
            {
                if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null && Main3D.cur3D().cockpits[0].cockpitDimControl)
                {
                    hierMesh().chunkVisible("Head1_D0", false);
                    hierMesh().chunkVisible("Glass_Head1_D0", true);
                } else
                {
                    hierMesh().chunkVisible("Head1_D0", true);
                    hierMesh().chunkVisible("Glass_Head1_D0", false);
                }
            } else
            {
                hierMesh().chunkVisible("Glass_Head1_D0", false);
            }
        if(this.FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteMiG21/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(1.0F);
            ((Actor) (chute)).pos.setRel(new Point3d(-5D, 0.0D, -0.59999999999999998D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && this.FM.CT.bHasDragChuteControl)
            if(this.FM.CT.DragChuteControl == 1.0F && this.FM.getSpeedKMH() > 600F || this.FM.CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 0.40000000000000002D), new Orient(0.0F, 60F, 0.0F));
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
        typeFighterAceMakerRangeFinder();
        guidedMissileUtils.update();
    }

    public void setTimer(int i)
    {
        Random random = new Random();
        Timer1 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
        Timer2 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
    }

    public void resetTimer(float f)
    {
        Timer1 = f;
        Timer2 = f;
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
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, -50F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void computeR11_300F_AB()
    {
        if(this.FM.EI.engines[0].getThrustOutput() > 1.001F && this.FM.EI.engines[0].getStage() > 5)
            this.FM.producedAF.x += 14720D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6)
            if((double)f > 19.5D)
            {
                f1 = 24F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = (0.0171719F * f3 - 0.324136F * f2) + 1.02179F * f;
            }
        FM.producedAF.x -= f1 * 1000F;
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
    private static Actor hunted = null;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    private int freq;
    public float Timer1;
    public float Timer2;
    private float airBrake_State;
    private boolean pylonOccupied;
    private boolean pylonOccupied_DT;

    static 
    {
        Class class1 = S_106.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG21");
        Property.set(class1, "meshName", "3DO/Plane/MiG-21F/hierS106.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944.9F);
        Property.set(class1, "yearExpired", 1948.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-21F13.fmd:MIG21");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitMIG21F.class
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
            "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalRock01", "_Rock01", "_ExternalRock02", 
            "_Rock02", "_ExternalRock03", "_Rock03", "_ExternalRock04", "_Rock04", "_ExternalDev06", "_ExternalDev07", "_ExternalBomb01", "_ExternalBomb02", "_Dev08", 
            "_Dev09", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", 
            "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", 
            "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", 
            "_ExternalRock34", "_ExternalRock35", "_ExternalRock36"
        });
    }
}
