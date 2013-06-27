// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   PaintSchemeBMPar02f.java

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;

// Referenced classes of package com.maddox.il2.objects.air:
//            PaintScheme

public class PaintSchemeBMPar02f extends PaintScheme
{

    public PaintSchemeBMPar02f()
    {
    }

    public String typedNameNum(Class class1, Regiment regiment, int i, int j, int k)
    {
        if(regiment.country() == PaintScheme.countryGermany)
        {
            int l = regiment.gruppeNumber() - 1;
            if(k < 1)
                k = 1;
            return regiment.id() + " + " + (char)(65 + (k % 26 - 1)) + PaintScheme.psGermanBomberLetter[l][i];
        }
        if(regiment.country() == PaintScheme.countryNetherlands)
            return "" + k;
        if(regiment.country() == PaintScheme.countryFinland)
            return PaintScheme.psFinnishFighterString[1][i] + k;
        if(regiment.country() == PaintScheme.countryFrance)
            return "o " + k;
        if(regiment.country() == PaintScheme.countryBritain)
        {
            k = clampToLiteral(k);
            return regiment.id() + " - " + (char)(65 + (k - 1));
        }
        if(regiment.country() == PaintScheme.countryBritain)
            return regiment.id() + " + " + (k >= 10 ? "" + k : "0" + k);
        if(regiment.country() == PaintScheme.countryItaly)
            return "" + k;
        if(regiment.country() == PaintScheme.countryJapan)
            return "" + k;
        if(regiment.country() == PaintScheme.countryPoland)
            return ((String) (k >= 10 ? "" + k : "0" + k));
        if(regiment.country() == PaintScheme.countryRomania)
            return "+ " + (k >= 10 ? "" + k : "0" + k);
        if(regiment.country() == PaintScheme.countryRussia)
            return "* " + PaintScheme.psRussianBomberString[i] + " " + k;
        if(regiment.country() == PaintScheme.countryNewZealand)
        {
            k = clampToLiteral(k);
            return regiment.id() + " - " + (char)(65 + (k - 1));
        }
        if(regiment.country() == PaintScheme.countrySlovakia)
            return "+ " + k;
        if(regiment.country() == PaintScheme.countryUSA)
            return (k >= 10 ? "" + k : "0" + k) + "*";
        else
            return super.typedNameNum(class1, regiment, i, j, k);
    }

