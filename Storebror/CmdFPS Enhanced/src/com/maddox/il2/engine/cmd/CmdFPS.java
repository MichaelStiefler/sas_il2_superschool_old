package com.maddox.il2.engine.cmd;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.maddox.JGP.Color4f;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.RendersMain;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HomePath;
import com.maddox.rts.MsgTimeOut;
import com.maddox.rts.MsgTimeOutListener;
import com.maddox.rts.Time;
import com.maddox.sound.AudioDevice;

public class CmdFPS extends Cmd implements MsgTimeOutListener {

    public void msgTimeOut(Object obj) {
        this.msg.post();
        if (!this.bGo) {
            return;
        }
        long l = Time.real();
        int i = RendersMain.frame();
        if (l >= (this.timePrev + Math.min(this.logExtPeriod, 250L))) {
            this.fpsCur = (1000D * (i - this.framePrev)) / (l - this.timePrev);
            if (this.fpsCur < this.fpsLast && this.fpsLast > 0.0D) {
                this.fpsMinValid = true;
            }
            this.fpsLast = this.fpsCur;
            if (this.fpsMinValid && this.fpsMin > this.fpsCur) {
                this.fpsMin = this.fpsCur;
            }
            if (this.fpsMax < this.fpsCur) {
                this.fpsMax = this.fpsCur;
            }
            if ((this.fpsMinValid && this.fpsMinRecent > this.fpsCur) || ((l - this.lastFpsMin) > this.recentResetTimeout)) {
                this.fpsMinRecent = this.fpsCur;
                this.lastFpsMin = l;
            }
            if ((this.fpsMaxRecent < this.fpsCur) || ((l - this.lastFpsMax) > this.recentResetTimeout)) {
                this.fpsMaxRecent = this.fpsCur;
                this.lastFpsMax = l;
            }
            this.framePrev = i;
            this.timePrev = l;
            this.checkMissionStatus();
            this.updateFrameTimeList(i, l);
        }
        if (l >= (this.timePrevLogExt + this.logExtPeriod)) {
            this.timePrevLogExt = l;
            if (this.logExt && Mission.isPlaying()) {
                this.logFpsToFile();
            }
        }
        if (this.framePrev == this.frameStart) {
            return;
        }
        if (this.bShow) {
            this.renderFps();
        }
        if ((this.logPeriod > 0L) && (l >= (this.logPrintTime + this.logPeriod))) {
            System.out.println("fps:" + this.fpsInfo());
            this.logPrintTime = l;
        }
    }

    private void renderFps() {
        ttfont = TextScr.font();
        ttcolor = TextScr.color();
        TextScr.setFont(TTFont.font[TTFont.SMALL]);
        TextScr.setColor(FPS_COLOR);
        this.renderText(10, 0, "min", "" + (int) Math.floor(this.fpsMin == FPS_MIN_DEFAULT ? this.fpsCur : this.fpsMin), false);
        this.renderText(10, 1, "average", "" + (int) Math.floor((1000D * (this.framePrev - this.frameStart)) / (this.timePrev - this.timeStart)), false);
        this.renderText(10, 2, "max", "" + (int) Math.floor(this.fpsMax), false);
        this.renderText(10, 3, "\u2190 total", null, false);
        this.renderText(10, 4, "frame#", "" + (this.framePrev - this.frameStart), false);
        this.renderText(10, 5, "FPS", "" + (int) Math.floor(this.fpsCur), false);
        this.renderText(10, 6, "recent \u2192", null, false);
        this.renderText(10, 7, "min", "" + (int) Math.floor(this.fpsMinRecent == FPS_MIN_DEFAULT ? this.fpsCur : this.fpsMinRecent), false);
        this.renderText(10, 8, "average", "" + (int) Math.floor((1000D * (this.framePrev - ((Integer) this.frameList.get(0)).intValue())) / (this.timePrev - ((Long) this.timeList.get(0)).longValue())), false);
        this.renderText(10, 9, "max", "" + (int) Math.floor(this.fpsMaxRecent), false);
        TextScr.setFont(ttfont);
        TextScr.setColor(ttcolor);
    }

