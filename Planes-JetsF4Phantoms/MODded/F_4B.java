
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            F_4, Aircraft, TypeFighterAceMaker, TypeRadarGunsight, 
//            Chute, PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure, 
//            TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, 
//            TypeFastJet, NetAircraft

public class F_4B extends F_4
    implements TypeX4Carrier, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, TypeFastJet
{

    public F_4B()
    {
        guidedMissileUtils = null;
        fxSirena = newSound("aircraft.F4warning", false);
        smplSirena = new Sample("sample.F4warning.wav", 256, 65535);
        sirenaSoundPlaying = false;
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
        smplSirena.setInfinite(true);
        bDynamoOperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        g1 = null;
        tX4Prev = 0L;
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
            ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
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

    private boolean sirenaWarning()
    {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
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
            super.pos.getAbs(point3d);
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
        if(flag && !sirenaSoundPlaying)
        {
            fxSirena.play(smplSirena);
            sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "AN/APR-36: Enemy at Six!");
        } else
        if(!flag && sirenaSoundPlaying)
        {
            fxSirena.cancel();
            sirenaSoundPlaying = false;
        }
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(1.0F, 1.0F, 1.0F, 1.8F, 1.5F, 1.0F);
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void typeX4CAdjSidePlus()
    {
        deltaAzimuth = 0.1F;
    }

    public void typeX4CAdjSideMinus()
    {
        deltaAzimuth = -0.1F;
    }

    public void typeX4CAdjAttitudePlus()
    {
        deltaTangage = 0.1F;
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -0.1F;
    }

    public void typeX4CResetControls()
    {
        deltaAzimuth = deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth()
    {
        return deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage()
    {
        return deltaTangage;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        FM.Skill = 3;
        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        if(((FlightModelMain) (super.FM)).CT.Weapons[0] != null)
            g1 = ((FlightModelMain) (super.FM)).CT.Weapons[0][0];
        if(thisWeaponsName.startsWith("S1"))
            hierMesh().chunkVisible("6x20mm_C", true);
        if(thisWeaponsName.startsWith("S2"))
        {
            hierMesh().chunkVisible("6x20mm_L", true);
            hierMesh().chunkVisible("6x20mm_R", true);
        }
        if(thisWeaponsName.startsWith("S3"))
        {
            hierMesh().chunkVisible("6x20mm_C", true);
            hierMesh().chunkVisible("6x20mm_L", true);
            hierMesh().chunkVisible("6x20mm_R", true);
        }
        if(thisWeaponsName.startsWith("S1B"))
        {
            hierMesh().chunkVisible("6x20mm_C", true);
            hierMesh().chunkVisible("PylonTERLI", true);
            hierMesh().chunkVisible("PylonTERRI", true);
        }
        if(thisWeaponsName.startsWith("S2B"))
        {
            hierMesh().chunkVisible("6x20mm_L", true);
            hierMesh().chunkVisible("6x20mm_R", true);
            hierMesh().chunkVisible("PylonTERLI", true);
            hierMesh().chunkVisible("PylonTERRI", true);
        }
        if(thisWeaponsName.startsWith("S3B"))
        {
            hierMesh().chunkVisible("6x20mm_C", true);
            hierMesh().chunkVisible("6x20mm_L", true);
            hierMesh().chunkVisible("6x20mm_R", true);
            hierMesh().chunkVisible("PylonTERLI", true);
            hierMesh().chunkVisible("PylonTERRI", true);
        }
        if(thisWeaponsName.startsWith("B2S"))
        {
            hierMesh().chunkVisible("6x20mm_C", true);
            hierMesh().chunkVisible("PylonTERLI", true);
            hierMesh().chunkVisible("PylonTERRI", true);
            hierMesh().chunkVisible("PylonTERLO", true);
            hierMesh().chunkVisible("PylonTERRO", true);
        }
        if(thisWeaponsName.startsWith("B1"))
        {
            hierMesh().chunkVisible("PylonTERLI", true);
            hierMesh().chunkVisible("PylonTERRI", true);
        }
        if(thisWeaponsName.startsWith("B2"))
        {
            hierMesh().chunkVisible("PylonTERLO", true);
            hierMesh().chunkVisible("PylonTERRO", true);
            hierMesh().chunkVisible("PylonTERLI", true);
            hierMesh().chunkVisible("PylonTERRI", true);
        }
        if(thisWeaponsName.startsWith("B3"))
        {
            hierMesh().chunkVisible("PylonTERLO", true);
            hierMesh().chunkVisible("PylonTERRO", true);
        }
        if(thisWeaponsName.startsWith("B4M"))
        {
            hierMesh().chunkVisible("PylonMERC", true);
            hierMesh().chunkVisible("PylonTERLO", true);
            hierMesh().chunkVisible("PylonTERRO", true);
            hierMesh().chunkVisible("PylonTERLI", true);
            hierMesh().chunkVisible("PylonTERRI", true);
        }
        if(thisWeaponsName.startsWith("M1"))
        {
            hierMesh().chunkVisible("PylonMERC", true);
            hierMesh().chunkVisible("PylonMERL", true);
            hierMesh().chunkVisible("PylonMERR", true);
            hierMesh().chunkVisible("PylonTERLI", true);
            hierMesh().chunkVisible("PylonTERRI", true);
        }
        if(thisWeaponsName.startsWith("M2"))
        {
            hierMesh().chunkVisible("PylonMERC", true);
            hierMesh().chunkVisible("PylonTERLI", true);
            hierMesh().chunkVisible("PylonTERRI", true);
        }
        if(thisWeaponsName.startsWith("M3"))
        {
            hierMesh().chunkVisible("PylonMERL", true);
            hierMesh().chunkVisible("PylonMERR", true);
            hierMesh().chunkVisible("PylonTERLI", true);
            hierMesh().chunkVisible("PylonTERRI", true);
        }
        if(thisWeaponsName.endsWith("P1"))
        {
            hierMesh().chunkVisible("PylonSC", false);
            hierMesh().chunkVisible("PylonSL", false);
            hierMesh().chunkVisible("PylonSR", false);
        }
        if(thisWeaponsName.endsWith("P2"))
        {
            hierMesh().chunkVisible("PylonSL", false);
            hierMesh().chunkVisible("PylonSR", false);
        }
        if(thisWeaponsName.endsWith("P3"))
            hierMesh().chunkVisible("PylonSC", false);
        if(thisWeaponsName.endsWith("P4"))
        {
            hierMesh().chunkVisible("PylonSL", false);
            hierMesh().chunkVisible("PylonSR", false);
            hierMesh().chunkVisible("PylonSC", false);
            hierMesh().chunkVisible("PylonML", false);
            hierMesh().chunkVisible("PylonMR", false);
        }
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        sirenaWarning();
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
        if(((FlightModelMain) (super.FM)).CT.FlapsControl > 0.2F)
            ((FlightModelMain) (super.FM)).CT.BlownFlapsControl = 1.0F;
        else
            ((FlightModelMain) (super.FM)).CT.BlownFlapsControl = 0.0F;
        super.update(f);
        if(((FlightModelMain) (super.FM)).CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteF-4/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.8F);
            ((Actor) (chute)).pos.setRel(new Point3d(-8D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl)
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() > 600F || ((FlightModelMain) (super.FM)).CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                ((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                ((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !((FlightModelMain) (super.FM)).CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
        if(super.FM.getSpeed() > 5F)
        {
            hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 14.5F), 0.0F);
            hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -14.5F), 0.0F);
            hierMesh().chunkSetAngles("SlatL_Out", 0.0F, 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -20.5F));
            hierMesh().chunkSetAngles("SlatR_Out", 0.0F, 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -20.5F));
        }
        double Eto0 = ((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput();
        double Eto1 = ((FlightModelMain) (super.FM)).EI.engines[1].getThrustOutput();
        boolean bEs0 = ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5;
        boolean bEs1 = ((FlightModelMain) (super.FM)).EI.engines[1].getStage() > 5;
        if(Eto0 > 1.001F && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x += Eto0 * 26000D;
        if(Eto1 > 1.001F && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x += Eto1 * 26000D;
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 1.1799999999999999D && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 10000D;
        if(super.FM.getAltitude() > 0.0F && (double)calculateMach() >= 1.1799999999999999D && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 10000D;
        if(super.FM.getAltitude() > 500F && (double)calculateMach() >= 1.1799999999999999D && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x += Eto0 * 7000D;
        if(super.FM.getAltitude() > 500F && (double)calculateMach() >= 1.1799999999999999D && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x += Eto1 * 7000D;
        if(super.FM.getAltitude() > 1000F && (double)calculateMach() >= 1.1799999999999999D && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x += Eto0 * 1500D;
        if(super.FM.getAltitude() > 1000F && (double)calculateMach() >= 1.1799999999999999D && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x += Eto1 * 1500D;
        if(super.FM.getAltitude() > 1500F && (double)calculateMach() >= 1.1799999999999999D && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x += Eto0 * 1500D;
        if(super.FM.getAltitude() > 1500F && (double)calculateMach() >= 1.1799999999999999D && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x += Eto1 * 1500D;
        if(super.FM.getAltitude() > 9000F && Eto0 < 1.001F && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 1900D;
        if(super.FM.getAltitude() > 9000F && Eto1 < 1.001F && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 1900D;
        if(super.FM.getAltitude() > 10000F && (double)calculateMach() >= 2.27D && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 8000D;
        if(super.FM.getAltitude() > 10000F && (double)calculateMach() >= 2.27D && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 8000D;
        if(super.FM.getAltitude() > 11000F && Eto0 > 1.001F && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x += Eto0 * 5000D;
        if(super.FM.getAltitude() > 11000F && Eto1 > 1.001F && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x += Eto1 * 5000D;
        if(super.FM.getAltitude() > 12500F && Eto0 > 1.001F && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 2000D;
        if(super.FM.getAltitude() > 12500F && Eto1 > 1.001F && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 2000D;
        if(super.FM.getAltitude() > 14000F && Eto0 > 1.001F && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 2000D;
        if(super.FM.getAltitude() > 14000F && Eto1 > 1.001F && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 2000D;
        if(super.FM.getAltitude() > 15000F && Eto0 > 1.001F && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 2000D;
        if(super.FM.getAltitude() > 15000F && Eto1 > 1.001F && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 2000D;
        if(super.FM.getAltitude() > 15500F && (double)calculateMach() >= 2.0099999999999998D && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 8000D;
        if(super.FM.getAltitude() > 15500F && (double)calculateMach() >= 2.0099999999999998D && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 8000D;
        if(super.FM.getAltitude() > 16000F && Eto0 > 1.001F && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 2000D;
        if(super.FM.getAltitude() > 16000F && Eto1 > 1.001F && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 2000D;
        if(super.FM.getAltitude() > 16000F && (double)calculateMach() >= 1.9199999999999999D && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 8000D;
        if(super.FM.getAltitude() > 16000F && (double)calculateMach() >= 1.9199999999999999D && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 8000D;
        if(super.FM.getAltitude() > 17000F && Eto0 > 1.001F && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 2700D;
        if(super.FM.getAltitude() > 17000F && Eto1 > 1.001F && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 2700D;
        if(super.FM.getAltitude() > 17000F && (double)calculateMach() >= 1.8200000000000001D && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 8000D;
        if(super.FM.getAltitude() > 17000F && (double)calculateMach() >= 1.8200000000000001D && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 8000D;
        if(super.FM.getAltitude() > 17500F && (double)calculateMach() >= 1.73D && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 8000D;
        if(super.FM.getAltitude() > 17500F && (double)calculateMach() >= 1.73D && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 8000D;
        if(super.FM.getAltitude() > 18000F && Eto0 > 1.001F && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 2700D;
        if(super.FM.getAltitude() > 18000F && Eto1 > 1.001F && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 2700D;
        if(super.FM.getAltitude() > 18000F && (double)calculateMach() >= 1.5900000000000001D && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 8000D;
        if(super.FM.getAltitude() > 18000F && (double)calculateMach() >= 1.5900000000000001D && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 8000D;
        if(super.FM.getAltitude() > 18500F && (double)calculateMach() >= 1.5800000000000001D && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 8000D;
        if(super.FM.getAltitude() > 18500F && (double)calculateMach() >= 1.5800000000000001D && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 8000D;
        if(super.FM.getAltitude() > 18800F && Eto0 > 1.001F && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 2700D;
        if(super.FM.getAltitude() > 18800F && Eto1 > 1.001F && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 2700D;
        if(super.FM.getAltitude() > 19000F && (double)calculateMach() >= 1.52D && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 8000D;
        if(super.FM.getAltitude() > 19000F && (double)calculateMach() >= 1.52D && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 8000D;
        if(super.FM.getAltitude() > 20000F && Eto0 > 1.001F && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 6400D;
        if(super.FM.getAltitude() > 20000F && Eto1 > 1.001F && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 6400D;
        if(super.FM.getAltitude() > 21000F && Eto0 > 1.001F && bEs0)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto0 * 16500D;
        if(super.FM.getAltitude() > 21000F && Eto1 > 1.001F && bEs1)
            ((FlightModelMain) (super.FM)).producedAF.x -= Eto1 * 16500D;
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

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private GuidedMissileUtils guidedMissileUtils;
    private SoundFX fxSirena;
    private Sample smplSirena;
    private boolean sirenaSoundPlaying;
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
    public static float FlowRate = 10F;
    public static float FuelReserve = 1500F;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
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

    private long tX4Prev;
    private float deltaAzimuth;
    private float deltaTangage;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F4B");
        Property.set(class1, "meshName", "3DO/Plane/F-4B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/F4B.fmd:F4E");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitF_4E.class, com.maddox.il2.objects.air.CockpitF_4_Bombardier.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 
            9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 
            3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 
            1, 1, 1, 3, 3, 3, 3, 9, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 2,

            2, 2, 2, 2, 2, 2, 2, 9, 9, 9,
            9, 9, 9, 9, 9, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 2, 2, 2

        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01",       "_CANNON02",       "_CANNON03",       "_CANNON04",       "_ExternalDev01",  "_ExternalDev02",  "_ExternalDev03",  "_ExternalDev04",  "_ExternalDev05",  "_ExternalDev06", 
            "_ExternalDev07",  "_ExternalDev10",  "_ExternalDev11",  "_ExternalDev12",  "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", 
            "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", 
            "_ExternalDev13",  "_ExternalDev14",  "_ExternalDev15",  "_ExternalDev16",  "_ExternalDev17",  "_ExternalDev18",  "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", 
            "_ExternalBomb05", "_ExternalBomb06", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", 
            "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalDev19",  "_ExternalDev20",  "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", 
            "_ExternalBomb11", "_ExternalBomb12", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalDev21",  "_ExternalDev22",  "_ExternalDev23",  "_ExternalRock33", 
            "_CANNON05",       "_CANNON06",       "_CANNON07",       "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalDev24",  "_ExternalBomb17", "_ExternalBomb18", 
            "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalBomb27", "_ExternalBomb28", 
            "_ExternalBomb29", "_ExternalBomb30", "_ExternalBomb31", "_ExternalBomb32", "_ExternalBomb33", "_ExternalBomb34", "_ExternalBomb35", "_ExternalBomb36", "_ExternalBomb37", "_ExternalRock34", 

            "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalDev25",  "_ExternalDev26",  "_ExternalDev27",
            "_ExternalDev28",  "_ExternalDev29",  "_ExternalDev30",  "_ExternalDev31",  "_ExternalDev32",  "_ExternalRock42", "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46",
            "_ExternalRock47", "_ExternalRock48", "_ExternalRock49", "_ExternalRock50", "_ExternalRock51", "_ExternalRock52", "_ExternalRock53", "_ExternalDev33",  "_ExternalDev34",  "_ExternalDev35",
            "_ExternalDev36",  "_ExternalDev37",  "_ExternalDev38",  "_ExternalDev39",  "_ExternalDev40",  "_ExternalDev41",  "_ExternalDev42",  "_ExternalDev43",  "_ExternalDev44",  "_ExternalDev45",
            "_ExternalDev46",  "_ExternalDev47",  "_ExternalRock54", "_ExternalRock55", "_ExternalRock56"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            int byte0 = 145;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Default_P4";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM9D_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM7E+4xAIM9D_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM7E+4xAIM9D+2xDT_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM7E+4xAIM9D+2xDT+2xQRC160_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[70] = null;
            a_lweaponslot[66] = null;
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xAIM7E+4xAIM9D+1xQRC160_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM7E+4xAIM9D+3xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4EF", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:6xMk83+4xAIM7E_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:6xMk83+4xAIM7E+2xDT_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:6xM117+4xAIM7E+2xDT_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:6xMk83+3xAIM7E+1xQRC160_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:6xMk83+3xAIM7E+2xDT+1xQRC160_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:6xM117+3xAIM7E+2xDT+1xQRC160_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:6xMk82SnakeEye+4xAIM7E+4xAIM9D_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:6xMk82SnakeEye+4xAIM7E+4xAIM9D+2xDT_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:6xMk82SnakeEye+3xAIM7E+4xAIM9D+2xDT+1xQRC160_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B2:10xMk83+4xAIM7E+1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4EF", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B2:12xMk83_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B2:12xMk83+1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4EF", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B3:6xMk83+4xAIM7E+1xDT+2xQRC160";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4EF", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B3:6xMk83+4xAIM7E+4xAIM9D+1xDT+2xQRC160";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4EF", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B2:12xMk82+4xAIM7E+1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4EF", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B2:10xMk82SnakeEye+4xAIM7E+1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4EF", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B2:10xM117+4xAIM7E+1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4EF", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:4xBLU2+4xAIM7E+2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B2:8xBLU2+4xAIM7E+1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4EF", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[56] = null;
            a_lweaponslot[57] = null;
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:4xMk20RockEye+4xAIM7E+2xDT_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:4xMk20RockEye+3xAIM7E+1xQRC160_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:4xMk20RockEye+4xAIM7E+4xAIM9D_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:4xMk20RockEye+3xAIM7E+4xAIM9D+2xDT+1xQRC160_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "M1:24xMk82_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "M1:24xMk81_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "M1:24xBLU2_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunBlu2Napalm", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "M2:12xMk82SnakeEye+3xAIM7E+4xAIM9D+1xQRC160_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "M2:12xMk82+3xAIM7E+4xAIM9D+1xQRC160_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "M2:12xMk81+3xAIM7E+4xAIM9D+1xQRC160_P1";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "M2:12xMk82SnakeEye+3xAIM7E+4xAIM9D+2xDT+1xQRC160_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "M2:12xMk82+3xAIM7E+4xAIM9D+2xDT+1xQRC160_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "M2:12xMk81+3xAIM7E+4xAIM9D+2xDT+1xQRC160_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "RocketGunAIM7Eps", 1);
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonQRC160", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:2xAGM12C+4xAIM7E+2xDT_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(4, "RocketGunAGM12C", 1);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(4, "RocketGunAGM12C", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B1:4xAGM12C+4xAIM7E+1xDT_P3";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4EF", 1);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(4, "RocketGunAGM12C", 1);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(4, "RocketGunAGM12C", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(4, "RocketGunAGM12C", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunAGM12C", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B2:6xM117+4xZUNI+4xAIM7E+1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4EF", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[107] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[110] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B2:8xZUNI+4xAIM7E+1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4EF", 1);
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[107] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[110] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[111] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[112] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[113] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[114] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[121] = new Aircraft._WeaponSlot(4, "RocketGun5inchZuni", 4);
            a_lweaponslot[122] = new Aircraft._WeaponSlot(4, "RocketGun5inchZuni", 4);
            a_lweaponslot[123] = new Aircraft._WeaponSlot(4, "RocketGun5inchZuni", 4);
            a_lweaponslot[124] = new Aircraft._WeaponSlot(4, "RocketGun5inchZuni", 4);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B4M:6xMk82SnakeEye+8xZUNI+4xAIM9D";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[93] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[110] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[111] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[112] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[113] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[114] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 4);
            a_lweaponslot[121] = new Aircraft._WeaponSlot(4, "RocketGun5inchZuni", 4);
            a_lweaponslot[122] = new Aircraft._WeaponSlot(4, "RocketGun5inchZuni", 4);
            a_lweaponslot[123] = new Aircraft._WeaponSlot(4, "RocketGun5inchZuni", 4);
            a_lweaponslot[124] = new Aircraft._WeaponSlot(4, "RocketGun5inchZuni", 4);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B2:6xM117+4xLAU3+4xAIM7E+1xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[77] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF4EF", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[127] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[128] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[129] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[130] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B2S:1xSUU+6xMk82SnakeEye+6xLAU3+4xAIM7E";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonSUU_C", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[127] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[128] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[129] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[130] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[131] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[132] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[119] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[120] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            a_lweaponslot[70] = new Aircraft._WeaponSlot(0, "MGunM61", 1200);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "B4M:6xMk82SnakeEye+9xLAU3+4xAIM9D";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(6, "RocketGunAIM9D", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            a_lweaponslot[47] = null;
            a_lweaponslot[48] = null;
            a_lweaponslot[49] = null;
            a_lweaponslot[50] = null;
            a_lweaponslot[51] = null;
            a_lweaponslot[52] = null;
            a_lweaponslot[53] = null;
            a_lweaponslot[54] = null;
            a_lweaponslot[55] = null;
            a_lweaponslot[127] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[128] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[129] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[130] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[131] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[132] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[133] = null;
            a_lweaponslot[134] = null;
            a_lweaponslot[135] = null;
            a_lweaponslot[136] = null;
            a_lweaponslot[137] = null;
            a_lweaponslot[138] = null;
            a_lweaponslot[139] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[140] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[141] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[115] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[116] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[117] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[118] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[119] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[120] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[121] = null;
            a_lweaponslot[122] = null;
            a_lweaponslot[123] = null;
            a_lweaponslot[124] = null;
            a_lweaponslot[125] = null;
            a_lweaponslot[126] = null;
            a_lweaponslot[142] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[143] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[144] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[60] = null;
            a_lweaponslot[61] = null;
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "S3B:3xSUU+6xLAU3+4xAIM7E";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonSUU_C", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonSUU_L", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonSUU_R", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(5, "RocketGunAIM7E", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(0, "MGunM61", 1200);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(0, "MGunM61", 1200);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(0, "MGunM61", 1200);
            a_lweaponslot[133] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[134] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[135] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[136] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[137] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[138] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[121] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[122] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[123] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[124] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[125] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[126] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA", 19);
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none_P4";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[70] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}