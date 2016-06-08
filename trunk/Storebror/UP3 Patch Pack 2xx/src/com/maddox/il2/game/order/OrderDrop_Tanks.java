/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderDrop_Tanks extends Order {

    public OrderDrop_Tanks() {
        super("Drop_Tanks");
    }

    public void run() {
        Voice.setSyncMode(1);
        for (int i = 0; i < this.CommandSet().length; i++) {
            Aircraft aircraft = this.CommandSet()[i];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot))
                if (((Pilot) aircraft.FM).CT.dropFuelTanks()) {
                    if (this.isEnableVoice() && aircraft != this.Player() && (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0))
                        Voice.speakDropTanks(aircraft);
                } else if (this.isEnableVoice() && aircraft != this.Player() && (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0))
                    Voice.speakUnable(aircraft);
        }

        Voice.setSyncMode(0);
    }
}