    public void prepareNum(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k)
    {
        super.prepareNum(class1, hiermesh, regiment, i, j, k);
        int l = regiment.gruppeNumber() - 1;
        if(regiment.country() == PaintScheme.countryGermany)
        {
            k = clampToLiteral(k);
            changeMat(hiermesh, "Overlay1", "psBM02GERREGI" + regiment.id(), "German/" + regiment.aid()[0] + ".tga", "German/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            changeMat(hiermesh, "Overlay3", "psBM02GERREGI" + regiment.id(), "German/" + regiment.aid()[0] + ".tga", "German/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            changeMat(hiermesh, "Overlay2", "psBM02GERCNUM" + l + i + (k < 10 ? "0" + k : "" + k), "German/" + (char)(65 + (k - 1)) + ".tga", "German/" + PaintScheme.psGermanBomberLetter[l][i] + ".tga", PaintScheme.psGermanBomberColor[i][0], PaintScheme.psGermanBomberColor[i][1], PaintScheme.psGermanBomberColor[i][2], 0.1F, 0.1F, 0.1F);
            changeMat(hiermesh, "Overlay4", "psBM02GERCNUM" + l + i + (k < 10 ? "0" + k : "" + k), "German/" + (char)(65 + (k - 1)) + ".tga", "German/" + PaintScheme.psGermanBomberLetter[l][i] + ".tga", PaintScheme.psGermanBomberColor[i][0], PaintScheme.psGermanBomberColor[i][1], PaintScheme.psGermanBomberColor[i][2], 0.1F, 0.1F, 0.1F);
            changeMat(class1, hiermesh, "Overlay6", "balken0", "German/balken0.tga", 1.0F, 1.0F, 1.0F);
            changeMat(class1, hiermesh, "Overlay7", "balken1", "German/balken1.tga", 1.0F, 1.0F, 1.0F);
            changeMat(class1, hiermesh, "Overlay8", "haken1", "German/" + (World.cur().isHakenAllowed() ? "haken1.tga" : "hakenfake.tga"), 1.0F, 1.0F, 1.0F);
        } else
        if(regiment.country() == PaintScheme.countryNetherlands)
        {
            changeMat(class1, hiermesh, "Overlay6", "DutchTriangle", "Dutch/roundel.tga", 1.0F, 1.0F, 1.0F);
            changeMat(class1, hiermesh, "Overlay7", "DutchTriangle", "Dutch/roundel.tga", 1.0F, 1.0F, 1.0F);
            changeMat(hiermesh, "Overlay1", "psBM00DUTCNUM" + (k < 10 ? "0" + k : "" + k), "German/" + k / 10 + ".tga", "German/" + k % 10 + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
            changeMat(hiermesh, "Overlay4", "psBM00DUTCNUM" + (k < 10 ? "0" + k : "" + k), "German/" + k / 10 + ".tga", "German/" + k % 10 + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
        } else
        if(regiment.country() == PaintScheme.countryFinland)
        {
            char c = (char)(48 + k % 10);
            changeMat(class1, hiermesh, "Overlay6", "FAFhaken", "Finnish/" + (World.cur().isHakenAllowed() ? "FAFhaken.tga" : "FAFroundel.tga"), 1.0F, 1.0F, 1.0F);
            changeMat(class1, hiermesh, "Overlay7", "FAFhaken", "Finnish/" + (World.cur().isHakenAllowed() ? "FAFhaken.tga" : "FAFroundel.tga"), 1.0F, 1.0F, 1.0F);
            if(k < 10)
                changeMat(class1, hiermesh, "Overlay8", "psBM02FINANUM" + l + i + "0" + k, "Finnish/0" + k + ".tga", PaintScheme.psFinnishFighterColor[i][0], PaintScheme.psFinnishFighterColor[i][1], PaintScheme.psFinnishFighterColor[i][2]);
            else
                changeMat(hiermesh, "Overlay8", "psBM02FINCNUM" + l + i + k, "Finnish/" + k / 10 + ".tga", "Finnish/" + k % 10 + ".tga", PaintScheme.psFinnishFighterColor[i][0], PaintScheme.psFinnishFighterColor[i][1], PaintScheme.psFinnishFighterColor[i][2], PaintScheme.psFinnishFighterColor[i][0], PaintScheme.psFinnishFighterColor[i][1], PaintScheme.psFinnishFighterColor[i][2]);
            String s = getFAFACCode(class1, i);
            changeMat(hiermesh, "Overlay2", "psBM02FINACOD" + s + c, "Finnish/" + s + ".tga", "Finnish/sn" + c + ".tga", 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            changeMat(hiermesh, "Overlay3", "psBM02FINACOD" + s + c, "Finnish/" + s + ".tga", "Finnish/sn" + c + ".tga", 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        } else
        {
            if(regiment.country() == PaintScheme.countryFrance)
            {
                if(k < 10)
                {
                    changeMat(class1, hiermesh, "Overlay2", "psFB02FRALNUM" + l + i + k, "Finnish/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F);
                    changeMat(hiermesh, "Overlay3", "psFB02FRARNUM" + l + i + k, "null.tga", "Finnish/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                } else
                {
                    changeMat(hiermesh, "Overlay2", "psFB02FRACNUM" + l + i + k, "Finnish/" + k / 10 + ".tga", "Finnish/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                    changeMat(hiermesh, "Overlay3", "psFB02FRACNUM" + l + i + k, "Finnish/" + k / 10 + ".tga", "Finnish/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                }
                changeMat(class1, hiermesh, "Overlay6", "frenchroundel", "French/roundel.tga", 1.0F, 1.0F, 1.0F);
                changeMat(class1, hiermesh, "Overlay7", "frenchroundel", "French/roundel.tga", 1.0F, 1.0F, 1.0F);
            }
            if(regiment.country() == PaintScheme.countryBritain)
            {
                if("ra".equals(regiment.branch()) || "rz".equals(regiment.branch()) || "rn".equals(regiment.branch()))
                {
                    k = clampToLiteral(k);
                    changeMat(hiermesh, "Overlay1", "psBM02BRINAVYREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2], PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2]);
                    changeMat(hiermesh, "Overlay4", "psBM02BRINAVYREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2], PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2]);
                    if(regiment.aid()[0] != '_')
                    {
                        changeMat(hiermesh, "Overlay2", "psBM02BRINAVYLNUM" + regiment.id() + l + i + (k < 10 ? "0" + k : "" + k), "British/" + regiment.aid()[0] + "f.tga", "British/" + (char)((65 + k) - 1) + ".tga", 0.11F, 0.11F, 0.11F, 0.11F, 0.11F, 0.11F);
                        changeMat(hiermesh, "Overlay3", "psBM02BRINAVYRNUM" + regiment.id() + l + i + (k < 10 ? "0" + k : "" + k), "British/" + regiment.aid()[0] + "f.tga", "British/" + (char)((65 + k) - 1) + ".tga", 0.11F, 0.11F, 0.11F, 0.11F, 0.11F, 0.11F);
                    } else
                    {
                        changeMat(class1, hiermesh, "Overlay2", "psBM02BRINAVYLNUM" + regiment.id() + l + i + (k < 10 ? "0" + k : "" + k), "British/" + (char)((65 + k) - 1) + ".tga", 0.11F, 0.11F, 0.11F);
                        changeMat(hiermesh, "Overlay3", "psBM02BRINAVYRNUM" + regiment.id() + l + i + (k < 10 ? "0" + k : "" + k), "British/" + regiment.aid()[0] + "f.tga", "British/" + (char)((65 + k) - 1) + ".tga", 0.11F, 0.11F, 0.11F, 0.11F, 0.11F, 0.11F);
                    }
                    changeMat(class1, hiermesh, "Overlay6", "britishroundel5n", "British/roundel5N.tga", 1.0F, 1.0F, 1.0F);
                    changeMat(class1, hiermesh, "Overlay7", "britishroundel5n", "British/roundel5N.tga", 1.0F, 1.0F, 1.0F);
                } else
                {
                    k = clampToLiteral(k);
                    changeMat(hiermesh, "Overlay1", "psBM02BRIREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
                    changeMat(hiermesh, "Overlay4", "psBM02BRIREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
                    changeMat(hiermesh, "Overlay2", "psBM02BRILNUM" + l + i + (k < 10 ? "0" + k : "" + k), "British/" + (char)((65 + k) - 1) + ".tga", "null.tga", PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2], 1.0F, 1.0F, 1.0F);
                    changeMat(hiermesh, "Overlay3", "psBM02BRIRNUM" + l + i + (k < 10 ? "0" + k : "" + k), "null.tga", "British/" + (char)((65 + k) - 1) + ".tga", 1.0F, 1.0F, 1.0F, PaintScheme.psBritishSkyColor[0], PaintScheme.psBritishSkyColor[1], PaintScheme.psBritishSkyColor[2]);
                    changeMat(class1, hiermesh, "Overlay6", "britishroundel2c", "British/roundel2c.tga", 1.0F, 1.0F, 1.0F);
                    changeMat(class1, hiermesh, "Overlay7", "britishroundel4cthin", "British/roundel4cthin.tga", 1.0F, 1.0F, 1.0F);
                }
            } else
            if(regiment.country() == PaintScheme.countryHungary)
            {
                changeMat(hiermesh, "Overlay1", "psBM02HUNREGI" + regiment.id(), "German/" + regiment.aid()[0] + ".tga", "German/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
                changeMat(hiermesh, "Overlay3", "psBM02HUNREGI" + regiment.id(), "German/" + regiment.aid()[0] + ".tga", "German/" + regiment.aid()[1] + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
                changeMat(hiermesh, "Overlay2", "psBM02HUNCNUM" + l + i + (k < 10 ? "0" + k : "" + k), "German/" + k / 10 + ".tga", "German/" + k % 10 + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
                changeMat(hiermesh, "Overlay4", "psBM02HUNCNUM" + l + i + (k < 10 ? "0" + k : "" + k), "German/" + k / 10 + ".tga", "German/" + k % 10 + ".tga", 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F);
                changeMat(class1, hiermesh, "Overlay6", "hungarianbalkenolder", "Hungarian/balkenolder.tga", 1.0F, 1.0F, 1.0F);
                changeMat(class1, hiermesh, "Overlay7", "hungarianbalkenolder", "Hungarian/balkenolder.tga", 1.0F, 1.0F, 1.0F);
            } else
            {
                if(regiment.country() == PaintScheme.countryItaly)
                {
                    System.out.println(" Regiment aid = " + regiment.aid()[0]);
                    if(k < 10)
                    {
                        changeMat(class1, hiermesh, "Overlay2", "psFB02ITALNUM" + l + i + k + regiment.aid()[1], "Italian/" + k % 10 + regiment.aid()[1] + ".tga", 1.0F, 1.0F, 1.0F);
                        changeMat(hiermesh, "Overlay3", "psFB02ITARNUM" + l + i + k + regiment.aid()[1], "null.tga", "Italian/" + k % 10 + regiment.aid()[1] + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                    } else
                    {
                        changeMat(hiermesh, "Overlay2", "psFB02ITACNUM" + l + i + k + regiment.aid()[1], "Italian/" + k / 10 + regiment.aid()[1] + ".tga", "Italian/" + k % 10 + regiment.aid()[1] + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                        changeMat(hiermesh, "Overlay3", "psFB02ITACNUM" + l + i + k + regiment.aid()[1], "Italian/" + k / 10 + regiment.aid()[1] + ".tga", "Italian/" + k % 10 + regiment.aid()[1] + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                    }
                    if(regiment.aid()[0] != 'Z')
                    {
                        changeMat(class1, hiermesh, "Overlay4", "psFB02ITARNUM" + regiment.id(), "Italian/" + regiment.aid()[0] + "4.tga", 1.0F, 1.0F, 1.0F);
                        changeMat(class1, hiermesh, "Overlay1", "psFB02ITALNUM" + regiment.id(), "Italian/" + regiment.aid()[0] + "1.tga", 1.0F, 1.0F, 1.0F);
                    }
                    changeMat(class1, hiermesh, "Overlay6", "italian3", "Italian/roundel0.tga", 0.1F, 0.1F, 0.1F);
                }
                if(regiment.country() == PaintScheme.countryJapan)
                {
                    changeMat(hiermesh, "Overlay2", "psBM02JAPCNUM" + l + i + (k < 10 ? "0" + k : "" + k), "Russian/1" + k / 10 + ".tga", "Russian/1" + k % 10 + ".tga", PaintScheme.psRussianBomberColor[0][0], PaintScheme.psRussianBomberColor[0][1], PaintScheme.psRussianBomberColor[0][2], PaintScheme.psRussianBomberColor[0][0], PaintScheme.psRussianBomberColor[0][1], PaintScheme.psRussianBomberColor[0][2]);
                    changeMat(hiermesh, "Overlay3", "psBM02JAPCNUM" + l + i + (k < 10 ? "0" + k : "" + k), "Russian/1" + k / 10 + ".tga", "Russian/1" + k % 10 + ".tga", PaintScheme.psRussianBomberColor[0][0], PaintScheme.psRussianBomberColor[0][1], PaintScheme.psRussianBomberColor[0][2], PaintScheme.psRussianBomberColor[0][0], PaintScheme.psRussianBomberColor[0][1], PaintScheme.psRussianBomberColor[0][2]);
                    changeMat(class1, hiermesh, "Overlay6", "JAR1", "Japanese/JAR.tga", 1.0F, 1.0F, 1.0F);
                    changeMat(class1, hiermesh, "Overlay7", "JAR1", "Japanese/JAR.tga", 1.0F, 1.0F, 1.0F);
                }
                if(regiment.country() == PaintScheme.countryPoland)
                {
                    changeMat(hiermesh, "Overlay1", "psBM02POLCNUM" + l + i + (k < 10 ? "0" + k : "" + k), "Russian/1" + k / 10 + ".tga", "Russian/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                    changeMat(hiermesh, "Overlay4", "psBM02POLCNUM" + l + i + (k < 10 ? "0" + k : "" + k), "Russian/1" + k / 10 + ".tga", "Russian/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                    changeMat(class1, hiermesh, "Overlay7", "polishcheckerboard", "Polish/checkerboard.tga", 1.0F, 1.0F, 1.0F);
                    changeMat(class1, hiermesh, "Overlay8", "polishcheckerboard", "Polish/checkerboard.tga", 1.0F, 1.0F, 1.0F);
                } else
                if(regiment.country() == PaintScheme.countryRomania)
                {
                    changeMat(hiermesh, "Overlay8", "psFB02ROMCNUM" + l + i + (k < 10 ? "0" + k : "" + k), "Russian/1" + k / 10 + ".tga", "Russian/1" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                    changeMat(class1, hiermesh, "Overlay6", "romaniancross", "Romanian/insignia.tga", 1.0F, 1.0F, 1.0F);
                    changeMat(class1, hiermesh, "Overlay7", "romaniancross", "Romanian/insignia.tga", 1.0F, 1.0F, 1.0F);
                } else
                if(regiment.country() == PaintScheme.countryRussia)
                {
                    if(k < 10)
                    {
                        changeMat(class1, hiermesh, "Overlay2", "psBM02RUSLNUM" + l + i + "0" + k, "Russian/0" + k + ".tga", PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2]);
                        changeMat(hiermesh, "Overlay3", "psBM02RUSRNUM" + l + i + "0" + k, "null.tga", "Russian/0" + k + ".tga", 1.0F, 1.0F, 1.0F, PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2]);
                    } else
                    {
                        changeMat(hiermesh, "Overlay2", "psBM02RUSCNUM" + l + i + k, "Russian/0" + k / 10 + ".tga", "Russian/0" + k % 10 + ".tga", PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2], PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2]);
                        changeMat(hiermesh, "Overlay3", "psBM02RUSCNUM" + l + i + k, "Russian/0" + k / 10 + ".tga", "Russian/0" + k % 10 + ".tga", PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2], PaintScheme.psRussianBomberColor[i][0], PaintScheme.psRussianBomberColor[i][1], PaintScheme.psRussianBomberColor[i][2]);
                    }
                    changeMat(class1, hiermesh, "Overlay7", "redstar2", "Russian/redstar2.tga", 1.0F, 1.0F, 1.0F);
                    changeMat(class1, hiermesh, "Overlay8", "redstar2", "Russian/redstar2.tga", 1.0F, 1.0F, 1.0F);
                } else
                if(regiment.country() == PaintScheme.countryNewZealand)
                {
                    k = clampToLiteral(k);
                    changeMat(hiermesh, "Overlay1", "psBM00RZREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2], PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2]);
                    changeMat(hiermesh, "Overlay4", "psBM00RZREGI" + regiment.id(), "British/" + regiment.aid()[0] + ".tga", "British/" + regiment.aid()[1] + ".tga", PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2], PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2]);
                    changeMat(hiermesh, "Overlay2", "psBM00RZLNUM" + l + i + (k < 10 ? "0" + k : "" + k), "British/" + (char)((65 + k) - 1) + ".tga", "null.tga", PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2], 1.0F, 1.0F, 1.0F);
                    changeMat(hiermesh, "Overlay3", "psBM00RZRNUM" + l + i + (k < 10 ? "0" + k : "" + k), "null.tga", "British/" + (char)((65 + k) - 1) + ".tga", 1.0F, 1.0F, 1.0F, PaintScheme.psBritishWhiteColor[0], PaintScheme.psBritishWhiteColor[1], PaintScheme.psBritishWhiteColor[2]);
                    changeMat(class1, hiermesh, "Overlay6", "newzealand6", "NewZealand/newzealand6.tga", 1.0F, 1.0F, 1.0F);
                    changeMat(class1, hiermesh, "Overlay7", "newzealand7", "NewZealand/newzealand7.tga", 1.0F, 1.0F, 1.0F);
                } else
                if(regiment.country() == PaintScheme.countrySlovakia)
                {
                    if(k < 10)
                    {
                        changeMat(class1, hiermesh, "Overlay2", "psBM02SLVKLNUM" + l + i + "0" + k, "Finnish/" + k + ".tga", 1.0F, 1.0F, 1.0F);
                        changeMat(hiermesh, "Overlay3", "psBM02SLVKRNUM" + l + i + "0" + k, "null.tga", "Finnish/" + k + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                    } else
                    {
                        changeMat(hiermesh, "Overlay2", "psBM02SLVKCNUM" + l + i + k, "Finnish/" + k / 10 + ".tga", "Finnish/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                        changeMat(hiermesh, "Overlay3", "psBM02SLVKCNUM" + l + i + k, "Finnish/" + k / 10 + ".tga", "Finnish/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                    }
                    changeMat(class1, hiermesh, "Overlay6", "slovakiancross1", "Slovakian/cross1.tga", 1.0F, 1.0F, 1.0F);
                    changeMat(class1, hiermesh, "Overlay7", "slovakiancross2", "Slovakian/cross2.tga", 1.0F, 1.0F, 1.0F);
                } else
                if(regiment.country() == PaintScheme.countryUSA)
                {
                    changeMat(hiermesh, "Overlay1", "psBM00USACNUM" + l + i + (k < 10 ? "0" + k : "" + k), "States/" + k / 10 + ".tga", "States/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                    changeMat(hiermesh, "Overlay4", "psBM00USACNUM" + l + i + (k < 10 ? "0" + k : "" + k), "States/" + k / 10 + ".tga", "States/" + k % 10 + ".tga", 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                    changeMat(class1, hiermesh, "Overlay6", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
                    changeMat(class1, hiermesh, "Overlay7", "whitestar1", "States/whitestar1.tga", 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }
}
