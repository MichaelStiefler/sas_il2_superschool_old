// 4.10.1 class - Here because of Lutz mod
package com.maddox.il2.fm;

import java.util.StringTokenizer;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.ScareEnemies;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiMDSVariables;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.TypeDiveBomber;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;

public class FlightModel extends FlightModelMain {
    public FlightModel(String s) {
        super(s);
        turnOffCollisions = false;
        brakeShoe = false;
        brakeShoeLoc = new Loc();
        brakeShoeLastCarrier = null;
        canChangeBrakeShoe = false;
        dryFriction = 1.0F;
        timeToTakeOff = World.getTimeofDay();
        numberOfTrigger = 0;
        bstartmotor = true;
    }

    public void setSkill(int newSkill) {
        setTrigger();
        if (newSkill < 0)
            newSkill = 0;
        if (newSkill > 3)
            newSkill = 3;
        // TODO: +++ UP3 Sniper Gunner Hotfix +++
        if (this.actor == World.getPlayerAircraft() || ((Aircraft) this.actor).isNetPlayer())
            newSkill = 3;
        // TODO: --- UP3 Sniper Gunner Hotfix ---
        this.Skill = newSkill;
        this.turretSkill = newSkill;
        for (int j = 0; j < turret.length; j++) {
            turret[j].gunnerSkill = setGunner();
            turret[j].gunnerSkill *= this.turretSkill;
            turret[j].igunnerSkill = Math.round(turret[j].gunnerSkill);
        }

        if (this.actor != World.getPlayerAircraft() && !((Aircraft) this.actor).isNetPlayer())
            switch (newSkill) {
            // TODO: +++ UP3 Sniper Gunner Hotfix +++
//                case 0: // '\0'
//                    SensPitch *= 0.75F;
//                    SensRoll *= 0.5F;
//                    SensYaw *= 0.5F;
//                    break;
//
//                case 1: // '\001'
//                    SensRoll *= 0.75F;
//                    SensPitch *= 0.75F;
//                    SensYaw *= 0.7F;
//                    break;
//
//                case 2: // '\002'
//                    SensRoll *= 0.88F;
//                    SensPitch *= 0.92F;
//                    SensYaw *= 0.9F;
//                    break;
//
//                case 3: // '\003'
//                    SensPitch *= 1.0F;
//                    SensRoll *= 1.0F;
//                    SensYaw *= 1.0F;
//                    break;

                case TU_SKILL_ROOKIE:
                    this.SensPitch *= 0.75F;
                    this.SensRoll *= 0.5F;
                    this.SensYaw *= 0.5F;

                    break;
                case TU_SKILL_AVERAGE:
                    this.SensRoll *= 0.7F;
                    this.SensPitch *= 0.75F;
                    this.SensYaw *= 0.7F;

                    break;
                case TU_SKILL_VETERAN:
                    this.SensRoll *= 0.88F;
                    this.SensPitch *= 0.92F;
                    this.SensYaw *= 0.9F;
                    break;
                case TU_SKILL_ACE:
                    this.SensPitch *= 1.1F;
                    this.SensRoll *= 1.0F;
                    this.SensYaw *= 1.0F;
                    break;
            // TODO: --- UP3 Sniper Gunner Hotfix ---
            }
        else
            Aircraft.debugprintln(this.actor, "Skill adjustment rejected on the Player AI parameters..");
    }

    public void set(HierMesh hiermesh) {
        HM = hiermesh;
        am = (ActorHMesh) this.actor;
        int i;
        for (i = 1; i <= 9; i++)
            if (HM.chunkFindCheck("Turret" + i + "A_D0") < 0 || HM.chunkFindCheck("Turret" + i + "B_D0") < 0)
                break;

        i--;
        turret = new Turret[i];
        for (int j = 0; j < i; j++) {
            turret[j] = new Turret();
            turret[j].indexA = HM.chunkFind("Turret" + (j + 1) + "A_D0");
            turret[j].indexB = HM.chunkFind("Turret" + (j + 1) + "B_D0");
            tu[0] = tu[1] = tu[2] = 0.0F;
            HM.setCurChunk(turret[j].indexA);
            am.hierMesh().chunkSetAngles(tu);
            HM.setCurChunk(turret[j].indexB);
            am.hierMesh().chunkSetAngles(tu);
            am.getChunkLoc(turret[j].Lstart);
        }

        this.Gears.set(hiermesh);
    }

