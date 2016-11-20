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
 *
 *      Die Liste wird noch auf übersehene Baustellen erweitert!
 *
 *
 *      ------------------------------------------------------------------------------------------
 **/

public class MIG_7 extends MIG_3 {

    // New Gear Animation Code
    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos) {
        // DONE: Need to pull back L2/R2 a bit to pass clear of CF_D0 on final retract / early extract.
        // Danke Skylla, exzellent!
        Aircraft.ypr[1] = Aircraft.cvt(leftGearPos, 0.01F, 0.89F, 0.0F, 88F);
        if (leftGearPos <= 0.08F) {
            Aircraft.xyz[0] = Aircraft.cvt(leftGearPos, 0.0F, 0.08F, 0.0F, 0.05F);
        } else if (leftGearPos > 0.08F) {
            Aircraft.xyz[0] = Aircraft.cvt(leftGearPos, 0.13F, 0.23F, 0.05F, 0.0F);
        }
        hiermesh.chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
        // DONE: make these move ~ 30 - 40 deg to the back
        hiermesh.chunkSetAngles("GearL3_BASE", 0.0F, Aircraft.cvt(leftGearPos, 0.01F, 0.89F, 0.0F, 82F), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", Aircraft.cvt(leftGearPos, 0.2F, 0.89F, 0.0F, -35F), 0.0F, 0.0F);
        Aircraft.ypr[1] = Aircraft.cvt(rightGearPos, 0.1F, 0.99F, 0.0F, -88F);
        if (rightGearPos <= 0.17F) {
            Aircraft.xyz[0] = Aircraft.cvt(rightGearPos, 0.09F, 0.17F, 0.0F, -0.05F);
        } else if (rightGearPos > 0.17F) {
            Aircraft.xyz[0] = Aircraft.cvt(rightGearPos, 0.22F, 0.32F, -0.05F, 0.0F);
        }
        hiermesh.chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR3_BASE", 0.0F, Aircraft.cvt(rightGearPos, 0.1F, 0.99F, 0.0F, -82F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", Aircraft.cvt(rightGearPos, 0.3F, 0.99F, 0.0F, 35F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(tailWheelPos, 0.4F, 0.89F, 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(tailWheelPos, 0.2F, 0.49F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(tailWheelPos, 0.2F, 0.49F, 0.0F, 80F), 0.0F);
    }

    protected void moveGear(float leftGearPos, float rightGearPos, float tailWheelPos) {
        moveGear(this.hierMesh(), leftGearPos, rightGearPos, tailWheelPos);
    }

    // ************************************************************************************************
    // Gear code for backward compatibility, older base game versions don't indepently move their gears
    public static void moveGear(HierMesh hiermesh, float gearPos) {
        moveGear(hiermesh, gearPos, gearPos, gearPos); // re-route old style function calls to new code
    }

    protected void moveGear(float gearPos) {
        moveGear(this.hierMesh(), gearPos);
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
