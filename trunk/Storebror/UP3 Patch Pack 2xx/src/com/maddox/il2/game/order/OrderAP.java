/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;

class OrderAP extends Order {

    public OrderAP(String s) {
        super(s);
    }

    public void run(int i) {
        for (int j = 0; j < this.CommandSet().length; j++) {
            Aircraft aircraft = this.CommandSet()[j];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot) && Actor.isAlive(aircraft.FM.actor)) {
                Pilot pilot = (Pilot) aircraft.FM;
                pilot.bFromPlayer = true;
                ((Maneuver) aircraft.FM).setBomberAttackType(i);
            }
        }

    }
}
