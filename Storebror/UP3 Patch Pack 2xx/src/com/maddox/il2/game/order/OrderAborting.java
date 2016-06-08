/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;

class OrderAborting extends Order {

    public OrderAborting() {
        super("Aborting_Mission");
    }

    public void run() {
        Maneuver maneuver = (Maneuver) this.Player().FM;
        if (maneuver.Group != null) {
            AirGroup airgroup = new AirGroup(maneuver.Group);
            airgroup.rejoinGroup = null;
            maneuver.Group.delAircraft(this.Player());
            airgroup.addAircraft(this.Player());
        }
    }
}
