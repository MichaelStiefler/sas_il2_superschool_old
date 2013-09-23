package com.maddox.il2.objects.air;

import java.lang.reflect.Field;

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
		System.out.println(superClass.getName());
		try {
			fkangle1 = superClass.getDeclaredField("kangle1");
			fkangle1.setAccessible(true);
			fkangle2 = superClass.getDeclaredField("kangle2");
			fkangle2.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(float paramFloat) {

		float f = this.FM.EI.engines[0].getControlRadiator();
		float kangle1 = 0.0F;
		
		// Attention: VERY dirty fingers here!
		// We have to use reflection and break Java access modifier rules in order to get access
		// to the PRIVATE fields "kangle1" and "kangle2" of MOSQUITO abstract base class.
		// Not only do we access these fields, we even alter their values.
		// Children should do this under parents supervision only!
		try {
			kangle1 = fkangle1.getFloat(this);
			if (Math.abs(kangle1 - f) > 0.01F) {
				fkangle1.setFloat(this, f);
				hierMesh().chunkSetAngles("WaterL_D0", 0.0F, -20.0F * f, 0.0F);
			}

			f = this.FM.EI.engines[1].getControlRadiator();
			float kangle2 = 0.0F;
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
}
