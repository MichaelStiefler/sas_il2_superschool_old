/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderRequest_For_Takeoff extends Order {

    public OrderRequest_For_Takeoff() {
        super("Request_For_Takeoff");
    }

    public void run() {
        Maneuver maneuver = (Maneuver) this.Player().FM;
        if (maneuver.canITakeoff())
            Voice.speakTakeoffPermited((Aircraft) maneuver.actor);
        else
            Voice.speakTakeoffDenied((Aircraft) maneuver.actor);
    }
}
