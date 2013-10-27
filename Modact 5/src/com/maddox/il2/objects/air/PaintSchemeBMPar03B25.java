// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   PaintSchemeBMPar03B25.java

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.HierMesh;

// Referenced classes of package com.maddox.il2.objects.air:
//            PaintSchemeBMPar03, PaintScheme

public class PaintSchemeBMPar03B25 extends PaintSchemeBMPar03
{

    public PaintSchemeBMPar03B25()
    {
    }

    public String typedNameNum(Class class1, Regiment regiment, int i, int j, int k)
    {
        if(regiment.country() == PaintScheme.countryUSA)
            return (k >= 10 ? "" + k : "0" + k) + "*";
        if(regiment.country() == PaintScheme.countryUSABlue)
            return (k >= 10 ? "" + k : "0" + k) + "*";
        else
            return super.typedNameNum(class1, regiment, i, j, k);
    }

    public void prepareNum(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k)
    {
        super.prepareNum(class1, hiermesh, regiment, i, j, k);
        int l = regiment.gruppeNumber() - 1;
        if(regiment.country() == PaintScheme.countryUSA)
        {
            changeMat(hiermesh, "Overlay1", "psBM0DUSACNUM" + l + i + (k < 10 ? "0" + k : "" + k), "States/" + k / 10 + ".tga", "States/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            changeMat(hiermesh, "Overlay4", "psBM0DUSACNUM" + l + i + (k < 10 ? "0" + k : "" + k), "States/" + k / 10 + ".tga", "States/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            changeMat(class1, hiermesh, "Overlay1", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            changeMat(class1, hiermesh, "Overlay4", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            return;
        }
        if(regiment.country() == PaintScheme.countryUSABlue)
        {
            changeMat(hiermesh, "Overlay1", "psBM0DUSACNUM" + l + i + (k < 10 ? "0" + k : "" + k), "States/" + k / 10 + ".tga", "States/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            changeMat(hiermesh, "Overlay4", "psBM0DUSACNUM" + l + i + (k < 10 ? "0" + k : "" + k), "States/" + k / 10 + ".tga", "States/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            changeMat(class1, hiermesh, "Overlay1", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            changeMat(class1, hiermesh, "Overlay4", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            return;
        } else
        {
            return;
        }
    }
}
