package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class I_16TYPE24DRONE extends I_16 implements MsgCollisionRequestListener, TypeTNBFighter, TypeStormovik, TypeDockable {

    public I_16TYPE24DRONE() {
        queen_last = null;
        queen_time = 0L;
        bNeedSetup = true;
        dtime = -1L;
        target_ = null;
        queen_ = null;
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        super.msgCollisionRequest(actor, aflag);
        if (queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L))
            aflag[0] = false;
        else
            aflag[0] = true;
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

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
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

    public void missionStarting() {
        checkAsDrone();
    }

    // TODO: +++ Patch Pack 107, limit refueling to previously selected fuel level
    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.initialFuel = this.FM.M.fuel;
    }
    // ---

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
        if (!FM.AS.isMaster())
            return;
        if (!typeDockableIsDocked()) {
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
        if (dockport_ != i) {
            return;
        } else {
            queen_last = queen_;
            queen_time = Time.current();
            queen_ = null;
            dockport_ = 0;
            return;
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        if (typeDockableIsDocked()) {
            netmsgguaranted.writeByte(1);
            com.maddox.il2.engine.ActorNet actornet = null;
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
    // TODO: +++ Patch Pack 107, limit refueling to previously selected fuel level
    private float   initialFuel;
    // ---

    static {
        Class class1 = I_16TYPE24DRONE.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-16");
        Property.set(class1, "meshName", "3DO/Plane/I-16type24(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ru", "3DO/Plane/I-16type24/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar01());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/I-16type24.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_16TYPE24_SPB.class });
        Property.set(class1, "LOSElevation", 0.82595F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb01", "_ExternalBomb02",
                "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08" });
        weaponsRegister(class1, "default", new String[] { "MGunShKASsi 650", "MGunShKASsi 650", "MGunShVAKk 120", "MGunShVAKk 120", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2fab250", new String[] { "MGunShKASsi 650", "MGunShKASsi 650", "MGunShVAKk 120", "MGunShVAKk 120", null, null, null, null, null, null, "BombGunFAB250", "BombGunFAB250", null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
