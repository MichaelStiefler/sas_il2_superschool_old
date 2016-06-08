/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.VisCheck;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderAttack_Bombers extends Order {

    public OrderAttack_Bombers() {
        super("Attack_Bombers");
    }

    public void run() {
        /* Actor actor = */ VisCheck.playerVisibilityCheck(this.Player(), false, 1.0F);
        Voice.setSyncMode(1);
        for (int i = 0; i < this.CommandSet().length; i++) {
            Aircraft aircraft = this.CommandSet()[i];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot) && Actor.isAlive(aircraft.FM.actor)) {
                Pilot pilot = (Pilot) aircraft.FM;
                pilot.targetBombers();
                boolean flag = false;
                if (pilot.Group != null) {
                    if (OrdersTree.curOrdersTree.alone())
                        if ((pilot.Group.grTask != 3 || pilot.Group.aTargetPreference != 8) && ((Maneuver) this.Player().FM).Group == pilot.Group) {
                            AirGroup airgroup = new AirGroup(pilot.Group);
                            pilot.Group.delAircraft(aircraft);
                            airgroup.addAircraft(aircraft);
                        } else {
                            pilot.aggressiveWingman = true;
                        }
                    pilot.Group.setATargMode(8);
                    AirGroup airgroup1 = pilot.Group.chooseTargetGroup();
                    if (airgroup1 != null) {
                        pilot.Group.targetGroup = airgroup1;
                        pilot.target = null;
                        pilot.Group.setGroupTask(3);
                        flag = true;
                        if (this.isEnableVoice() && aircraft != this.Player() && pilot.canAttack())
                            if (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0)
                                Voice.speakAttackBombers(aircraft);
                            else
                                Voice.speakOk(aircraft);
                    }
                }
                if (this.isEnableVoice() && aircraft != this.Player() && !flag)
                    Voice.speakUnable(aircraft);
            }
        }

        Voice.setSyncMode(0);
    }
}
