/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;

public class Orders {

    public void run() {
        if (this.lastPlayer == null || World.getPlayerAircraft() != this.lastPlayer) {
            Mission.addHayrakesToOrdersTree();
            this.lastPlayer = World.getPlayerAircraft();
        }
        if (!this.order[0].name.equals("MainMenu") && Main3D.cur3D().ordersTree.frequency() == null)
            Main3D.cur3D().ordersTree.Player.FM.AS.setBeacon(0);
        if (OrdersTree.curOrdersTree.isLocal())
            HUD.order(this.order);
        OrdersTree.curOrdersTree.cur = this;
    }

    public Orders(Order aorder[]) {
        this.upOrders = null;
        this.lastPlayer = null;
        this.order = aorder;
        for (int i = 0; i < aorder.length; i++)
            if (aorder[i] != null)
                aorder[i].orders = this;

    }

    public Order     order[];
    protected Orders upOrders;
    private Aircraft lastPlayer;
}