    private void updateRotation(Turret theTurret, float tickLen) {
        tu[0] = theTurret.tuLim[0];
        tu[1] = theTurret.tuLim[1];
        // TODO: +++ UP3 Sniper Gunner Hotfix +++
        // float f1 = 10F * f;
        this.rotSpeed = ((Math.abs(this.prev0 - tu[0]) + Math.abs(this.prev1 - tu[1])) * tickLen);
        this.prev0 = tu[0];
        this.prev1 = tu[1];
        float maxRotSpeed = 40F * tickLen;
        // TODO: --- UP3 Sniper Gunner Hotfix ---
        float azimuth = tu[0] - theTurret.tu[0];
        if (azimuth < -maxRotSpeed)
            azimuth = -maxRotSpeed;
        else if (azimuth > maxRotSpeed)
            azimuth = maxRotSpeed;
        tu[0] = theTurret.tu[0] + azimuth;
        theTurret.tu[0] = tu[0];
        float elevation = tu[1] - theTurret.tu[1];
        if (elevation < -maxRotSpeed)
            elevation = -maxRotSpeed;
        else if (elevation > maxRotSpeed)
            elevation = maxRotSpeed;
        tu[1] = theTurret.tu[1] + elevation;
        theTurret.tu[1] = tu[1];
        if (azimuth == 0.0F && elevation == 0.0F)
            return;
        float fTemp = tu[0];
        tu[0] = 0.0F;
        HM.setCurChunk(theTurret.indexB);
        am.hierMesh().chunkSetAngles(tu);
        tu[1] = fTemp;
        HM.setCurChunk(theTurret.indexA);
        am.hierMesh().chunkSetAngles(tu);
        return;
    }

