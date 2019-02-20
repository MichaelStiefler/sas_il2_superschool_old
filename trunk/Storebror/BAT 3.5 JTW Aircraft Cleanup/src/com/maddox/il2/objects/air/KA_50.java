package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class KA_50 extends Scheme2a
    implements TypeStormovik, TypeBayDoor, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public KA_50()
    {
        rocketHookSelected = 2;
        suka = new Loc();
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        rotorrpm = 0;
        obsLookTime = 0;
        obsLookAzimuth = 0.0F;
        obsLookElevation = 0.0F;
        obsAzimuth = 0.0F;
        obsElevation = 0.0F;
        obsAzimuthOld = 0.0F;
        obsElevationOld = 0.0F;
        obsMove = 0.0F;
        obsMoveTot = 0.0F;
        bObserverKilled = false;
        guidedMissileUtils = new GuidedMissileUtils(this);
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
        fxSirena = newSound("aircraft.Sirena2", false);
        smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        smplSirena.setInfinite(true);
        setCollV(0.0F);
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
        long curTime = Time.current();
        if(curTime - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = curTime;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = curTime;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = curTime;
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
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        Actor actor = War.getNearestEnemy(this, 4000F);
        if((actor instanceof Aircraft) && actor.getArmy() != World.getPlayerArmy() && (actor instanceof TypeFighterAceMaker) && (actor instanceof TypeRadarGunsight) && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
        {
            this.pos.getAbs(point3d);
            double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
            double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
            new String();
            new String();
            double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
            new String();
            double d7 = d3 - d;
            double d8 = d4 - d1;
            float f = 57.32484F * (float)Math.atan2(d8, -d7);
            int i1 = (int)(Math.floor((int)f) - 90D);
            if(i1 < 0)
                i1 += 360;
            int j1 = i1 - i;
            double d9 = d - d3;
            double d10 = d1 - d4;
            double d11 = Math.sqrt(d6 * d6);
            int k1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
            float f1 = 57.32484F * (float)Math.atan2(k1, d11);
            int l1 = (int)(Math.floor((int)f1) - 90D);
            if(l1 < 0)
                l1 += 360;
            int i2 = l1 - j;
            int j2 = (int)(Math.ceil(((double)k1 * 3.2808399000000001D) / 100D) * 100D);
            if(j2 >= 5280)
                j2 = (int)Math.floor(j2 / 5280);
            bRadarWarning = (double)k1 <= 3000D && (double)k1 >= 50D && i2 >= 195 && i2 <= 345 && Math.sqrt(j1 * j1) >= 120D;
            playSirenaWarning(bRadarWarning);
        } else
        {
            bRadarWarning = false;
            playSirenaWarning(bRadarWarning);
        }
        return true;
    }

    public void playSirenaWarning(boolean isThreatened)
    {
        if(isThreatened && !sirenaSoundPlaying)
        {
            fxSirena.play(smplSirena);
            sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: Enemy on Six!");
        } else
        if(!isThreatened && sirenaSoundPlaying)
        {
            fxSirena.cancel();
            sirenaSoundPlaying = false;
        }
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(this.FM.AS.bNavLightsOn)
        {
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            this.pos.getAbs(point3d, orient);
            l.set(point3d, orient);
            Eff3DActor eff3dactor = Eff3DActor.New(this, findHook("_RedLight"), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", 1.0F);
            eff3dactor.postDestroy(Time.current() + 500L);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.3F, 0.3F);
            lightpointactor.light.setEmit(1.0F, 3F);
            ((Actor) (eff3dactor)).draw.lightMap().put("light", lightpointactor);
        }
        if(!bObserverKilled)
            if(obsLookTime == 0)
            {
                obsLookTime = 2 + World.Rnd().nextInt(1, 3);
                obsMoveTot = 1.0F + World.Rnd().nextFloat() * 1.5F;
                obsMove = 0.0F;
                obsAzimuthOld = obsAzimuth;
                obsElevationOld = obsElevation;
                if((double)World.Rnd().nextFloat() > 0.80000000000000004D)
                {
                    obsAzimuth = 0.0F;
                    obsElevation = 0.0F;
                } else
                {
                    obsAzimuth = World.Rnd().nextFloat() * 140F - 70F;
                    obsElevation = World.Rnd().nextFloat() * 50F - 20F;
                }
            } else
            {
                obsLookTime--;
            }
    }

    protected void moveElevator(float f)
    {
        updateControlsVisuals();
    }

    private final void updateControlsVisuals()
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -7F * this.FM.CT.getElevator());
    }

    protected void moveAileron(float f)
    {
        updateAileron();
    }

    private final void updateAileron()
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -7F * this.FM.CT.getAileron(), 0.0F);
    }

    protected void moveFlap(float f)
    {
    }

    protected void moveRudder(float f)
    {
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f_1_ = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -90F);
        float f_2_ = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -85F);
        float f_3_ = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, 85F);
        float f_4_ = f <= 0.5F ? Aircraft.cvt(f, 0.1F, 0.5F, 0.0F, 70F) : Aircraft.cvt(f, 0.8F, 1.0F, 70F, 0.0F);
        float f_5_ = f <= 0.5F ? Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -70F) : Aircraft.cvt(f, 0.8F, 1.0F, -70F, 0.0F);
        Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -90F);
        float f_7_ = Aircraft.cvt(f, 0.1F, 0.8F, 0.0F, 90F);
        float f_8_ = Aircraft.cvt(f, 0.2F, 0.9F, 0.0F, -90F);
        float f_9_ = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -140F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, f_1_);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 0.0F, f_9_);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, f_2_, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, f_3_, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f_4_, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f_5_, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", f_8_, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", f_7_, 0.0F, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.95F, 0.0F, 0.9F);
        hierMesh().chunkSetAngles("Door1_D0", -90F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Door2_D0", 0.0F, -65F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -120F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 120F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 120F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -120F * f, 0.0F);
    }

    protected void moveFan(float f)
    {
        rotorrpm = Math.abs((int)((double)(this.FM.EI.engines[0].getw() * 0.025F) + this.FM.Vwld.length() / 30D));
        if(rotorrpm >= 1)
            rotorrpm = 1;
        if(this.FM.EI.engines[0].getw() > 100F && this.FM.EI.engines[1].getw() > 100F)
        {
            hierMesh().chunkVisible("Prop1_D0", false);
            hierMesh().chunkVisible("PropRot1_D0", true);
        }
        if(this.FM.EI.engines[0].getw() < 100F && this.FM.EI.engines[1].getw() < 100F)
        {
            hierMesh().chunkVisible("Prop1_D0", true);
            hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if(hierMesh().isChunkVisible("Prop1_D1"))
        {
            hierMesh().chunkVisible("Prop1_D0", false);
            hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if(this.FM.EI.engines[0].getw() > 100F && this.FM.EI.engines[1].getw() > 100F)
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", true);
        }
        if(this.FM.EI.engines[0].getw() < 100F && this.FM.EI.engines[1].getw() < 100F)
        {
            hierMesh().chunkVisible("Prop2_D0", true);
            hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if(hierMesh().isChunkVisible("Prop2_D1"))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if(hierMesh().isChunkVisible("Tail1_CAP"))
        {
            hierMesh().chunkVisible("Prop2_D0", false);
            hierMesh().chunkVisible("PropRot2_D0", false);
            hierMesh().chunkVisible("Prop2_D1", false);
        }
        dynamoOrient = bDynamoRotary ? (dynamoOrient - 100F) % 360F : (float)((double)dynamoOrient - (double)rotorrpm * 25D) % 360F;
        hierMesh().chunkSetAngles("Prop1_D0", -dynamoOrient, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, dynamoOrient);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("GearL22_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -10F), 0.0F);
        hierMesh().chunkSetAngles("GearR22_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.32F, 0.0F, 10F), 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        boolean flag1 = this instanceof KA_50;
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.startsWith("xxarmorp"))
                {
                    int i = s.charAt(8) - 48;
                    switch(i)
                    {
                    default:
                        break;

                    case 1:
                        getEnergyPastArmor(7.070000171661377D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                        shot.powerType = 0;
                        break;

                    case 2:
                    case 3:
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
                        shot.powerType = 0;
                        if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
                            doRicochet(shot);
                        break;

                    case 4:
                        if(point3d.x > -1.3500000000000001D)
                        {
                            getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
                            shot.powerType = 0;
                            if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
                                doRicochet(shot);
                        } else
                        {
                            getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                        }
                        break;

                    case 5:
                    case 6:
                        getEnergyPastArmor(20.200000762939453D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
                        if(shot.power > 0.0F)
                            break;
                        if(Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
                            doRicochet(shot);
                        else
                            doRicochetBack(shot);
                        break;

                    case 7:
                        getEnergyPastArmor(20.200000762939453D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F)
                            doRicochetBack(shot);
                        break;
                    }
                }
                if(s.startsWith("xxarmorc1"))
                    getEnergyPastArmor(7.070000171661377D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.startsWith("xxarmort1"))
                    getEnergyPastArmor(6.059999942779541D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.10000000149011612D && getEnergyPastArmor(3.4000000953674316D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: Keel Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if(s.startsWith("xxsparlm"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingLMid") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingLMid Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingLMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparrm"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingRMid") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingRMid Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingRMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparlo"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingLOut") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingLOut Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingLOut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparro"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingROut") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingROut Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingROut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.11999999731779099D && getEnergyPastArmor(6.9600000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(6.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(6.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float)Math.sqrt(((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y + ((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
            }
            if(s.startsWith("xxeng"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("prop"))
                {
                    if(getEnergyPastArmor(3.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F)
                        if(World.Rnd().nextFloat() < 0.5F)
                        {
                            debuggunnery("Engine Module: Prop Governor Hit, Disabled..");
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        } else
                        {
                            debuggunnery("Engine Module: Prop Governor Hit, Oil Pipes Damaged..");
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        }
                } else
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Engine Module: Reductor Hit, Bullet Jams Reductor Gear..");
                        this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                    }
                } else
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.01F, shot) > 0.0F)
                    {
                        debuggunnery("Engine Module: Supercharger Disabled..");
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                    }
                } else
                if(s.endsWith("feed"))
                {
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if(World.Rnd().nextFloat() < 0.05F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else
                if(s.endsWith("fue1"))
                {
                    if(getEnergyPastArmor(0.89F, shot) > 0.0F)
                    {
                        debuggunnery("Engine Module: Fuel Feed Line Pierced, Engine Fires..");
                        this.FM.AS.hitEngine(shot.initiator, 0, 100);
                    }
                } else
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(2.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 175000F)
                        {
                            debuggunnery("Engine Module: Crank Case Hit, Bullet Jams Ball Bearings..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    getEnergyPastArmor(22.5F, shot);
                } else
                if(s.endsWith("cyl1"))
                {
                    if(getEnergyPastArmor(1.3F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.75F)
                    {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                        debuggunnery("Engine Module: Cylinders Assembly Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Operating..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            debuggunnery("Engine Module: Cylinders Assembly Hit, Engine Fires..");
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                        }
                        if(World.Rnd().nextFloat() < 0.01F)
                        {
                            debuggunnery("Engine Module: Cylinders Assembly Hit, Bullet Jams Piston Head..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        getEnergyPastArmor(22.5F, shot);
                    }
                    if(Math.abs(point3d.y) < 0.1379999965429306D && getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if(World.Rnd().nextFloat() < 0.05F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else
                if(s.startsWith("xxeng1mag"))
                {
                    int k = s.charAt(9) - 49;
                    debuggunnery("Engine Module: Magneto " + k + " Hit, Magneto " + k + " Disabled..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, k);
                } else
                if(s.startsWith("xxeng1oil") && getEnergyPastArmor(0.5F, shot) > 0.0F)
                {
                    debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
            }
            if(s.startsWith("xxw1"))
                if(this.FM.AS.astateEngineStates[0] == 0)
                {
                    debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 1);
                } else
                if(this.FM.AS.astateEngineStates[0] == 1)
                {
                    debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 2);
                }
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(this.FM.AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        this.FM.AS.hitTank(shot.initiator, 2, 1);
                        this.FM.AS.doSetTankState(shot.initiator, l, 1);
                    } else
                    if(this.FM.AS.astateTankStates[l] == 1)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        this.FM.AS.hitTank(shot.initiator, 2, 1);
                        this.FM.AS.doSetTankState(shot.initiator, l, 2);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                    {
                        this.FM.AS.hitTank(shot.initiator, 2, 2);
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced, State Shifted..");
                    }
                }
            }
            if(s.startsWith("xxmgun"))
            {
                if(s.endsWith("01"))
                {
                    debuggunnery("Armament System: Left Machine Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if(s.endsWith("02"))
                {
                    debuggunnery("Armament System: Right Machine Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
            }
            if(s.startsWith("xxcannon"))
            {
                if(s.endsWith("01") && getEnergyPastArmor(0.25F, shot) > 0.0F)
                {
                    debuggunnery("Armament System: Left Cannon: Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if(s.endsWith("02") && getEnergyPastArmor(0.25F, shot) > 0.0F)
                {
                    debuggunnery("Armament System: Right Cannon: Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if(s.startsWith("xxammo"))
            {
                if(s.startsWith("xxammol1") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Left Cannon: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if(s.startsWith("xxammor1") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Right Cannon: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                if(s.startsWith("xxammol2") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Left Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if(s.startsWith("xxammor2") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Right Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 12.6F), shot);
            }
            if(s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.00345F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets())
            {
                debuggunnery("Armament System: Bomb Payload Detonated..");
                this.FM.AS.hitTank(shot.initiator, 0, 10);
                this.FM.AS.hitTank(shot.initiator, 1, 10);
                nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            if(s.startsWith("xxpnm") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
            {
                debuggunnery("Pneumo System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 1);
            }
            if(s.startsWith("xxhyd") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
            {
                debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.startsWith("xxins"))
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            return;
        }
        if(s.startsWith("xcockpit") || s.startsWith("xblister"))
            if(point3d.z > 0.47299999999999998D)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            else
            if(point3d.y > 0.0D)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            else
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
        if(s.startsWith("xcf"))
        {
            if(point3d.x < -1.9399999999999999D)
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            } else
            {
                if(point3d.x <= 1.3420000000000001D)
                    if(point3d.z < -0.59099999999999997D || point3d.z > 0.40799999237060547D && point3d.x > 0.0D)
                    {
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
                            doRicochet(shot);
                    } else
                    {
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D)
                            doRicochet(shot);
                    }
                if(chunkDamageVisible("CF") < 3)
                    hitChunk("CF", shot);
            }
        } else
        if(s.startsWith("xoil"))
        {
            if(point3d.z < -0.98099999999999998D)
            {
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            } else
            if(point3d.x > 0.53700000000000003D || point3d.x < -0.10000000000000001D)
            {
                getEnergyPastArmor(0.20000000298023224D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochetBack(shot);
            } else
            {
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            }
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xeng"))
        {
            if(point3d.z > 0.159D)
                getEnergyPastArmor((double)(1.25F * World.Rnd().nextFloat(0.95F, 1.12F)) / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 1.335D && point3d.x < 2.3860000000000001D && point3d.z > -0.059999999999999998D && point3d.z < 0.064000000000000001D)
                getEnergyPastArmor(0.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 2.5299999999999998D && point3d.x < 2.992D && point3d.z > -0.23499999999999999D && point3d.z < 0.010999999999999999D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 2.5590000000000002D && point3d.z < -0.59499999999999997D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 1.849D && point3d.x < 2.2509999999999999D && point3d.z < -0.70999999999999996D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 3.0030000000000001D)
                getEnergyPastArmor(World.Rnd().nextFloat(2.3F, 3.2F), shot);
            else
            if(point3d.z < -0.60600000619888306D)
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999997473787516E-005D), shot);
            else
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999997473787516E-005D), shot);
            if(Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.86599999666213989D && (shot.power <= 0.0F || World.Rnd().nextFloat() < 0.1F))
                doRicochet(shot);
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
            hitChunk("Tail1", shot);
        else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel"))
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner"))
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
            {
                debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if((s.endsWith("2a") || s.endsWith("2b")) && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else
        if(s.startsWith("xturret"))
        {
            if(getEnergyPastArmor(0.25F, shot) > 0.0F)
            {
                debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                this.FM.AS.setJamBullets(10, 0);
                this.FM.AS.setJamBullets(10, 1);
                getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
            }
        } else
        if(s.startsWith("xhelm"))
        {
            getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 3.56F), shot);
            if(shot.power <= 0.0F)
                doRicochetBack(shot);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int i1;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                i1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                i1 = s.charAt(6) - 49;
            } else
            {
                i1 = s.charAt(5) - 49;
            }
            hitFlesh(i1, shot, byte0);
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            // fall through

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            bObserverKilled = true;
            // fall through

        default:
            return;
        }
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        if(obsMove < obsMoveTot && !bObserverKilled && !this.FM.AS.isPilotParatrooper(1))
        {
            if(obsMove < 0.2F || obsMove > obsMoveTot - 0.2F)
                obsMove += 0.29999999999999999D * (double)f;
            else
            if(obsMove < 0.1F || obsMove > obsMoveTot - 0.1F)
                obsMove += 0.15F;
            else
                obsMove += 1.2D * (double)f;
            obsLookAzimuth = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsAzimuthOld, obsAzimuth);
            obsLookElevation = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsElevationOld, obsElevation);
            hierMesh().chunkSetAngles("Head2_D0", 0.0F, obsLookAzimuth, obsLookElevation);
        }
        super.update(f);
        if((double)this.FM.getSpeedKMH() >= 290D && this.FM.EI.engines[0].getStage() > 5)
            this.FM.Sq.dragParasiteCx += 0.02F;
        if((double)this.FM.getSpeedKMH() >= 290D && this.FM.EI.engines[1].getStage() > 5)
            this.FM.Sq.dragParasiteCx += 0.02F;
        if((double)this.FM.getSpeedKMH() >= 310D && this.FM.EI.engines[0].getStage() > 5)
            this.FM.Sq.dragParasiteCx += 0.02F;
        if((double)this.FM.getSpeedKMH() >= 310D && this.FM.EI.engines[1].getStage() > 5)
            this.FM.Sq.dragParasiteCx += 0.02F;
        if((double)this.FM.getSpeedKMH() >= 320D && this.FM.EI.engines[0].getStage() > 5)
            this.FM.Sq.dragParasiteCx += 0.02F;
        if((double)this.FM.getSpeedKMH() >= 320D && this.FM.EI.engines[1].getStage() > 5)
            this.FM.Sq.dragParasiteCx += 0.02F;
        if(this.FM.getAltitude() > 4000F && this.FM.EI.engines[0].getThrustOutput() > 0.8F && this.FM.EI.engines[0].getStage() > 5)
            this.FM.producedAF.z -= 1000D;
        if(this.FM.getAltitude() > 4000F && this.FM.EI.engines[1].getThrustOutput() > 0.8F && this.FM.EI.engines[1].getStage() > 5)
            this.FM.producedAF.z -= 1000D;
        if(this.FM.getAltitude() > 4500F && this.FM.EI.engines[0].getThrustOutput() > 0.8F && this.FM.EI.engines[0].getStage() > 5)
            this.FM.producedAF.z -= 1000D;
        if(this.FM.getAltitude() > 4500F && this.FM.EI.engines[1].getThrustOutput() > 0.8F && this.FM.EI.engines[1].getStage() > 5)
            this.FM.producedAF.z -= 1000D;
        float avW = (this.FM.EI.engines[0].getw() + this.FM.EI.engines[1].getw()) / 2.0F;
        if(avW > 100F)
        {
            Vector3f eVect = new Vector3f();
            eVect.x = -(this.FM.CT.getElevator() + this.FM.CT.getTrimElevatorControl());
            eVect.y = -(this.FM.CT.getAileron() + this.FM.CT.getTrimRudderControl());
            eVect.z = 1.5F;
            eVect.normalize();
            this.FM.EI.engines[0].setVector(eVect);
            this.FM.EI.engines[1].setVector(eVect);
            this.FM.Or.increment(this.FM.CT.getRudder() + this.FM.CT.getTrimRudderControl(), this.FM.CT.getElevator() + this.FM.CT.getTrimElevatorControl(), this.FM.CT.getAileron() + this.FM.CT.getTrimAileronControl());
            this.FM.getW().scale(0.59999999999999998D);
        } else
        {
            if(this.FM.Gears.nOfGearsOnGr < 2)
                this.FM.producedAF.z -= (100D - (double)avW) * 300D + 15000D;
            Vector3d vector3d = new Vector3d();
            getSpeed(vector3d);
            Point3d point3d = new Point3d();
            this.pos.getAbs(point3d);
            float f1 = (float)((double)this.FM.getAltitude() - World.land().HQ(point3d.x, point3d.y));
            if(f1 < 10F && this.FM.getSpeedKMH() < 60F && ((Tuple3d) (vector3d)).z < -1D)
            {
                vector3d.z *= 0.70000000000000007D;
                setSpeed(vector3d);
            }
        }
        if(this == World.getPlayerAircraft() && super.FM.turret.length > 0 && this.FM.AS.astatePilotStates[1] < 90 && super.FM.turret[0].bIsAIControlled && (this.FM.getOverload() > 3F || this.FM.getOverload() < -0.7F))
            Voice.speakRearGunShake();
        sirenaWarning();
        if(hierMesh().isChunkVisible("Tail1_CAP"))
            this.FM.Or.increment(avW, 0.0F, 0.0F);
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        if(af[0] < -60F)
        {
            af[0] = -60F;
            flag = false;
        } else
        if(af[0] > 60F)
        {
            af[0] = 60F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if(af[1] < -80F)
        {
            af[1] = -80F;
            flag = false;
        }
        if(af[1] > 20F)
        {
            af[1] = 20F;
            flag = false;
        }
        if(!flag)
            return false;
        float f1 = af[1];
        if(f < 1.2F && f1 < 13.3F)
            return false;
        return f1 >= -3.1F || f1 <= -4.6F;
    }

    public float getCollV()
    {
        return collV;
    }

    public void setCollV(float collV)
    {
        this.collV = collV;
    }

    public BulletEmitter Weapons[][];
    public int rocketHookSelected;
    private static Loc l = new Loc();
    private int obsLookTime;
    private float obsLookAzimuth;
    private float obsLookElevation;
    private float obsAzimuth;
    private float obsElevation;
    private float obsAzimuthOld;
    private float obsElevationOld;
    private float obsMove;
    private float obsMoveTot;
    boolean bObserverKilled;
    public static boolean bChangedPit = false;
    public Loc suka;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int rotorrpm;
    private GuidedMissileUtils guidedMissileUtils;
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
    private SoundFX fxSirena;
    private Sample smplSirena;
    private boolean sirenaSoundPlaying;
    private float collV;

    static 
    {
        Class class1 = KA_50.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "KA-50");
        Property.set(class1, "meshName", "3DO/Plane/KA_50/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1950F);
        Property.set(class1, "yearExpired", 1960.5F);
        Property.set(class1, "FlightModel", "FlightModels/Mi-24V.fmd:HIND");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitH19D.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 9, 2, 2, 2, 2, 
            7, 7, 9, 9, 2, 2, 2, 2, 2, 2, 
            2, 2, 9, 9, 9, 9, 3, 3, 3, 3, 
            9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_BombSpawn01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", 
            "_Flare01", "_Flare02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", 
            "_ExternalRock11", "_ExternalRock12", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", 
            "_ExternalDev11", "_ExternalDev12"
        });
    }
}
