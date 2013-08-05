// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   PaintSchemeSEAGLAD.java

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.HierMesh;

// Referenced classes of package com.maddox.il2.objects.air:
//            PaintSchemeFMPar00, PaintScheme

public class PaintSchemeSEAGLAD extends PaintSchemeFMPar00
{

    public PaintSchemeSEAGLAD()
    {
    }

    public String typedNameNum(Class class1, Regiment regiment, int i, int j, int k)
    {
        if(regiment.country() == PaintScheme.countryBritain)
        {
            k = clampToLiteral(k);
            return "" + (char)(65 + (k - 1));
        } else
        {
            return super.typedNameNum(class1, regiment, i, j, k);
        }
    }

    public void prepareNum(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k)
    {
        super.prepareNum(class1, hiermesh, regiment, i, j, k);
        int l = regiment.gruppeNumber() - 1;
        if(regiment.country() == PaintScheme.countryBritain && ("rn".equals(regiment.branch()) || "ra".equals(regiment.branch()) || "ca".equals(regiment.branch()) || "gb".equals(regiment.branch())))
        {
            k = clampToLiteral(k);
            changeMat(hiermesh, "Overlay1", "psFM02BRINAVYREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2], PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2]);
            changeMat(hiermesh, "Overlay4", "psFM02BRINAVYREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2], PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2]);
            changeMat(hiermesh, "Overlay2", "psSEAGLADBRINAVYLNUM" + l + i + (k < 10 ? "0" + k : "" + k), "British/" + (char)((65 + k) - 1) + ".tga", "null.tga", PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2], PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2]);
            changeMat(hiermesh, "Overlay3", "psSEAGLADBRINAVYRNUM" + l + i + (k < 10 ? "0" + k : "" + k), "null.tga", "British/" + (char)((65 + k) - 1) + ".tga", PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2], PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2]);
            if(class1.getClass().getName().contains("SEAGLADIATOR2"))
            {
                changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
                changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            } else
            {
                k = clampToLiteral(k);
                changeMat(hiermesh, "Overlay1", "psFM02BRINAVYREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2], PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2]);
                changeMat(hiermesh, "Overlay4", "psFM02BRINAVYREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2], PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2]);
                changeMat(hiermesh, "Overlay2", "psSEAGLADBRINAVYLNUM" + l + i + (k < 10 ? "0" + k : "" + k), "British/" + (char)((65 + k) - 1) + ".tga", "null.tga", PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2], PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2]);
                changeMat(hiermesh, "Overlay3", "psSEAGLADBRINAVYRNUM" + l + i + (k < 10 ? "0" + k : "" + k), "null.tga", "British/" + (char)((65 + k) - 1) + ".tga", PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2], PaintScheme.psBritishBlackColor[0], PaintScheme.psBritishBlackColor[1], PaintScheme.psBritishBlackColor[2]);
                if(class1.getClass().getName().contains("SEAGLADIATOR2"))
                {
                    changeMat(class1, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
                    changeMat(class1, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }
}