    private void updateTurret(Turret theTurret, int turretIndex, float tickLen) {
        if (!theTurret.bIsOperable) {
            this.CT.WeaponControl[turretIndex + _FIRST_TURRET] = false;
            return;
        }
        if (!theTurret.bIsAIControlled) {
            tu[0] = tu[1] = tu[2] = 0.0F;
            tu[1] = theTurret.tu[0];
            HM.setCurChunk(theTurret.indexA);
            am.hierMesh().chunkSetAngles(tu);
            tu[1] = theTurret.tu[1];
            HM.setCurChunk(theTurret.indexB);
            am.hierMesh().chunkSetAngles(tu);
            return;
        }

        if (theTurret.indexA == -1 || theTurret.indexB == -1)
            return;

        am = (ActorHMesh) this.actor;
        float targetDistance = 0.0F;
        float gunnerSkillFloat = theTurret.gunnerSkill;
        if (this.W.lengthSquared() > 0.25D)
            gunnerSkillFloat *= 1.0F - (float) Math.sqrt(this.W.length() - 0.5D);
        if (getOverload() > 0.5F)
            gunnerSkillFloat *= FlightModel.cvt(getOverload(), 0.0F, 5F, 1.0F, 0.0F);
        else if (getOverload() < -0.25F)
            gunnerSkillFloat *= FlightModel.cvt(getOverload(), -1F, 0.0F, 0.0F, 1.0F);
        if (theTurret.target != null && (theTurret.target instanceof Aircraft) && ((SndAircraft) ((Aircraft) theTurret.target)).FM.isTakenMortalDamage())
            theTurret.target = null;
        if (theTurret.target == null) {
            if (theTurret.tMode != 0) {
                theTurret.tMode = Turret.TU_MO_SLEEP;
                theTurret.timeNext = Time.current();
            }
        } else {
            theTurret.target.pos.getAbs(Pt);
            theTurret.target.getSpeed(Vt);
            this.actor.getSpeed(Ve);
            Vt.sub(Ve);
            HM.setCurChunk(theTurret.indexA);
            am.getChunkLocAbs(Actor._tmpLoc);
            Ve.sub(Pt, Actor._tmpLoc.getPoint());
            targetDistance = (float) Ve.length();
            float speedDeviation = 10F * (float) Math.sin((float) (Time.current() & 0xFFFFL) * 0.003F);
            Vt.scale((targetDistance + speedDeviation) / 670.0F);
            Ve.add(Vt);
            this.Or.transformInv(Ve);
            theTurret.Lstart.transformInv(Ve);
            Ve.y = -Ve.y;
            HM.setCurChunk(theTurret.indexB);
            theTurret.Lstart.get(Oo);
            Oo.setAT0(Ve);
            Oo.get(tu);
            this.Or.transformInv(Vt);
            theTurret.Lstart.transformInv(Vt);
            Vt.normalize();
            shoot = ((Aircraft) this.actor).turretAngles(turretIndex, tu);
        }

        // TODO: +++ UP3 Sniper Gunner Hotfix +++
        float randomWidth = 0.4F;
        float skillParam = 0.1F;
        float rotationDeviation = this.rotSpeed * (6.0F - gunnerSkillFloat);
        if ((this.actor == World.getPlayerAircraft() || ((Aircraft) this.actor).isNetPlayer()) && (theTurret.target != null)) {
            randomWidth = FlightModel.cvt(targetDistance, 1000F, 200F, 0.4F, 0.2F);
            skillParam = FlightModel.cvt(targetDistance, 1000F, 200F, 0.1F, 0.06F);
            rotationDeviation /= FlightModel.cvt(targetDistance, 1000F, 0F, 1F, 5F);
        }
        float skillDeviation = skillParam * gunnerSkillFloat;
        // TODO: --- UP3 Sniper Gunner Hotfix ---

        switch (theTurret.tMode) {
            default:
                break;

            case Turret.TU_MO_SLEEP:

                // TODO: +++ UP3 Sniper Gunner Hotfix +++
                float darkness = -0.2F;
                float dawn = 0.03F;
                float searchDistance1 = FlightModel.cvt(World.Sun().ToSun.z, darkness, dawn, 500F, 3619F);
                float searchDistance2 = FlightModel.cvt(World.Sun().ToSun.z, darkness, dawn, 1500F, 6822F);
                long timeNext1 = (long) FlightModel.cvt(World.Sun().ToSun.z, darkness, dawn, 3200L, 1000L);
                long timeNext2 = 10000L;
                long timeNext3 = (long) FlightModel.cvt(World.Sun().ToSun.z, darkness, dawn, 1500L, 100L);
                long timeNext4 = (long) FlightModel.cvt(World.Sun().ToSun.z, darkness, dawn, 4000L, 3000L);
                long timeNext5 = 100L;
                long timeNext6 = 500L;
                boolean isDawn = World.Sun().ToSun.z < dawn;

                theTurret.bIsShooting = false;
                theTurret.tuLim[0] = theTurret.tuLim[1] = 0.0F;
                if (Time.current() > theTurret.timeNext) {
                    theTurret.target = War.GetNearestEnemyAircraft(actor, searchDistance1, 9);
                    if (theTurret.target == null) {
                        theTurret.target = War.GetNearestEnemyAircraft(actor, searchDistance2, 9);
                        if (theTurret.target == null)
                            theTurret.timeNext = Time.current() + World.Rnd().nextLong(timeNext1, timeNext2);
                        else if (isDawn && isTargetExposed(theTurret.target)) {
                            theTurret.tMode = Turret.TU_MO_TRACKING;
                            theTurret.timeNext = 0L;
                        } else {
                            theTurret.timeNext = Time.current() + World.Rnd().nextLong(timeNext3, timeNext4);
                        }
                    } else {
                        if (isDawn) {
                            theTurret.timeNext = Time.current() + World.Rnd().nextLong(timeNext5, timeNext6);
                            if (isNightTargetVisible(theTurret.target, gunnerSkillFloat) || isTargetExposed(theTurret.target)) {
                                theTurret.tMode = Turret.TU_MO_TRACKING;
                                theTurret.timeNext = 0L;
                            }
                        } else {
                            theTurret.tMode = Turret.TU_MO_TRACKING;
                            theTurret.timeNext = 0L;
                        }
                    }
                }

//                if (World.Sun().ToSun.z < -0.22F) {
//                    turret1.bIsShooting = false;
//                    turret1.tuLim[0] = turret1.tuLim[1] = 0.0F;
//                    if (Time.current() > turret1.timeNext) {
//                        turret1.target = War.GetNearestEnemyAircraft(actor, 1000F, 9);
//                        if (turret1.target == null) {
//                            turret1.target = War.GetNearestEnemyAircraft(actor, 3000F, 9);
//                            if (turret1.target == null)
//                                turret1.timeNext = Time.current() + World.Rnd().nextLong(3200L, 10000L);
//                            else if (isTargetExposed(turret1.target)) {
//                                turret1.tMode = Turret.TU_MO_TRACKING;
//                                turret1.timeNext = 0L;
//                            } else {
//                                turret1.timeNext = Time.current() + World.Rnd().nextLong(1500L, 4000L);
//                            }
//                        } else {
//                            turret1.timeNext = Time.current() + World.Rnd().nextLong(100L, 500L);
//                            if (isNightTargetVisible(turret1.target, f2) || isTargetExposed(turret1.target)) {
//                                turret1.tMode = Turret.TU_MO_TRACKING;
//                                turret1.timeNext = 0L;
//                            }
//                        }
//                    }
//                } else {
//                    turret1.bIsShooting = false;
//                    turret1.tuLim[0] = turret1.tuLim[1] = 0.0F;
//                    if (Time.current() <= turret1.timeNext)
//                        break;
//                    turret1.target = War.GetNearestEnemyAircraft(actor, 3619F, 9);
//                    if (turret1.target == null) {
//                        turret1.target = War.GetNearestEnemyAircraft(actor, 6822F, 9);
//                        if (turret1.target == null)
//                            turret1.timeNext = Time.current() + World.Rnd().nextLong(1000L, 10000L);
//                        else
//                            turret1.timeNext = Time.current() + World.Rnd().nextLong(100L, 3000L);
//                    } else {
//                        turret1.tMode = Turret.TU_MO_TRACKING;
//                        turret1.timeNext = 0L;
//                    }
//                }

                // TODO: --- UP3 Sniper Gunner Hotfix ---
                break;

            case Turret.TU_MO_TRACKING:
                theTurret.bIsShooting = false;
                theTurret.tuLim[0] = tu[0];
                theTurret.tuLim[1] = tu[1];
                if (!isTick(39, 16))
                    break;
                if (!shoot && targetDistance > 550F || World.Rnd().nextFloat() < 0.1F) {
                    theTurret.tMode = Turret.TU_MO_SLEEP;
                    theTurret.timeNext = Time.current();
                }
                if ((float) (World.Rnd().nextInt() & 0xff) >= 32F * (gunnerSkillFloat + 1.0F) && targetDistance >= 148F + 27F * gunnerSkillFloat)
                    break;
                if (targetDistance < 450F + 66.6F * gunnerSkillFloat) {
                    switch (theTurret.igunnerSkill) {
                        default:
                            break;

                        case TU_SKILL_ROOKIE:
                            if (Vt.x < -0.96D) {
                                switch (World.Rnd().nextInt(1, 3)) {
                                    case 1:
                                        theTurret.tMode = Turret.TU_MO_STOPPED;
                                        theTurret.timeNext = Time.current() + World.Rnd().nextLong(500L, 1200L);
                                        break;

                                    case 2:
                                        theTurret.tuLim[0] += World.Rnd().nextFloat(-15F, 15F);
                                        theTurret.tuLim[1] += World.Rnd().nextFloat(-10F, 10F);
                                        // fall through

                                    case 3:
                                        theTurret.tMode = Turret.TU_MO_FIRING_STOPPED;
                                        theTurret.timeNext = Time.current() + World.Rnd().nextLong(500L, 10000L);
                                        break;
                                }
                            } else if (Vt.x < -0.33D) {
                                theTurret.tMode = Turret.TU_MO_FIRING_STOPPED;
                                theTurret.timeNext = Time.current() + World.Rnd().nextLong(1000L, 5000L);
                            }
                            break;

                        case TU_SKILL_AVERAGE:
                        case TU_SKILL_VETERAN:
                            if (Vt.x < -0.91) {
                                if (World.Rnd().nextBoolean())
                                    theTurret.tMode = Turret.TU_MO_FIRING_STOPPED;
                                else
                                    theTurret.tMode = Turret.TU_MO_FIRING_TRACKING;
                                theTurret.timeNext = Time.current() + World.Rnd().nextLong(500L, 2200L);
                            } else {
                                if (World.Rnd().nextFloat() < 0.5F)
                                    theTurret.tMode = Turret.TU_MO_FIRING_TRACKING;
                                else
                                    theTurret.tMode = Turret.TU_MO_FIRING_STOPPED;
                                theTurret.timeNext = Time.current() + World.Rnd().nextLong(1500L, 7500L);
                            }
                            break;

                        case TU_SKILL_ACE:
                            theTurret.tMode = Turret.TU_MO_FIRING_TRACKING;
                            theTurret.timeNext = Time.current() + World.Rnd().nextLong(500L, 7500L);
                            break;
                    }
                    break;
                } else if (targetDistance < 902F + 88F * gunnerSkillFloat) {
                    theTurret.tMode = Turret.TU_MO_FIRING_STOPPED;
                    theTurret.timeNext = Time.current() + World.Rnd().nextLong(100L, 1000L);
                }
                break;

            case Turret.TU_MO_STOPPED:
                theTurret.bIsShooting = false;
                if (Time.current() > theTurret.timeNext) {
                    theTurret.tMode = Turret.TU_MO_SLEEP;
                    theTurret.timeNext = 0L;
                }
                break;

            case Turret.TU_MO_FIRING_STOPPED:
                theTurret.bIsShooting = true;
                // TODO: +++ UP3 Sniper Gunner Hotfix +++
                theTurret.tuLim[0] = tu[0] * World.Rnd().nextFloat_DomeInv(1.0F - randomWidth + skillDeviation, 1.0F + randomWidth - skillDeviation)
                        + World.Rnd().nextFloat(-tAcc2 + tAcc3 * gunnerSkillFloat - rotationDeviation, tAcc2 - tAcc3 * gunnerSkillFloat + rotationDeviation);
                theTurret.tuLim[1] = tu[1] * World.Rnd().nextFloat_DomeInv(1.0F - randomWidth + skillDeviation, 1.0F + randomWidth - skillDeviation)
                        + World.Rnd().nextFloat(-tAcc2 + tAcc3 * gunnerSkillFloat - rotationDeviation, tAcc2 - tAcc3 * gunnerSkillFloat + rotationDeviation);
//                turret1.tuLim[0] = tu[0] * World.Rnd().nextFloat(0.85F + 0.05F * f2, 1.15F - 0.05F * f2) + World.Rnd().nextFloat(-tAcc2 + tAcc3 * f2, tAcc2 - tAcc3 * f2);
//                turret1.tuLim[1] = tu[1] * World.Rnd().nextFloat(0.85F + 0.05F * f2, 1.15F - 0.05F * f2) + World.Rnd().nextFloat(-tAcc2 + tAcc3 * f2, tAcc2 - tAcc3 * f2);
                // TODO: --- UP3 Sniper Gunner Hotfix ---
                if (Time.current() > theTurret.timeNext)
                    theTurret.tMode = Turret.TU_MO_TRACKING;
                break;

            case Turret.TU_MO_FIRING_TRACKING:
                theTurret.bIsShooting = true;
                // TODO: +++ UP3 Sniper Gunner Hotfix +++
                theTurret.tuLim[0] = tu[0] * World.Rnd().nextFloat_DomeInv(1.0F - randomWidth + skillDeviation, 1.0F + randomWidth - skillDeviation) + World.Rnd().nextFloat(-tAcc2 + tAcc3 * gunnerSkillFloat, tAcc2 - tAcc3 * gunnerSkillFloat);
                theTurret.tuLim[1] = tu[1] * World.Rnd().nextFloat_DomeInv(1.0F - randomWidth + skillDeviation, 1.0F + randomWidth - skillDeviation)
                        + World.Rnd().nextFloat(-tAcc2 + tAcc3 * gunnerSkillFloat - rotationDeviation, tAcc2 - tAcc3 * gunnerSkillFloat + rotationDeviation);
//                turret1.tuLim[0] = tu[0] * World.Rnd().nextFloat(0.91F + 0.03F * f2, 1.09F - 0.03F * f2) + World.Rnd().nextFloat(-5F + 1.666F * f2, 5F - 1.666F * f2);
//                turret1.tuLim[1] = tu[1] * World.Rnd().nextFloat(0.91F + 0.03F * f2, 1.09F - 0.03F * f2) + World.Rnd().nextFloat(-5F + 1.666F * f2, 5F - 1.666F * f2);
                // TODO: --- UP3 Sniper Gunner Hotfix ---
                if (Time.current() <= theTurret.timeNext)
                    break;
                theTurret.tMode = Turret.TU_MO_TRACKING;
                // if (turret1.igunnerSkill == 0 || turret1.igunnerSkill == 1)
                // TODO: +++ UP3 Sniper Gunner Hotfix +++
                if (this.turretSkill < TU_SKILL_VETERAN) {
                    // if (f2 <= 1.0F) {
                    // TODO: --- UP3 Sniper Gunner Hotfix ---
                    theTurret.tMode = Turret.TU_MO_SLEEP;
                    theTurret.timeNext = Time.current() + World.Rnd().nextLong(100L, (long) ((gunnerSkillFloat + 1.0F) * 700F));
                }
                break;

            case Turret.TU_MO_PANIC:
                theTurret.bIsShooting = true;
                shoot = true;
                ((Aircraft) this.actor).turretAngles(turretIndex, theTurret.tuLim);
                if (isTick(20, 0)) {
                    theTurret.tuLim[0] += World.Rnd().nextFloat(-50F, 50F);
                    theTurret.tuLim[1] += World.Rnd().nextFloat(-50F, 50F);
                }
                if (Time.current() > theTurret.timeNext) {
                    theTurret.tMode = Turret.TU_MO_STOPPED;
                    theTurret.timeNext = Time.current() + World.Rnd().nextLong(100L, 1500L);
                }
                break;
        }
        shoot &= theTurret.bIsShooting;

        if (shoot)
            shoot = !isComingFromTheSun(theTurret.target);

        this.CT.WeaponControl[turretIndex + _FIRST_TURRET] = shoot;
        updateRotation(theTurret, tickLen);
    }

