// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   PaintScheme.java

package com.maddox.il2.objects.air;

import java.io.File;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Squadron;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.FObj;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Mission;
import com.maddox.rts.HomePath;
import com.maddox.rts.SectFile;

// Referenced classes of package com.maddox.il2.objects.air:
//            Aircraft

public abstract class PaintScheme
{

    public PaintScheme()
    {
    }

    public String typedName(Aircraft aircraft)
    {
        Wing wing = (Wing)aircraft.getOwner();
        Squadron squadron = wing.squadron();
        Regiment regiment = wing.regiment();
        return typedName(aircraft.getClass(), regiment, squadron.indexInRegiment(), wing.indexInSquadron(), wing.aircIndex(aircraft));
    }

    public String typedNameNum(Aircraft aircraft, int i)
    {
        Wing wing = (Wing)aircraft.getOwner();
        Squadron squadron = wing.squadron();
        Regiment regiment = wing.regiment();
        return typedNameNum(aircraft.getClass(), regiment, squadron.indexInRegiment(), wing.indexInSquadron(), i);
    }

    public String typedName(Class class1, Regiment regiment, int i, int j, int k)
    {
        return typedNameNum(class1, regiment, i, j, j * 4 + k + 1);
    }

    public void prepare(Aircraft aircraft)
    {
        prepare(aircraft, true);
    }

    public void prepare(Aircraft aircraft, boolean flag)
    {
        Wing wing = (Wing)aircraft.getOwner();
        Squadron squadron = wing.squadron();
        Regiment regiment = wing.regiment();
        prepare(aircraft.getClass(), aircraft.hierMesh(), regiment, squadron.indexInRegiment(), wing.indexInSquadron(), wing.aircIndex(aircraft), flag);
    }

    public void prepareNum(Aircraft aircraft, int i)
    {
        prepareNum(aircraft, i, true);
    }

    public void prepareNum(Aircraft aircraft, int i, boolean flag)
    {
        Wing wing = (Wing)aircraft.getOwner();
        Squadron squadron = wing.squadron();
        Regiment regiment = wing.regiment();
        prepareNum(aircraft.getClass(), aircraft.hierMesh(), regiment, squadron.indexInRegiment(), wing.indexInSquadron(), i, flag);
    }

    public void prepare(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k)
    {
        prepare(class1, hiermesh, regiment, i, j, k, true);
    }

    public void prepare(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k, boolean flag)
    {
        prepareNum(class1, hiermesh, regiment, i, j, j * 4 + k + 1, flag);
    }

    public String typedNameNum(Class class1, Regiment regiment, int i, int j, int k)
    {
        return "" + k;
    }

