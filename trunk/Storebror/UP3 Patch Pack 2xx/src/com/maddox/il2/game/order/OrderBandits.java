/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.VisCheck;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Selector;

class OrderBandits extends Order {

    public OrderBandits() {
        super("Bandits");
    }

    public void run() {
        com.maddox.il2.engine.Actor actor = Selector.getTarget();
        if (actor == null) {
            Maneuver maneuver = (Maneuver) this.Player().FM;
            Actor actor1 = null;
            if (maneuver.target != null) {
                actor1 = maneuver.target.actor;
            } else {
                com.maddox.il2.engine.Actor actor2 = VisCheck.playerVisibilityCheck(this.Player(), false, 0.5F);
                if (actor2 == null)
                    ;
            }
            if (actor1 == null)
                ;
        }
    }
}