    public void hit(int i) {
        if (!Actor.isValid(this.actor))
            return;
        if (this.actor.isNetMirror())
            return;
        super.hit(i);
    }

    public float getSpeed() {
        if (!Actor.isValid(this.actor))
            return 0.0F;
        if (this.actor.isNetMirror())
            return (float) this.Vwld.length();
        else
            return super.getSpeed();
    }

    public void update(float tickLen) {
        if (!bstartmotor)
            this.EI.setEngineStops();
        if (this.actor.isNetMirror())
            ((NetAircraft.Mirror) this.actor.net).fmUpdate(tickLen);
        else
            FMupdate(tickLen);
    }

    public final void FMupdate(float tickLen) {
        if (turret != null) {
            int i = turret.length;
            for (int j = 0; j < i; j++)
                updateTurret(turret[j], j, tickLen);

        }
        super.update(tickLen);
    }

    protected void putScareShpere() {
        v1.set(1.0D, 0.0D, 0.0D);
        this.Or.transform(v1);
        v1.scale(2000D);
        p2.set(this.Loc);
        p2.add(v1);
        Engine.land();
        if (Landscape.rayHitHQ(this.Loc, p2, p)) {
            float f = (float) ((double) getAltitude() - Engine.land().HQ_Air(this.Loc.x, this.Loc.y));
            if ((this.actor instanceof TypeDiveBomber) && f > 780F && this.Or.getTangage() < -70F) {
                ScareEnemies.set(16);
                Engine.collideEnv().getNearestEnemies(p, 75D, this.actor.getArmy(), ScareEnemies.enemies());
            }
            if (this.actor instanceof TypeStormovik) {
                if (f < 600F && this.Or.getTangage() < -15F) {
                    ScareEnemies.set(2);
                    Engine.collideEnv().getNearestEnemies(p, 45D, this.actor.getArmy(), ScareEnemies.enemies());
                }
            } else if ((this.actor instanceof TypeFighter) && f < 500F && this.Or.getTangage() < -15F) {
                ScareEnemies.set(2);
                Engine.collideEnv().getNearestEnemies(p, 45D, this.actor.getArmy(), ScareEnemies.enemies());
            }
        }
    }