    private void renderText(int slotMax, int slot, String caption, String value, boolean changeFont) {
        Render render = (Render) Actor.getByName("renderTextScr");
        if (render == null) {
            return;
        }
        if (changeFont) {
            ttfont = TextScr.font();
            ttcolor = TextScr.color();
        }
        int viewPortWidth = render.getViewPortWidth();
        int viewPortXOffset = 0;
        if (viewPortWidth > VIEWPORT_WIDTH_MAX) {
            viewPortXOffset = (viewPortWidth - VIEWPORT_WIDTH_MAX) / 2;
            viewPortWidth = VIEWPORT_WIDTH_MAX;
        }
        int viewPortHeight = render.getViewPortHeight();
        if (changeFont) {
            TextScr.setFont(TTFont.font[TTFont.SMALL]);
            TextScr.setColor(FPS_COLOR);
        }
        int widthSegment = viewPortWidth / slotMax;
        int widthCaption = (int) TTFont.font[TTFont.SMALL].width(caption);
        int captionMargin = value == null ? (CAPTION_MARGIN + VALUE_MARGIN) / 2 : CAPTION_MARGIN;
        int heightCaption = viewPortHeight - TTFont.font[TTFont.SMALL].height() - captionMargin;
        int widthCaptionOffset = viewPortXOffset + widthSegment * slot + (widthSegment - widthCaption) / 2;
        TextScr.output(widthCaptionOffset, heightCaption, caption);
        if (value != null) {
            int widthValue = (int) TTFont.font[TTFont.SMALL].width(value);
            int heightValue = viewPortHeight - TTFont.font[TTFont.SMALL].height() - VALUE_MARGIN;
            int widthValueOffset = viewPortXOffset + widthSegment * slot + (widthSegment - widthValue) / 2;
            TextScr.output(widthValueOffset, heightValue, value);
        }
        if (changeFont) {
            TextScr.setFont(ttfont);
            TextScr.setColor(ttcolor);
        }
    }

    public Object exec(CmdEnv cmdenv, Map map) {
        boolean flag = false;
        this.checkMsg();
        if (map.containsKey("SHOW")) {
            this.bShow = true;
            int tmpRecentResetTimeout = arg(map, "SHOW", 0, (int) RECENT_RESET_DEFAULT);
            if (tmpRecentResetTimeout < 1) {
                tmpRecentResetTimeout = 1;
            }
            this.recentResetTimeout = tmpRecentResetTimeout;
            System.out.println("Enhanced FPS Counter Recent Reset Timeout = " + this.recentResetTimeout + " milliseconds.");
            flag = true;
        } else if (map.containsKey("HIDE")) {
            this.bShow = false;
            flag = true;
        }
        if (map.containsKey("LOG")) {
            int i = arg(map, "LOG", 0, 5);
            if (i < 0) {
                i = 0;
            }
            this.logPeriod = i * 1000L;
            flag = true;
        }
        if (map.containsKey("LOGEXT")) {
            int tmpLogExt = arg(map, "LOGEXT", 0, 1);
            this.logExt = (tmpLogExt==1);
            int tmpLogExtPeriod = arg(map, "LOGEXT", 1, (int) LOG_EXT_PERIOD_DEFAULT);
            if (tmpLogExtPeriod < 1) {
                tmpLogExtPeriod = 1;
            }
            this.logExtPeriod = tmpLogExtPeriod;
            String logLine = "Enhanced FPS Logging ";
            if (!this.logExt) logLine+= "de";
            logLine+= "activated";
            if (this.logExt) {
                logLine+= ", file logging period = " + this.logExtPeriod + " milliseconds";
            }
            logLine+= ".";
            System.out.println(logLine);
            flag = true;
        }
        if (map.containsKey("PERF")) {
            AudioDevice.setPerformInfo(true);
            flag = true;
        }
        if (map.containsKey("START")) {
            if (this.bGo && (this.timeStart != this.timePrev) && (this.logPeriod == 0L)) {
                this.INFO_HARD("fps:" + this.fpsInfo());
            }
            this.fpsMin = this.fpsMinRecent = FPS_MIN_DEFAULT;
            this.fpsMax = this.fpsCur = this.fpsLast = this.fpsMaxRecent = 0.0D;
            this.timePrev = this.timePrevLogExt = this.logPrintTime = this.timeStart = Time.real();
            this.framePrev = this.frameStart = RendersMain.frame();
            this.fpsMinValid = false;
            this.initFrameTimeList();
            this.bGo = true;
            flag = true;
        } else if (map.containsKey("STOP")) {
            if (this.bGo && (this.timeStart != this.timePrev) && (this.logPeriod == 0L)) {
                this.INFO_HARD("fps:" + this.fpsInfo());
            }
            this.bGo = false;
            flag = true;
        }
        if (!flag) {
            this.INFO_HARD("  LOG  " + (this.logPeriod / 1000L));
            if (this.bShow) {
                this.INFO_HARD("  SHOW");
            } else {
                this.INFO_HARD("  HIDE");
            }
            if (this.bGo) {
                if (this.timeStart != this.timePrev) {
                    this.INFO_HARD("  " + this.fpsInfo());
                }
            } else {
                this.INFO_HARD("  STOPPED");
            }
        }
        return CmdEnv.RETURN_OK;
    }

