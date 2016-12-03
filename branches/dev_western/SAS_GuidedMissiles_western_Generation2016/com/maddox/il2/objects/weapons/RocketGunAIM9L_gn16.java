package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.rts.Property;

public class RocketGunAIM9L_gn16 extends MissileGun implements RocketGunWithDelay {

	static {
		Class class1 = RocketGunAIM9L_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileAIM9L_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 5.0F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}

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