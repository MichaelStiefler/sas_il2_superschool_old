// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   BmpUtils.java

package com.maddox.il2.engine;


// Referenced classes of package com.maddox.il2.engine:
//            FObj, GObj

public class BmpUtils
{

	// TODO: + SAS 24 Bpp Texture Mod
//    public static native int squareSizeBMP8Pal(String s);
	public static int squareSizeBMP8Pal(String s) { return BMPLoader.squareSizeBMP8Pal(s); }
	// TODO: - SAS 24 Bpp Texture Mod

    public static boolean checkBMP8Pal(String s, int i, int j)
    {
        int k = RectSizeBMP8Pal(s);
        if(k == -1)
            return false;
        else
            return i == (k & 0xffff) && j == (k >> 16 & 0xffff);
    }

    public static boolean bmp8Scale05(String s, String s1)
    {
        boolean flag = BMP8Scale05(s, s1);
        return flag;
    }

    public static boolean bmp8PalToTGA3(String s, String s1)
    {
        boolean flag = BMP8PalToTGA3(s, s1);
        if(flag)
            tryReloadFObj(s1);
        return flag;
    }

    public static boolean bmp8Pal192x256ToTGA3(String s, String s1)
    {
        boolean flag = BMP8Pal192x256ToTGA3(s, s1);
        if(flag)
            tryReloadFObj(s1);
        return flag;
    }

    public static boolean bmp8PalToTGA4(String s, String s1)
    {
        boolean flag = BMP8PalToTGA4(s, s1);
        if(flag)
            tryReloadFObj(s1);
        return flag;
    }

    public static boolean bmp8PalTo2TGA4(String s, String s1, String s2)
    {
        boolean flag = BMP8PalTo2TGA4(s, s1, s2);
        if(flag)
        {
            tryReloadFObj(s1);
            tryReloadFObj(s2);
        }
        return flag;
    }

    public static boolean bmp8PalTo4TGA4(String s, String s1, String s2)
    {
    	// TODO: + SAS 24 Bpp Texture Mod
    	boolean flag = BMPLoader.bmp8PalTo4TGA4(s, s1, s2);
        if (!flag) flag = BMP8PalTo4TGA4(s, s1, s2);
    	// TODO: - SAS 24 Bpp Texture Mod
        if(flag)
        {
            tryReloadFObj(s2 + "/skin1o.tga");
            tryReloadFObj(s2 + "/skin1p.tga");
            tryReloadFObj(s2 + "/skin1q.tga");
        }
        return flag;
    }

    private static void tryReloadFObj(String s)
    {
        if(FObj.Exist(s))
        {
            int i = FObj.GetFObj(s);
            if(i != 0)
            {
                FObj.ReLoad(i);
                GObj.Unlink(i);
            }
        }
    }

    private static native int RectSizeBMP8Pal(String s);

    private static native boolean BMP8Scale05(String s, String s1);

    private static native boolean BMP8PalToTGA3(String s, String s1);

    private static native boolean BMP8Pal192x256ToTGA3(String s, String s1);

    private static native boolean BMP8PalToTGA4(String s, String s1);

    private static native boolean BMP8PalTo2TGA4(String s, String s1, String s2);

    private static native boolean BMP8PalTo4TGA4(String s, String s1, String s2);

    private BmpUtils()
    {
    }

    static 
    {
        GObj.loadNative();
    }
}
