// This file is part of the SAS IL-2 Sturmovik 1946
// Westland Whirlwind Mod.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Mod Creator:          tfs101
// Original file source: SAS~S3
// Modified by:          SAS - Special Aircraft Services
//                       www.sas1946.com
//
// Last Edited by:       SAS~Storebror
// Last Edited at:       2013/09/29

package com.maddox.il2.objects.air;

import java.lang.reflect.Field;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;

public abstract class Whirlwind extends MOSQUITO {

	// Declare anonymous Fields for "kangle1" and "kangle2" properties of MOSQUITO abstract base class,
	// we need to access these fields later in order to manipulate the radiator controls and kick
	// out base classes' code.
	private Field fkangle1;
	private Field fkangle2;
	private static boolean initLogWritten = false;

	public Whirlwind() {
		// Attention: VERY dirty fingers here!
		// We have to use reflection and break Java access modifier rules in order to get access
		// to the PRIVATE fields "kangle1" and "kangle2" of MOSQUITO abstract base class.
		// Children should do this under parents supervision only!
		Class superClass = this.getClass().getSuperclass().getSuperclass();
		try {
			fkangle1 = superClass.getDeclaredField("kangle1");
			fkangle1.setAccessible(true);
			fkangle2 = superClass.getDeclaredField("kangle2");
			fkangle2.setAccessible(true);
			if (!initLogWritten) {
				System.out.println("Whirlwind radiator flaps fields accessibility set successfully.");
				initLogWritten = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		
		// Try to set per-gear values for Gear Period. This will work in 4.12+ game versions only.
		// This step is required for 4.12 and 4.12.1 since TD forgot to reflect changes of
		// Gear Period parameter to the per-gear specific fields.
		// We have to touch these fields through reflection since 4.11.1m and older games are missing
		// the per-gear dvGear fields in Controls class.
		try {
			Class controlsClass = this.FM.CT.getClass();
			Field dvGearField = controlsClass.getField("dvGearL");
			dvGearField.setFloat(this, this.FM.CT.dvGear * 0.95F);
			dvGearField = controlsClass.getField("dvGearR");
			dvGearField.setFloat(this, this.FM.CT.dvGear * 1.05F);
			dvGearField = controlsClass.getField("dvGearC");
			dvGearField.setFloat(this, this.FM.CT.dvGear * 1.8F);
		} catch (Exception e) { // Game is 4.11.1m or older, fall through...
		}
	}
	
	// ************************************************************************************************
	// Overwritten Super Classes, this is to ensure error-free log output since the base class
	// "MOSQUITO" adresses several meshes which are not part of the Whirlie (e.g. Co-Pilot etc.)
    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
    }

    protected void moveBayDoor(float f) {}
    
    public void doMurderPilot(int i)
    {
        if(i == 0)
        {
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
    	if (i==13) return false;
        return super.cutFM(i, j, actor);
    }
	// ************************************************************************************************

    public void moveCockpitDoor(float f) {
		resetYPRmodifier();
		xyz[1] = cvt(f, 0.01F, 0.99F, 0.0F, 0.6F);  // Move Cockpit door backwards by 0.6 units
		xyz[2] = cvt(f, 0.01F, 0.8F, 0.0F, 0.025F); // On the first 8/10th of opening, move Cockpit door upwards by 0.025 units
		ypr[2] = cvt(f, 0.01F, 0.6F, 0.0F, 3.5F);   // On the first 6/10th of opening, tilt Cockpit door backwards by 3.5 degrees
		hierMesh().chunkSetLocate("Blister1_D0", xyz, ypr);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);                                 // Tail Wheel Direction
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, cvt(f2, 0.25F, 0.99F, 0.0F, -80F), 0.0F);    // Tail Wheel
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f2, 0.01F, 0.3F, 0.0F, -70F), 0.0F);     // Tail Wheel Door 1
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, cvt(f2, 0.11F, 0.35F, 0.0F, -70F), 0.0F);    // Tail Wheel Door 2
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.35F, 0.89F, 0.0F, -114F), 0.0F);    // Left Wheel Part 1
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.35F, 0.89F, 0.0F, -59F), 0.0F);     // Left Wheel Part 2
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f, 0.35F, 0.89F, 0.0F, -116.5F), 0.0F);  // Left Wheel Part 3
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 0.0F, 0.0F);                                 // Left Wheel Direction
		hiermesh.chunkSetAngles("GearL7_D0", 0.0F, cvt(f, 0.01F, 0.4F, 0.0F, -72F), 0.0F);      // Left Wheel Door 1
		hiermesh.chunkSetAngles("GearL8_D0", 0.0F, cvt(f, 0.01F, 0.4F, 0.0F, -68F), 0.0F);      // Left Wheel Door 2
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f1, 0.45F, 0.99F, 0.0F, -114F), 0.0F);   // Right Wheel Part 1
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f1, 0.45F, 0.99F, 0.0F, -59F), 0.0F);    // Right Wheel Part 2
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f1, 0.45F, 0.99F, 0.0F, -116.5F), 0.0F); // Right Wheel Part 3
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 0.0F, 0.0F);                                 // Right Wheel Direction
		hiermesh.chunkSetAngles("GearR7_D0", 0.0F, cvt(f1, 0.11F, 0.5F, 0.0F, -72F), 0.0F);     // Right Wheel Door 1
		hiermesh.chunkSetAngles("GearR8_D0", 0.0F, cvt(f1, 0.11F, 0.5F, 0.0F, -68F), 0.0F);     // Right Wheel Door 2
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
	}

	// ************************************************************************************************
	// Gear code for backward compatibility, older base game versions don't indepently move their gears
	public static void moveGear(HierMesh hiermesh, float f) {
		moveGear(hiermesh, f, f, f); // re-route old style function calls to new code
	}

	protected void moveGear(float F) {
		moveGear(hierMesh(), F);
	}
	// ************************************************************************************************

	public void update(float paramFloat) {

		float f, kangle1, kangle2;

		// Attention: VERY dirty fingers here!
		// We have to use reflection and break Java access modifier rules in order to get access
		// to the PRIVATE fields "kangle1" and "kangle2" of MOSQUITO abstract base class.
		// Not only do we access these fields, we even alter their values.
		// Children should do this under parental supervision only!
		try {
			f = this.FM.EI.engines[0].getControlRadiator();
			kangle1 = fkangle1.getFloat(this);
			if (Math.abs(kangle1 - f) > 0.01F) {
				fkangle1.setFloat(this, f);
				hierMesh().chunkSetAngles("WaterL_D0", 0.0F, -20.0F * f, 0.0F);
			}

			f = this.FM.EI.engines[1].getControlRadiator();
			kangle2 = fkangle2.getFloat(this);
			if (Math.abs(kangle2 - f) > 0.01F) {
				fkangle2.setFloat(this, f);
				hierMesh().chunkSetAngles("WaterR_D0", 0.0F, -20.0F * f, 0.0F);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.update(paramFloat);
	}

	public static String getSkinPrefix(String s, Regiment regiment) {
		if (regiment == null || regiment.country() == null)
			return "";
		if (regiment.country().equals("gb")) {
			// Usually IL-2 uses default skins for "post invasion" and special
			// ones for "pre invasion", here we swap this so that per default
			// the Whirlie uses "pre invasion" skins in order to ensure
			// usage of "pre invasion" skin on older game versions which
			// can't handle multiple default skins according to mission date
			if (Mission.getMissionDate(true) > 19440605) // July 6th 1945 or later
				return "PostInvasion_";                  // use invasion stripes
		}
		return "";
	}
}
