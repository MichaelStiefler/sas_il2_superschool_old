package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Eff3DActor;

public abstract class SPITLATEFLAME extends SPITFIRE {

	public SPITLATEFLAME() {
	}

	public void update(float f) {
		super.update(f);
		CombustionFlame();
	}

	private void CombustionFlame() {
		int i = World.Rnd().nextInt(0, 100);
		int j = World.Rnd().nextInt(1, 12);
		switch (i) {
		case 1: // '\001'
			random = "01";
			break;

		case 8: // '\b'
			random = "02";
			break;

		case 14: // '\016'
			random = "03";
			break;

		case 21: // '\025'
			random = "04";
			break;

		case 30: // '\036'
			random = "05";
			break;

		case 38: // '&'
			random = "06";
			break;

		case 44: // ','
			random = "07";
			break;

		case 68: // 'D'
			random = "08";
			break;

		case 70: // 'F'
			random = "09";
			break;

		case 81: // 'Q'
			random = "10";
			break;

		case 89: // 'Y'
			random = "11";
			break;

		case 94: // '^'
			random = "12";
			break;
		}
		switch (j) {
		case 1: // '\001'
			random3 = "01";
			break;

		case 2: // '\002'
			random3 = "03";
			break;

		case 3: // '\003'
			random3 = "05";
			break;

		case 4: // '\004'
			random3 = "07";
			break;

		case 5: // '\005'
			random3 = "09";
			break;

		case 6: // '\006'
			random3 = "11";
			break;

		case 7: // '\007'
			random3 = "02";
			break;

		case 8: // '\b'
			random3 = "04";
			break;

		case 9: // '\t'
			random3 = "06";
			break;

		case 10: // '\n'
			random3 = "08";
			break;

		case 11: // '\013'
			random3 = "10";
			break;

		case 12: // '\f'
			random3 = "12";
			break;
		}
		if (this.FM.EI.engines[0].getStage() == 6 && this.FM.EI.engines[0].getPowerOutput() > 0.85F)
			Eff3DActor.New(this, findHook("_Engine1EF_" + random3), null, 1.0F, "3DO/Effects/Fireworks/HolyGrail2.eff", -1F);
		if (this.FM.EI.engines[0].getStage() > 0 && this.FM.EI.engines[0].getStage() < 3 && super.FM.getSpeedKMH() < 50F)
			Eff3DActor.New(this, findHook("_Engine1EF_" + random), null, 1.0F, "3DO/Effects/Aircraft/HolyGrail1.eff", -1F);
	}

	private String random;
	private String random3;
}
