/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

public class OrderAnyone_Help_Me extends Order {

    public OrderAnyone_Help_Me() {
        super("Anyone_Help_Me");
    }

    public void run() {
        this.cset(this.PlayerSquad());
        Voice.setSyncMode(1);
        for (int i = 0; i < this.CommandSet().length; i++) {
            Aircraft aircraft = this.CommandSet()[i];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot) && Actor.isAlive(aircraft.FM.actor)) {
                Pilot pilot = (Pilot) aircraft.FM;
                if (pilot.Group != null) {
                    Maneuver maneuver = (Maneuver) this.Player().FM;
                    pilot.airClient = this.Player().FM;
                    boolean flag = false;
                    if (maneuver.danger != null) {
                        Maneuver maneuver1 = (Maneuver) maneuver.danger;
                        pilot.target = maneuver1;
                        if (maneuver1.Group != null) {
                            if (this.isEnableVoice() && aircraft != this.Player() && pilot.canAttack() && (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0))
                                Voice.speakAttackFighters(aircraft);
                            pilot.Group.targetGroup = maneuver1.Group;
                            pilot.Group.setGroupTask(3);
                            flag = true;
                        }
                    } else if (maneuver.Group != null) {
                        maneuver.Group.setATargMode(7);
                        AirGroup airgroup = maneuver.Group.chooseTargetGroup();
                        if (airgroup != null) {
                            if (this.isEnableVoice() && aircraft != this.Player() && pilot.canAttack() && (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0))
                                Voice.speakAttackFighters(aircraft);
                            pilot.Group.targetGroup = airgroup;
                            pilot.Group.setGroupTask(3);
                            flag = true;
                        }
                    }
                    if (this.isEnableVoice() && aircraft != this.Player() && !flag)
                        Voice.speakYouAreClear(this.CommandSet()[i]);
                }
            }
        }

        Voice.setSyncMode(0);
    }
}
