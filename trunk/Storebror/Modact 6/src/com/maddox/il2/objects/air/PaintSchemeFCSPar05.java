package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Mission;

public class PaintSchemeFCSPar05 extends PaintSchemeFMPar05 {

	public PaintSchemeFCSPar05() {
	}

	public String typedNameNum(Class class1, Regiment regiment, int i, int j, int k) {
		if (regiment.country() == PaintScheme.countryFrance)
			if (regiment.fileName().equals("PaintSchemes/Red/NN"))
				return (k >= 10 ? "" + k : "0" + k) + " *";
			else
				return super.typedNameNum(class1, regiment, i, j, k);
		if (regiment.country() == PaintScheme.countryRussia)
			return ((String)(k >= 10 ? "" + k : "0" + k));
		if (regiment.country() == PaintScheme.countryUSA) {
			k = clampToLiteral(k);
			return regiment.id() + " - " + (char)(65 + (k - 1));
		}
		if (regiment.country() == PaintScheme.countryUSABlue) {
			k = clampToLiteral(k);
			return regiment.id() + " - " + (char)(65 + (k - 1));
		} else {
			return super.typedNameNum(class1, regiment, i, j, k);
		}
	}

	public void prepareNum(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k) {
		super.prepareNum(class1, hiermesh, regiment, i, j, k);
		int l = regiment.gruppeNumber() - 1;
		if (regiment.country() == PaintScheme.countryFrance) {
			if (regiment.fileName().equals("PaintSchemes/Red/NN")) {
				changeMat(hiermesh, "Overlay1", "psFCS05FRACNUM" + l + i + k, "Russian/1" + k / 10 + ".tga", "Russian/1" + k
						% 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
				changeMat(class1, hiermesh, "Overlay2", "null", "null.tga", 1.0F, 1.0F, 1.0F);
				changeMat(class1, hiermesh, "Overlay3", "null", "null.tga", 1.0F, 1.0F, 1.0F);
				changeMat(hiermesh, "Overlay4", "psFCS05FRACNUM" + l + i + k, "Russian/1" + k / 10 + ".tga", "Russian/1" + k
						% 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
				changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
				changeMat(class1, hiermesh, "Overlay7", "redstar3", "Russian/redstar3.tga", 1.0F, 1.0F, 1.0F);
				changeMat(class1, hiermesh, "Overlay8", "redstar3", "Russian/redstar3.tga", 1.0F, 1.0F, 1.0F);
			} else {
				if (class1.isAssignableFrom(com.maddox.il2.objects.air.YAK_1B.class)
						|| class1.isAssignableFrom(com.maddox.il2.objects.air.YAK_3.class)
						|| class1.isAssignableFrom(com.maddox.il2.objects.air.YAK_9T.class))
					changeMat(hiermesh, "Overlay1", "psFCS05FRACNUM" + l + i + k, "Russian/1" + k / 10 + ".tga", "Russian/1"
							+ k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
				return;
			}
			return;
		}
		if (regiment.country() == PaintScheme.countryJapan) {
			changeMat(hiermesh, "Overlay1", "psFCS05JAPCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/1" + k / 10
					+ ".tga", "Russian/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(hiermesh, "Overlay4", "psFCS05JAPCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/1" + k / 10
					+ ".tga", "Russian/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay6", "JAR2", "Japanese/JAR2.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "JAR1", "Japanese/JAR.tga", 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryRussia) {
			changeMat(hiermesh, "Overlay1", "psFCS05RUSCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/2" + k / 10
					+ ".tga", "Russian/2" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(hiermesh, "Overlay4", "psFCS05RUSCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/2" + k / 10
					+ ".tga", "Russian/2" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "redstar3", "Russian/redstar3.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay8", "redstar3", "Russian/redstar3.tga", 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryUSA) {
			float f = 0.21F * psBritishSkyColor[0];
			float f1 = 0.21F * psBritishSkyColor[1];
			float f2 = 0.21F * psBritishSkyColor[2];
			String s = "";
			int i1 = Mission.getMissionDate(true);
			if (i1 < 0x128a3de && i1 > 0 && !regiment.name().equals("15AF_332FG_099FS")
					&& !regiment.name().startsWith("8AF_359FG") && !regiment.name().startsWith("8AF_352FG")
					&& ((P_51B.class).isAssignableFrom(class1) || (P_51C.class).isAssignableFrom(class1))) {
				f = 1.0F;
				f1 = 1.0F;
				f2 = 1.0F;
				s = "Wht";
			}
			k = clampToLiteral(k);
			changeMat(class1, hiermesh, "Overlay1", "psFCS05USAREGI" + regiment.id() + s, "British/" + regiment.aid()[0]
					+ ".tga", "British/" + regiment.aid()[1] + ".tga", f, f1, f2, f, f1, f2);
			changeMat(class1, hiermesh, "Overlay4", "psFCS05USAREGI" + regiment.id() + s, "British/" + regiment.aid()[0]
					+ ".tga", "British/" + regiment.aid()[1] + ".tga", f, f1, f2, f, f1, f2);
			changeMat(class1, hiermesh, "Overlay2", "psFCS05USALNUM" + l + i + (k >= 10 ? "" + k : "0" + k) + s, "British/"
					+ (char)((65 + k) - 1) + ".tga", "null.tga", f, f1, f2, f, f1, f2);
			changeMat(class1, hiermesh, "Overlay3", "psFCS05USARNUM" + l + i + (k >= 10 ? "" + k : "0" + k) + s, "null.tga",
					"British/" + (char)((65 + k) - 1) + ".tga", f, f1, f2, f, f1, f2);
		}
		if (regiment.country() == PaintScheme.countryUSABlue) {
			k = clampToLiteral(k);
			changeMat(hiermesh, "Overlay1", "psFCS05USAREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga",
					"British/" + regiment.aid()[1] + ".tga", 0.21F * PaintScheme.psBritishSkyColor[0],
					0.21F * PaintScheme.psBritishSkyColor[1], 0.21F * PaintScheme.psBritishSkyColor[2],
					0.21F * PaintScheme.psBritishSkyColor[0], 0.21F * PaintScheme.psBritishSkyColor[1],
					0.21F * PaintScheme.psBritishSkyColor[2]);
			changeMat(hiermesh, "Overlay4", "psFCS05USAREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga",
					"British/" + regiment.aid()[1] + ".tga", 0.21F * PaintScheme.psBritishSkyColor[0],
					0.21F * PaintScheme.psBritishSkyColor[1], 0.21F * PaintScheme.psBritishSkyColor[2],
					0.21F * PaintScheme.psBritishSkyColor[0], 0.21F * PaintScheme.psBritishSkyColor[1],
					0.21F * PaintScheme.psBritishSkyColor[2]);
			changeMat(hiermesh, "Overlay2", "psFCS05USALNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "British/"
					+ (char)((65 + k) - 1) + ".tga", "null.tga", 0.21F * PaintScheme.psBritishSkyColor[0],
					0.21F * PaintScheme.psBritishSkyColor[1], 0.21F * PaintScheme.psBritishSkyColor[2], 1.0F, 1.0F, 1.0F);
			changeMat(hiermesh, "Overlay3", "psFCS05USARNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "null.tga", "British/"
					+ (char)((65 + k) - 1) + ".tga", 1.0F, 1.0F, 1.0F, 0.21F * PaintScheme.psBritishSkyColor[0],
					0.21F * PaintScheme.psBritishSkyColor[1], 0.21F * PaintScheme.psBritishSkyColor[2]);
		}
		if (regiment.country() == PaintScheme.countryBulgariaAllied) {
			changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + k / 10 + ".tga",
					"Default/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + k / 10 + ".tga",
					"Default/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay8", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryPRC) {
			changeMat(hiermesh, "Overlay1", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + k / 10 + ".tga",
					"Default/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(hiermesh, "Overlay4", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + k / 10 + ".tga",
					"Default/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryChinaRed) {
			changeMat(hiermesh, "Overlay1", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + k / 10 + ".tga",
					"Default/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(hiermesh, "Overlay4", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + k / 10 + ".tga",
					"Default/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryThailand) {
			changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + k / 10 + ".tga",
					"Default/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + k / 10 + ".tga",
					"Default/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay1", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay4", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay8", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryThailandBlue) {
			changeMat(hiermesh, "Overlay2", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + k / 10 + ".tga",
					"Default/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(hiermesh, "Overlay3", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + k / 10 + ".tga",
					"Default/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay1", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay4", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay8", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			return;
		}
		if (regiment.country() == PaintScheme.countryManchukuo) {
			changeMat(class1, hiermesh, "Overlay2", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay3", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
			changeMat(hiermesh, "Overlay8", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + k / 10 + ".tga",
					"Default/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
			return;
		} else {
			return;
		}
	}
}
