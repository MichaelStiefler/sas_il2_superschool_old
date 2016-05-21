package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.weapons.BombFAB5000;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.Reflection;

public class JU_88MSTL extends JU_88 implements TypeDockable {

    public JU_88MSTL() {
        this.droneInitiator = null;
        this.patinAutopilotEngaged = false;
        this.pImpact = new Point3d();
        this.isExploded = false;
    }

    public void msgEndAction(Object obj, int i) {
        super.msgEndAction(obj, i);
        EndActionParam endactionparam = (EndActionParam) obj;
        System.out.println("initiator=" + endactionparam.initiator.getClass().getName() + ", droneInitiator=" + this.droneInitiator.getClass().getName());
        switch (i) {
            case 2:
                if (!this.isExploded) {
                    if (!this.sendNetExploded());
                        this.spawnBomb();
                }
                break;
        }
        
    }
   
    private void spawnBomb() {
        System.out.println("Ju88 Mistel spawnBomb() 1");
        BombFAB5000 bomb = new BombFAB5000();
        bomb.pos.setUpdateEnable(true);
        bomb.pos.setAbs(this.pos.getAbs());
        bomb.pos.reset();
        bomb.start();
        bomb.setOwner(this.droneInitiator);
        bomb.setSpeed(new Vector3d());
        bomb.armingTime = 0L;
        Reflection.setBoolean(bomb, "isArmed", true);
        this.isExploded = true;
        System.out.println("Ju88 Mistel spawnBomb() 2");
    }
    
