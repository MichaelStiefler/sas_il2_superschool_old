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

class OrderStartEngines extends Order {

    public OrderStartEngines() {
        super("Start_Engines");
    }

    public void run() {
        this.cset(this.PlayerWing());
        Voice.setSyncMode(1);
        for (int i = 0; i < this.CommandSet().length; i++) {
            Aircraft aircraft = this.CommandSet()[i];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot) && Actor.isAlive(aircraft.FM.actor)) {
                Maneuver maneuver = (Maneuver) aircraft.FM;
                maneuver.bGentsStartYourEngines = true;
                if (this.isEnableVoice() && aircraft != this.Player() && (aircraft.getWing() == this.Player().getWing() || this.CommandSet()[i].aircIndex() == 0))
                    Voice.speakOk(aircraft);
            }
        }

        Voice.setSyncMode(0);
    }
}
