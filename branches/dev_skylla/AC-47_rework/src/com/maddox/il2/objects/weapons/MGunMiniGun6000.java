// 
// Decompiled by Procyon v0.5.29
// 

package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

/**
 * This class was modified by SAS~Skylla in the course of the AC-47 rework.
**/

public class MGunMiniGun6000 extends MGunMiniGun3000
{
    public GunProperties createProperties() {
        final GunProperties properties = super.createProperties();
        //properties.bCannon = false;
        //properties.bUseHookAsRel = true;
        //properties.fire = null;
        //properties.shells = null;
        //properties.sound = "weapon.MiniGun";
        properties.shotFreq = 100.0f;
        //properties.bullets = 8000;
        return properties;
    }
}