    public void moveCarrier() {
        if (this.AP.way.isLandingOnShip()) {
            if (this.AP.way.landingAirport == null) {
                int i = this.AP.way.Cur();
                this.AP.way.last();
                if (this.AP.way.curr().Action == 2) {
                    Actor actor = this.AP.way.curr().getTarget();
                    if (actor != null && (actor instanceof BigshipGeneric))
                        this.AP.way.landingAirport = ((BigshipGeneric) actor).getAirport();
                }
                this.AP.way.setCur(i);
            }
            if (Actor.isAlive(this.AP.way.landingAirport) && !this.AP.way.isLanding())
                this.AP.way.landingAirport.rebuildLastPoint(this);
        }
    }

    private void setTrigger() {
        timeToTakeOff = World.getTimeofDay();
        SectFile sectfile = Mission.cur().sectFile();
        String s = "AOC_Trigger";
        String s1 = this.actor.name();
        int i = s1.length();
        s1 = s1.substring(0, i - 1);
        String s2 = sectfile.get(s, s1);
        if (s2 != null) {
            s2 = s2.toLowerCase();
            initTrigger(s1, s2);
        }
    }

    private void initTrigger(String s, String s1) {
        ZutiMDSVariables variables = Mission.MDS_VARIABLES();

        StringTokenizer stringtokenizer = new StringTokenizer(s1, " ");
        numberOfTrigger = stringtokenizer.countTokens();
        typeOfTrigger = new int[numberOfTrigger];
        valueOfTrigger = new String[numberOfTrigger];
        for (int i = 0; i < numberOfTrigger; i++) {
            String s2 = stringtokenizer.nextToken();
            String s3 = s2.substring(0, 5);
            if (s3.equals("delay")) {
                typeOfTrigger[i] = -1;
                valueOfTrigger[i] = "delay";
                s3 = s2.substring(6);
                timeToTakeOff = World.getTimeofDay() + Float.parseFloat(s3) / 60F;
            }
            if (s3.equalsIgnoreCase("clone")) {
                typeOfTrigger[i] = -2;
                s3 = s2.substring(6);
                valueOfTrigger[i] = s3;
            }
            s3 = s2.substring(0, 4);
            if (s3.equals("zone")) {
                int k = s2.indexOf(':');
                String s4 = s2.substring(4, k);
                int l = Integer.parseInt(s4);
                World.cur();
                if (l < variables.getZone()) {
                    String s5 = s2.substring(k + 1);
                    typeOfTrigger[i] = l;
                    valueOfTrigger[i] = s5;
                } else {
                    numberOfTrigger--;
                }
            }
        }

        if (numberOfTrigger > 0)
            bstartmotor = false;
    }

