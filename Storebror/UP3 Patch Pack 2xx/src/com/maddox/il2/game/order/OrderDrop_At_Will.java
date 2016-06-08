/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderDrop_At_Will extends Order {

    public OrderDrop_At_Will() {
        super("Drop_At_Will");
    }

    public void run() {
        this.Player().FM.CT.bDropWithPlayer = false;
        Voice.setSyncMode(1);
        for (int i = 0; i < this.CommandSet().length; i++) {
            Aircraft aircraft = this.CommandSet()[i];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot)) {
                aircraft.FM.CT.dropWithPlayer = null;
                aircraft.setBombScoreOwner(null);
                aircraft.FM.CT.BayDoorControl = 0.0F;
                if (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0)
                    Voice.speakOk(aircraft);
            }
        }

        Voice.setSyncMode(0);
    }
}
