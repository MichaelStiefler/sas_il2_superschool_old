/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderChange_Formation_To_2 extends Order {

    public OrderChange_Formation_To_2() {
        super("C_F_ECHELONLEFT");
    }

    public void run() {
        Voice.setSyncMode(1);
        for (int i = 0; i < this.CommandSet().length; i++) {
            Aircraft aircraft = this.CommandSet()[i];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot) && Actor.isAlive(aircraft.FM.actor)) {
                Pilot pilot = (Pilot) aircraft.FM;
                if (pilot.Group != null) {
                    if (this.isEnableVoice() && this.CommandSet()[i] != this.Player() && (this.CommandSet()[i].getWing() == this.Player().getWing() || this.CommandSet()[i].aircIndex() == 0))
                        Voice.speakEchelonLeft(this.CommandSet()[i]);
                    pilot.Group.setFormationAndScale((byte) 3, pilot.formationScale, true);
                }
            }
        }

        Voice.setSyncMode(0);
    }
}
