
package com.maddox.il2.objects.weapons;

import com.maddox.rts.*;
import java.io.IOException;


public class BombGBU32_Mk83JDAM_gn16 extends BombUSGPS_JDAM_Generic_gn16
{

    public BombGBU32_Mk83JDAM_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGBU32_Mk83JDAM_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/GBU32_Mk83JDAM_gn16/mono.sim");
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "emitLen", 0.0F);
        Property.set(class1, "emitMax", 0.0F);
        Property.set(class1, "radius", 150F);
        Property.set(class1, "timeLife", 120F);
        Property.set(class1, "timeFire", 1F);
        Property.set(class1, "force", 2.0F);
        Property.set(class1, "power", 275F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 498F);
        Property.set(class1, "massaEnd", 498F);
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_M115.class, com.maddox.il2.objects.weapons.Fuze_M112.class
        })));
        Spawn.add(class1, new SPAWN());
    }

}
