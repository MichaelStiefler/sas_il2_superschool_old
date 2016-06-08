/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Squadron;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;

public class Order {

    public int attrib() {
        return this.attrib & 3;
    }

    public String name(int i) {
        return i != 2 ? this.name : this.nameDE;
    }

    public Aircraft Player() {
        return OrdersTree.curOrdersTree.Player;
    }

    public Section PlayerWingSection() {
        return OrdersTree.curOrdersTree.Section;
    }

    public Wing PlayerWing() {
        return OrdersTree.curOrdersTree.PlayerWing;
    }

    public Squadron PlayerSquad() {
        return OrdersTree.curOrdersTree.PlayerSquad;
    }

    public Regiment PlayerRegiment() {
        return OrdersTree.curOrdersTree.PlayerRegiment;
    }

    public Aircraft[] CommandSet() {
        return OrdersTree.curOrdersTree.CommandSet;
    }

    public Aircraft Wingman() {
        Aircraft aircraft = this.Player();
        Aircraft aircraft1;
        if (aircraft.aircIndex() < 3)
            aircraft1 = aircraft.getWing().airc[aircraft.aircIndex() + 1];
        else
            aircraft1 = null;
        if (aircraft1 != null && (!Actor.isAlive(aircraft1) || !(aircraft1.FM instanceof Maneuver) || !((Maneuver) aircraft1.FM).isOk()))
            return null;
        else
            return aircraft1;
    }

    public void cset(Actor actor) {
        OrdersTree.curOrdersTree._cset(actor);
    }

    public boolean isEnableVoice() {
        return OrdersTree.curOrdersTree._isEnableVoice();
    }

    public void preRun() {
        if (this.subOrders != null) {
            if (this.orders.order.length != 0 && this.subOrders.order.length != 0) {
                this.subOrders.order[0] = new Order(this.name, this.nameDE);
                this.subOrders.order[0].orders = this.subOrders;
            }
            this.subOrders.upOrders = this.orders;
            this.subOrders.run();
        }
    }

    public void run() {}

    public Order() {
        this.attrib = 0;
    }

    public Order(String s, String s1) {
        this.attrib = 0;
        this.name = s;
        this.nameDE = s1;
    }

    public Order(String s) {
        this(s, s);
    }

    public Order(String s, Orders orders1) {
        this(s, s);
        this.subOrders = orders1;
        this.attrib = 2;
    }

    public Order(String s, String s1, Orders orders1) {
        this(s, s1);
        this.subOrders = orders1;
        this.attrib = 2;
    }

    public String getName() {
        if (this.name == null)
            return null;
        else
            return this.name;
    }

    public Orders getSubOrders() {
        return this.subOrders;
    }

    public static final int ATT_NORMAL = 0;
    public static final int ATT_GRAY   = 1;
    public static final int ATT_BOLD   = 2;
    public static final int ATT_BITS   = 3;
    int                     attrib;
    protected String        name;
    protected String        nameDE;
    protected Orders        orders;
    protected Orders        subOrders;
    protected static int    cmdCounter = 0;

}
