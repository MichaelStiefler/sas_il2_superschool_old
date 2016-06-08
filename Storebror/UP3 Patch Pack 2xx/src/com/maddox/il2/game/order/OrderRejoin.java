/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderRejoin extends Order {

    public OrderRejoin() {
        super("Rejoin");
    }

    public void run() {
        Voice.setSyncMode(1);
        for (int i = 0; i < this.CommandSet().length; i++) {
            Aircraft aircraft = this.CommandSet()[i];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot) && Actor.isAlive(aircraft.FM.actor)) {
                Pilot pilot = (Pilot) aircraft.FM;
                if (pilot.Group != null) {
                    Maneuver maneuver = (Maneuver) this.Player().FM;
                    if (aircraft.getWing() == this.Player().getWing() && pilot.Group != maneuver.Group && maneuver.Group != null)
                        pilot.Group.rejoinToGroup(maneuver.Group);
                    if (this.isEnableVoice() && aircraft != this.Player() && (aircraft.getWing() == this.Player().getWing() || this.CommandSet()[i].aircIndex() == 0))
                        Voice.speakRejoin(aircraft);
                    if (OrdersTree.curOrdersTree.alone()) {
                        pilot.aggressiveWingman = false;
                        pilot.followOffset.set(200D, 0.0D, 20D);
                        pilot.set_task(5);
                        pilot.clear_stack();
                        pilot.set_maneuver(65);
                    } else {
                        pilot.Group.setFormationAndScale((byte) 0, 1.0F, false);
                        pilot.Group.setGroupTask(1);
                        pilot.Group.timeOutForTaskSwitch = 480;
                    }
                }
            }
        }

        Voice.setSyncMode(0);
    }
}
