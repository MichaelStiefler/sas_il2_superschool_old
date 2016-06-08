/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.VisCheck;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderTarget_All extends Order {

    public OrderTarget_All() {
        super("Target_All");
        this.Pd = new Point3d();
    }

    public void run() {
        /* Actor actor = */ VisCheck.playerVisibilityCheck(this.Player(), false, 1.0F);
        Voice.setSyncMode(1);
        for (int i = 0; i < this.CommandSet().length; i++) {
            Aircraft aircraft = this.CommandSet()[i];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot) && Actor.isAlive(aircraft.FM.actor)) {
                Pilot pilot = (Pilot) aircraft.FM;
                pilot.targetAll();
                boolean flag = false;
//                System.out.println("OrderTarget_All 1");
                if (pilot.Group != null) {
                    this.Pd.set(pilot.Group.Pos);
//                    System.out.println("OrderTarget_All 2");
                    if (OrdersTree.curOrdersTree.alone()) {
//                        System.out.println("OrderTarget_All 3");
                        if (pilot.Group.grTask != 3 && ((Maneuver) this.Player().FM).Group == pilot.Group) {
//                            System.out.println("OrderTarget_All 4");
                            AirGroup airgroup = new AirGroup(pilot.Group);
                            pilot.Group.delAircraft(aircraft);
                            airgroup.addAircraft(aircraft);
                        } else {
//                           System.out.println("OrderTarget_All 5");
                            pilot.aggressiveWingman = true;
                        }
                    }
                    pilot.Group.setATargMode(9);
                    AirGroup airgroup1 = pilot.Group.chooseTargetGroup();
//                    System.out.println("OrderTarget_All 6");
                    if (airgroup1 != null) {
//                        System.out.println("OrderTarget_All 7");
                        pilot.Group.targetGroup = airgroup1;
                        pilot.Group.setGroupTask(3);
                        flag = true;
                        if (this.isEnableVoice() && aircraft != this.Player() && pilot.canAttack()) {
//                            System.out.println("OrderTarget_All 8");
                            if (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0) {
//                               System.out.println("OrderTarget_All 9");
                                Voice.speakTargetAll(aircraft);
                            } else {
//                                System.out.println("OrderTarget_All 10");
                                Voice.speakOk(aircraft);
                            }
                        }
                    }
//                    System.out.println("OrderTarget_All 11");
                    if (!flag) {
//                        System.out.println("OrderTarget_All 12");
                        if (OrdersTree.curOrdersTree.alone() && pilot.Group.grTask != 4 && ((Maneuver) this.Player().FM).Group == pilot.Group) {
//                            System.out.println("OrderTarget_All 13");
                            AirGroup airgroup2 = new AirGroup(pilot.Group);
                            pilot.Group.delAircraft(aircraft);
                            airgroup2.addAircraft(aircraft);
                        }
                        pilot.Group.setGTargMode(0);
                        pilot.Group.setGTargMode(this.Pd, 5000F);
                        Actor actor1 = pilot.Group.setGAttackObject(pilot.Group.numInGroup(aircraft));
//                        System.out.println("OrderTarget_All 14");
                        if (actor1 != null) {
//                            System.out.println("OrderTarget_All 15");
                            pilot.Group.setGroupTask(4);
                            flag = true;
                            if (this.isEnableVoice() && aircraft != this.Player()) {
//                                System.out.println("OrderTarget_All 16");
                                if (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0) {
//                                    System.out.println("OrderTarget_All 17");
                                    Voice.speakTargetAll(aircraft);
                                } else {
//                                    System.out.println("OrderTarget_All 18");
                                    Voice.speakOk(aircraft);
                                }
                            }
                        }
                    }
                }
//                System.out.println("OrderTarget_All 19");
                if (this.isEnableVoice() && aircraft != this.Player() && !flag)
                    Voice.speakUnable(aircraft);
            }
        }

//        System.out.println("OrderTarget_All 20");
        Voice.setSyncMode(0);
    }

    private Point3d Pd;
}