    private float setGunner() {
        SectFile sectfile = Mission.cur().sectFile();
        String s = "AOC_Gunner";
        String s1 = this.actor.name();
        int i = s1.length();
        s1 = s1.substring(0, i - 1);
        String s2 = sectfile.get(s, s1);
        if (s2 == null) {
            s2 = sectfile.get(s, this.actor.name());
            if (s2 == null)
                return 1.0F;
        }
        StringTokenizer stringtokenizer = new StringTokenizer(s2, " ");
        float af[] = new float[2];
        int j;
        for (j = 0; stringtokenizer.hasMoreElements(); j++)
            af[j] = Float.parseFloat(stringtokenizer.nextToken());

        if (j != 1) {
            float f = af[1] * (2.0F * (float) Math.random() - 1.0F);
            af[0] += f;
        }
        if (af[0] < 0.0F)
            af[0] = 0.0F;
        if (af[0] > 1.0F)
            af[0] = 1.0F;
        return af[0];
    }

    private boolean isNightTargetVisible(Actor actor, float f) {
        float f1 = World.Sun().sunMultiplier;
        int i = Mission.curCloudsType();
        float f2 = Mission.curCloudsHeight();
        float f3 = 500F;
        if (am.pos.getAbs().getZ() < actor.pos.getAbs().getZ()) {
            if (actor.pos.getAbs().getZ() < (double) (f2 + f3))
                if (i > 2)
                    f1 *= 1.2F;
                else if (i > 3)
                    f1 *= 1.3F;
            f1 *= 1.1F;
        } else if (actor.pos.getAbs().getZ() > (double) (f2 + f3))
            if (i > 2)
                f1 *= 1.2F;
            else if (i > 3)
                f1 *= 1.3F;
        v3.sub(am.pos.getAbsPoint(), actor.pos.getAbsPoint());
        float f4 = FlightModel.cvt(f, 0.0F, 3F, 0.75F, 1.2F);
        float f5 = FlightModel.cvt((float) v3.length(), 0.0F, 800F, 1.0F, 0.1F);
        float f6 = FlightModel.cvt(f1, 0.095F, 1.0F, 1E-005F, tAcc1) * f5 * f4;
        float f7 = World.Rnd().nextFloat();
        return f7 < f6;
    }

