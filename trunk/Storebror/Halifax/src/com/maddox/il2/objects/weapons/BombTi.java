package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombTi extends BombTiBase1 {

    public void start() {
        super.start();
        this.t1 = Time.current() + 129500L + World.Rnd().nextLong(0L, 850L);
        this.t2 = Time.current() + 129200L + World.Rnd().nextLong(0L, 3800L);
        Eff3DActor.New(this, null, new Loc(), 1.0F, "3DO/Effects/Fireworks/TIRed.eff", (this.t1 - Time.current()) / 1000F);
    }

    static {
        Class class1 = BombTi.class;
        Property.set(class1, "mesh", "3DO/Arms/15kgFragJ/mono.sim");
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 7.5F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 15F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
