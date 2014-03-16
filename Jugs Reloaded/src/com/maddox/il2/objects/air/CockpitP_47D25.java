package com.maddox.il2.objects.air;

import com.maddox.il2.fm.FlightModelMain;

public class CockpitP_47D25 extends CockpitP_47DModPack {
	public CockpitP_47D25() {
		super("3DO/Cockpit/P-47D-25/hier.him");
		if (!(aircraft() instanceof P_47ModPackAceMakerGunsight)) {
			this.mesh.chunkVisible("Z_Z_RETICLE1", false);
			for (int i = 1; i < 7; i++)
				this.mesh.chunkVisible("Z_Z_AIMMARK" + i, false);
		}
	}

	public void reflectWorldToInstruments(float f) {
		super.reflectWorldToInstruments(f);
		super.mesh.chunkSetAngles("supercharge", 0.0F, cvt(super.setNew.supercharge, 0.3F, 0.75F, -2F, 44F), 0.0F);
		super.mesh.chunkSetAngles("throtle", 0.0F, 0.0F, cvt(super.setNew.throttle, 0.0F, 1.1F, -6F, -68F));
		super.mesh.chunkSetAngles("zManifold1a", 0.0F, cvt(((FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 210F), 0.0F);
	}
}
