/* 410 class */
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiWeaponsManagement;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombCargoA extends Bomb {
    private Chute           chute    = null;
    private boolean         bOnChute = false;
    private static Orient   or       = new Orient();
    private static Vector3d v3d      = new Vector3d();
    private float           ttcurTM;

    protected boolean haveSound() {
        return false;
    }

    public void start() {
        super.start();
        ttcurTM = World.Rnd().nextFloat(0.5F, 1.75F);
    }

    public void interpolateTick() {
        super.interpolateTick();
        getSpeed(v3d);
        or.setAT0(v3d);
        pos.setAbs(or);
        if (bOnChute) {
            v3d.scale(0.99);
            if (v3d.z < -5.0)
                v3d.z += (double) (1.1F * Time.tickConstLenFs());
            setSpeed(v3d);
        } else if (curTm > ttcurTM) {
            bOnChute = true;
            chute = new Chute(this);
            chute.collide(false);
            chute.mesh().setScale(1.5F);
            chute.pos.setRel(new Point3d(2.0, 0.0, 0.0), new Orient(0.0F, 90.0F, 0.0F));
        }
    }

    public void msgCollision(Actor actor, String string, String string_0_) {
        if (actor instanceof ActorLand) {
            if (chute != null)
                chute.landing();
            Loc loc = new Loc();
            pos.getAbs(loc);
            loc.getPoint().z = Engine.land().HQ(loc.getPoint().x, loc.getPoint().y);
            if (!Engine.land().isWater(loc.getPoint().x, loc.getPoint().y)) {
                loc.getOrient().set(loc.getOrient().getAzimut(), -90.0F, 0.0F);
                ActorSimpleMesh actorsimplemesh = new ActorSimpleMesh("3DO/Arms/Cargo-TypeA/mono.sim", loc);
                actorsimplemesh.collide(false);
                actorsimplemesh.postDestroy(150000L);
            }
            // TODO: Added by |ZUTI|: report drop received and update resources
            // ----------------------------------------------------------------
            if (Mission.isServer() || Main.cur().netServerParams.isMaster()) {
                // System.out.println(" Cargo >" + this.toString() + "< landed OK! Pos: " + this.pos.getAbsPoint().x + ", " + this.pos.getAbsPoint().y);
                ZutiWeaponsManagement.addCargoResources(this.pos.getAbsPoint());
            }
            // ----------------------------------------------------------------
        } else if (chute != null)
            chute.destroy();
        destroy();
    }

    static {
        Class var_class = BombCargoA.class;
        Property.set(var_class, "mesh", "3DO/Arms/Cargo-TypeA/mono.sim");
        Property.set(var_class, "radius", 1.0F);
        Property.set(var_class, "power", 6.0F);
        Property.set(var_class, "powerType", 1);
        Property.set(var_class, "kalibr", 1.0F);
        Property.set(var_class, "massa", 500.0F);
        Property.set(var_class, "sound", (String) null);
    }
}