    private String fpsInfo() {
     // @formatter:off
        return "" + (int) Math.floor(this.fpsCur)
             + " avg:" + (int) Math.floor((1000D * (this.framePrev - this.frameStart)) / (this.timePrev - this.timeStart))
             + " max:" + (int) Math.floor(this.fpsMax)
             + " min:" + (int) Math.floor(this.fpsMin == FPS_MIN_DEFAULT ? this.fpsCur : this.fpsMin)
             + " # RECENT"
             + " avg:" + (int) Math.floor((1000D * (this.framePrev - ((Integer) this.frameList.get(0)).intValue())) / (this.timePrev - ((Long) this.timeList.get(0)).longValue()))
             + " max:" + (int) Math.floor(this.fpsMaxRecent)
             + " min:" + (int) Math.floor(this.fpsMinRecent == FPS_MIN_DEFAULT ? this.fpsCur : this.fpsMinRecent)
             + " #"
             + " #fr:" + (this.framePrev - this.frameStart);
     // @formatter:on
    }

    private void checkMsg() {
        if (this.msg == null) {
            this.msg = new MsgTimeOut();
            this.msg.setListener(this);
            this.msg.setNotCleanAfterSend();
            this.msg.setFlags(72);
        }
        if (!this.msg.busy()) {
            this.msg.post();
        }
    }

    public CmdFPS() {
        this.bGo = false;
        this.bShow = false;
        this.logPeriod = 5000L;
        this.logPrintTime = -1L;
        this.logExtPeriod = LOG_EXT_PERIOD_DEFAULT;
        this.missionName = "unknown";
        this.lastFpsMax = 0;
        this.lastFpsMin = 0;
        this.logExt = false;
        this.missionPlaying = false;
        this.recentResetTimeout = RECENT_RESET_DEFAULT;
        this.param.put(LOG, null);
        this.param.put(LOGEXT, null);
        this.param.put(START, null);
        this.param.put(STOP, null);
        this.param.put(SHOW, null);
        this.param.put(HIDE, null);
        this.param.put(PERF, null);
        this._properties.put("NAME", "fps");
        this._levelAccess = 1;
    }

    private void checkMissionStatus() {
        boolean missionActive = Mission.isPlaying();
        if (this.missionPlaying != missionActive) {
            this.missionPlaying = missionActive;
            if (!missionActive) {
                this.flushFpsLogFile();
                this.missionName = "unknown";
            } else {
                this.frameStart = this.framePrev = RendersMain.frame();
                this.fpsMin = this.fpsMinRecent = FPS_MIN_DEFAULT;
                this.fpsMax = this.fpsCur = this.fpsLast = this.fpsMaxRecent = 0.0D;
                this.timePrev = this.timePrevLogExt = this.logPrintTime = this.timeStart = Time.real();
                this.fpsMinValid = false;
                this.initFrameTimeList();
            }
        }
    }

    private String getMissionName() {
        if (!Mission.isPlaying())
            return "unknown";
        String curMissionName = Mission.cur().name();
        int lastSeparatorPos = Math.max(curMissionName.lastIndexOf("/"), curMissionName.lastIndexOf("\\"));
        if (lastSeparatorPos != -1)
            curMissionName = curMissionName.substring(lastSeparatorPos + 1);
        return curMissionName;
    }

    private void updateFrameTimeList(int i, long l) {
        this.frameList.add(new Integer(i));
        this.timeList.add(new Long(l));
        while (l >= (this.recentResetTimeout + ((Long) this.timeList.get(0)).longValue())) {
            this.timeList.remove(0);
            this.frameList.remove(0);
        }
    }

    private void initFrameTimeList() {
        if (this.frameList == null) {
            this.frameList = new ArrayList();
        }
        if (this.timeList == null) {
            this.timeList = new ArrayList();
        }
        this.frameList.clear();
        this.timeList.clear();
    }

