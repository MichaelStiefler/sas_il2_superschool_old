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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        xyz[1] = cvt(f, 0.01F, 0.99F, 0.0F, 0.6F);
        xyz[2] = cvt(f, 0.01F, 0.8F, 0.0F, 0.025F);
        ypr[2] = cvt(f, 0.01F, 0.6F, 0.0F, 3.5F);
        hierMesh().chunkSetLocate("Blister1_D0", xyz, ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, cvt(f2, 0.0F, 0.4F, 0.0F, -80F), 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.21F, 0.89F, 0.0F, -114F), 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.21F, 0.89F, 0.0F, -59F), 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f, 0.21F, 0.89F, 0.0F, -116.5F), 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearL7_D0", 0.0F, cvt(f, 0.01F, 0.6F, 0.0F, -72F), 0.0F);
		hiermesh.chunkSetAngles("GearL8_D0", 0.0F, cvt(f, 0.01F, 0.6F, 0.0F, -68F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f1, 0.31F, 0.99F, 0.0F, -114F), 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f1, 0.31F, 0.99F, 0.0F, -59F), 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f1, 0.31F, 0.99F, 0.0F, -116.5F), 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearR7_D0", 0.0F, cvt(f1, 0.11F, 0.7F, 0.0F, -72F), 0.0F);
		hiermesh.chunkSetAngles("GearR8_D0", 0.0F, cvt(f1, 0.11F, 0.7F, 0.0F, -68F), 0.0F);
	}

	protected void moveGear(float f, float f1, float f2) {
		moveGear(hierMesh(), f, f1, f2);
	}

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
			if (Mission.getMissionDate(true) < 19440606)
				return "PreInvasion_";
		}
		return "";
	}
}
