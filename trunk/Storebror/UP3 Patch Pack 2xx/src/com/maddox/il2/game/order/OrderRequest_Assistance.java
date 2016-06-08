/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.AirGroupList;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.sounds.Voice;

class OrderRequest_Assistance extends Order {

    public OrderRequest_Assistance() {
        super("Request_Assistance");
        this.tmpV = new Vector3d();
    }

    public void run() {
        int i = this.Player().getArmy();
        Maneuver maneuver = (Maneuver) this.Player().FM;
        boolean flag = false;
        if (maneuver.Group != null) {
            int j = AirGroupList.length(War.Groups[i - 1 & 1]);
            for (int k = 0; k < j; k++) {
                AirGroup airgroup = AirGroupList.getGroup(War.Groups[i - 1 & 1], k);
                if (airgroup != null && airgroup.nOfAirc > 0 && (airgroup.airc[0] instanceof TypeFighter) && airgroup.grTask == 1) {
                    this.tmpV.sub(maneuver.Group.Pos, airgroup.Pos);
                    if (this.tmpV.lengthSquared() < 1000000000D) {
                        flag = true;
                        airgroup.clientGroup = maneuver.Group;
                        airgroup.setGroupTask(2);
                    }
                }
            }

        }
        Voice.setSyncMode(0);
        Voice.speakHelpNeededFromBase(this.Player(), flag);
    }

    Vector3d tmpV;
}
