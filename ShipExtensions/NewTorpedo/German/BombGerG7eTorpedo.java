package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGerG7eTorpedo extends GerTorpedo_e
{
    static Class class$com$maddox$il2$objects$weapons$BombGerG7eTorpedo;

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
	    = (class$com$maddox$il2$objects$weapons$BombGerG7eTorpedo == null
	       ? (class$com$maddox$il2$objects$weapons$BombGerG7eTorpedo
		  = class$("com.maddox.il2.objects.weapons.BombGerG7eTorpedo"))
	       : class$com$maddox$il2$objects$weapons$BombGerG7eTorpedo);
      
        Property.set(class1, "mesh", "3do/arms/45-12/mono.sim");

        Property.set(class1, "radius", 18.0F);
        Property.set(class1, "power", 140.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.533F);
        Property.set(class1, "massa", 711.0F);
        Property.set(class1, "sound", "weapon.torpedo");

        Property.set(class1, "velocity", 15.4333F);
        Property.set(class1, "traveltime", 324.0F);
        Property.set(class1, "startingspeed", 40.0F);
  }
}
