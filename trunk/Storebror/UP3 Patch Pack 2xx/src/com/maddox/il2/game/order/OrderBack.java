/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

public class OrderBack extends Order {

    public void preRun() {
        if (this.orders.upOrders != null)
            this.orders.upOrders.run();
        else
            OrdersTree.curOrdersTree.unactivate();
    }

    public OrderBack() {
        this.attrib = 2;
        this.name = "back";
        this.nameDE = "back";
    }
}
