package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;

public class JU_88MSTL extends JU_88 implements TypeDockable {

    public JU_88MSTL() {
        droneInitiator = null;
    }

    public void msgEndAction(Object obj, int i) {
        super.msgEndAction(obj, i);
        switch (i) {
            case 2: // '\002'
                MsgExplosion.send(this, null, FM.Loc, droneInitiator, 0.0F, 4550F, 0, 890F);
                break;
        }
    }

    protected void doExplosion() {
        super.doExplosion();
        if (FM.Loc.z - 300D < World.land().HQ_Air(FM.Loc.x, FM.Loc.y))
            if (Engine.land().isWater(FM.Loc.x, FM.Loc.y))
                Explosions.bomb1000_water(FM.Loc, 1.0F, 1.0F);
            else
                Explosions.bomb1000_land(FM.Loc, 1.0F, 1.0F, true);
    }

    public void msgShot(Shot shot) {
        setShot(shot);
        if (shot.chunkName.startsWith("WingLMid") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            FM.AS.hitTank(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("WingRMid") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            FM.AS.hitTank(shot.initiator, 3, 1);
        if (shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            FM.AS.hitTank(shot.initiator, 1, 1);
        if (shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            FM.AS.hitTank(shot.initiator, 2, 1);
        if (shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            FM.AS.hitEngine(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            FM.AS.hitEngine(shot.initiator, 1, 1);
        super.msgShot(shot);
    }

    public boolean typeDockableIsDocked() {
        return true;
    }

    public void typeDockableAttemptAttach() {}

    public void typeDockableAttemptDetach() {
        if (FM.AS.isMaster()) {
            for (int i = 0; i < drones.length; i++)
                if (Actor.isValid(drones[i]))
                    typeDockableRequestDetach(drones[i], i, true);

        }
    }

    public void typeDockableRequestAttach(Actor actor) {
        // TODO: Storebror: Enable attachment of Bf 109 and Fw-190 drones
        if (!(actor instanceof Aircraft))
            return;
        Aircraft parasiteAircraft = (Aircraft) actor;

        boolean attachEnabled = false;
        double attachMaxDistance = 50D;
        if (parasiteAircraft.FM.AS.isMaster() && parasiteAircraft.FM.Gears.onGround() && parasiteAircraft.FM.getSpeedKMH() < 10F && FM.getSpeedKMH() < 10F) {
            attachMaxDistance = 50D;
            attachEnabled = true;
        }

        if (!attachEnabled)
            return;
        for (int i = 0; i < drones.length; i++) {
            if (Actor.isValid(drones[i]))
                continue;
            HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
            Loc loc = new Loc();
            Loc loc1 = new Loc();
            pos.getAbs(loc1);
            hooknamed.computePos(this, loc1, loc);
            actor.pos.getAbs(loc1);
            if (loc.getPoint().distance(loc1.getPoint()) >= attachMaxDistance)
                continue;
            if (FM.AS.isMaster()) {
                typeDockableRequestAttach(actor, i, true);
                return;
            } else {
                FM.AS.netToMaster(32, i, 0, actor);
                return;
            }
        }
    }

    public void typeDockableRequestDetach(Actor actor) {
        for (int i = 0; i < drones.length; i++) {
            if (actor != drones[i])
                continue;
            Aircraft aircraft = (Aircraft) actor;
            if (!aircraft.FM.AS.isMaster())
                continue;
            if (FM.AS.isMaster())
                typeDockableRequestDetach(actor, i, true);
            else
                FM.AS.netToMaster(33, i, 1, actor);
        }

    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
        if (i != 0)
            return;
        if (flag) {
            if (FM.AS.isMaster()) {
                FM.AS.netToMirrors(34, i, 1, actor);
                typeDockableDoAttachToDrone(actor, i);
            } else {
                FM.AS.netToMaster(34, i, 1, actor);
            }
        } else if (FM.AS.isMaster()) {
            if (!Actor.isValid(drones[i])) {
                FM.AS.netToMirrors(34, i, 1, actor);
                typeDockableDoAttachToDrone(actor, i);
            }
        } else {
            FM.AS.netToMaster(34, i, 0, actor);
        }
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
        if (flag)
            if (FM.AS.isMaster()) {
                FM.AS.netToMirrors(35, i, 1, actor);
                typeDockableDoDetachFromDrone(i);
            } else {
                FM.AS.netToMaster(35, i, 1, actor);
            }
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i) {
        if (!Actor.isValid(drones[i])) {
            HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
            Loc loc = new Loc();
            Loc loc1 = new Loc();
            pos.getAbs(loc1);
            hooknamed.computePos(this, loc1, loc);
            actor.pos.setAbs(loc);
            actor.pos.setBase(this, null, true);
            actor.pos.resetAsBase();
            drones[i] = actor;
            droneInitiator = actor;
            ((TypeDockable) drones[i]).typeDockableDoAttachToQueen(this, i);
        }
    }

    public void typeDockableDoDetachFromDrone(int i) {
        if (!Actor.isValid(drones[i])) {
            return;
        } else {
            drones[i].pos.setBase(null, null, true);
            ((TypeDockable) drones[i]).typeDockableDoDetachFromQueen(i);
            drones[i] = null;
            return;
        }
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i) {}

    public void typeDockableDoDetachFromQueen(int i) {}

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        for (int i = 0; i < drones.length; i++)
            if (Actor.isValid(drones[i])) {
                netmsgguaranted.writeByte(1);
                com.maddox.il2.engine.ActorNet actornet = drones[i].net;
                if (actornet.countNoMirrors() == 0)
                    netmsgguaranted.writeNetObj(actornet);
                else
                    netmsgguaranted.writeNetObj(null);
            } else {
                netmsgguaranted.writeByte(0);
            }

    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        for (int i = 0; i < drones.length; i++) {
            if (netmsginput.readByte() != 1)
                continue;
            NetObj netobj = netmsginput.readNetObj();
            if (netobj != null)
                typeDockableDoAttachToDrone((Actor) netobj.superObj(), i);
        }

    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if (FM.AS.isMaster()) {
            if (i == 2)
                typeDockableRequestDetach(drones[0], 0, true);
            if (i == 13 && j == 0) {
                nextDMGLevels(4, 1, "CF_D0", this);
                return true;
            }
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        if (FM instanceof Pilot)
            ((Pilot) FM).setDumbTime(9999L);
        if ((FM instanceof Maneuver) && FM.EI.engines[0].getStage() == 6 && FM.EI.engines[1].getStage() == 6) {
            ((Maneuver) FM).set_maneuver(44);
            ((Maneuver) FM).setSpeedMode(-1);
        }
        FM.CT.bHasGearControl = !FM.Gears.onGround();
        super.update(f);
    }

    private Actor drones[] = { null };
    private Actor droneInitiator;

    static {
        Class class1 = com.maddox.il2.objects.air.JU_88MSTL.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju-88");
        Property.set(class1, "meshName", "3DO/Plane/Ju-88MSTL/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-88A-4Mistel.fmd");
        weaponTriggersRegister(class1, new int[] { 9 });
        weaponHooksRegister(class1, new String[] { "_Dockport0" });
        weaponsRegister(class1, "default", new String[] { null });
        weaponsRegister(class1, "none", new String[] { null });
    }
}
