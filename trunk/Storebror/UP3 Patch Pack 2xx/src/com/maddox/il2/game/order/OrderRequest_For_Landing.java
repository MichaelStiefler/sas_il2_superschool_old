/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderRequest_For_Landing extends Order {

    public OrderRequest_For_Landing() {
        super("Request_For_Landing");
    }

    public void run() {
        try {
            FlightModel flightmodel = this.Player().FM;
            Airport airport = null;
            Way way = null;
            if (Main.cur().netServerParams.isDogfight()) {
                WP = this.Player().FM.actor.pos.getAbsPoint();
                BornPlace bornplace = ZutiSupportMethods.getPlayerBornPlace(WP, this.Player().getArmy());
                if (bornplace != null)
                    airport = ZutiSupportMethods.getAirport(bornplace.place.x, bornplace.place.y);
            } else {
                way = flightmodel.AP.way;
                way.get(way.size() - 1).getP(WP);
            }
            flightmodel.BarometerZ = (float) World.land().HQ(WP.x, WP.y);
            int i = 0;
            if (Main.cur().netServerParams.isDogfight() && airport != null)
                i = airport.landingFeedback(WP, (Aircraft) flightmodel.actor);
            else if (Actor.isAlive(way.landingAirport))
                i = way.landingAirport.landingFeedback(WP, (Aircraft) flightmodel.actor);
            if (i == 0)
                Voice.speakLandingPermited((Aircraft) flightmodel.actor);
            if (i == 1)
                Voice.speakLandingDenied((Aircraft) flightmodel.actor);
            if (i == 2)
                Voice.speakWaveOff((Aircraft) flightmodel.actor);
        } catch (Exception exception) {
            System.out.println("OrderRequest_For_Landing error, ID_01: " + exception.toString());
        }
    }

    private static Point3d WP = new Point3d();

}
