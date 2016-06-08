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

public class OrderVector_To_Home_Base extends Order {

    public OrderVector_To_Home_Base() {
        super("Vector_To_Home_Base");
    }

    public void run() {
        try {
            if (Mission.MDS_VARIABLES().zutiMisc_DisableVectoring) {
                HUD.log("mds.vectoringNotPossible");
                return;
            }
            this.Player().FM.moveCarrier();
            Point3d point3d = this.Player().FM.actor.pos.getAbsPoint();
            if (Main.cur().netServerParams != null && Main.cur().netServerParams.isDogfight()) {
                BornPlace bornplace = ZutiSupportMethods.getPlayerBornPlace(point3d, this.Player().getArmy());
                if (bornplace != null)
                    V2 = new Point3d(bornplace.place.x, bornplace.place.y, 0.0D);
                else
                    return;
            } else {
                Way way = this.Player().FM.AP.way;
                WayPoint waypoint = way.get(way.size() - 1);
                waypoint.getP(V2);
            }
            this.Player().FM.actor.pos.getAbs(V1);
            V1.sub(V2);
            double d = Math.sqrt(V1.x * V1.x + V1.y * V1.y);
            double d1 = BeaconGeneric.lineOfSightDelta(this.Player().FM.getAltitude(), V2.z, d);
            if ((d1 < 0.0D || d > 90000D) && World.cur().diffCur.RealisticNavigationInstruments)
                return;
            if (this.isEnableVoice())
                if (World.cur().diffCur.RealisticNavigationInstruments)
                    this.delayedOrder(this.Player(), d);
                else
                    Voice.speakHeadingToHome(this.Player(), V1);
        } catch (Exception exception) {
            System.out.println("OrderVector_To_Home_Base error, ID_01: " + exception.toString());
        }
    }

    private void delayedOrder(final Aircraft ac, double d) {
        float f = (float) (10000D + Math.random() * (d / 7D)) / 1000F;
        new MsgAction(f) {

            public void doAction() {
                Voice.speakHeadingToHome(ac, OrderVector_To_Home_Base.V1);
            }

        };
    }

    private static Point3d V1 = new Point3d();
    private static Point3d V2 = new Point3d();

}
