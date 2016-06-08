/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderBreak extends Order {

    public OrderBreak() {
        super("Break");
    }

    public void run() {
        Voice.setSyncMode(1);
        for (int i = 0; i < this.CommandSet().length; i++) {
            Aircraft aircraft = this.CommandSet()[i];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot)) {
                Pilot pilot = (Pilot) aircraft.FM;
                if (OrdersTree.curOrdersTree.alone())
                    pilot.aggressiveWingman = true;
                if (pilot.get_task() == 2) {
                    pilot.Leader = null;
                    pilot.set_task(3);
                    pilot.push(8);
                    pilot.push(8);
                    pilot.push(8);
                    pilot.push(8);
                    for (int j = 0; j < 3 - aircraft.aircIndex(); j++)
                        pilot.push(48);

                    pilot.pop();
                    pilot.setDumbTime(0L);
                    if (this.isEnableVoice() && aircraft != this.Player() && (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0))
                        Voice.speakBreak(aircraft);
                }
            }
        }

        Voice.setSyncMode(0);
    }
}
