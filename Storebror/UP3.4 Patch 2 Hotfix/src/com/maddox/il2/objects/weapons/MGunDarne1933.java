package com.maddox.il2.objects.weapons;
import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunDarne1933 extends MGunAircraftGeneric
{
    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = "3DO/Effects/GunFire/7mm/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/7mm/GunFlare.eff";
        gunproperties.smoke = "effects/smokes/SmallGun.eff";
        gunproperties.shells = "3DO/Effects/GunShells/GunShells.eff";
        gunproperties.sound = "weapon.MGunMAC1934s";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 10F;
        gunproperties.emitR = 3F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 800F;
        gunproperties.weaponType = 1;
        gunproperties.maxDeltaAngle = 0.157F;
        gunproperties.shotFreq = 20F;
        gunproperties.traceFreq = 2;
        gunproperties.bullets = 500;
        gunproperties.bulletsCluster = 2;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.0092F;
        gunproperties.bullet[0].kalibr = 7.7E-005F;
        gunproperties.bullet[0].speed = 830F;
        gunproperties.bullet[0].power = 0.00226F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.0F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/7mmYellow/mono.sim";
        gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[0].traceColor = 0xd90000ff;
        gunproperties.bullet[0].timeLife = 2.5F;
        gunproperties.bullet[1].massa = 0.0092F;
        gunproperties.bullet[1].kalibr = 7.7E-005F;
        gunproperties.bullet[1].speed = 830F;
        gunproperties.bullet[1].power = 0.00232F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.005F;
        gunproperties.bullet[1].traceMesh = null;
        gunproperties.bullet[1].traceTrail = null;
        gunproperties.bullet[1].traceColor = 0xd90000ff;
        gunproperties.bullet[1].timeLife = 2.5F;
        gunproperties.bullet[2].massa = 0.0092F;
        gunproperties.bullet[2].kalibr = 7.7E-005F;
        gunproperties.bullet[2].speed = 830F;
        gunproperties.bullet[2].power = 0.00226F;
        gunproperties.bullet[2].powerType = 0;
        gunproperties.bullet[2].powerRadius = 0.0F;
        gunproperties.bullet[2].traceMesh = "3do/effects/tracers/7mmYellow/mono.sim";
        gunproperties.bullet[2].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[2].traceColor = 0xd90000ff;
        gunproperties.bullet[2].timeLife = 2.5F;
        gunproperties.bullet[3].massa = 0.0092F;
        gunproperties.bullet[3].kalibr = 7.7E-005F;
        gunproperties.bullet[3].speed = 830F;
        gunproperties.bullet[3].power = 0.00228F;
        gunproperties.bullet[3].powerType = 0;
        gunproperties.bullet[3].powerRadius = 0.005F;
        gunproperties.bullet[3].traceMesh = "3do/effects/tracers/7mmYellow/mono.sim";
        gunproperties.bullet[3].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[3].traceColor = 0xd90000ff;
        gunproperties.bullet[3].timeLife = 2.5F;
        gunproperties.bullet[4].massa = 0.0092F;
        gunproperties.bullet[4].kalibr = 7.7E-005F;
        gunproperties.bullet[4].speed = 830F;
        gunproperties.bullet[4].power = 0.00226F;
        gunproperties.bullet[4].powerType = 0;
        gunproperties.bullet[4].powerRadius = 0.0F;
        gunproperties.bullet[4].traceMesh = "3do/effects/tracers/7mmYellow/mono.sim";
        gunproperties.bullet[4].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[4].traceColor = 0xd90000ff;
        gunproperties.bullet[4].timeLife = 2.5F;
        gunproperties.bullet[5].massa = 0.0092F;
        gunproperties.bullet[5].kalibr = 7.7E-005F;
        gunproperties.bullet[5].speed = 830F;
        gunproperties.bullet[5].power = 0.00232F;
        gunproperties.bullet[5].powerType = 0;
        gunproperties.bullet[5].powerRadius = 0.005F;
        gunproperties.bullet[5].traceMesh = null;
        gunproperties.bullet[5].traceTrail = null;
        gunproperties.bullet[5].traceColor = 0xd90000ff;
        gunproperties.bullet[5].timeLife = 2.5F;
        gunproperties.bullet[6].massa = 0.0092F;
        gunproperties.bullet[6].kalibr = 7.7E-005F;
        gunproperties.bullet[6].speed = 830F;
        gunproperties.bullet[6].power = 0.00226F;
        gunproperties.bullet[6].powerType = 0;
        gunproperties.bullet[6].powerRadius = 0.0F;
        gunproperties.bullet[6].traceMesh = "3do/effects/tracers/7mmYellow/mono.sim";
        gunproperties.bullet[6].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[6].traceColor = 0xd90000ff;
        gunproperties.bullet[6].timeLife = 2.5F;
        gunproperties.bullet[7].massa = 0.0092F;
        gunproperties.bullet[7].kalibr = 7.7E-005F;
        gunproperties.bullet[7].speed = 830F;
        gunproperties.bullet[7].power = 0.00228F;
        gunproperties.bullet[7].powerType = 0;
        gunproperties.bullet[7].powerRadius = 0.005F;
        gunproperties.bullet[7].traceMesh = "3do/effects/tracers/7mmYellow/mono.sim";
        gunproperties.bullet[7].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[7].traceColor = 0xd90000ff;
        gunproperties.bullet[7].timeLife = 2.5F;
        return gunproperties;
    }
}
