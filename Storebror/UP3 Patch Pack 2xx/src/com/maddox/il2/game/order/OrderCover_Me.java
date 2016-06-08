/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderCover_Me extends Order {

    public OrderCover_Me() {
        super("Cover_Me");
    }

    public void run() {
        Voice.setSyncMode(1);
        for (int i = 0; i < this.CommandSet().length; i++) {
            Aircraft aircraft = this.CommandSet()[i];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot) && Actor.isAlive(aircraft.FM.actor)) {
                Pilot pilot = (Pilot) aircraft.FM;
                Maneuver maneuver = (Maneuver) this.Player().FM;
                if (pilot.Group != null) {
                    if (OrdersTree.curOrdersTree.alone()) {
                        if (pilot.Group.grTask != 3) {
                            if (((Maneuver) this.Player().FM).Group == pilot.Group) {
                                AirGroup airgroup = new AirGroup(pilot.Group);
                                pilot.Group.delAircraft(aircraft);
                                airgroup.addAircraft(aircraft);
                            }
                        } else {
                            if (pilot.Group != maneuver.Group && maneuver.Group != null)
                                pilot.Group.rejoinToGroup(maneuver.Group);
                            pilot.aggressiveWingman = false;
                        }
                    } else if (aircraft == this.Wingman() && pilot.Group != maneuver.Group && maneuver.Group != null)
                        pilot.Group.rejoinToGroup(maneuver.Group);
                    pilot.airClient = this.Player().FM;
                    float f = 3.402823E+038F;
                    if (maneuver.danger != null) {
                        Vector3d vector3d = new Vector3d();
                        vector3d.sub(maneuver.danger.Loc, this.Player().FM.Loc);
                        f = (float) vector3d.length();
                    }
                    if (f < 500F) {
                        Maneuver maneuver1 = (Maneuver) maneuver.danger;
                        pilot.target = maneuver1;
                        if (maneuver1.Group != null) {
                            pilot.Group.targetGroup = maneuver1.Group;
                            pilot.Group.setGroupTask(3);
                        }
                    } else if (maneuver.Group != pilot.Group && maneuver.Group != null) {
                        pilot.Group.clientGroup = maneuver.Group;
                        pilot.Group.setGroupTask(2);
                    } else if (!pilot.isBusy() && aircraft != this.Player()) {
                        pilot.followOffset.set(300D, (aircraft.aircIndex() - 2) * 50F, 20D);
                        pilot.set_task(5);
                        pilot.clear_stack();
                        pilot.wingmanAttacks(1);
                        pilot.setDumbTime(60000L);
                    }
                    if (this.isEnableVoice() && aircraft != this.Player() && (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0))
                        if (pilot.canAttack() || aircraft == this.Wingman())
                            Voice.speakCoverMe(aircraft);
                        else
                            Voice.speakUnable(aircraft);
                }
            }
        }

        Voice.setSyncMode(0);
    }
}
