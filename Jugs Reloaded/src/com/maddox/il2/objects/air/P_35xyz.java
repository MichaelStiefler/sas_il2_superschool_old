package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public abstract class P_35xyz extends RE_2000xyz {

	public void update(float f) {
		super.update(f);
		if ((FM instanceof RealFlightModel) && ((RealFlightModel) FM).isRealMode()) {
			float f2 = FM.EI.engines[0].getRPM();
			if (f2 < 1000F && f2 > 100F)
				((RealFlightModel) FM).producedShakeLevel = (1000F - f2) / 8000F;
		}
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();

		if (this.thisWeaponsName.indexOf("2x") != -1) {
			this.hierMesh().chunkVisible("RackL_D0", true);
			this.hierMesh().chunkVisible("RackR_D0", true);
		} else {
			this.hierMesh().chunkVisible("RackL_D0", false);
			this.hierMesh().chunkVisible("RackR_D0", false);
		}
	}

	protected void moveFan(float f) {
		if (!Config.isUSE_RENDER())
			return;
		int i = FM.EI.engines[0].getStage();
		if (i > 0 && i < 6)
			f = 0.005F * (float) i;
		super.moveFan(f);
		hierMesh().chunkSetAngles(Aircraft.Props[0][0], 0.0F, -propPos[0] + 45F, 0.0F);
	}

	private static boolean _DEBUG = false;

	protected static void printDebugMessage(String theMessage) {
		if (_DEBUG)
			System.out.println(theMessage);
	}

	static {
		Property.set(P_35xyz.class, "originCountry", PaintScheme.countryUSA);
	}
}