    public static String convertMillisecondsToHMmSs(long milliseconds) {
        long ms = milliseconds % 1000L;
        long s = (milliseconds / 1000L) % 60L;
        long m = (milliseconds / (1000L * 60L)) % 60L;
        long h = (milliseconds / (1000L * 60L * 60L)) % 24L;
        return df2.format(h) + ":" + df2.format(m) + ":" + df2.format(s) + "." + df3.format(ms);
    }

    private void logFpsToFile() {
        if (this.fpsLogWriter == null) {
            this.missionName = getMissionName();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh-mm-ss z");
            Date date = new Date();
            try {
                this.fpsLogWriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(FPS_LOG_FILE + " (" + this.missionName + " @ " + dateFormat.format(date) + ")" + FPS_LOG_FILE_EXT, 0))));
                this.fpsLogWriter.println("Time,FPS Current,FPS Average,FPS Max,FPS Min,Recent FPS Average,Recent FPS Max,Recent FPS Min,Frame");
            } catch (IOException IOE) {
                System.out.println("*** FPS DEBUG: Creating FPS Log File failed: " + IOE.getMessage());
            }
        }
        // @formatter:off
        this.fpsLogWriter.println(
                convertMillisecondsToHMmSs(Time.real() - this.timeStart) + ","
              + (int) Math.floor(this.fpsCur) + ","
              + (int) Math.floor((1000D * (this.framePrev - this.frameStart)) / (this.timePrev - this.timeStart)) + ","
              + (int) Math.floor(this.fpsMax) + ","
              + (int) Math.floor(this.fpsMin == FPS_MIN_DEFAULT ? this.fpsCur : this.fpsMin) + ","
              + (int) Math.floor((1000D * (this.framePrev - ((Integer) this.frameList.get(0)).intValue())) / (this.timePrev - ((Long) this.timeList.get(0)).longValue())) + ","
              + (int) Math.floor(this.fpsMaxRecent) + ","
              + (int) Math.floor(this.fpsMinRecent == FPS_MIN_DEFAULT ? this.fpsCur : this.fpsMinRecent) + ","
              + (this.framePrev - this.frameStart));
        // @formatter:on
    }

    public void flushFpsLogFile() {
        if (this.fpsLogWriter == null) {
            return;
        }
        this.fpsLogWriter.flush();
        this.fpsLogWriter.close();
        this.fpsLogWriter = null;
    }

    private static final double        FPS_MIN_DEFAULT        = 1000000D;
    private static final String        FPS_LOG_FILE           = "FPS_LOG";
    private static final String        FPS_LOG_FILE_EXT       = ".csv";
    private static final long          RECENT_RESET_DEFAULT   = 10000;
    private static final long          LOG_EXT_PERIOD_DEFAULT = 250L;
    private PrintWriter                fpsLogWriter           = null;
    private static final int           VIEWPORT_WIDTH_MAX     = 600;
    private static final int           CAPTION_MARGIN         = -1;
    private static final int           VALUE_MARGIN           = 8;
    private static final Color4f       FPS_COLOR              = new Color4f(0.0F, 1.0F, 1.0F, 1.0F);
    private static TTFont              ttfont;
    private static Color4f             ttcolor;
    private boolean                    bGo;
    private boolean                    bShow;
    private long                       timeStart;
    private int                        frameStart;
    private double                     fpsMin;
    private double                     fpsMax;
    private double                     fpsMinRecent;
    private double                     fpsMaxRecent;
    private double                     fpsCur;
    private double                     fpsLast;
    private boolean                    fpsMinValid;
    private long                       timePrev;
    private long                       lastFpsMax;
    private long                       lastFpsMin;
    private int                        framePrev;
    private boolean                    logExt;
    private boolean                    missionPlaying;
    private String                     missionName;
    private long                       recentResetTimeout;
    private long                       logExtPeriod;
    private long                       timePrevLogExt;
    private long                       logPeriod;
    private long                       logPrintTime;
    private ArrayList                  frameList;
    private ArrayList                  timeList;
    private static final DecimalFormat df2                    = new DecimalFormat("00");
    private static final DecimalFormat df3                    = new DecimalFormat("000");
    public static final String         LOG                    = "LOG";
    public static final String         LOGEXT                 = "LOGEXT";
    public static final String         START                  = "START";
    public static final String         STOP                   = "STOP";
    public static final String         SHOW                   = "SHOW";
    public static final String         HIDE                   = "HIDE";
    public static final String         PERF                   = "PERF";
    private MsgTimeOut                 msg;
}
