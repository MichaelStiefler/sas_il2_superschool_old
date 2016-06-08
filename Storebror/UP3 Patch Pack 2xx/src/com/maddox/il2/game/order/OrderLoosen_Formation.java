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

class OrderLoosen_Formation extends Order {

    public OrderLoosen_Formation() {
        super("Loosen_Formation");
    }

    public void run() {
        Voice.setSyncMode(1);
        for (int i = 0; i < this.CommandSet().length; i++) {
            Aircraft aircraft = this.CommandSet()[i];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot) && Actor.isAlive(aircraft.FM.actor)) {
                Pilot pilot = (Pilot) aircraft.FM;
                if (this.isEnableVoice() && this.CommandSet()[i] != this.Player() && (this.CommandSet()[i].getWing() == this.Player().getWing() || this.CommandSet()[i].aircIndex() == 0))
                    Voice.speakLoosenFormation(this.CommandSet()[i]);
                if (pilot.Group != null && pilot.Group.nOfAirc > 0 && pilot == pilot.Group.airc[0].FM) {
                    Maneuver maneuver = (Maneuver) pilot.Group.airc[pilot.Group.nOfAirc - 1].FM;
                    float f = maneuver.formationScale * 1.333F;
                    if (f > 20F)
                        f = 20F;
                    pilot.Group.setFormationAndScale(maneuver.formationType, f, true);
                }
            }
        }

        Voice.setSyncMode(0);
    }
}