    private boolean isComingFromTheSun(Actor actor) {
        if ((actor instanceof Aircraft) && World.Sun().ToSun.z > 0.0F) {
            if (Mission.curCloudsType() > 3 && actor.pos.getAbs().getZ() < (double) (Mission.curCloudsHeight() + 200F))
                return false;
            v3.set(World.Sun().ToSun.x, World.Sun().ToSun.y, World.Sun().ToSun.z);
            v2.sub(am.pos.getAbsPoint(), actor.pos.getAbsPoint());
            float f = (float) v2.length();
            v2.normalize();
            double d = v3.angle(v2);
            float f1 = FlightModel.cvt(f, 100F, 3000F, 2.9F, 3F);
            if (d > (double) f1)
                return true;
        }
        return false;
    }

    private boolean isTargetExposed(Actor actor) {
        if (actor instanceof Aircraft) {
            Aircraft aircraft = (Aircraft) actor;
            if (aircraft.FM.AS.bLandingLightOn || aircraft.FM.AS.bNavLightsOn || aircraft.FM.CT.WeaponControl[0] || aircraft.FM.CT.WeaponControl[1])
                return true;
            if (World.Sun().ToMoon.z > 0.0F) {
                if (Mission.curCloudsType() > 3 && actor.pos.getAbs().getZ() < (double) (Mission.curCloudsHeight() + 200F))
                    return false;
                v3.set(World.Sun().ToMoon.x, World.Sun().ToMoon.y, World.Sun().ToMoon.z);
                v2.sub(am.pos.getAbsPoint(), actor.pos.getAbsPoint());
                float f = (float) v2.length();
                v2.normalize();
                double d = v3.angle(v2);
                float f1 = FlightModel.cvt(f, 100F, 3000F, 2.9F, 3F);
                if (d > (double) f1)
                    return true;
            }
            return false;
        } else {
            return false;
        }
    }

