// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   ScrShot.java

package com.maddox.opengl.util;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.maddox.TexImage.TexImage;
import com.maddox.opengl.GLContext;
import com.maddox.opengl.gl;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ScrShot
{

    public void grab()
    {
        
        SimpleDateFormat format=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String s = "MyScreenShots/" + format.format(cal.getTime()) + ".jpg";
                 
        s = s.replace('\\', '-');
        s = s.replace(':', '-');
        s = s.replace('*', '-');
        s = s.replace('?', '-');
        s = s.replace('"', '-');
        s = s.replace('<', '-');
        s = s.replace('>', '-');
        s = s.replace('|', '-');
        gl.ReadPixels(0, 0, dx, dy, 6407, 5121, img.image);
        for(int i = 0; i < dy; i++)
        {
            int j = dy - i - 1;
            for(int k = 0; k < dx; k++)
            {
                bi.getRaster().setSample(k, j, 0, img.intR(k, i));
                bi.getRaster().setSample(k, j, 1, img.intG(k, i));
                bi.getRaster().setSample(k, j, 2, img.intB(k, i));
            }

        }

        try
        {
            FileOutputStream fileoutputstream = new FileOutputStream(s);
            JPEGImageEncoder jpegimageencoder = JPEGCodec.createJPEGEncoder(fileoutputstream);
            JPEGEncodeParam jpegencodeparam = jpegimageencoder.getDefaultJPEGEncodeParam(bi);
            jpegencodeparam.setQuality(0.9F, false);
            jpegencodeparam.setHorizontalSubsampling(0, 1);
            jpegencodeparam.setHorizontalSubsampling(1, 1);
            jpegencodeparam.setHorizontalSubsampling(2, 1);
            jpegencodeparam.setVerticalSubsampling(0, 1);
            jpegencodeparam.setVerticalSubsampling(1, 1);
            jpegencodeparam.setVerticalSubsampling(2, 1);
            jpegimageencoder.setJPEGEncodeParam(jpegencodeparam);
            jpegimageencoder.encode(bi);
            fileoutputstream.close();
        }
        catch(Exception exception) { }
    }

    public ScrShot(String s)
    {
        dx = GLContext.getCurrent().width();
        dy = GLContext.getCurrent().height();
        img = new TexImage();
        img.set(dx, dy, 6407);
        bi = new BufferedImage(dx, dy, 1);
    }

    private int dx;
    private int dy;
    private TexImage img;
    private BufferedImage bi;
}
