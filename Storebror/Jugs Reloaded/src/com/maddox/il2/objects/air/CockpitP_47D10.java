package com.maddox.il2.objects.air;

import com.maddox.il2.fm.FlightModelMain;

public class CockpitP_47D10 extends CockpitP_47DModPack {
	public CockpitP_47D10() {
		super("3DO/Cockpit/P-47D-10/hier.him");
	}

	public void reflectWorldToInstruments(float f) {
		super.reflectWorldToInstruments(f);
		super.mesh.chunkSetAngles("supercharge", 0.0F, this.cvt(this.setNew.supercharge, 0.3F, 0.75F, -8F, 40F), 0.0F);
		super.mesh.chunkSetAngles("throtle", 0.0F, 0.0F, this.cvt(this.setNew.throttle, 0.0F, 1.1F, 0F, -64F));
		super.mesh.chunkSetAngles("zManifold1a", 0.0F, this.cvt(((FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 270F), 0.0F);
	}
}
