/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderGT extends Order {

    public OrderGT(String s) {
        super(s);
        this.Pd = new Point3d();
    }

    public void run(int i) {
        Voice.setSyncMode(1);
        for (int j = 0; j < this.CommandSet().length; j++) {
            Aircraft aircraft = this.CommandSet()[j];
            if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot) && Actor.isAlive(aircraft.FM.actor)) {
                Pilot pilot = (Pilot) aircraft.FM;
                pilot.attackGround(i);
                boolean flag = false;
                if (pilot.Group != null) {
                    this.Pd.set(pilot.Group.Pos);
                    if (OrdersTree.curOrdersTree.alone() && (pilot.Group.grTask != 4 || pilot.Group.gTargetPreference != i) && ((Maneuver) this.Player().FM).Group == pilot.Group) {
                        AirGroup airgroup = new AirGroup(pilot.Group);
                        pilot.Group.delAircraft(aircraft);
                        airgroup.addAircraft(aircraft);
                    }
                    pilot.Group.setGTargMode(i);
                    float f = 4000F;
                    if (i == 6 || i == 5)
                        f = 9000F;
                    pilot.Group.setGTargMode(this.Pd, f);
                    Actor actor = pilot.Group.setGAttackObject(pilot.Group.numInGroup(aircraft));
                    if (actor != null) {
                        if (this.isEnableVoice() && this.CommandSet()[j] != this.Player())
                            if (this.CommandSet()[j].getWing() == this.Player().getWing() || this.CommandSet()[j].aircIndex() == 0)
                                Voice.speakAttackGround(this.CommandSet()[j]);
                            else
                                Voice.speakOk(this.CommandSet()[j]);
                        pilot.target_ground = null;
                        pilot.Group.setGroupTask(4);
                        flag = true;
                    }
                }
                if (this.isEnableVoice() && this.CommandSet()[j] != this.Player() && !flag)
                    Voice.speakUnable(this.CommandSet()[j]);
            }
        }

        Voice.setSyncMode(0);
    }

    private Point3d Pd;
}
