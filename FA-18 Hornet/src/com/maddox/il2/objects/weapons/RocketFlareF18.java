package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.MsgShot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class RocketFlareF18 extends RocketFlare {

	public RocketFlareF18(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
		super(actor, netchannel, i, point3d, orient, f);
	}

	public void start(float f, int i) {
		float f1 = 30F;
		super.start(f1, i);
		getOwner().getSpeed(speed);
		speed.z = -15D;
		setSpeed(speed);
		Eff3DActor.New(this, null, new Loc(), 1.0F, "EFFECTS/Smokes/SmokeBlack_missiles.eff", f1);
		Engine.countermeasures().add(this);
	}

	protected void doExplosion(Actor actor, String s) {
		pos.getTime(Time.current(), p);
		Class class1 = getClass();
		float f = Property.floatValue(class1, "power", 1000F);
		int i = Property.intValue(class1, "powerType", 0);
		float f1 = Property.floatValue(class1, "radius", 0.0F);
		getSpeed(speed);
		Vector3f vector3f = new Vector3f(speed);
		vector3f.normalize();
		vector3f.scale(850F);
		MsgShot.send(actor, s, p, vector3f, M, getOwner(), (float) ((double) (0.5F * M) * speed.lengthSquared()), 3, 0.0D);
		MsgExplosion.send(actor, s, p, getOwner(), M, f, i, f1);
		destroy();
	}

	protected void doExplosionAir() {
	}

	private static Point3d p = new Point3d();

	static {
		Class class1 = RocketFlareF18.class;
		Property.set(class1, "mesh", "3do/arms/2KgBomblet/mono.sim");
		Property.set(class1, "sprite", (Object) null);
		Property.set(class1, "flame", (Object) null);
		Property.set(class1, "smoke", (Object) null);
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(class1, "emitLen", 3F);
		Property.set(class1, "emitMax", 20F);
		Property.set(class1, "sound", (Object) null);
		Property.set(class1, "timeLife", 5F);
		Property.set(class1, "timeFire", 0.3F);
		Property.set(class1, "force", 4000F);
		Property.set(class1, "dragCoefficient", 0.0F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "power", 1E-005F);
		Property.set(class1, "radius", 0.0F);
		Property.set(class1, "kalibr", 0.001F);
		Property.set(class1, "massa", 3F);
		Property.set(class1, "massaEnd", 20000F);
		Property.set(class1, "shotFreq", 3F);

	}
}