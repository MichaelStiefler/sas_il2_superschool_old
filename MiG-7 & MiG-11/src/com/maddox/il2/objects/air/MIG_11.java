package com.maddox.il2.objects.air;

import com.maddox.rts.Property;
//temp:
import com.maddox.sas1946.il2.util.AircraftTools;

/**
 * @author SAS~Skylla, SAS~Storebror
 * @see CockpitMIG_7.java, MIG_7.java
 * 
 * air.ini: 
 * 	MiG-11            air.MIG_11 1                          NOINFO  r01   SUMMER
 * 
 * plane.properties:
 * 
 * 	MIG-11        MiG-11 '44
 * 
 * weapons.properties:
 * 
 *	#####################################################################
 *	# MIG-11
 *	#####################################################################
 *	MIG-11.default         Default (2x 20mm ShVak)
 *	MIG-11.none            None
 * 
 * Ich würde die (Grund)Entwicklung in UP3 bevorzugen, 4.12.2, 4.09 danach.
 * skin1o.tgb passt für uns Clowns natürlich nicht, muss auf 512x512 verkleinert werden.
 * 
 * -----------------------------------------------------------------------------------------
 * 
 * TODO ([-] ausstehend, [x] erledigt):
 * 
 * 	> [x] Cockpitinstrumente 
 * 	> [x] move Cockpit Door
 * 	> [-] Fahrwerkanimation korrigieren
 * 	> [-] Loadouts in cod Format
 * 	> [x] FlightModel
 * 
 * Die Liste wird noch auf übersehene Baustellen erweitert!
 * 
 * 
 *
 * ------------------------------------------------------------------------------------------
**/

//TODO wär's nicht sinnvoller hier einfach extends MIG_7 zu schreiben?
// Vollkommen richtig.
public class MIG_11 extends MIG_7 {

    static {
	        final Class aircraftClass = MIG_11.class;
	        new SPAWN(aircraftClass);
	        Property.set(aircraftClass, "iconFar_shortClassName", "MiG-11");
	        Property.set(aircraftClass, "meshName", "3DO/Plane/MiG-11/hier.him");
	        Property.set(aircraftClass, "PaintScheme", new PaintSchemeFMPar01());
	        Property.set(aircraftClass, "yearService", 1944.0f);
	        Property.set(aircraftClass, "yearExpired", 1953.5f);
//	        Property.set(aircraftClass, "FlightModel", "FlightModels/MiG-3ud.fmd");
            Property.set(aircraftClass, "FlightModel", "FlightModels/MiG-11.fmd:MIG_7_11_FM");
	        Property.set(aircraftClass, "cockpitClass", new Class[] { CockpitMIG_7.class });
	        Property.set(aircraftClass, "LOSElevation", 0.906f);
	        Aircraft.weaponTriggersRegister(aircraftClass, new int[] { 0, 0 });
	        Aircraft.weaponHooksRegister(aircraftClass, new String[] { "_MGUN01", "_MGUN02" });
	        AircraftTools.weaponsRegister(aircraftClass, "default", new String[] { "MGunShVAKs 100", "MGunShVAKs 100" });
	        AircraftTools.weaponsRegister(aircraftClass, "none", new String[] { null, null });
	    }
}
