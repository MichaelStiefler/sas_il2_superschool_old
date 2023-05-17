package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public class FI_103RIV extends Scheme1 implements TypeDockable, MsgCollisionRequestListener {

    public FI_103RIV() {
        this.bNeedSetup = true;
        this.dtime = -1L;
        this.queen_last = null;
        this.queen_time = 0L;
        this.target_ = null;
        this.queen_ = null;
        this.flame = null;
        this.dust = null;
        this.trail = null;
        this.sprite = null;
        this.max_pow = 0.3F;
        this.soundfx = null;
        this.execute = false;
        this.engineRunup = false;
    }

    public void onAircraftLoaded() {
        this.FM.Gears.setHydroOperable(false);
        this.FM.Gears.setOperable(false);
        this.FM.CT.bHasBrakeControl = false;
        this.FM.setCapableOfTaxiing(false);
        this.FM.setCapableOfACM(false);
        this.FM.EI.setThrottle(this.max_pow);
        this.FM.EI.engines[0].doSetKillControlThrottle();
        if (Config.isUSE_RENDER()) {
            this.sprite = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), (Loc) null, 1.25F, "3DO/Effects/Aircraft/TurboJRD1100S.eff", -1F);
            this.dust = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), (Loc) null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100D.eff", -1F);
            this.flame = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), (Loc) null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            this.trail = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), (Loc) null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100T.eff", -1F);
            Eff3DActor.setIntesity(this.sprite, 1.0F);
            Eff3DActor.setIntesity(this.dust, 0.0F);
            Eff3DActor.setIntesity(this.flame, 1.0F);
            Eff3DActor.setIntesity(this.trail, 0.0F);
        }
        this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
    }

    public void destroy() {
        if (Actor.isValid(this.sprite)) {
            this.sprite.destroy();
        }
        if (Actor.isValid(this.flame)) {
            this.flame.destroy();
        }
        if (Actor.isValid(this.trail)) {
            this.trail.destroy();
        }
        if (this.soundfx != null) {
            this.soundfx.cancel();
            this.soundfx = null;
        }
        super.destroy();
    }

    public void doMurderPilot(int i) {
        if (i != 0) {
            return;
        }
        this.hierMesh().chunkVisible("Pilot1_D0", false);
        this.hierMesh().chunkVisible("Head1_D0", false);
        this.hierMesh().chunkVisible("Pilot1_D1", true);
        this.hierMesh().chunkVisible("HMask1_D0", false);
        if (!this.FM.AS.bIsAboutToBailout) {
            if (this.hierMesh().isChunkVisible("Blister1_D0")) {
                this.hierMesh().chunkVisible("Gore1_D0", true);
            }
            this.hierMesh().chunkVisible("Gore2_D0", true);
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(12.71F, shot);
                }
                if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                }
                return;
            }
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                    case 4:
                        if (this.getEnergyPastArmor(4.5F, shot) > 0.0F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Ailerons Controls Out..");
                        }
                        break;

                    case 2:
                    case 3:
                        if ((this.getEnergyPastArmor(1.5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            this.debuggunnery("Ailerons Controls Out..");
                        }
                        break;

                    case 5:
                        if ((this.getEnergyPastArmor(1.5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.45F)) {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            this.debuggunnery("*** Engine1 Throttle Controls Out..");
                        }
                        break;

                    case 6:
                        if (this.getEnergyPastArmor(4F, shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Elevator Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Rudder Controls Out..");
                        }
                        break;

                    case 7:
                        if (this.getEnergyPastArmor(1.0F, shot) <= 0.0F) {
                            break;
                        }
                        if (World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            this.debuggunnery("Elevator Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            this.debuggunnery("Rudder Controls Out..");
                        }
                        break;
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxpnm")) {
                if (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
                    this.FM.AS.setInternalDamage(shot.initiator, 1);
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** Tail1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Tail1_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                if ((this.getEnergyPastArmor(2.1F, shot) > 0.0F) && (shot.powerType == 3) && (World.Rnd().nextFloat() < 0.1F)) {
                    this.FM.AS.hitTank(shot.initiator, 0, 2);
                    this.debuggunnery("Fuel Tank: Hit..");
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 1) {
                this.hitChunk("CF", shot);
                if ((this.getEnergyPastArmor(2.1F, shot) > 0.0F) && (shot.powerType == 3) && (World.Rnd().nextFloat() < 0.1F)) {
                    this.FM.AS.hitTank(shot.initiator, 0, 2);
                    this.debuggunnery("Fuel Tank: Hit..");
                }
                if ((point3d.x > 0.4D) && (point3d.x < 1.672D) && (point3d.z <= 0.399D)) {
                    if (point3d.x > 1.387D) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                    } else if (point3d.y > 0.0D) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                    } else {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                    }
                }
            }
        } else if (s.startsWith("xcockpit")) {
            this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int j;
            if (s.endsWith("a")) {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else {
                j = s.charAt(5) - 49;
            }
            this.hitFlesh(j, shot, byte0);
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 1) {
                this.hitChunk("Tail1", shot);
                if (World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 5);
                }
            }
        } else if (s.startsWith("xkeel1")) {
            this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xrudder1")) {
            this.hitChunk("Rudder1", shot);
        } else if (s.startsWith("xstabl")) {
            this.hitChunk("StabL", shot);
        } else if (s.startsWith("xstabr")) {
            this.hitChunk("StabR", shot);
        } else if (s.startsWith("xVatorL")) {
            this.hitChunk("xVatorL", shot);
        } else if (s.startsWith("xVatorR")) {
            this.hitChunk("xVatorR", shot);
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 1) {
                this.hitChunk("WingLIn", shot);
            }
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 1) {
                this.hitChunk("WingRIn", shot);
            }
        } else if (s.startsWith("xengine1") && (this.chunkDamageVisible("Engine1") < 1)) {
            this.hitChunk("Engine1", shot);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.cut("StabL");
                this.cut("StabR");
                this.FM.cut(17, j, actor);
                this.FM.cut(18, j, actor);
                this.FM.cut(11, j, actor);
                this.cutFM(3, j, actor);
                break;

            case 3:
                this.FM.EI.engines[0].setEngineDies(actor);
                if (this.soundfx != null) {
                    this.soundfx.cancel();
                    this.soundfx = null;
                }
                this.doRemoveEngine();
                this.FM.M.massEmpty -= 138F;
                this.FM.Sq.dragEngineCx[0] = 0.0F;
                this.FM.Sq.liftKeel = 0.0F;
                this.FM.Sq.squareRudders = 0.0F;
                this.FM.SensYaw = 0.0F;
                this.FM.setCapableOfACM(false);
                this.FM.setReadyToDie(true);
                return false;
        }
        return super.cutFM(i, j, actor);
    }

    public boolean cut(String s) {
        if (s.startsWith("xcf")) {
            this.FM.AS.hitTank(this, 2, 4);
        }
        if (s.startsWith("xtail")) {
            this.FM.AS.hitTank(this, 2, 4);
        }
        return super.cut(s);
    }

    private void doRemoveEngine() {
        Aircraft aircraft = (Aircraft) this.FM.actor;
        if (aircraft.hierMesh().chunkFindCheck("Engine1_D0") != -1) {
            aircraft.hierMesh().hideSubTrees("Engine1_D0");
            Wreckage wreckage = new Wreckage((ActorHMesh) this.FM.actor, aircraft.hierMesh().chunkFind("Engine1_D0"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(aircraft.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    protected void doExplosion() {
        super.doExplosion();
        if (this.thisWeaponsName.startsWith("none")) {
            return;
        }
        World.cur();
        if ((this.FM.Loc.z - 10D) < World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y)) {
            if (Engine.land().isWater(this.FM.Loc.x, this.FM.Loc.y)) {
                Explosions.BOMB1000a_Water(this.FM.Loc, 1.0F, 1.0F);
            } else {
                Explosions.BOMB1000a_Land(this.FM.Loc, 1.0F, 1.0F, true); //, true);
            }
        } else {
            Explosions.BOMB1000a_Object(this.FM.Loc, 1.0F, 1.0F);
        }
    }

    public void startEngine() {
        if (!this.FM.AS.isMaster()) return;
        if (this.FM.EI.engines[0].getStage() == 0) {
            this.FM.EI.setEngineRunning();
            engineRunup = true;
            this.FM.CT.setPowerControl(0.9F);
            this.FM.EI.setThrottle(0.9F);
            this.FM.EI.engines[0].setStage(this, 6);
        }
    }

    public void stopEngine() {
        if (!this.FM.AS.isMaster()) return;
        if (this.FM.EI.engines[0].getStage() != 0) {
            this.FM.CT.setPowerControl(0.0F);
            this.FM.EI.setThrottle(0.0F);
            this.FM.EI.engines[0].setEngineStops(this);
            this.FM.AS.setSootState(this, 0, 0);
            Eff3DActor.setIntesity(this.sprite, 0F);
            Eff3DActor.setIntesity(this.flame, 0F);
            Eff3DActor.setIntesity(this.dust, 0F);
            engineRunup = false;
        }
    }
    
    public void update(float f) {
        if (this.bNeedSetup) {
            this.checkAsDrone();
        }
        if ((this.FM instanceof Maneuver) && (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode())) {
            if (this.typeDockableIsDocked()) {
                ((Maneuver) this.FM).unblock();
                ((Maneuver) this.FM).set_maneuver(48);
                ((Maneuver) this.FM).AP.way.setCur(((Aircraft) this.queen_).FM.AP.way.Cur());
                ((Pilot) this.FM).setDumbTime(3000L);
            } else {
                if (this.FM.EI.engines[0].getStage() == 0) {
                    this.FM.EI.setEngineRunning();
                }
                if (this.dtime > 0L) {
                    ((Maneuver) this.FM).setBusy(false);
                    ((Maneuver) this.FM).Group.leaderGroup = null;
                    ((Maneuver) this.FM).set_maneuver(22);
                    ((Pilot) this.FM).setDumbTime(3000L);
                    if (this.queen_last instanceof AR_234C2) {
                        if (this.queen_last instanceof AR_234C2) this.FM.Vwld.z += 0.5D;
                    }
                    if (Time.current() > (this.dtime + 3000L)) {
                        this.dtime = -1L;
                        ((Maneuver) this.FM).clear_stack();
                        ((Maneuver) this.FM).pop();
                        ((Pilot) this.FM).setDumbTime(0L);
                    }
                }
            }
        }
        super.update(f);
        if (this.FM.AS.isMaster()) {
            if ((this.FM.CT.PowerControl > 0.77F) && (this.FM.EI.engines[0].getStage() == 0) && (this.FM.M.fuel > 0.0F) && !this.typeDockableIsDocked()) {
                this.FM.EI.engines[0].setStage(this, 6);
            }
            if ((this.FM.M.fuel == 0.0F) || this.typeDockableIsDocked() && !engineRunup) {
                this.FM.EI.setThrottle(0.0F);
                this.FM.EI.engines[0].setEngineStops(this);
            }
            if (Config.isUSE_RENDER()) {
                if ((this.FM.EI.engines[0].getw() > 50F) && (this.FM.EI.engines[0].getStage() == 6)) {
                    this.FM.AS.setSootState(this, 0, 1);
                } else {
                    this.FM.AS.setSootState(this, 0, 0);
                }
            }
            if (this.FM.CT.PowerControl < (this.max_pow / 2.0F)) {
                this.FM.EI.setThrottle(0.0F);
            }
            
            if (!this.typeDockableIsDocked()) engineRunup = false;
            if (!this.isNetMirror() && engineRunup) {
                this.FM.CT.setPowerControl(0.9F);
                this.FM.EI.setThrottle(0.9F);
                this.FM.EI.engines[0].setStage(this, 6);
            }
            if (this.FM instanceof Pilot) {
                if (!this.FM.isPlayers() || (this.FM.isPlayers() && (this.FM instanceof RealFlightModel) && !((RealFlightModel) this.FM).isRealMode())) {
                    Pilot pilot = (Pilot)this.FM;
                    pilot.kamikaze = pilot.silence = true;
                    if (!this.isNetMirror() && !this.typeDockableIsDocked()) {
                        if (this.execute || (this.FM.AP.way.isLast() && (this.FM.AP.getWayPointDistance() < (this.FM.Loc.z + 1275D)))) {
                            this.execute = true;
                            this.FM.CT.setPowerControl(0.0F);
                            this.FM.EI.setThrottle(0.0F);
                            this.FM.EI.engines[0].setEngineStops(this);
                            this.FM.AS.setSootState(this, 0, 0);
                            Eff3DActor.setIntesity(this.sprite, 0F);
                            Eff3DActor.setIntesity(this.flame, 0F);
                            Eff3DActor.setIntesity(this.dust, 0F);
                            pilot.setDumbTime(9999L);
                            pilot.set_maneuver(13);
                        }
                    }
                }
            }
            this.thrust_eff = this.FM.EI.getThrustOutput() / this.max_pow;
            if (this.FM.EI.engines[0].getStage() != 6) this.thrust_eff = 0F;
        } else {
            this.thrust_eff = (float)Math.min(this.FM.EI.engines[0].getw() / 30D, 2.5F);
        }
        if (Config.isUSE_RENDER()) {
            if (this.soundfx != null) {
                this.soundfx.setVolume(this.thrust_eff);
            }
            Eff3DActor.setIntesity(this.sprite, this.thrust_eff);
            Eff3DActor.setIntesity(this.flame, this.thrust_eff);
            Eff3DActor.setIntesity(this.dust, this.thrust_eff);
        }
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        super.msgCollisionRequest(actor, aflag);
        if ((this.queen_last != null) && (this.queen_last == actor) && ((this.queen_time == 0L) || (Time.current() < (this.queen_time + 5000L)))) {
            aflag[0] = false;
        } else {
            aflag[0] = true;
        }
    }

    public void msgEndAction(Object obj, int i) {
        super.msgEndAction(obj, i);
        switch (i) {
            case 2:
                Actor actor;
                if (Actor.isValid(this.queen_last)) {
                    actor = this.queen_last;
                } else {
                    actor = Engine.cur.actorLand;
                }
                if (!this.thisWeaponsName.startsWith("none")) {
                    MsgExplosion.send(this, (String) null, this.FM.Loc, actor, 0.0F, 2400F, 0, 600F);
                }
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag && (this.FM.AP.way.curr().Action == 3) && this.typeDockableIsDocked() && (Math.abs(((Aircraft) this.queen_).FM.Or.getKren()) < 30F) && (!this.FM.isPlayers() || (this.FM.isPlayers() && (this.FM instanceof RealFlightModel) && !((RealFlightModel) this.FM).isRealMode()))) {
            this.typeDockableAttemptDetach();
            ((Maneuver) this.FM).set_maneuver(22);
            ((Maneuver) this.FM).setCheckStrike(false);
        }
    }

    public void missionStarting() {
        this.checkAsDrone();
    }

    private void checkAsDrone() {
        if (this.target_ == null) {
            if (this.FM.AP.way.curr().getTarget() == null) {
                this.FM.AP.way.next();
            }
            this.target_ = this.FM.AP.way.curr().getTarget();
            if (Actor.isValid(this.target_) && (this.target_ instanceof Wing)) {
                Wing wing = (Wing) this.target_;
                int i = this.aircIndex();
                if (Actor.isValid(wing.airc[i])) {
                    this.target_ = wing.airc[i];
                } else {
                    this.target_ = null;
                }
                if ((this.target_ instanceof AR_234C2) || (this.target_ instanceof HE_111H2)) {
                    if (Actor.isValid(wing.airc[i])) {
                        this.target_ = wing.airc[i];
                    } else {
                        this.target_ = null;
                    }
                }
            }
        }
        if (Actor.isValid(this.target_) && ((this.target_ instanceof HE_111H2) || (this.target_ instanceof AR_234C2))) {
            this.queen_last = this.target_;
            this.queen_time = Time.current();
            if (this.isNetMaster()) {
                ((TypeDockable) this.target_).typeDockableRequestAttach(this, this.aircIndex(), true);
            }
        }
        this.bNeedSetup = false;
        this.target_ = null;
    }

    public int typeDockableGetDockport() {
        if (this.typeDockableIsDocked()) {
            return this.dockport_;
        } else {
            return -1;
        }
    }

    public Actor typeDockableGetQueen() {
        return this.queen_;
    }

    public boolean typeDockableIsDocked() {
        return Actor.isValid(this.queen_);
    }

    public void typeDockableAttemptAttach() {
        if (this.FM.AS.isMaster() && !this.typeDockableIsDocked()) {
            Aircraft aircraft = War.getNearestFriend(this);
            if ((aircraft instanceof AR_234C2) || (aircraft instanceof HE_111H2) || (this.target_ instanceof G4M2E) || (this.target_ instanceof HE_111Z) || (this.target_ instanceof FW_200C3U4) || (this.target_ instanceof AR_234C2)) {
                ((TypeDockable) aircraft).typeDockableRequestAttach(this);
            }
        }
    }

    public void typeDockableAttemptDetach() {
        if (this.FM.AS.isMaster() && this.typeDockableIsDocked() && Actor.isValid(this.queen_)) {
            ((TypeDockable) this.queen_).typeDockableRequestDetach(this);
        }
    }

    public void typeDockableRequestAttach(Actor actor) {
    }

    public void typeDockableRequestDetach(Actor actor) {
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i) {
    }

    public void typeDockableDoDetachFromDrone(int i) {
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i) {
        this.queen_ = actor;
        this.dockport_ = i;
        this.queen_last = this.queen_;
        this.queen_time = 0L;
    }

    public void typeDockableDoDetachFromQueen(int i) {
        if (this.dockport_ == i) {
            this.queen_last = this.queen_;
            this.queen_time = Time.current();
            this.queen_ = null;
            this.dockport_ = 0;
        }
        this.startEngine();
        if (!(this.queen_last instanceof AR_234C2)) this.FM.Vwld.z -= 5D;
        this.dtime = Time.current();
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        if (this.typeDockableIsDocked()) {
            netmsgguaranted.writeByte(1);
            ActorNet actornet = null;
            if (Actor.isValid(this.queen_)) {
                actornet = this.queen_.net;
                if (actornet.countNoMirrors() > 0) {
                    actornet = null;
                }
            }
            netmsgguaranted.writeByte(this.dockport_);
            netmsgguaranted.writeNetObj(actornet);
        } else {
            netmsgguaranted.writeByte(0);
        }
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        if (netmsginput.readByte() == 1) {
            this.dockport_ = netmsginput.readByte();
            NetObj netobj = netmsginput.readNetObj();
            if (netobj != null) {
                Actor actor = (Actor) netobj.superObj();
                ((TypeDockable) actor).typeDockableDoAttachToDrone(this, this.dockport_);
            }
        }
    }

    private boolean    bNeedSetup;
    private long       dtime;
    private Actor      queen_last;
    private long       queen_time;
    private Actor      target_;
    private Actor      queen_;
    private int        dockport_;
    private Eff3DActor flame;
    private Eff3DActor dust;
    private Eff3DActor trail;
    private Eff3DActor sprite;
    private float      thrust_eff;
    private float      max_pow;
    private SoundFX    soundfx;
    private boolean    execute;
    private boolean    engineRunup;

    static {
        Class class1 = FI_103RIV.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Reichenberg IV");
        Property.set(class1, "meshName", "3do/Rocketry/Fi-103_V-4_rocket/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/V-1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFI_103R.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_Clip00" });
    }
}
