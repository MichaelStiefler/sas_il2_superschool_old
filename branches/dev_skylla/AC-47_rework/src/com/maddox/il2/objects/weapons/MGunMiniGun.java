// 
// Decompiled by Procyon v0.5.29
// 

package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.BulletProperties;
import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.GunProperties;

public class MGunMiniGun extends MGunBrowning50s
{
    public GunProperties createProperties() {
        final GunProperties properties = super.createProperties();
        properties.bCannon = false;
        properties.bUseHookAsRel = true;
        properties.fireMesh = "3DO/Effects/GunFire/12mm/mono.sim";
        properties.fire = null;
        properties.sprite = "3DO/Effects/GunFire/12mm/GunFlare.eff";
        properties.smoke = "effects/smokes/MachineGun.eff";
        properties.shells = null;
        properties.sound = "weapon.MiniGun";
        properties.emitColor = new Color3f(1.0f, 1.0f, 0.0f);
        properties.emitI = 2.5f;
        properties.emitR = 1.5f;
        properties.emitTime = 0.03f;
        properties.aimMinDist = 10.0f;
        properties.aimMaxDist = 1000.0f;
        properties.weaponType = -1;
        properties.maxDeltaAngle = 0.229f;
        properties.shotFreq = 50.0f;
        properties.traceFreq = 4;
        properties.bullets = 8000;
        properties.bulletsCluster = 1;
        properties.bullet = new BulletProperties[] { new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties() };
        properties.bullet[0].massa = 0.0105f;
        properties.bullet[0].kalibr = 1.05062E-7f;
        properties.bullet[0].speed = 1070.0f;
        properties.bullet[0].power = 0.002f;
        properties.bullet[0].powerType = 0;
        properties.bullet[0].powerRadius = 0.0f;
        properties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        properties.bullet[0].traceTrail = "Effects/Smokes/SmokeBlack_BuletteTrail.eff";
        properties.bullet[0].traceColor = -654311169;
        properties.bullet[0].timeLife = 6.5f;
        properties.bullet[1].massa = 0.0105f;
        properties.bullet[1].kalibr = 1.05062E-7f;
        properties.bullet[1].speed = 1070.0f;
        properties.bullet[1].power = 0.002f;
        properties.bullet[1].powerType = 0;
        properties.bullet[1].powerRadius = 0.0f;
        properties.bullet[1].traceMesh = null;
        properties.bullet[1].traceTrail = null;
        properties.bullet[1].traceColor = 0;
        properties.bullet[1].timeLife = 6.5f;
        properties.bullet[2].massa = 0.0105f;
        properties.bullet[2].kalibr = 1.05062E-7f;
        properties.bullet[2].speed = 1070.0f;
        properties.bullet[2].power = 0.002f;
        properties.bullet[2].powerType = 0;
        properties.bullet[2].powerRadius = 0.0f;
        properties.bullet[2].traceMesh = null;
        properties.bullet[2].traceTrail = null;
        properties.bullet[2].traceColor = 0;
        properties.bullet[2].timeLife = 6.5f;
        properties.bullet[3].massa = 0.0105f;
        properties.bullet[3].kalibr = 1.05062E-7f;
        properties.bullet[3].speed = 1070.0f;
        properties.bullet[3].power = 0.002f;
        properties.bullet[3].powerType = 0;
        properties.bullet[3].powerRadius = 0.0f;
        properties.bullet[3].traceMesh = null;
        properties.bullet[3].traceTrail = null;
        properties.bullet[3].traceColor = 0;
        properties.bullet[3].timeLife = 6.5f;
        return properties;
    }
}
