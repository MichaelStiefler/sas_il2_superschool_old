package com.maddox.il2.objects.air;

import com.maddox.rts.Property;
//temp:
import com.maddox.sas1946.il2.util.AircraftTools;

/**
 * @author SAS~Skylla, SAS~Storebror
 * @see CockpitMIG_7.java
 * 
 * Momentan nur das Gerüst, zu mehr keine Zeit heute .. 
 * 
 * air.ini: 
 * 	MIG_7         air.MIG_7 1                 NOINFO  r01  SUMMER 
 * 
 * plane.properties:
 * 
 * 	MIG_7         MiG-7 '46
 * 
 * weapons.properties:
 * 
 *	#####################################################################
 *	# MIG_7
 *	#####################################################################
 *	MIG_7.default         Default (2x 20mm ShVak)
 *	MIG_7.none            None
 * 
 * Ich würde die (Grund)Entwicklung in UP3 bevorzugen, 4.12.2, 4.09 danach.
 * skin1o.tgb passt für uns Clowns natürlich nicht, muss auf 512x512 verkleinert werden.
 * 
 * -----------------------------------------------------------------------------------------
 * 
 * TODO ([-] ausstehend, [x] erledigt):
 * 
 * 	> [-] Cockpitinstrumente 
 * 	> [-] move Cockpit Door
 * 	> [-] Fahrwerkanimation korrigieren
 * 	> [-] Loadouts in cod Formad
 * 	> [-] FlightModel
 * 
 * Die Liste wird noch auf übersehene Baustellen erweitert!
 * 
 * ------------------------------------------------------------------------------------------
**/

public class MIG_7 extends MIG_3{
	
	 static {
	        final Class var_class = MIG_7.class;
	        new SPAWN(var_class);
	        Property.set(var_class, "iconFar_shortClassName", "MiG-7");
	        Property.set(var_class, "meshName", "3DO/Plane/MIG_7/hier.him");
	        Property.set(var_class, "PaintScheme", new PaintSchemeFMPar01());
	        //fictional, also 1946 ..
	        Property.set(var_class, "yearService", 1946.0f);
	        //wäre in Korea noch verwendet worden .. 
	        Property.set(var_class, "yearExpired", 1953.5f); 
	        Property.set(var_class, "FlightModel", "FlightModels/MiG-3ud.fmd");
	        Property.set(var_class, "cockpitClass", new Class[] { CockpitMIG_7.class });
	        //TODO: was macht denn dieser Wert?
	        Property.set(var_class, "LOSElevation", 0.906f);
	        //was soll die Krebserei mit Trigger 1? Es gibt nur eine Waffenart zum feuern, also Trigger 0!
	        Aircraft.weaponTriggersRegister(var_class, new int[] { 0, 0 });
	        Aircraft.weaponHooksRegister(var_class, new String[] { "_MGUN01", "_MGUN02" });
	        AircraftTools.weaponsRegister(var_class, "default", new String[] { "MGunShVAKs 250", "MGunShVAKs 250" });
	        AircraftTools.weaponsRegister(var_class, "none", new String[] { null, null });
	    }

}