    protected static float cvt(float f, float f1, float f2, float f3, float f4) {
        f = Math.min(Math.max(f, f1), f2);
        return f3 + ((f4 - f3) * (f - f1)) / (f2 - f1);
    }

    public Turret           turret[];
    protected HierMesh      HM;
    ActorHMesh              am;
    private boolean         shoot;
    public boolean          turnOffCollisions;
    public boolean          brakeShoe;
    public Loc              brakeShoeLoc;
    public Actor            brakeShoeLastCarrier;
    public boolean          canChangeBrakeShoe;
    public static final int _FIRST_TURRET    = 10;
    private static Point3d  Pt               = new Point3d();
    private static Vector3d Ve               = new Vector3d();
    private static Vector3d Vt               = new Vector3d();
    private static Orient   Oo               = new Orient();
    private static float    tu[]             = new float[3];
    public float            dryFriction;
    static Point3d          p                = new Point3d();
    static Vector3d         v1               = new Vector3d();
    static Point3d          p2               = new Point3d();
    public float            timeToTakeOff;
    public int              numberOfTrigger;
    public int              typeOfTrigger[];
    public String           valueOfTrigger[];
    public boolean          bstartmotor;

    private final float     tAcc1            = 0.05F;
    private final float     tAcc2            = 8F;
    private final float     tAcc3            = 2.666667F;
    private static Vector3d v2               = new Vector3d();
    private static Vector3d v3               = new Vector3d();

    // TODO: +++ UP3 Sniper Gunner Hotfix +++
    public static final int TU_SKILL_ROOKIE  = 0;
    public static final int TU_SKILL_AVERAGE = 1;
    public static final int TU_SKILL_VETERAN = 2;
    public static final int TU_SKILL_ACE     = 3;
    private float           rotSpeed         = 0.0F;
    private float           prev0            = 0.0F;
    private float           prev1            = 0.0F;
    // TODO: --- UP3 Sniper Gunner Hotfix ---

}
