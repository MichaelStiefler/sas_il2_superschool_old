package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
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
 * ------------------------------------------------------------------------------------------
**/

//TODO wär's nicht sinnvoller hier einfach extends MIG_7 zu schreiben? 
public class MIG_11 extends MIG_7 {
/*
    
    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -88F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 40F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -40F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 78F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -78F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 70F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, -80F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 80F * f2, 0.0F);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }
    
    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasCockpitDoorControl = true;
        this.FM.CT.dvCockpitDoor = 1.0F;
    }
	
    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }
*/
    static {
	        final Class aircraftClass = MIG_11.class;
	        new SPAWN(aircraftClass);
	        Property.set(aircraftClass, "iconFar_shortClassName", "MiG-11");
	        Property.set(aircraftClass, "meshName", "3DO/Plane/MiG-11/hier.him");
	        Property.set(aircraftClass, "PaintScheme", new PaintSchemeFMPar01());
	        Property.set(aircraftClass, "yearService", 1944.0f);
	        Property.set(aircraftClass, "yearExpired", 1953.5f);
	        Property.set(aircraftClass, "FlightModel", "FlightModels/MiG-3ud.fmd");
	        Property.set(aircraftClass, "cockpitClass", new Class[] { CockpitMIG_7.class });
	        Property.set(aircraftClass, "LOSElevation", 0.906f);
	        Aircraft.weaponTriggersRegister(aircraftClass, new int[] { 0, 0 });
	        Aircraft.weaponHooksRegister(aircraftClass, new String[] { "_MGUN01", "_MGUN02" });
	        AircraftTools.weaponsRegister(aircraftClass, "default", new String[] { "MGunShVAKs 100", "MGunShVAKs 100" });
	        AircraftTools.weaponsRegister(aircraftClass, "none", new String[] { null, null });
	    }

}
