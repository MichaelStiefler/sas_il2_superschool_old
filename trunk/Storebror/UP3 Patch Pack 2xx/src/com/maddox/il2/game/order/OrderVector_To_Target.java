/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.radios.BeaconGeneric;
import com.maddox.rts.MsgAction;

public class OrderVector_To_Target extends Order {

    public OrderVector_To_Target() {
        super("Vector_To_Target");
        this.dist = 0.0D;
    }

    public void run() {
        if (Mission.MDS_VARIABLES().zutiMisc_DisableVectoring) {
            HUD.log("mds.vectoringNotPossible");
            return;
        }
        if (World.cur().diffCur.RealisticNavigationInstruments && !this.isLosToHomeBase())
            return;
        if (Main.cur().netServerParams.isDogfight())
            return;
        Way way = this.Player().FM.AP.way;
        WayPoint waypoint = way.curr();
        waypoint.getP(V2);
        for (int i = way.Cur(); i < way.size(); i++) {
            WayPoint waypoint1 = way.get(i);
            if (waypoint1.Action == 3 || waypoint1.getTarget() != null && waypoint1.Action != 2)
                waypoint1.getP(V2);
        }

        this.Player().FM.actor.pos.getAbs(V1);
        V1.sub(V2);
        if (this.isEnableVoice())
            if (World.cur().diffCur.RealisticNavigationInstruments)
                this.delayedOrder(this.Player());
            else
                Voice.speakHeadingToTarget(this.Player(), V1);
    }

    private boolean isLosToHomeBase() {
        this.Player().FM.moveCarrier();
        Point3d point3d = this.Player().FM.actor.pos.getAbsPoint();
        if (Main.cur().netServerParams != null && Main.cur().netServerParams.isDogfight()) {
            BornPlace bornplace = ZutiSupportMethods.getPlayerBornPlace(point3d, this.Player().getArmy());
            if (bornplace != null)
                V2 = new Point3d(bornplace.place.x, bornplace.place.y, 0.0D);
            else
                return false;
        } else {
            Way way = this.Player().FM.AP.way;
            WayPoint waypoint = way.get(way.size() - 1);
            waypoint.getP(V2);
        }
        this.Player().FM.actor.pos.getAbs(V1);
        V1.sub(V2);
        this.dist = Math.sqrt(V1.x * V1.x + V1.y * V1.y);
        double d = BeaconGeneric.lineOfSightDelta(this.Player().FM.getAltitude(), V2.z, this.dist);
        return d >= 0.0D && this.dist <= 90000D;
    }

    private void delayedOrder(final Aircraft ac) {
        float f = (float) (10000D + Math.random() * (this.dist / 6D)) / 1000F;
        new MsgAction(f) {

            public void doAction() {
                Voice.speakHeadingToTarget(ac, OrderVector_To_Target.V1);
            }

        };
    }

    private static Point3d V1 = new Point3d();
    private static Point3d V2 = new Point3d();
    private double         dist;

}
