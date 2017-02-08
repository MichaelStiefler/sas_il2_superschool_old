package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.HierMesh;

public class PaintSchemeBCSPar01 extends PaintSchemeBMPar01 {

    public PaintSchemeBCSPar01() {
    }

    public String typedNameNum(Class class1, Regiment regiment, int i, int j, int k) {
        if (regiment.country() == PaintScheme.countryBritain) {
            return "" + (k % 10) + regiment.aid()[1];
        }
        if (regiment.country() == PaintScheme.countryBritainBlue) {
            return "" + (k % 10) + regiment.aid()[1];
        } else {
            return super.typedNameNum(class1, regiment, i, j, k);
        }
    }

    public void prepareNum(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k) {
        super.prepareNum(class1, hiermesh, regiment, i, j, k);
        int l = regiment.gruppeNumber() - 1;
        if (regiment.country() == PaintScheme.countryBritain) {
            this.changeMat(hiermesh, "Overlay1", "psBCS01BRINUM" + (k % 10) + regiment.aid()[1], "British/" + (k % 10) + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishGrayColor[0], PaintScheme.psBritishGrayColor[1], PaintScheme.psBritishGrayColor[2], PaintScheme.psBritishGrayColor[0], PaintScheme.psBritishGrayColor[1], PaintScheme.psBritishGrayColor[2]);
        }
        if (regiment.country() == PaintScheme.countryBritainBlue) {
            this.changeMat(hiermesh, "Overlay1", "psbBCS01BRINUM" + (k % 10) + regiment.aid()[1], "British/" + (k % 10) + ".tga", "British/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
        }
        if (regiment.country() == PaintScheme.countryJapan) {
            this.changeMat(hiermesh, "Overlay1", "psBCS01JAPREDNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.6901961F, 0.1568628F, 0.1098039F, 0.6901961F, 0.1568628F, 0.1098039F);
            this.changeMat(hiermesh, "Overlay2", "psBCS01JAPWHTNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "German/" + (k / 10) + ".tga", "German/" + (k % 10) + ".tga", 0.95F, 0.95F, 0.95F, 0.95F, 0.95F, 0.95F);
        }
        if (regiment.country() == PaintScheme.countryRussia) {
            this.changeMat(hiermesh, "Overlay1", "psBCS01RUSCNUM" + l + i + (k >= 10 ? "" + k : "0" + k), "Russian/1" + (k / 10) + ".tga", "Russian/1" + (k % 10) + ".tga", PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2], PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2]);
            this.changeMat(class1, hiermesh, "Overlay2", "psAVGRUSMARKcolor" + i, "mark.tga", PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2]);
            this.changeMat(class1, hiermesh, "Overlay6", "redstar1", "Russian/redstar1.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "redstar1", "Russian/redstar1.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay8", "redstar1", "Russian/redstar1.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countryChina) {
            this.changeMat(class1, hiermesh, "Overlay2", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay3", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay8", "BlackNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
        if (regiment.country() == PaintScheme.countrySpainRep) {
            this.changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            this.changeMat(hiermesh, "Overlay8", "DefaultNum_" + l + i + (k < 10 ? "0" + k : "" + k), "Default/1" + (k / 10) + ".tga", "Default/1" + (k % 10) + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            return;
        } else {
            return;
        }
    }

    public void prepareNumOff(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k) {
        if (regiment.country() == PaintScheme.countryRussia) {
            this.changeMat(class1, hiermesh, "Overlay2", "psAVGRUSMARKcolor" + i, "mark.tga", PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2]);
        }
    }
}
