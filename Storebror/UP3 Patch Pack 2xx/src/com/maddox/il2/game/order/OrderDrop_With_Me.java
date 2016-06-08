/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderDrop_With_Me extends Order {

    public OrderDrop_With_Me() {
        super("Drop_With_Me");
    }

    public void run() {
        this.Player().FM.CT.bDropWithPlayer = true;
        Voice.setSyncMode(1);
        for (int i = 0; i < this.CommandSet().length; i++) {
            Aircraft aircraft = this.CommandSet()[i];
            if (aircraft != this.Player() && Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot)) {
                aircraft.FM.CT.dropWithPlayer = this.Player();
                aircraft.setBombScoreOwner(this.Player());
                if (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0)
                    Voice.speakOk(aircraft);
            }
        }

        Voice.setSyncMode(0);
    }
}