    protected void doExplosion() {
        super.doExplosion();
        if (this.FM.Loc.z - 300D < World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y))
            if (Engine.land().isWater(this.FM.Loc.x, this.FM.Loc.y))
                Explosions.bomb1000_water(this.FM.Loc, 1.0F, 1.0F);
            else
                Explosions.bomb1000_land(this.FM.Loc, 1.0F, 1.0F, true);
    }
    
   
    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLMid") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            this.FM.AS.hitTank(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("WingRMid") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            this.FM.AS.hitTank(shot.initiator, 3, 1);
        if (shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            this.FM.AS.hitTank(shot.initiator, 1, 1);
        if (shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            this.FM.AS.hitTank(shot.initiator, 2, 1);
        if (shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
            this.FM.AS.hitEngine(shot.initiator, 1, 1);
        super.msgShot(shot);
    }

    public boolean typeDockableIsDocked() {
        return true;
    }

    public void typeDockableAttemptAttach() {}

    public void typeDockableAttemptDetach() {
        if (this.FM.AS.isMaster()) {
            for (int i = 0; i < this.drones.length; i++)
                if (Actor.isValid(this.drones[i]))
                    this.typeDockableRequestDetach(this.drones[i], i, true);

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
        for (int i = 0; i < this.drones.length; i++) {
            if (actor != this.drones[i])
                continue;
            Aircraft aircraft = (Aircraft) actor;
            if (!aircraft.FM.AS.isMaster())
                continue;
            if (this.FM.AS.isMaster())
                this.typeDockableRequestDetach(actor, i, true);
            else
                this.FM.AS.netToMaster(33, i, 1, actor);
        }

    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
        if (i != 0)
            return;
        if (flag) {
            if (this.FM.AS.isMaster()) {
                this.FM.AS.netToMirrors(34, i, 1, actor);
                this.typeDockableDoAttachToDrone(actor, i);
            } else {
                this.FM.AS.netToMaster(34, i, 1, actor);
            }
        } else if (this.FM.AS.isMaster()) {
            if (!Actor.isValid(this.drones[i])) {
                this.FM.AS.netToMirrors(34, i, 1, actor);
                this.typeDockableDoAttachToDrone(actor, i);
            }
        } else {
            this.FM.AS.netToMaster(34, i, 0, actor);
        }
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
        if (flag)
            if (this.FM.AS.isMaster()) {
                this.FM.AS.netToMirrors(35, i, 1, actor);
                this.typeDockableDoDetachFromDrone(i);
            } else {
                this.FM.AS.netToMaster(35, i, 1, actor);
            }
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i) {
        if (!Actor.isValid(this.drones[i])) {
            HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
            Loc loc = new Loc();
            Loc loc1 = new Loc();
            this.pos.getAbs(loc1);
            hooknamed.computePos(this, loc1, loc);
            actor.pos.setAbs(loc);
            actor.pos.setBase(this, null, true);
            actor.pos.resetAsBase();
            this.drones[i] = actor;
            this.droneInitiator = actor;
            ((TypeDockable) this.drones[i]).typeDockableDoAttachToQueen(this, i);
        }
    }

    public void typeDockableDoDetachFromDrone(int i) {
        if (!Actor.isValid(this.drones[i])) {
            return;
        } else {
            this.drones[i].pos.setBase(null, null, true);
            ((TypeDockable) this.drones[i]).typeDockableDoDetachFromQueen(i);
            this.drones[i] = null;
            if (!this.FM.Gears.onGround() && this.FM.getSpeedKMH() > 100F) {
                this.engagePatinAutopilot();
            }
            return;
        }
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i) {}

    public void typeDockableDoDetachFromQueen(int i) {}

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        for (int i = 0; i < this.drones.length; i++)
            if (Actor.isValid(this.drones[i])) {
                netmsgguaranted.writeByte(1);
                com.maddox.il2.engine.ActorNet actornet = this.drones[i].net;
                if (actornet.countNoMirrors() == 0)
                    netmsgguaranted.writeNetObj(actornet);
                else
                    netmsgguaranted.writeNetObj(null);
            } else {
                netmsgguaranted.writeByte(0);
            }
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        for (int i = 0; i < this.drones.length; i++) {
            if (netmsginput.readByte() != 1)
                continue;
            NetObj netobj = netmsginput.readNetObj();
            if (netobj != null)
                this.typeDockableDoAttachToDrone((Actor) netobj.superObj(), i);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if (this.FM.AS.isMaster()) {
            if (i == 2) {
                this.typeDockableRequestDetach(this.drones[0], 0, true);
            }
            if (i == 13 && j == 0) {
                this.nextDMGLevels(4, 1, "CF_D0", this);
                return true;
            }
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        if (this.FM instanceof Pilot)
            ((Pilot) this.FM).setDumbTime(9999L);
        if ((this.FM instanceof Maneuver) && this.FM.EI.engines[0].getStage() == 6 && this.FM.EI.engines[1].getStage() == 6) {
            ((Maneuver) this.FM).set_maneuver(44);
            ((Maneuver) this.FM).setSpeedMode(-1);
        }
        this.FM.CT.bHasGearControl = !this.FM.Gears.onGround();
        
        if (this.patinAutopilotEngaged) {
            this.FM.CT.AileronControl = -0.2F * FM.Or.getKren() - 2.0F * (float)FM.getW().x;
            Point3d p1 = new Point3d(pImpact);
            Point3d p2 = new Point3d();
            this.pos.getAbs(p2);
            Vector3d v1 = new Vector3d();
            v1.sub(p1, p2);
            v1.normalize();
            Orient o1 = new Orient();
            o1.setAT0(v1);
            Vector3d v2 = new Vector3d(this.FM.Vwld);
            v2.normalize();
            Orient o2 = new Orient();
            o2.setAT0(v2);
            o1.sub(o2);
            this.FM.CT.ElevatorControl = o1.getTangage() * 0.2F;
            this.FM.CT.RudderControl = -o1.getYaw() * 0.2F;
            this.FM.EI.engines[0].setControlThrottle(1.0F);
            this.FM.EI.engines[1].setControlThrottle(1.0F);
        }
        super.update(f);
    }
    
    private void engagePatinAutopilot() {
        Vector3d v1 = new Vector3d();
        v1.set(1.0D, 0.0D, 0.0D);
        this.FM.Or.transform(v1);
        v1.scale(50000D);
        Point3d p2 = new Point3d();        
        p2.set(this.FM.Loc);
        p2.add(v1);
        Point3d p1 = new Point3d();        
        p1.set(this.FM.Loc);
        if(Landscape.rayHitHQ(this.FM.Loc, p2, p1)) {
            pImpact.set(p1);
            this.FM.CT.setPowerControl(1.1F);
            this.patinAutopilotEngaged = true;
        }
    }
    
    private boolean sendNetExploded()
    {
      System.out.println("Ju88 Mistel sendNetExploded() 1");
      if ((!this.isNet()) || (this.net.countMirrors() == 0)) {
        return false;
      }
      System.out.println("Ju88 Mistel sendNetExploded() 2");
      try
      {
        System.out.println("JU_88MSTL > sendNetExploded < to mirrors!");
        NetMsgGuaranted netMsgGuaranted = new NetMsgGuaranted();
        netMsgGuaranted.writeByte(92);
        netMsgGuaranted.writeNetObj(this.net);
        if (this.droneInitiator == null)
            netMsgGuaranted.writeNetObj(this.net);
        else
            netMsgGuaranted.writeNetObj(this.droneInitiator.net);
        this.net.post(netMsgGuaranted);
        System.out.println("Ju88 Mistel sendNetExploded() 3");
        return true;
      }
      catch (Exception exception)
      {
        System.out.println(exception.getMessage());
        exception.printStackTrace();
      }
      System.out.println("Ju88 Mistel sendNetExploded() 4");
      return false;
    }
    
    public boolean netGetGMsg(NetMsgInput netmsginput, boolean bool)
            throws IOException
    {
        if (this.droneInitiator == null)
            return super.netGetGMsg(netmsginput, bool);
        netmsginput.mark(2);
        int i = netmsginput.readUnsignedByte();
        switch (i) {
            case 92:
                System.out.println("Ju88 Mistel netGetGMsg() 92 1");
                if (this.net != netmsginput.readNetObj()) return false;
                System.out.println("Ju88 Mistel netGetGMsg() 92 2");
                if (this.droneInitiator.net != netmsginput.readNetObj()) return false;
                System.out.println("Ju88 Mistel netGetGMsg() 92 3");
                if (!Actor.isValid(this.droneInitiator)) return false;
                System.out.println("Ju88 Mistel netGetGMsg() 92 4");
                if (this.droneInitiator != World.getPlayerAircraft()) return false;
                System.out.println("Ju88 Mistel netGetGMsg() 92 5");
                this.spawnBomb();
                System.out.println("Ju88 Mistel netGetGMsg() 92 6");
                return true;
            default:
                netmsginput.reset();
                return super.netGetGMsg(netmsginput, bool);
        }
    }

    private Actor drones[] = { null };
    private Actor droneInitiator;
    private boolean patinAutopilotEngaged;
    private boolean isExploded;
    private Point3d pImpact;

    static {
        Class class1 = JU_88MSTL.class;
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
