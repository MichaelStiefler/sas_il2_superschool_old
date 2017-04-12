// 
// Decompiled by Procyon v0.5.29
// 

package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.BulletProperties;
import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.GunProperties;

/**
 * This class was modified by SAS~Skylla in the course of the AC-47 rework.
**/

public class MGunMiniGun3000 extends MGunAircraftGeneric {
		
    public GunProperties createProperties() {
        final GunProperties properties = super.createProperties();
        properties.bCannon = false;
        properties.bUseHookAsRel = true;
        properties.fire = null;
        properties.shells = null;
        properties.sound = "weapon.MiniGun";
        properties.shotFreq = 50.0f;
        properties.bullets = 8000;
      //TODO Streuung:
        properties.maxDeltaAngle = 0.458F; //default = 0.229;    
        properties.fireMesh = "3DO/Effects/GunFire/12mm/mono.sim";
        properties.sprite = "3DO/Effects/GunFire/12mm/GunFlare.eff";
        properties.smoke = "effects/smokes/MachineGun.eff";
        properties.emitColor = new Color3f(1.0f, 1.0f, 0.0f);
        properties.emitI = 2.5f;
        properties.emitR = 1.5f;
        properties.emitTime = 0.03f;
        properties.aimMinDist = 10.0f;
        properties.aimMaxDist = 1000.0f;
        properties.weaponType = -1;
        properties.traceFreq = 2;
        properties.bulletsCluster = 2;
        final int bl = 9;
        properties.bullet = new BulletProperties[bl];
        for(int j = 0; j < bl; j++) {
        	properties.bullet[j] =  new BulletProperties();
        }
        for(int i = 0; i < bl - 4; i += 5) {
        	properties.bullet[i+0].massa = 0.046f;
        	properties.bullet[i+0].kalibr = 1.181215E-4f;
        	properties.bullet[i+0].speed = 870.0f;
        	properties.bullet[i+0].power = 0.0f;
        	properties.bullet[i+0].powerType = 0;
        	properties.bullet[i+0].powerRadius = 0.0f;
        	properties.bullet[i+0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        	properties.bullet[i+0].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        	properties.bullet[i+0].traceColor = -117440257;
        	properties.bullet[i+0].timeLife = 6.5f;
        	properties.bullet[i+1].massa = 0.041f;
        	properties.bullet[i+1].kalibr = 1.185215E-4f;
        	properties.bullet[i+1].speed = 900.0f;
        	properties.bullet[i+1].power = 0.0022f;
        	properties.bullet[i+1].powerType = 0;
        	properties.bullet[i+1].powerRadius = 0.0f;
        	properties.bullet[i+1].traceMesh = null;
        	properties.bullet[i+1].traceTrail = null;
        	properties.bullet[i+1].traceColor = 0;
        	properties.bullet[i+1].timeLife = 6.3f;
        	properties.bullet[i+2].massa = 0.046f;
        	properties.bullet[i+2].kalibr = 1.181215E-4f;
        	properties.bullet[i+2].speed = 870.0f;
        	properties.bullet[i+2].power = 0.0f;
        	properties.bullet[i+2].powerType = 0;
        	properties.bullet[i+2].powerRadius = 0.0f;
        	properties.bullet[i+2].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        	properties.bullet[i+2].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        	properties.bullet[i+2].traceColor = -117440257;
        	properties.bullet[i+2].timeLife = 6.5f;
        	properties.bullet[i+3].massa = 0.041f;
        	properties.bullet[i+3].kalibr = 1.185215E-4f;
        	properties.bullet[i+3].speed = 900.0f;
        	properties.bullet[i+3].power = 0.0022f;
        	properties.bullet[i+3].powerType = 0;
        	properties.bullet[i+3].powerRadius = 0.0f;
        	properties.bullet[i+3].traceMesh = null;
        	properties.bullet[i+3].traceTrail = null;
        	properties.bullet[i+3].traceColor = 0;
        	properties.bullet[i+3].timeLife = 6.3f;
        	properties.bullet[i+4].massa = 0.042f;
        	properties.bullet[i+4].kalibr = 1.182215E-4f;
        	properties.bullet[i+4].speed = 820.0f;
        	properties.bullet[i+4].power = 0.001f;
        	properties.bullet[i+4].powerType = 0;
        	properties.bullet[i+4].powerRadius = 0.0f;
        	properties.bullet[i+4].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        	properties.bullet[i+4].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        	properties.bullet[i+4].traceColor = -117440257;
        	properties.bullet[i+4].timeLife = 6.5f;
        }
        properties.bullet[bl-4].massa = 0.043f;
        properties.bullet[bl-4].kalibr = 1.209675E-4f;
        properties.bullet[bl-4].speed = 890.0f;
        properties.bullet[bl-4].power = 0.002f;
        properties.bullet[bl-4].powerType = 0;
        properties.bullet[bl-4].powerRadius = 0.0f;
        properties.bullet[bl-4].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        properties.bullet[bl-4].traceTrail = "Effects/Smokes/SmokeBlack_BuletteTrail.eff";
        properties.bullet[bl-4].traceColor = -654311169;
        properties.bullet[bl-4].timeLife = 6.5f;
        properties.bullet[bl-3].massa = 0.043f;
        properties.bullet[bl-3].kalibr = 1.105062E-4f;
        properties.bullet[bl-3].speed = 890.0f;
        properties.bullet[bl-3].power = 0.002f;
        properties.bullet[bl-3].powerType = 0;
        properties.bullet[bl-3].powerRadius = 0.0f;
        properties.bullet[bl-3].traceMesh = null;
        properties.bullet[bl-3].traceTrail = null;
        properties.bullet[bl-3].traceColor = 0;
        properties.bullet[bl-3].timeLife = 6.52f;
        properties.bullet[bl-2].massa = 0.043f;
        properties.bullet[bl-2].kalibr = 1.105062E-4f;
        properties.bullet[bl-2].speed = 890.0f;
        properties.bullet[bl-2].power = 9.768E-4f;
        properties.bullet[bl-2].powerType = 0;
        properties.bullet[bl-2].powerRadius = 0.15f;
        properties.bullet[bl-2].traceMesh = null;
        properties.bullet[bl-2].traceTrail = null;
        properties.bullet[bl-2].traceColor = 0;
        properties.bullet[bl-1].timeLife = 6.5f;
        properties.bullet[bl-1].massa = 0.043f;
        properties.bullet[bl-1].kalibr = 1.105062E-4f;
        properties.bullet[bl-1].speed = 890.0f;
        properties.bullet[bl-1].power = 0.002f;
        properties.bullet[bl-1].powerType = 0;
        properties.bullet[bl-1].powerRadius = 0.0f;
        properties.bullet[bl-1].traceMesh = null;
        properties.bullet[bl-1].traceTrail = null;
        properties.bullet[bl-1].traceColor = 0;
        properties.bullet[bl-1].timeLife = 6.25f;
        return properties;
    }
    
    /*
    public void initRealisticGunnery(final boolean b) {
    	for (int length = this.prop.bullet.length, i = 0; i < length; ++i) {
    		if (b) {
            	this.bulletAG[i] = -9.81f;
                this.bulletKV[i] = -(1000.0f * this.prop.bullet[i].kalibr / this.prop.bullet[i].massa);
            } else {
                this.bulletAG[i] = 0.0f;
                this.bulletKV[i] = 0.0f;
            }
        }
        System.out.println("SKYLLA MINIGUN: maxDeltaAngle = " + this.prop.maxDeltaAngle);
	}
    */
}
