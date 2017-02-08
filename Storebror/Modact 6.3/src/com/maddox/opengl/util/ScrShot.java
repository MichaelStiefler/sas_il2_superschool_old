package com.maddox.opengl.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.maddox.il2.engine.Config;
import com.maddox.rts.HomePath;

public class ScrShot {
    public void grab() {
        this.getScreenShot(Config.cur.screenshotType, this.scrName(0), this.scrName(1), (int) (this.jpgQuality * 100F));
    }

    private String scrName(int i) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String s = "MyScreenShots/" + format.format(cal.getTime()) + (i <= 0 ? ".tga" : ".jpg");

        s = s.replace('\\', '-');
        s = s.replace(':', '-');
        s = s.replace('*', '-');
        s = s.replace('?', '-');
        s = s.replace('"', '-');
        s = s.replace('<', '-');
        s = s.replace('>', '-');
        s = s.replace('|', '-');

        return s;
    }

    public ScrShot(String s) {
        this.jpgQuality = Config.cur.ini.get("game", "jpgQuality ", 1.0F, 0.0F, 1.0F);
        this.scrDir = new File(HomePath.toFileSystemName("MyScreenShots/", 0));
        if (!this.scrDir.exists()) {
            this.scrDir.mkdirs();
        }
    }

    private native void getScreenShot(int i, String s, String s1, int j);

    private float               jpgQuality;
    private static final String TGA = ".tga";
    private static final String JPG = ".jpg";
    private File                scrDir;
}
