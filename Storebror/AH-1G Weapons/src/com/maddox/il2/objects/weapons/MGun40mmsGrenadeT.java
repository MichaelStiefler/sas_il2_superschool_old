package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGun40mmsGrenadeT extends MGunAircraftGeneric {

	public MGun40mmsGrenadeT() {
	}

	public GunProperties createProperties() {
		GunProperties gunproperties = super.createProperties();
		gunproperties.bCannon = true;
		gunproperties.bUseHookAsRel = false;
		gunproperties.fireMesh = null;
		gunproperties.fire = "3DO/Effects/GunFire/37mm/GunFire.eff";
		gunproperties.sprite = null;
		gunproperties.smoke = null;
		gunproperties.shells = null;
		gunproperties.sound = "weapon.MGunM9s";
		gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
		gunproperties.emitI = 2.5F;
		gunproperties.emitR = 1.5F;
		gunproperties.emitTime = 0.03F;
		gunproperties.aimMinDist = 10F;
		gunproperties.aimMaxDist = 1000F;
		gunproperties.weaponType = -1;
		gunproperties.maxDeltaAngle = 0.0F;
		gunproperties.shotFreq = 4.6F;
		gunproperties.traceFreq = 0;
		gunproperties.bullets = 15;
		gunproperties.bulletsCluster = 1;
		gunproperties.bullet = (new BulletProperties[] { new BulletProperties() });
		gunproperties.bullet[0].massa = 0.604F;
		gunproperties.bullet[0].kalibr = 0.0009035401F;
		gunproperties.bullet[0].speed = 760F;
		gunproperties.bullet[0].power = 2.65F;
		gunproperties.bullet[0].powerType = 1;
		gunproperties.bullet[0].powerRadius = 10F;
		gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
		gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
		gunproperties.bullet[0].traceColor = 0xd9002eff;
		gunproperties.bullet[0].timeLife = 10F;
		return gunproperties;
	}
}
