// 
// Decompiled by Procyon v0.5.29
// 

package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

/**
 * This class was modified by SAS~Skylla in the course of the AC-47 rework.
 * @see AC_47, MGunMiniGun3000
**/

public class MGunMiniGun6000 extends MGunMiniGun3000 {
	
	public MGunMiniGun6000() {
		rpm = 6000F;
	}
	
  //Alterable RPM: -------------------------------------------------------------------------------------
	
	public float incRPM() {
		if(rpm + 3000F <= 6000F) {
			rpm += 3000F;
		}
		return rpm;
	}
	
	public float decRPM() {
		if(rpm - 3000F >= 3000F) {
			rpm -= 3000F;
		}
		return rpm;
	}
	
	public float resRPM() {
		this.rpm = 6000F;
		return rpm;
	}
	
  //----------------------------------------------------------------------------------------------------
	
    public GunProperties createProperties() {
        final GunProperties properties = super.createProperties();
        properties.shotFreq = 100.0f;
        return properties;
    }
}
