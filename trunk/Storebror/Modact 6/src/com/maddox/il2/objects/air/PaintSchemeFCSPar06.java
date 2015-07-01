package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.HierMesh;

public class PaintSchemeFCSPar06 extends PaintSchemeFMPar05 {

	public PaintSchemeFCSPar06() {
	}

	public String typedNameNum(Class class1, Regiment regiment, int i, int j, int k) {
		if (regiment.country() == PaintScheme.countryRussia)
			return ((String)(k >= 10 ? "" + k : "0" + k));
		else
			return super.typedNameNum(class1, regiment, i, j, k);
	}

	public void prepareNum(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k) {
		super.prepareNum(class1, hiermesh, regiment, i, j, k);
		int l = regiment.gruppeNumber() - 1;
		if (regiment.country() == PaintScheme.countryRussia) {
			changeMat(hiermesh, "Overlay1", "psFCS06RUSCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/3" + k / 10
					+ ".tga", "Russian/3" + k % 10 + ".tga", 0.72549F, 0.17647F, 0.10196F, 0.72549F, 0.17647F, 0.10196F);
			changeMat(hiermesh, "Overlay4", "psFCS06RUSCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/3" + k / 10
					+ ".tga", "Russian/3" + k % 10 + ".tga", 0.72549F, 0.17647F, 0.10196F, 0.72549F, 0.17647F, 0.10196F);
			changeMat(class1, hiermesh, "Overlay7", "redstar4", "Russian/redstar4.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay8", "redstar4", "Russian/redstar4.tga", 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryPRC) {
			changeMat(hiermesh, "Overlay1", "psFCS06RUSCNUM" + l + i + (k < 10 ? "0" + k : "" + k), "Russian/3" + k / 10
					+ ".tga", "Russian/3" + k % 10 + ".tga", 0.72549F, 0.17647F, 0.10196F, 0.72549F, 0.17647F, 0.10196F);
			changeMat(hiermesh, "Overlay4", "psFCS06RUSCNUM" + l + i + (k < 10 ? "0" + k : "" + k), "Russian/3" + k / 10
					+ ".tga", "Russian/3" + k % 10 + ".tga", 0.72549F, 0.17647F, 0.10196F, 0.72549F, 0.17647F, 0.10196F);
			changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryChinaRed) {
			changeMat(hiermesh, "Overlay1", "psFCS06RUSCNUM" + l + i + (k < 10 ? "0" + k : "" + k), "Russian/3" + k / 10
					+ ".tga", "Russian/3" + k % 10 + ".tga", 0.72549F, 0.17647F, 0.10196F, 0.72549F, 0.17647F, 0.10196F);
			changeMat(hiermesh, "Overlay4", "psFCS06RUSCNUM" + l + i + (k < 10 ? "0" + k : "" + k), "Russian/3" + k / 10
					+ ".tga", "Russian/3" + k % 10 + ".tga", 0.72549F, 0.17647F, 0.10196F, 0.72549F, 0.17647F, 0.10196F);
			changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			return;
		} else {
			return;
		}
	}
}
