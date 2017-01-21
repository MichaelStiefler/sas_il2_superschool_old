package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombUSStorpMk14Torpex extends USSTorpedo
{
    static Class class$com$maddox$il2$objects$weapons$BombUSStorpMk14Torpex;

    static Class class$(String s)
    {
        Class c;
        try{
            c = Class.forName(s);
        } catch ( ClassNotFoundException e ){
            throw new NoClassDefFoundError(e.getMessage());
        }
        return c;
    }

  static
  {
    	Class class1
	    = (class$com$maddox$il2$objects$weapons$BombUSStorpMk14Torpex == null
	       ? (class$com$maddox$il2$objects$weapons$BombUSStorpMk14Torpex
		  = class$("com.maddox.il2.objects.weapons.BombUSStorpMk14Torpex"))
	       : class$com$maddox$il2$objects$weapons$BombUSStorpMk14Torpex);
      
    Property.set(class1, "mesh", "3do/arms/45-12/mono.sim");

    Property.set(class1, "radius", 18.0F);
    Property.set(class1, "power", 438.0F);
    Property.set(class1, "powerType", 0);
    Property.set(class1, "kalibr", 0.533F);
    Property.set(class1, "massa", 1490.0F);
    Property.set(class1, "sound", "weapon.torpedo");

    Property.set(class1, "velocity", 23.6644444F);
    Property.set(class1, "traveltime", 173.0F);
    Property.set(class1, "startingspeed", 40.0F);
  }
}
