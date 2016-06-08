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
import com.maddox.il2.game.Selector;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderAttack_My_Target extends Order {

    public OrderAttack_My_Target() {
        super("Attack_My_Target");
    }

    public void run() {
        Actor actor = Selector.getTarget();
        if (actor == null) {
            Maneuver maneuver = (Maneuver) this.Player().FM;
            if (maneuver.target != null)
                actor = maneuver.target.actor;
            else if (maneuver.target_ground != null)
                actor = maneuver.target_ground;
            else
                actor = VisCheck.playerVisibilityCheck(this.Player(), false, 0.5F);
        }
        Voice.setSyncMode(1);
        for (int i = 0; i < this.CommandSet().length; i++) {
            Aircraft aircraft = this.CommandSet()[i];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot) && Actor.isAlive(aircraft.FM.actor)) {
                Pilot pilot = (Pilot) aircraft.FM;
                boolean flag = false;
                if (actor != null && (actor instanceof Aircraft)) {
                    pilot.Group.setATargMode(9);
                    AirGroup airgroup = ((Maneuver) ((Aircraft) actor).FM).Group;
                    if (pilot.Group != null && airgroup != null) {
                        if (OrdersTree.curOrdersTree.alone())
                            if (pilot.Group.grTask != 3 && ((Maneuver) this.Player().FM).Group == pilot.Group) {
                                AirGroup airgroup2 = new AirGroup(pilot.Group);
                                pilot.Group.delAircraft(aircraft);
                                airgroup2.addAircraft(aircraft);
                            } else {
                                pilot.aggressiveWingman = true;
                            }
                        pilot.targetAll();
                        pilot.Group.targetGroup = airgroup;
                        pilot.target = null;
                        pilot.Group.setGroupTask(3);
                        flag = true;
                        if (this.isEnableVoice() && aircraft != this.Player() && pilot.canAttack())
                            if (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0)
                                Voice.speakAttackMyTarget(aircraft);
                            else
                                Voice.speakOk(aircraft);
                    }
                } else if (actor != null) {
                    if (OrdersTree.curOrdersTree.alone() && pilot.Group.grTask != 4 && ((Maneuver) this.Player().FM).Group == pilot.Group) {
                        AirGroup airgroup1 = new AirGroup(pilot.Group);
                        pilot.Group.delAircraft(aircraft);
                        airgroup1.addAircraft(aircraft);
                    }
                    pilot.Group.setGTargMode(0);
                    pilot.Group.setGTargMode(actor);
                    Actor actor1 = pilot.Group.setGAttackObject(pilot.Group.numInGroup(aircraft));
                    if (actor1 != null) {
                        if (this.isEnableVoice() && aircraft != this.Player())
                            if (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0)
                                Voice.speakAttackMyTarget(aircraft);
                            else
                                Voice.speakOk(aircraft);
                        pilot.target_ground = null;
                        pilot.Group.setGroupTask(4);
                        flag = true;
                    }
                }
                if (this.isEnableVoice() && aircraft != this.Player() && !flag)
                    Voice.speakUnable(aircraft);
            }
        }

        Voice.setSyncMode(0);
    }
}
