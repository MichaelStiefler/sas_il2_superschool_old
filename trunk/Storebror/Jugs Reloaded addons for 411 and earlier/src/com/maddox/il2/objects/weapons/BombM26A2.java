package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.BaseGameVersion;
import com.maddox.sound.SoundFX;
import com.maddox.sound.SoundPreset;

public class BombM26A2 extends Bomb {

	public BombM26A2() {
	}

	public void start() {
		super.start();
		if (BaseGameVersion.is412orLater()) {
			this.setStartDelayedExplosion(true);
		}
		if (BaseGameVersion.is411orLater() && this.fuze == null) {
			this.t1 = Time.current() + 1000L;
		} else if (this.delayExplosion == 0.0F) {
			this.t1 = Time.current() + 200L;
		} else {
			this.t1 = Time.current() + (long) (1000F * this.delayExplosion);
		}
	}

	public void interpolateTick() {
		super.interpolateTick();
		if (this.t1 < Time.current()) {
			this.doFireContaineds();
		}
	}

	public void msgCollision(Actor actor, String s, String s1) {
		super.msgCollision(actor, s, s1);
		if (this.isArmed()) {
			this.doFireContaineds();
		} else {
			this.destroy();
		}
	}

	private void ExplodeCharge(Point3d point3d) {
		if (!Config.isUSE_RENDER()) return;
		else {
			o.set(0.0F, 0.0F, 0.0F);
			l.set(point3d, o);
			String s = "effects/Explodes/Air/Zenitka/US_Frag/";
			this.FragChargeSound(point3d);
			float f = -1F;
			Eff3DActor.New(l, 1.0F, s + "Sparks.eff", f);
			Eff3DActor.New(l, 1.0F, s + "SparksP.eff", f);
			return;
		}
	}

	private void FragChargeSound(Point3d point3d) {
		SoundPreset soundpreset = new SoundPreset("explode.bullet");
		SoundFX soundfx = new SoundFX(soundpreset);
		soundfx.setPosition(point3d);
		soundfx.setUsrFlag(1);
		soundfx.play();
	}

	private void doFireContaineds() {
		this.ExplodeCharge(this.pos.getAbsPoint());
		Actor actor = null;
		if (Actor.isValid(this.getOwner())) {
			actor = this.getOwner();
		}
		Orient orient = new Orient();
		Vector3d vector3d = new Vector3d();
		Point3d point3d = new Point3d(this.pos.getCurrentPoint());
		o.set(this.pos.getAbsOrient());
		l.set(point3d, o);
		point3d = new Point3d(l.getX(), l.getY(), l.getZ());
		for (int i = 0; i < 20; i++) {
			switch (i) {
			case 0: // '\0'
				point3d.add(-0.14130000000000001D, -0.097900000000000001D, -0.12230000000000001D);
				break;

			case 1: // '\001'
				point3d.add(-0.14369999999999999D, -0.0030000000000000001D, -0.1221D);
				break;

			case 2: // '\002'
				point3d.add(-0.1429D, 0.092600000000000002D, -0.1205D);
				break;

			case 3: // '\003'
				point3d.add(-0.14430000000000001D, 0.047300000000000002D, -0.031D);
				break;

			case 4: // '\004'
				point3d.add(-0.14430000000000001D, -0.045600000000000002D, -0.029499999999999998D);
				break;

			case 5: // '\005'
				point3d.add(-0.14380000000000001D, -0.14050000000000001D, -0.029999999999999999D);
				break;

			case 6: // '\006'
				point3d.add(-0.14299999999999999D, 0.13769999999999999D, -0.030599999999999999D);
				break;

			case 7: // '\007'
				point3d.add(-0.14380000000000001D, 0.092700000000000005D, 0.060299999999999999D);
				break;

			case 8: // '\b'
				point3d.add(-0.1439D, -0.00029999999999999997D, 0.060699999999999997D);
				break;

			case 9: // '\t'
				point3d.add(-0.14269999999999999D, -0.093799999999999994D, 0.060299999999999999D);
				break;

			case 10: // '\n'
				point3d.add(0.24260000000000001D, -0.093299999999999994D, 0.059900000000000002D);
				break;

			case 11: // '\013'
				point3d.add(0.2414D, 0.00020000000000000001D, 0.060299999999999999D);
				break;

			case 12: // '\f'
				point3d.add(0.24149999999999999D, 0.093200000000000005D, 0.059900000000000002D);
				break;

			case 13: // '\r'
				point3d.add(0.2424D, 0.13819999999999999D, -0.031099999999999999D);
				break;

			case 14: // '\016'
				point3d.add(0.24149999999999999D, -0.14000000000000001D, -0.030499999999999999D);
				break;

			case 15: // '\017'
				point3d.add(0.24110000000000001D, -0.045100000000000001D, -0.029999999999999999D);
				break;

			case 16: // '\020'
				point3d.add(0.24099999999999999D, 0.047899999999999998D, -0.0315D);
				break;

			case 17: // '\021'
				point3d.add(0.2424D, 0.093100000000000002D, -0.121D);
				break;

			case 18: // '\022'
				point3d.add(0.24160000000000001D, -0.0025000000000000001D, -0.1225D);
				break;

			case 19: // '\023'
				point3d.add(0.24399999999999999D, -0.0974D, -0.1227D);
				break;
			}
			orient.set(o);
			this.getSpeed(vector3d);
			vector3d.add(0.0D, World.Rnd().nextDouble(-4D, 4D), World.Rnd().nextDouble(-4D, 4D));
			BombM41A1 bombm41a1 = new BombM41A1();
			bombm41a1.pos.setUpdateEnable(true);
			bombm41a1.pos.setAbs(point3d, orient);
			bombm41a1.pos.reset();
			if (BaseGameVersion.is411orLater()) {
				if (World.cur().diffCur.BombFuzes) {
					try {
						bombm41a1.setFuse((Fuze) (Fuze_AN_M110A1.class).newInstance());
					} catch (InstantiationException instantiationexception) {
						instantiationexception.printStackTrace();
					} catch (IllegalAccessException illegalaccessexception) {
						illegalaccessexception.printStackTrace();
					}
				}
			}
			bombm41a1.start();
			bombm41a1.setOwner(actor, false, false, false);
			bombm41a1.setSpeed(vector3d);
		}

		this.postDestroy();
	}

	private long t1;
	private static Loc l = new Loc();
	private static Orient o = new Orient();

	static {
		Class class1 = BombM26A2.class;
		Property.set(class1, "mesh", "3DO/Arms/M26A2_20/mono.sim");
		Property.set(class1, "power", 0.05F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.2F);
		Property.set(class1, "massa", 188.6944F);
		Property.set(class1, "sound", "weapon.bomb_std");
		if (BaseGameVersion.is411orLater()) {
			Property.set(class1, "fuze", new Object[] { Fuze_fragBundle.class });
		}
	}
}
