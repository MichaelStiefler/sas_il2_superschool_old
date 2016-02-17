package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CLASS;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class I_16TYPE5_SPB extends I_16 implements MsgCollisionRequestListener, TypeTNBFighter, TypeStormovik, TypeDockable {

    public I_16TYPE5_SPB() {
        queen_last = null;
        queen_time = 0L;
        bNeedSetup = true;
        dtime = -1L;
        target_ = null;
        queen_ = null;
        bailingOut = false;
        canopyForward = false;
        okToJump = false;
        flaperonAngle = 0.0F;
        aileronsAngle = 0.0F;
        oneTimeCheckDone = false;
        sideDoorOpened = false;
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        super.msgCollisionRequest(actor, aflag);
        if (queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L))
            aflag[0] = false;
        else
            aflag[0] = true;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19: // '\023'
                FM.Gears.hitCentreGear();
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xxtank1") && getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.3F) {
            if (FM.AS.astateTankStates[0] == 0) {
                Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                FM.AS.hitTank(shot.initiator, 0, 2);
            }
            if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F) {
                FM.AS.hitTank(shot.initiator, 0, 2);
                Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
            }
        } else {
            super.hitBone(s, shot, point3d);
        }
    }

    public void moveCockpitDoor(float f) {
        if (bailingOut && f >= 1.0F && !canopyForward) {
            canopyForward = true;
            FM.CT.forceCockpitDoor(0.0F);
            FM.AS.setCockpitDoor(this, 1);
        } else if (canopyForward) {
            hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 160F * f, 0.0F);
            if (f >= 1.0F) {
                okToJump = true;
                super.hitDaSilk();
            }
        } else {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[2] = 0.0F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            Aircraft.xyz[1] = f * 0.548F;
            hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void hitDaSilk() {
        if (okToJump)
            super.hitDaSilk();
        else if (FM.isPlayers() || isNetPlayer()) {
            if ((double) FM.CT.getCockpitDoor() == 1.0D && !bailingOut) {
                bailingOut = true;
                okToJump = true;
                canopyForward = true;
                super.hitDaSilk();
            }
        } else if (!FM.AS.isPilotDead(0))
            if ((double) FM.CT.getCockpitDoor() < 1.0D && !bailingOut) {
                bailingOut = true;
                FM.AS.setCockpitDoor(this, 1);
            } else if ((double) FM.CT.getCockpitDoor() == 1.0D && !bailingOut) {
                bailingOut = true;
                okToJump = true;
                canopyForward = true;
                super.hitDaSilk();
            }
        if (!sideDoorOpened && FM.AS.bIsAboutToBailout && !FM.AS.isPilotDead(0)) {
            sideDoorOpened = true;
            FM.CT.forceCockpitDoor(0.0F);
            FM.AS.setCockpitDoor(this, 1);
        }
    }

    public void moveGear(float f) {
        super.moveGear(f);
        if (f > 0.5F) {
            hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f, 0.5F, 1.0F, 14.5F, -8F), Aircraft.cvt(f, 0.5F, 1.0F, 44F, 62.5F), 0.0F);
            hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.5F, 1.0F, -14.5F, 8F), Aircraft.cvt(f, 0.5F, 1.0F, -44F, -62.5F), 0.0F);
        } else if (f > 0.25F) {
            hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f, 0.25F, 0.5F, 33F, 14.5F), Aircraft.cvt(f, 0.25F, 0.5F, 38F, 44F), 0.0F);
            hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.25F, 0.5F, -33F, -14.5F), Aircraft.cvt(f, 0.25F, 0.5F, -38F, -44F), 0.0F);
        } else {
            hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, 33F), Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, 38F), 0.0F);
            hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, -33F), Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, -38F), 0.0F);
        }
        if (f > 0.5F) {
            hierMesh().chunkVisible("GearWireR2_D0", true);
            hierMesh().chunkVisible("GearWireL2_D0", true);
        } else {
            hierMesh().chunkVisible("GearWireR2_D0", false);
            hierMesh().chunkVisible("GearWireL2_D0", false);
        }
    }

    public void update(float f) {
        if (bNeedSetup)
            checkAsDrone();
        int i = aircIndex();
        if (FM instanceof Maneuver)
            if (typeDockableIsDocked()) {
                if (!(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) {
                    ((Maneuver) FM).unblock();
                    ((Maneuver) FM).set_maneuver(48);
                    for (int j = 0; j < i; j++)
                        ((Maneuver) FM).push(48);

                    if (FM.AP.way.curr().Action != 3)
                        ((Maneuver) FM).AP.way.setCur(((Aircraft) queen_).FM.AP.way.Cur());
                    ((Pilot) FM).setDumbTime(3000L);
                }
                // TODO: +++ Patch Pack 107, limit refueling to previously selected fuel level
                //if (FM.M.fuel < FM.M.maxFuel)
                if (FM.M.fuel < this.initialFuel)
                // ---
                    FM.M.fuel += 0.06F * f;
            } else if (!(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) {
                if (FM.EI.engines[0].getStage() == 0)
                    FM.EI.setEngineRunning();
                if (dtime > 0L && ((Maneuver) FM).Group != null) {
                    ((Maneuver) FM).Group.leaderGroup = null;
                    ((Maneuver) FM).set_maneuver(22);
                    ((Pilot) FM).setDumbTime(3000L);
                    if (Time.current() > dtime + 3000L) {
                        dtime = -1L;
                        ((Maneuver) FM).clear_stack();
                        ((Maneuver) FM).set_maneuver(0);
                        ((Pilot) FM).setDumbTime(0L);
                    }
                } else if (FM.AP.way.curr().Action == 0) {
                    Maneuver maneuver = (Maneuver) FM;
                    if (maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                        maneuver.Group.setGroupTask(2);
                }
            }
        super.update(f);
    }

    protected void moveAileron(float f) {
        aileronsAngle = f;
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f - flaperonAngle, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f + flaperonAngle, 0.0F);
    }

    protected void moveFlap(float f) {
        flaperonAngle = f * 17F;
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * aileronsAngle - flaperonAngle, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * aileronsAngle + flaperonAngle, 0.0F);
    }

    protected void moveFan(float f) {
        if (Config.isUSE_RENDER()) {
            super.moveFan(f);
            float f1 = FM.CT.getAileron();
            float f2 = FM.CT.getElevator();
            hierMesh().chunkSetAngles("Stick_D0", 0.0F, 12F * f1, cvt(f2, -1F, 1.0F, -12F, 18F));
            hierMesh().chunkSetAngles("pilotarm2_d0", cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f1, -1F, 1.0F, 6F, -8F) - (cvt(f2, -1F, 0.0F, -36F, 0.0F) + cvt(f2, 0.0F, 1.0F, 0.0F, 32F)));
            hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, cvt(f1, -1F, 1.0F, -16F, 14F) + cvt(f2, -1F, 0.0F, -62F, 0.0F) + cvt(f2, 0.0F, 1.0F, 0.0F, 44F));
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (!oneTimeCheckDone && !FM.isPlayers() && !isNetPlayer() && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) {
            oneTimeCheckDone = true;
            if (World.cur().camouflage == 1) {
                if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.25F) {
                    FM.CT.cockpitDoorControl = 1.0F;
                    FM.AS.setCockpitDoor(this, 1);
                }
            } else if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.5F) {
                FM.CT.cockpitDoorControl = 1.0F;
                FM.AS.setCockpitDoor(this, 1);
            }
        }
        if (flag && FM.AP.way.curr().Action == 3 && typeDockableIsDocked() && Math.abs(((Aircraft) queen_).FM.Or.getKren()) < 3F)
            if (FM.isPlayers()) {
                if ((FM instanceof RealFlightModel) && !((RealFlightModel) FM).isRealMode()) {
                    typeDockableAttemptDetach();
                    ((Maneuver) FM).set_maneuver(22);
                    ((Maneuver) FM).setCheckStrike(false);
                    FM.Vwld.z -= 5D;
                    dtime = Time.current();
                }
            } else {
                typeDockableAttemptDetach();
                ((Maneuver) FM).set_maneuver(22);
                ((Maneuver) FM).setCheckStrike(false);
                FM.Vwld.z -= 5D;
                dtime = Time.current();
            }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0: // '\0'
                hierMesh().chunkVisible("Pilot1_D0", false);
                hierMesh().chunkVisible("Head1_D0", false);
                hierMesh().chunkVisible("HMask1_D0", false);
                hierMesh().chunkVisible("Pilot1_D1", true);
                hierMesh().chunkVisible("Head1_D1", true);
                hierMesh().chunkVisible("pilotarm2_d0", false);
                hierMesh().chunkVisible("pilotarm1_d0", false);
                break;
        }
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        hierMesh().chunkVisible("pilotarm2_d0", false);
        hierMesh().chunkVisible("pilotarm1_d0", false);
    }

    public void missionStarting() {
        super.missionStarting();
        hierMesh().chunkVisible("pilotarm2_d0", true);
        hierMesh().chunkVisible("pilotarm1_d0", true);
        checkAsDrone();
    }

    public void prepareCamouflage() {
        super.prepareCamouflage();
        hierMesh().chunkVisible("pilotarm2_d0", true);
        hierMesh().chunkVisible("pilotarm1_d0", true);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        hierMesh().chunkVisible("GearWireR1_D0", true);
        hierMesh().chunkVisible("GearWireL1_D0", true);
        // TODO: +++ Patch Pack 107, limit refueling to previously selected fuel level
        this.initialFuel = this.FM.M.fuel;
        // ---
    }

    private void checkAsDrone() {
        if (target_ == null) {
            if (FM.AP.way.curr().getTarget() == null)
                FM.AP.way.next();
            target_ = FM.AP.way.curr().getTarget();
            if (Actor.isValid(target_) && (target_ instanceof Wing)) {
                Wing wing = (Wing) target_;
                int i = aircIndex();
                if (Actor.isValid(wing.airc[i / 2]))
                    target_ = wing.airc[i / 2];
                else
                    target_ = null;
            }
        }
        if (Actor.isValid(target_) && (target_ instanceof TB_3_4M_34R_SPB)) {
            queen_last = target_;
            queen_time = Time.current();
            if (isNetMaster())
                ((TypeDockable) target_).typeDockableRequestAttach(this, aircIndex() % 2, true);
        }
        bNeedSetup = false;
        target_ = null;
    }

    public int typeDockableGetDockport() {
        if (typeDockableIsDocked())
            return dockport_;
        else
            return -1;
    }

    public Actor typeDockableGetQueen() {
        return queen_;
    }

    public boolean typeDockableIsDocked() {
        return Actor.isValid(queen_);
    }

    public void typeDockableAttemptAttach() {
        if (FM.AS.isMaster() && !typeDockableIsDocked()) {
            Aircraft aircraft = War.getNearestFriend(this);
            if (aircraft instanceof TB_3_4M_34R_SPB)
                ((TypeDockable) aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach() {
        if (FM.AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
            ((TypeDockable) queen_).typeDockableRequestDetach(this);
    }

    public void typeDockableRequestAttach(Actor actor) {}

    public void typeDockableRequestDetach(Actor actor) {}

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {}

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {}

    public void typeDockableDoAttachToDrone(Actor actor, int i) {}

    public void typeDockableDoDetachFromDrone(int i) {}

    public void typeDockableDoAttachToQueen(Actor actor, int i) {
        queen_ = actor;
        dockport_ = i;
        queen_last = queen_;
        queen_time = 0L;
        FM.EI.setEngineRunning();
        FM.CT.setGearAirborne();
        moveGear(0.0F);
        FlightModel flightmodel = ((Aircraft) queen_).FM;
        if (aircIndex() == 0 && (FM instanceof Maneuver) && (flightmodel instanceof Maneuver)) {
            Maneuver maneuver = (Maneuver) flightmodel;
            Maneuver maneuver1 = (Maneuver) FM;
            if (maneuver.Group != null && maneuver1.Group != null && maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1) {
                AirGroup airgroup = new AirGroup(maneuver1.Group);
                maneuver1.Group.delAircraft(this);
                airgroup.addAircraft(this);
                airgroup.attachGroup(maneuver.Group);
                airgroup.rejoinGroup = null;
                airgroup.leaderGroup = null;
                airgroup.clientGroup = maneuver.Group;
            }
        }
    }

    public void typeDockableDoDetachFromQueen(int i) {
        if (dockport_ == i) {
            queen_last = queen_;
            queen_time = Time.current();
            queen_ = null;
            dockport_ = 0;
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        if (typeDockableIsDocked()) {
            netmsgguaranted.writeByte(1);
            ActorNet actornet = null;
            if (Actor.isValid(queen_)) {
                actornet = queen_.net;
                if (actornet.countNoMirrors() > 0)
                    actornet = null;
            }
            netmsgguaranted.writeByte(dockport_);
            netmsgguaranted.writeNetObj(actornet);
        } else {
            netmsgguaranted.writeByte(0);
        }
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        if (netmsginput.readByte() == 1) {
            dockport_ = netmsginput.readByte();
            NetObj netobj = netmsginput.readNetObj();
            if (netobj != null) {
                Actor actor = (Actor) netobj.superObj();
                ((TypeDockable) actor).typeDockableDoAttachToDrone(this, dockport_);
            }
        }
    }

    private Actor   queen_last;
    private long    queen_time;
    private boolean bNeedSetup;
    private long    dtime;
    private Actor   target_;
    private Actor   queen_;
    private int     dockport_;
    private boolean bailingOut;
    private boolean canopyForward;
    private boolean okToJump;
    private float   flaperonAngle;
    private float   aileronsAngle;
    private boolean oneTimeCheckDone;
    private boolean sideDoorOpened;
    // TODO: +++ Patch Pack 107, limit refueling to previously selected fuel level
    private float   initialFuel;
    // ---

    static {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-16");
        Property.set(class1, "meshName", "3DO/Plane/I-16type5(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar07());
        Property.set(class1, "meshName_ru", "3DO/Plane/I-16type5/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar07());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/I-16type5.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_16TYPE5_SPB.class });
        Property.set(class1, "LOSElevation", 0.82595F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev07", "_ExternalDev08" });
        weaponsRegister(class1, "default", new String[] { "MGunShKASk 900", "MGunShKASk 900", null, null, null, null });
        weaponsRegister(class1, "2xFAB250", new String[] { "MGunShKASk 900", "MGunShKASk 900", "BombGunFAB250 1", "BombGunFAB250 1", null, null });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null });
    }
}
