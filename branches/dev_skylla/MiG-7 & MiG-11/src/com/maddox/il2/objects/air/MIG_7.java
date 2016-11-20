package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

/**
 * @author SAS~Skylla, SAS~Storebror
 * @see CockpitMIG_7.java
 *
 *
 * air.ini: 
 *  MiG-7             air.MIG_7 1                           NOINFO  r01   SUMMER
 * 
 * plane.properties:
 * 
 *  MiG-7         MiG-7 '44
 * 
 * weapons.properties:
 * 
 *  #####################################################################
 *  # MiG-7
 *  #####################################################################
 *  MiG-7.default         Default (2x 20mm ShVak)
 *  MiG-7.none            None
 *
 *
 *      -----------------------------------------------------------------------------------------
 *
 *      TODO ([-] pending, [x] done):
 *
 *      > [x] Cockpitinstrumente
 *      > [x] move Cockpit Door
 *      > [x] Fahrwerkanimation korrigieren
 *      > [x] Loadouts in cod Format
 *      > [x] FlightModel
 *      > [-] fix the jump-on-ground-start bug.
 *
 *      Die Liste wird noch auf übersehene Baustellen erweitert!
 *
 *      ------------------------------------------------------------------------------------------
**/

public class MIG_7 extends MIG_3 {
	
	private float [] rndgear = {0.0F, 0.0F, 0.0F};
	
	public MIG_7() {
		//this will randomize the gear animation for every new spawned plane. 
		//Why? Cause the difference between flight sims and reality are many unecessary details .. (and death, of course :P)
		rndgear[2] = (float)(Math.round(Math.random() * 15) / 100.0);
		rndgear[0] = Math.abs((float)(Math.round(Math.random() * 15) / 100.0)-rndgear[2]);
		rndgear[1] = Math.abs((float)(Math.round(Math.random() * 15) / 100.0)-rndgear[0]);
	}

    // New Gear Animation Code
    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos, float [] rnd) {
        // DONE: Need to pull back L2/R2 a bit to pass clear of CF_D0 on final retract / early extract.
        // Danke Skylla, exzellent!
        Aircraft.ypr[1] = Aircraft.cvt(leftGearPos, rnd[0], 0.85F+rnd[0], 0.0F, 88F);
        if (leftGearPos <= (rnd[0]+0.07F)) {
            Aircraft.xyz[0] = Aircraft.cvt(leftGearPos, rnd[0], rnd[0]+0.07F, 0.0F, 0.05F);
        } else if (leftGearPos > (rnd[0]+0.07F)) {
            Aircraft.xyz[0] = Aircraft.cvt(leftGearPos, rnd[0]+0.12F, rnd[0]+0.22F, 0.05F, 0.0F);
        }
        hiermesh.chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
        
        // DONE: make these move ~ 30 - 40 deg to the back
        //super, vielen Dank!
        hiermesh.chunkSetAngles("GearL3_BASE", 0.0F, Aircraft.cvt(leftGearPos, rnd[0], 0.85F+rnd[0], 0.0F, 82F), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", Aircraft.cvt(leftGearPos, rnd[0]+0.01F, 0.86F+rnd[0], 0.0F, -35F), 0.0F, 0.0F);
        
        Aircraft.ypr[1] = Aircraft.cvt(rightGearPos, rnd[1], 0.85F+rnd[1], 0.0F, -88F);
        if (rightGearPos <= (rnd[1]+0.07F)) {
            Aircraft.xyz[0] = Aircraft.cvt(rightGearPos, rnd[1], rnd[1]+0.07F, 0.0F, -0.05F);
        } else if (rightGearPos > (rnd[1]+0.07F)) {
            Aircraft.xyz[0] = Aircraft.cvt(rightGearPos, rnd[1]+0.12F, rnd[1]+0.22F, -0.05F, 0.0F);
        }
        hiermesh.chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR3_BASE", 0.0F, Aircraft.cvt(rightGearPos, rnd[1], 0.85F+rnd[1], 0.0F, -82F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", Aircraft.cvt(rightGearPos, rnd[1]+0.01F, 0.86F+rnd[1], 0.0F, 35F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(tailWheelPos, rnd[2], rnd[2]+0.85F, 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(tailWheelPos, rnd[2], rnd[2]+0.47F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(tailWheelPos, rnd[2], rnd[2]+0.47F, 0.0F, 80F), 0.0F);
    }

    protected void moveGear(float leftGearPos, float rightGearPos, float tailWheelPos) {
        moveGear(this.hierMesh(), leftGearPos, rightGearPos, tailWheelPos, rndgear);
    }

    // ************************************************************************************************
    // Gear code for backward compatibility, older base game versions don't indepently move their gears
    public static void moveGear(HierMesh hiermesh, float gearPos, float [] rnd) {
        moveGear(hiermesh, gearPos, gearPos, gearPos, rnd); // re-route old style function calls to new code
    }

    protected void moveGear(float gearPos) {
        moveGear(this.hierMesh(), gearPos, rndgear);
    }
    // ************************************************************************************************

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    static {
        final Class aircraftClass = MIG_7.class;
        new SPAWN(aircraftClass);
        Property.set(aircraftClass, "iconFar_shortClassName", "MiG-7");
        Property.set(aircraftClass, "meshName", "3DO/Plane/MiG-7/hier.him");
        Property.set(aircraftClass, "PaintScheme", new PaintSchemeFMPar01());
        // This is the Mikojan-Gurewitsch I-222, maiden flight was on 7 may 1944
        // In contrast to 1946 plane types, _IF_ this plane ever went into service, it would have done so in 1944.
        // The reason for this plane _NOT_ to enter service wasn't an unfinished plane, but a dissolved task.
        Property.set(aircraftClass, "yearService", 1944.0f);
        // Would have been used in Korea as well ..
        Property.set(aircraftClass, "yearExpired", 1953.5f);
        Property.set(aircraftClass, "FlightModel", "FlightModels/MiG-7.fmd:MIG_7_11_FM");
        Property.set(aircraftClass, "cockpitClass", new Class[] { CockpitMIG_7.class });
        Property.set(aircraftClass, "LOSElevation", 0.906f);
        Aircraft.weaponTriggersRegister(aircraftClass, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(aircraftClass, new String[] { "_MGUN01", "_MGUN02" });
    }

}
