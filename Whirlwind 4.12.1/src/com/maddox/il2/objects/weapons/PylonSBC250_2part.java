package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class PylonSBC250_2part extends Pylon
{
//	public PylonSBC250_2part() {
//		super();
//		Bomb40lbsE theBomb = new Bomb40lbsE();
//		theBomb.index = 1;
//		theBomb.pos.setBase(this.getOwner(), this.getOwner().findHook(this.getHookName()), false);
//		theBomb.pos.changeHookToRel();
//		theBomb.pos.resetAsBase();
//		theBomb.visibilityAsBase(true);
//		theBomb.setSeed(0);
//		theBomb.pos.setUpdateEnable(false);
//	}
	
    static
    {
        Property.set(PylonSBC250_2part.class, "mesh", "3do/arms/250LbsSBC/2part/mono.sim");
    }
}