    public void prepareNum(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k, boolean flag)
    {
        if(flag)
        {
            prepareNum(class1, hiermesh, regiment, i, j, k);
        } else
        {
            if(regiment.country() == countryUSA || regiment.country() == countryBritain && "rn".equals(regiment.branch()) || regiment.country() == countryJapan || regiment.country() == countryUSABlue)
                changeMat(null, hiermesh, "Overlay5", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            else
                changeMat(null, hiermesh, "Overlay5", regiment.name(), regiment.fileNameTga(), 1.0F, 1.0F, 1.0F);
            changeMat(null, hiermesh, "Overlay1", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            changeMat(null, hiermesh, "Overlay2", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            changeMat(null, hiermesh, "Overlay3", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            changeMat(null, hiermesh, "Overlay4", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            changeMat(null, hiermesh, "Overlay6", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            changeMat(null, hiermesh, "Overlay7", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            changeMat(null, hiermesh, "Overlay8", "null", "null.tga", 1.0F, 1.0F, 1.0F);
            changeMat(null, hiermesh, "Overlay7a", "null", "null.tga", 1.0F, 1.0F, 1.0F);
           prepareNumOff(class1, hiermesh, regiment, i, j, k);
        }
    }

    public void prepareNumOff(Class class2, HierMesh hiermesh1, Regiment regiment1, int l, int i1, int j1)
    {
    }

    public void prepareNum(Class class1, HierMesh hiermesh, Regiment regiment, int i, int j, int k)
    {
        prepareNum(class1, hiermesh, regiment, i, j, k, false);
    }

    public static void init()
    {
        psGermanFighterString = (new String[][] {
            new String[] {
                I18N.color("White") + " ", I18N.color("Red") + " ", I18N.color("Gold") + " ", I18N.color("Green") + " "
            }, new String[] {
                I18N.color("White") + " ", I18N.color("Black") + " ", I18N.color("Brown") + " ", I18N.color("Blue") + " "
            }
        });
        psRussianBomberString = (new String[] {
            I18N.color("Red"), I18N.color("Yellow"), I18N.color("Aqua"), I18N.color("Green")
        });
        psFinnishFighterString = (new String[][] {
            new String[] {
                I18N.color("Red") + " ", I18N.color("White") + " ", I18N.color("Yellow") + " ", I18N.color("Blue") + " "
            }, new String[] {
                I18N.color("Red") + " ", I18N.color("White") + " ", I18N.color("Yellow") + " ", I18N.color("Blue") + " "
            }
        });
    }

    protected void changeMat(Class class1, HierMesh hiermesh, String s, String s1, String s2, float f, float f1, 
            float f2)
    {
        if(!Config.isUSE_RENDER())
            return;
        if(s2 == null || s2.length() < 4)
            return;
        char c = s2.charAt(s2.length() - 1);
        if(c == '\\' || c == '/')
            return;

        //+++ TODO: 4.12 changed code +++
        if(s.equals("Overlay7"))
        {
            int i = hiermesh.materialFind("Overlay10");
            if(i != -1)
                if(s1.startsWith("balken"))
                    changeMat(class1, hiermesh, "Overlay10", "balken7", s2, f, f1, f2);
                else
                    changeMat(class1, hiermesh, "Overlay10", s1, s2, f, f1, f2);
        }
        //--- TODO: 4.12 changed code ---
        
        String as[] = getCustomMarkings(class1, s1, s2, s);
        if(as != null)
        {
            s1 = as[0];
            s2 = as[1];
        }
        String s3 = null;
        if(class1 != null)
        {
            s3 = getPlaneSpecificMaterialID(class1);
            if(s3 != null)
                s1 = s1 + "_" + s3;
        }
        int i = hiermesh.materialFind(s);
        if(i != -1)
        {
            Mat mat = makeMat(s1, s2, f, f1, f2);
            if(mat != null)
            {
                hiermesh.materialReplace(s, mat);
                if(s3 != null)
                    changeMatAppearance(hiermesh, s);
            }
        }
    }
    
    //+++ TODO: 4.12 changed code +++
    protected void changeMat(Class class1, HierMesh hiermesh, String s, String s1, String s2, String s3, float f, 
            float f1, float f2, float f3, float f4, float f5)
    {
        if(!Config.isUSE_RENDER())
            return;
        int i = hiermesh.materialFind(s);
        if(i != -1)
        {
            Mat mat = makeMat(class1, s, s1, s2, s3, f, f1, f2, f3, f4, f5);
            if(mat != null)
                hiermesh.materialReplace(s, mat);
        }
    }
    //--- TODO: 4.12 changed code ---

    protected void changeMat(HierMesh hiermesh, String s, String s1, String s2, String s3, float f, float f1, 
            float f2, float f3, float f4, float f5)
    {
        if(!Config.isUSE_RENDER())
            return;
        int i = hiermesh.materialFind(s);
        if(i != -1)
        {
            Mat mat = makeMat(null, s, s1, s2, s3, f, f1, f2, f3, f4, f5);
            if(mat != null)
                hiermesh.materialReplace(s, mat);
        }
    }

    public static Mat makeMatGUI(String s, String s1, float f, float f1, float f2)
    {
        if(!Config.isUSE_RENDER())
            return null;
        if(s1 == null || s1.length() < 4)
            return null;
        char c = s1.charAt(s1.length() - 1);
        if(c == '\\' || c == '/')
            return null;
        String s2 = "PaintSchemes/Cache/" + s + ".mat";
        if(FObj.Exist(s2))
            return Mat.New(s2);
        if((new File(s2)).exists())
        {
            return Mat.New(s2);
        } else
        {
            SectFile sectfile = new SectFile("PaintSchemes/base1.mat", 0);
            sectfile.set("Layer0", "TextureName", HomePath.concatNames("../Decals/", s1));
            sectfile.set("Layer0", "ColorScale", "" + f + " " + f1 + " " + f2 + " 1.0");
            sectfile.set("Layer0", "tfTestZ", "0");
            sectfile.saveFile(s2);
            return Mat.New(s2);
        }
    }

    //+++ TODO: 4.12 changed code +++
    public static Mat makeMat(String s, String s1, float f, float f1, float f2)
    {
        return makeMat(null, null, s, s1, f, f1, f2);
    }

    public static Mat makeMat(Class class1, String s, String s1, String s2, float f, float f1, float f2)
    {
        if(!Config.isUSE_RENDER())
            return null;
        if(s2 == null || s2.length() < 4)
            return null;
        char c = s2.charAt(s2.length() - 1);
        if(c == '\\' || c == '/')
            return null;
        if(class1 != null)
            s1 = getUniqueMatName(class1, s, s1);
        String s3 = "PaintSchemes/Cache/" + s1 + ".mat";
        if(FObj.Exist(s3))
            return Mat.New(s3);
        if((new File(s3)).exists())
            return Mat.New(s3);
        SectFile sectfile = new SectFile("PaintSchemes/base1.mat", 0);
        sectfile.set("Layer0", "TextureName", HomePath.concatNames("../Decals/", s2));
        sectfile.set("Layer0", "ColorScale", "" + f + " " + f1 + " " + f2 + " 1.0");
        if(class1 != null)
            customizeMat(sectfile, class1, s, s1, false);
        sectfile.saveFile(s3);
        return Mat.New(s3);
    }
    
    public static Mat makeMat(Class class1, String s, String s1, String s2, String s3, float f, float f1, float f2, 
            float f3, float f4, float f5)
    {
        if(!Config.isUSE_RENDER())
            return null;
        if(class1 != null)
            s1 = getUniqueMatName(class1, s, s1);
        String s4 = "PaintSchemes/Cache/" + s1 + ".mat";
        if(FObj.Exist(s4))
            return Mat.New(s4);
        if((new File(s4)).exists())
            return Mat.New(s4);
        SectFile sectfile = new SectFile("PaintSchemes/base2.mat", 0);
        sectfile.set("Layer0", "TextureName", HomePath.concatNames("../Decals/", s2));
        sectfile.set("Layer0", "ColorScale", "" + f + " " + f1 + " " + f2 + " 1.0");
        sectfile.set("Layer1", "TextureName", HomePath.concatNames("../Decals/", s3));
        sectfile.set("Layer1", "ColorScale", "" + f3 + " " + f4 + " " + f5 + " 1.0");
        if(class1 != null)
            customizeMat(sectfile, class1, s, s1, true);
        sectfile.saveFile(s4);
        return Mat.New(s4);
    }

    private static String getUniqueMatName(Class class1, String s, String s1)
    {
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.P_51B.class))
        {
            if(s.equals("Overlay1"))
                s1 = s1 + "P51BL";
            else
            if(s.equals("Overlay4"))
                s1 = s1 + "P51BR";
        } else
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.P_51D20NA.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.P_51D5NT.class))
            s1 = s1 + "P51D";
        else
        if(class1.toString().indexOf("BF_109") != -1 && s1.indexOf("ITA") != -1)
            s1 = s1 + "BF109";
        return s1;
    }

    private static void customizeMat(SectFile sectfile, Class class1, String s, String s1, boolean flag)
    {
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.P_51B.class))
        {
            if(s.equals("Overlay1"))
            {
                sectfile.set("Layer0", "TextureCoordScale", "0.15 0.0 1.1 1.1");
                if(flag)
                    sectfile.set("Layer1", "TextureCoordScale", "-0.3 0.0 1.1 1.1");
            } else
            if(s.equals("Overlay4"))
            {
                sectfile.set("Layer0", "TextureCoordScale", "-0.2 0.0 1.1 1.1");
                if(flag)
                    sectfile.set("Layer1", "TextureCoordScale", "-0.65 0.0 1.1 1.1");
            }
        } else
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.P_51D20NA.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.P_51D5NT.class))
        {
            if(s.equals("Overlay1"))
            {
                sectfile.set("Layer0", "TextureCoordScale", "0.0 0.3 1.0 1.0");
                if(flag)
                    sectfile.set("Layer1", "TextureCoordScale", "-0.5 0.3 1.0 1.0");
            } else
            if(s.equals("Overlay4"))
            {
                sectfile.set("Layer0", "TextureCoordScale", "0.0 0.3 1.0 1.0");
                if(flag)
                    sectfile.set("Layer1", "TextureCoordScale", "-0.5 0.3 1.0 1.0");
            }
        } else
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.DXXI_SARJA3_EARLY.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.DXXI_SARJA3_LATE.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.DXXI_SARJA4.class))
        {
            if(s.equals("Overlay8") && flag)
            {
                sectfile.set("Layer0", "TextureCoordScale", "-0.05 -0.2 1.2 1.2");
                sectfile.set("Layer1", "TextureCoordScale", "-0.45 -0.2 1.2 1.2");
            }
        } else
        if(s1.endsWith("BF109"))
        {
            sectfile.set("Layer0", "TextureCoordScale", "0.0 0.0 1.3 1.3");
            if(flag)
                sectfile.set("Layer1", "TextureCoordScale", "-0.5 0.0 1.3 1.3");
        } else
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.P_36A3.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.P_36A4.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.H_75A3.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.H_75A4.class))
        {
            if(s1.endsWith("_CUleft"))
            {
                sectfile.set("Layer0", "TextureCoordScale", "0.2 0.05 0.7 0.7");
                if(flag)
                    sectfile.set("Layer1", "TextureCoordScale", "-0.3 0.05 0.7 0.7");
            }
            if(s1.endsWith("_CUright"))
            {
                sectfile.set("Layer0", "TextureCoordScale", "0.15 0.05 0.7 0.7");
                if(flag)
                    sectfile.set("Layer1", "TextureCoordScale", "-0.35 0.05 0.7 0.7");
            }
            if(s1.endsWith("_CUnum") && flag)
            {
                sectfile.set("Layer0", "TextureCoordScale", "-0.05 -0.2 1.3 1.3");
                sectfile.set("Layer1", "TextureCoordScale", "-0.45 -0.2 1.3 1.3");
            }
        }
    }
    //--- TODO: 4.12 changed code ---

    public static Mat makeMat(String s, String s1, String s2, float f, float f1, float f2, float f3, float f4, 
            float f5)
    {
        if(!Config.isUSE_RENDER())
            return null;
        String s3 = "PaintSchemes/Cache/" + s + ".mat";
        if(FObj.Exist(s3))
            return Mat.New(s3);
        if((new File(s3)).exists())
        {
            return Mat.New(s3);
        } else
        {
            SectFile sectfile = new SectFile("PaintSchemes/base2.mat", 0);
            sectfile.set("Layer0", "TextureName", HomePath.concatNames("../Decals/", s1));
            sectfile.set("Layer0", "ColorScale", f + " " + f1 + " " + f2 + " 1.0");
            sectfile.set("Layer1", "TextureName", HomePath.concatNames("../Decals/", s2));
            sectfile.set("Layer1", "ColorScale", f3 + " " + f4 + " " + f5 + " 1.0");
            sectfile.saveFile(s3);
            return Mat.New(s3);
        }
    }

    protected String getFAFACCode(Class class1, int i)
    {
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109E4.class))
            return "MT-1" + (i <= 1 ? "6" : "7");
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109E4B.class))
            return "MT-1" + (i <= 1 ? "6" : "7");
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109E7.class))
            return "MT-1" + (i <= 1 ? "8" : "9");
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109E7NZ.class))
            return "MT-1" + (i <= 1 ? "8" : "9");
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109F2.class))
            return "MT-1" + (i <= 1 ? "8" : "9");
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109G2.class))
            return "MT-2" + i;
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109G6.class))
            return "MT-4" + (i + 4);
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109G6Late.class))
            return "MT-4" + (i + 4);
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109G6AS.class))
            return "MT-4" + (i + 4);
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109K4.class))
            return "MT-52";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BLENHEIM1.class))
            return "BL-1" + (i + 4);
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BLENHEIM4.class))
            return "BL-" + (i != 0 ? i != 1 ? i != 2 ? "20" : "19" : "13" : "12");
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.F2A_B239.class))
            return "BW-3" + (i + 5);
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.FI_156.class))
            return "ST-11";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.FW_189A2.class))
            return "UH-35";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.FW_190.class))
            return "FW-1" + i;
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.G50.class))
            return "FA-" + i;
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.GLADIATOR1.class))
            return "GL-2" + (5 + i);
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.GLADIATOR1J8A.class))
            return "GL-2" + (5 + i);
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.GLADIATOR2.class))
            return "GL-2" + (5 + i);
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.HE_111H2.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.HE_111H6.class))
            return "HE-1" + (i + 1);
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.HE_111Z.class))
            return "HZ-33";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.HS_129B2.class))
            return "HS-2";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.HS_129B3Wa.class))
            return "HS-2";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.HurricaneMkIa.class))
            return "HC45";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.HurricaneMkIIb.class))
            return "HC46";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.HurricaneMkIIbMod.class))
            return "HC46";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.HurricaneMkIIc.class))
            return "HC46";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.I_153_M62.class))
            return "IT-" + (i + 1);
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.IL_2.class))
            return "IL-";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.IL_4_DB3B.class))
            return "DB-" + (i <= 1 ? "1" : "2");
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.IL_4_DB3F.class))
            return "DB-" + (i <= 1 ? "1" : "2");
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.IL_4_DB3M.class))
            return "DB-" + (i <= 1 ? "1" : "2");
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.IL_4_DB3T.class))
            return "DB-" + (i <= 1 ? "1" : "2");
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.IL_4_IL4.class))
            return "DF-2";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.JU_52.class))
            return "UJ-" + i;
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.JU_87B2.class))
            return "B-3";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.JU_87D3.class))
            return "B-4";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.JU_87G1.class))
            return "B-9";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.JU_88A4.class))
            return "JK-2" + ((int)(0.5F * (float)i) + 5);
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.LA_5FN.class))
            return "LA-2" + i;
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.LAGG_3IT.class))
            return "LG-";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.LAGG_3SERIES4.class))
            return "LG-";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.LAGG_3SERIES66.class))
            return "LG-";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.LI_2.class))
            return "DO-";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.MBR_2AM34.class))
            return "VV-18";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.MIG_3EARLY.class))
            return "MIG-0";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.MIG_3UD.class))
            return "MIG-1";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.MIG_3U.class))
            return "MIG-2";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.MS406.class))
            return "MS-3" + i;
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.MS410.class))
            return "MS-6" + i;
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.MSMORKO.class))
            return "MSv-3" + i;
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.P_36A4.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.H_75A4.class))
            return "CU-50";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.P_36A3.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.H_75A3.class))
            return "CU-5" + (5 + i);
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.P_39N.class))
            return "KN-35";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.P_39Q1.class))
            return "KU-35";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.P_39Q10.class))
            return "KU-79";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.P_40E.class))
            return "CU-50";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.P_40EM105.class))
            return "CU-51";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.P_40M.class))
            return "CU-52";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.PE_3SERIES1.class))
            return "PE-30";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.PE_2SERIES1.class))
            return "PE-21";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.PE_2SERIES110.class))
            return "PE-21";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.PE_2SERIES359.class))
            return "PE-21";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.PE_2SERIES84.class))
            return "PE-21";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.PE_3BIS.class))
            return "PE-30";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.R_10.class))
            return "R-";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.SB_2M100A.class))
            return "SB-" + i;
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.SB_2M103.class))
            return "SB-" + i;
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.U_2VS.class))
            return "VU-";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.YAK_1B.class))
            return "YK-12";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.YAK_3.class))
            return "YK-52";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.YAK_9U.class))
            return "YK-54";
        else
            return "MT-00";
    }

    protected String getCHFMCode(Class class1)
    {
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109E4.class))
            return "J-2";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109E4B.class))
            return "J-2";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109E7.class))
            return "J-2";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109E7NZ.class))
            return "J-2";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109F2.class))
            return "J-4";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109G2.class))
            return "J-6";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109G6.class))
            return "J-6";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109G6Late.class))
            return "J-8";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109G6AS.class))
            return "J-8";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.BF_109K4.class))
            return "J-8";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.MS406.class))
            return "J-0";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.MS410.class))
            return "J-2";
        else
            return "J-4";
    }

    protected String getCHBMCode(Class class1)
    {
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.C_47.class))
            return "A-7";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.JU_52_3MG4E.class))
            return "A-7";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.JU_52_3MG5E.class))
            return "A-7";
        else
            return "B-0";
    }

    protected int clampToLiteral(int i)
    {
        if(i < 1)
            return 1;
        if(i > 26)
            return 26;
        else
            return i;
    }

    private String getPlaneSpecificMaterialID(Class class1)
    {
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.IL_2I.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.IL_2MLate.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.IL_2Type3.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.IL_2Type3M.class))
            return "IL-2";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.MIG_3EARLY.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.MIG_3AM38.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.MIG_3POKRYSHKIN.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.MIG_3SHVAK.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.MIG_3UB.class) || class1.isAssignableFrom(com.maddox.il2.objects.air.MIG_3UD.class))
            return "MIG-3";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.U_2VS.class))
            return "U-2";
        if(class1.isAssignableFrom(com.maddox.il2.objects.air.R_10.class))
            return "R-10";
        else
            return null;
    }

    private String[] getCustomMarkings(Class class1, String s, String s1, String s2)
    {
        if(class1 == null)
            return null;
        if(s.equals("whitestar1"))
        {
            int i = Mission.getMissionDate(true);
            if(i > 0)
            {
                if(i < 0x1285563)
                {
                    s1 = "States/whitestar1_early.tga";
                    s = "whitestar1_early";
                }
                if(i > 0x12857b4 && i < 0x1287ce4 && s2.equals("Overlay7") && (World.cur().camouflage == 2 || World.cur().camouflage == 5))
                {
                    s1 = "States/whitestar1_OT.tga";
                    s = "whitestar1_OT";
                }
                return (new String[] {
                    s, s1
                });
            }
        } else
        {
            if(s.equals("redstar0") || s.equals("redstar1") || s.equals("redstar2") || s.equals("redstar3"))
            {
                int j = Mission.getMissionDate(true);
                if(j > 0)
                {
                	//+++ TODO: 4.12 changed code +++
                    if((com.maddox.il2.objects.air.PE_8.class).isAssignableFrom(class1) || (com.maddox.il2.objects.air.PE_8_M40F.class).isAssignableFrom(class1))
                    //--- TODO: 4.12 changed code ---	
                    {
                        if(j < 19421231)
                        {
                            s1 = "Russian/redstar1.tga";
                            s = "redstar1";
                        } else
                        {
                            s1 = "Russian/redstar3.tga";
                            s = "redstar3";
                        }
                        return (new String[] {
                            s, s1
                        });
                    }
                    //if(j < 19391000 && !com.maddox.il2.objects.air.SB_2M100A.class.isAssignableFrom(class1) && !com.maddox.il2.objects.air.SB_2M103.class.isAssignableFrom(class1) && !com.maddox.il2.objects.air.IL_4_DB3B.class.isAssignableFrom(class1) && !com.maddox.il2.objects.air.IL_4_DB3F.class.isAssignableFrom(class1) && !com.maddox.il2.objects.air.IL_4_DB3M.class.isAssignableFrom(class1) && !com.maddox.il2.objects.air.IL_4_DB3T.class.isAssignableFrom(class1) && !com.maddox.il2.objects.air.IL_4_IL4.class.isAssignableFrom(class1))
                    //+++ TODO: 4.12 changed code +++
                    if(j < 19391000 && !(com.maddox.il2.objects.air.SB_2M100A.class).isAssignableFrom(class1) && !(com.maddox.il2.objects.air.SB_2M103.class).isAssignableFrom(class1) && !(com.maddox.il2.objects.air.IL_4_DB3B.class).isAssignableFrom(class1) && !(com.maddox.il2.objects.air.IL_4_DB3F.class).isAssignableFrom(class1) && !(com.maddox.il2.objects.air.IL_4_DB3M.class).isAssignableFrom(class1) && !(com.maddox.il2.objects.air.IL_4_DB3T.class).isAssignableFrom(class1) && !(com.maddox.il2.objects.air.IL_4_IL4.class).isAssignableFrom(class1))
                    //--- TODO: 4.12 changed code ---
                    {
                        s1 = "Russian/redstar0.tga";
                        s = "redstar0";
                    } else
                    if(j < 19420500)
                    {
                        s1 = "Russian/redstar1_border.tga";
                        s = "redstar1_border";
                    } else
                    if(j < 19430700)
                    {
                        s1 = "Russian/redstar1.tga";
                        s = "redstar1";
                    } else
                    if(j < 19440300)
                    {
                        s1 = "Russian/redstar2.tga";
                        s = "redstar2";
                    } else
                    {
                        s1 = "Russian/redstar3.tga";
                        s = "redstar3";
                    }
                    return (new String[] {
                        s, s1
                    });
                } else
                {
                    return null;
                }
            }
            if(com.maddox.il2.objects.air.DO_335A0.class.isAssignableFrom(class1) || com.maddox.il2.objects.air.DO_335V13.class.isAssignableFrom(class1))
            {
                if(s.equals("balken2"))
                    return (new String[] {
                        "balken3", "German/balken3.tga"
                    });
            } else
            if(com.maddox.il2.objects.air.BF_109G6AS.class.isAssignableFrom(class1) || com.maddox.il2.objects.air.BF_109G6Late.class.isAssignableFrom(class1) || com.maddox.il2.objects.air.BF_109G10.class.isAssignableFrom(class1) || com.maddox.il2.objects.air.FW_190F8.class.isAssignableFrom(class1) || com.maddox.il2.objects.air.FW_190A8.class.isAssignableFrom(class1) || com.maddox.il2.objects.air.FW_190A6.class.isAssignableFrom(class1))
            {
                if(s.equals("balken0"))
                    return (new String[] {
                        "balken4", "German/balken4.tga"
                    });
                if(s.equals("balken1"))
                    return (new String[] {
                        "balken2", "German/balken2.tga"
                    });
            } else
            if(com.maddox.il2.objects.air.FW_190A9.class.isAssignableFrom(class1))
            {
                if(s.equals("balken0"))
                    return (new String[] {
                        "balken4", "German/balken4.tga"
                    });
                if(s.equals("balken1"))
                    return (new String[] {
                        "balken5", "German/balken5.tga"
                    });
            }
        }
        return null;
    }

    private void changeMatAppearance(Mesh mesh, String s)
    {
        int i = mesh.materialFind("Matt1D0o");
        int j = mesh.materialFind(s);
        if(i == -1 || j == -1)
            return;
        Mat mat = mesh.material(i);
        Mat mat1 = mesh.material(j);
        if(mat1 == null || mat == null)
            return;
        try
        {
            mat1.set((byte)22, mat.get((byte)22));
            mat1.set((byte)23, mat.get((byte)23));
            mat1.set((byte)24, mat.get((byte)24));
            mat1.set((byte)20, mat.get((byte)20));
            mat1.set((byte)21, mat.get((byte)21));
        }
        catch(Exception exception) { }
    }

    public static final String countryGermany = "de".intern();
    public static final String countryFinland = "fi".intern();
    public static final String countryFrance = "fr".intern();
    public static final String countryBritain = "gb".intern();
    public static final String countryHungary = "hu".intern();
    public static final String countryItaly = "it".intern();
    public static final String countryJapan = "ja".intern();
    public static final String countryNetherlands = "du".intern();
    public static final String countryNoName = "nn".intern();
    public static final String countryPoland = "pl".intern();
    public static final String countryRomania = "ro".intern();
    public static final String countryRussia = "ru".intern();
    public static final String countryNewZealand = "rz".intern();
    public static final String countrySlovakia = "sk".intern();
    public static final String countryUSA = "us".intern();
    public static final String countrySwitzerland = "ch".intern();
    public static final String countryChina = "cn".intern();
    public static final String countrySpainRep = "es".intern();
    public static final String countryManagua = "mg".intern();
    public static final String countrySpainNat = "sp".intern();
    public static final String countryIsrael = "is".intern();
    public static final String countryCanada = "ct".intern();
    public static final String countryItalyAllied = "il".intern();
    public static final String countryRomaniaAllied = "rl".intern();
    public static final String countryFinlandAllied = "fl".intern();
    public static final String countryBelgium = "be".intern();
    public static final String countryBulgaria = "bg".intern();
    public static final String countryNorway = "no".intern();
    public static final String countryGreece = "gr".intern();
    public static final String countryBrazil = "br".intern();
    public static final String countrySweden = "sw".intern();
    public static final String countrySouthAfrica = "sa".intern();
    public static final String countryYugoslavia = "yu".intern();
    public static final String countryYugoPar = "yp".intern();
    public static final String countryDenmark = "dk".intern();
    public static final String countryPhilippines = "ph".intern();
    public static final String countryCroatia = "hr".intern();
    public static final String countryBulgariaAllied = "bl".intern();
    public static final String countryThailand = "th".intern();
    public static final String countryRussianLiberationArmy = "rb".intern();
    public static final String countryManchukuo = "mk".intern();
    public static final String countryRussianEmpire = "re".intern();
    public static final String countryGDR = "dd".intern();
    public static final String countryBolivia = "bo".intern();
    public static final String countryAbyssinia = "ab".intern();
    public static final String countryMexico = "mx".intern();
    public static final String countryUruguay = "ur".intern();
    public static final String countryHungaryAllied = "hy".intern();
    public static final String countryTurkey = "tk".intern();
    public static final String countryIraq = "ik".intern();
    public static final String countryEgypt = "eg".intern();
    public static final String countryParaguay = "pa".intern();
    public static final String countryArgentina = "ar".intern();
    public static final String countryCzechoslovakia = "cz".intern();
    public static final String countryPolandAxis = "pn".intern();
    public static final String countryPortugal = "pt".intern();
    public static final String countryIran = "ir".intern();
    public static final String countryPRC = "cc".intern();
    public static final String countryNorthKorea = "kp".intern();
    public static final String countrySouthKorea = "kr".intern();
    public static final String countryEstonia = "ee".intern();
    public static final String countryLatvia = "lv".intern();
    public static final String countryLithuania = "lt".intern();
    public static final String countryPeru = "pe".intern();
    public static final String countryColombia = "co".intern();
    public static final String countrySyria = "sy".intern();
    public static final String countryAustria = "at".intern();
    public static final String countryItalyANR = "an".intern();
    public static final String countryVichyFrance = "vi".intern();
    public static final String countryIndia = "id".intern();
    public static final String countryPakistan = "pk".intern();
    public static final String countrySouthAfricaS = "ss".intern();
    public static final String countrySouthAfricaB = "sb".intern();
    public static final String countryAngola = "az".intern();
    public static final String countryMozambique = "mz".intern();
    public static final String countryChinaRed = "cx".intern();
    public static final String countryNorthKoreaRed = "kx".intern();
    public static final String countrySyriaRed = "sx".intern();
    public static final String countryEgyptRed = "ex".intern();
    public static final String countryIndonesia = "do".intern();
    public static final String countryHonduras = "ho".intern();
    public static final String countryLebanon = "le".intern();
    public static final String countryJordan = "jo".intern();
    public static final String countryCuba = "cu".intern();
    public static final String countryGenericRed = "rd".intern();
    public static final String countryCanadaBlue = "cq".intern();
    public static final String countryIsraelBlue = "iq".intern();
    public static final String countrySouthKoreaBlue = "kq".intern();
    public static final String countryThailandBlue = "ty".intern();
    public static final String countryBritainBlue = "vb".intern();
    public static final String countryUSABlue = "na".intern();
    public static final String countryNicaragua = "ni".intern();
    public static final String countryElSalvador = "el".intern();
    public static final String countryMalaysia = "my".intern();
    public static final String countrySingapore = "si".intern();
    public static final String countryAustralia = "as".intern();
    public static final String countryNewZeeland = "nz".intern();
    public static final String countryGermanyBlue = "gp".intern();
    public static final String countryGenericBlue = "bu".intern();
    public static final String countrySylvadia = "sl".intern();
    public static final String countryVietnamNorth = "nv".intern();
    public static final String countryNigeria = "ng".intern();
    public static final String countryAlbania = "al".intern();
    public static final String countryCostaRica = "cr".intern();
    public static final String countryVenezuala = "vz".intern();
    public static final String countryAlgeria = "ag".intern();
    public static final String countryChile = "ci".intern();
    public static final String countryEthiopia = "eh".intern();
    public static final String countryLibya = "ly".intern();
    public static final String countryBotswana = "bt".intern();
    public static final String countryUganda = "ug".intern();
    public static final String countrySaudiArabia = "su".intern();
    public static final String countryCambodia = "cm".intern();
    public static final String countryMyanmar = "mm".intern();
    public static final String countryVietnamSouth = "vn".intern();
    public static final String countryCostaVerde = "cv".intern();
    public static final String countryBiafra = "bi".intern();
    public static final String countryBorduria = "bd".intern();
    public static final String countryIreland = "ad".intern();
    public static final String countryPanama = "pm".intern();
    public static final String countryGuatemala = "gl".intern();
    public static final String countryMorocco = "mr".intern();
    public static final String countryEritrea = "et".intern();
    public static final String countryCzechUp = "cj".intern();
    public static final char psGermanBomberLetter[][] = {
        {
            'H', 'K', 'L', 'B'
        }, {
            'M', 'N', 'P', 'C'
        }, {
            'R', 'S', 'T', 'D'
        }, {
            'U', 'V', 'W', 'E'
        }, {
            'X', 'Y', 'Z', 'F'
        }
    };
    public static final float psGermanBomberColor[][] = {
        {
            0.945F, 0.929F, 0.886F
        }, {
            0.584F, 0.122F, 0.122F
        }, {
            0.89F, 0.729F, 0.0F
        }, {
            0.247F, 0.806F, 0.0F
        }
    };
    public static String psGermanFighterString[][];
    public static final char psGermanFighterGruppeChar[][] = {
        {
            ' ', '-', '~', '\244'
        }, {
            ' ', '-', '|', '\261'
        }
    };
    public static String psRussianBomberString[];
    public static final float psRussianBomberColor[][] = {
        {
            0.718F, 0.176F, 0.102F
        }, {
            1.0F, 0.843F, 0.314F
        }, {
            0.247F, 0.733F, 0.937F
        }, {
            0.263F, 0.447F, 0.184F
        }
    };
    public static String psFinnishFighterString[][];
    public static final float psFinnishFighterColor[][] = {
        {
            0.584F, 0.122F, 0.122F
        }, {
            0.945F, 0.929F, 0.886F
        }, {
            0.89F, 0.729F, 0.0F
        }, {
            0.1F, 0.235F, 0.695F
        }
    };
    public static final String psFinnishFighterPrefix[][] = {
        {
            "German/02", "German/", "German/03", "German/10"
        }, {
            "German/02", "German/01", "German/03", "German/10"
        }
    };
    public static final float psBritishGrayColor[] = {
        0.7255F, 0.8431F, 0.6471F
    };
    public static final float psBritishSkyColor[] = {
        0.7255F, 0.8431F, 0.6471F
    };
    public static final float psBritishOrangeColor[] = {
        0.988F, 0.8F, 0.08F
    };
    public static final float psBritishBlackColor[] = {
        0.0F, 0.0F, 0.0F
    };
    public static final float psBritishRedColor[] = {
        0.6824F, 0.2471F, 0.1451F
    };
    public static final float psBritishWhiteColor[] = {
        0.99F, 0.99F, 0.99F
    };
    protected static final String prefixCachePath = "PaintSchemes/Cache/";
    protected static final String prefixInsigniaPath = "../Decals/";
    protected static final String mat1Path = "PaintSchemes/base1.mat";
    protected static final String mat2Path = "PaintSchemes/base2.mat";

}
