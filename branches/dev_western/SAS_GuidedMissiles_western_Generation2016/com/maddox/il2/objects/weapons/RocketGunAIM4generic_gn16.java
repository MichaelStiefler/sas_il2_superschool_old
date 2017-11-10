// Source File Name: RocketGunAIM4generic_gn16.java
// Author:           western0221
// Last Modified by: western0221 2017-11-09
package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.rts.Property;

public class RocketGunAIM4generic_gn16 extends MissileGun implements RocketGunWithDelay {

	public void updateHook(String s) {
		if (((Missile) super.rocket).isReleased()) return;
		Class class1 = this.getClass();
		this.bullets(Property.intValue(class1, "bullets", 1));
		super.hook = (HookNamed) super.actor.findHook(s);
		try {
			super.rocket.destroy();
			super.rocket = (Rocket) super.bulletClass.newInstance();
			((Actor) (super.rocket)).pos.setBase(super.actor, super.hook, false);
			((Actor) (super.rocket)).pos.changeHookToRel();
			((Actor) (super.rocket)).pos.resetAsBase();
			super.rocket.visibilityAsBase(true);
			((Actor) (super.rocket)).pos.setUpdateEnable(false);
		} catch (Exception exception) {
		}
	}
}