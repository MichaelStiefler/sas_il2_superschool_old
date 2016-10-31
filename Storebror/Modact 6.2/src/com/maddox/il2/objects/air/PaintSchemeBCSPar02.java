package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.HierMesh;

public class PaintSchemeBCSPar02 extends PaintSchemeBMPar02 {

	public PaintSchemeBCSPar02() {
	}

	public String typedNameNum(Class class1, Regiment regiment, int i, int j, int k) {
		if (regiment.country() == PaintScheme.countryBritain)
			return ((String)(k < 10 ? "0" + k : "" + k));
		if (regiment.country() == PaintScheme.countryBritainBlue)
			return ((String)(k < 10 ? "0" + k : "" + k));
		else
			return super.typedNameNum(class1, regiment, i, j, k);
	}

	public void prepareNum(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k) {
		super.prepareNum(class1, hiermesh, regiment, i, j, k);
		int l = regiment.gruppeNumber() - 1;
		if (regiment.country() == PaintScheme.countryBritain) {
			changeMat(hiermesh, "Overlay1", "psBM0XUSACNUM" + l + i + (k >= 10 ? "" + k : "0" + k),
					"States/" + k / 10 + ".tga", "States/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(hiermesh, "Overlay4", "psBM0XUSACNUM" + l + i + (k >= 10 ? "" + k : "0" + k),
					"States/" + k / 10 + ".tga", "States/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay6", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryBritainBlue) {
			changeMat(hiermesh, "Overlay1", "psBbM0XUSACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + k / 10
					+ ".tga", "States/" + k % 10 + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
			changeMat(hiermesh, "Overlay4", "psBbM0XUSACNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "States/" + k / 10
					+ ".tga", "States/" + k % 10 + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
			changeMat(class1, hiermesh, "Overlay6", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryPoland) {
			changeMat(hiermesh, "Overlay1", "psBCS02POLCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/1" + k / 10
					+ ".tga", "Russian/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay2", "polishcheckerboard", "Polish/checkerboard.tga", 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryRussia) {
			if (k < 10)
				changeMat(class1, hiermesh, "Overlay1", "psBCS02RUSCNUM" + l + i + "0" + k, "Russian/0" + k + ".tga",
						PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1],
						PaintScheme.psRussianBomberColor[i][2]);
			else
				changeMat(hiermesh, "Overlay1", "psBCS02RUSCNUM" + l + i + k, "Russian/0" + k / 10 + ".tga", "Russian/0" + k
						% 10 + ".tga", PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1],
						PaintScheme.psRussianBomberColor[i][2], PaintScheme.psRussianBomberColor[i][0],
						PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2]);
			changeMat(class1, hiermesh, "Overlay2", "psAVGRUSMARKcolor" + i, "mark.tga",
					PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1],
					PaintScheme.psRussianBomberColor[i][2]);
			changeMat(class1, hiermesh, "Overlay6", "redstar2", "Russian/redstar2.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "redstar2", "Russian/redstar2.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay8", "redstar2", "Russian/redstar2.tga", 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryPRC) {
			changeMat(class1, hiermesh, "Overlay1", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay4", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(hiermesh, "Overlay8", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + k / 10 + ".tga",
					"Default/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryChinaRed) {
			changeMat(class1, hiermesh, "Overlay1", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay4", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(hiermesh, "Overlay8", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + k / 10 + ".tga",
					"Default/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			return;
		} else {
			return;
		}
	}

	public void prepareNumOff(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k) {
		if (regiment.country() == PaintScheme.countryRussia)
			changeMat(class1, hiermesh, "Overlay2", "psAVGRUSMARKcolor" + i, "mark.tga",
					PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1],
					PaintScheme.psRussianBomberColor[i][2]);
	}
}
