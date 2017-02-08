package com.maddox.il2.engine;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import com.maddox.il2.ai.RangeRandom;
import com.maddox.opengl.GLCaps;
import com.maddox.opengl.GLContext;
import com.maddox.opengl.GLContextException;
import com.maddox.opengl.GLInitCaps;
import com.maddox.opengl.Provider;
import com.maddox.opengl.ProviderException;
import com.maddox.opengl.gl;
import com.maddox.rts.CLASS;
import com.maddox.rts.Console;
import com.maddox.rts.Cpu86ID;
import com.maddox.rts.IniFile;
import com.maddox.rts.MainWin32;
import com.maddox.rts.NetChannel;
import com.maddox.rts.RTSConf;
import com.maddox.rts.RTSConfWin;
import com.maddox.rts.ScreenMode;
import com.maddox.rts.net.SocksUdpSocket;
import com.maddox.sound.AudioDevice;
import com.maddox.util.UnicodeTo8bit;

public class Config {

    public boolean isUse3Renders() {
        return this.windowUse3Renders && this.windowSaveAspect;
    }

    public void checkWindowUse3Renders() {
        if (!this.windowUse3Renders) {
            return;
        } else {
            this.windowUse3Renders = this.windowWidth == (this.windowHeight * 4);
            return;
        }
    }

    public static boolean isUSE_RENDER() {
        return bUseRender;
    }

    private void loadGlProvider() {
        String s = this.ini.get("GLPROVIDER", "GL", (String) null);
        if (s != null) {
            this.glLib = s;
        }
        this.gluLib = this.ini.get("GLPROVIDER", "GLU", (String) null);
    }

    public void load() throws GLContextException, ProviderException {
        this.loadNet();
        this.loadGame();
        this.loadConsole();
        if (isUSE_RENDER()) {
            this.windowWidth = this.ini.get("window", "width", this.windowWidth);
            this.windowHeight = this.ini.get("window", "height", this.windowHeight);
            this.windowColourBits = this.ini.get("window", "ColourBits", this.windowColourBits);
            this.windowDepthBits = this.ini.get("window", "DepthBits", this.windowDepthBits);
            this.windowStencilBits = this.ini.get("window", "StencilBits", this.windowStencilBits);
            this.windowFullScreen = this.ini.get("window", "FullScreen", this.windowFullScreen);
            this.windowChangeScreenRes = this.ini.get("window", "ChangeScreenRes", this.windowChangeScreenRes);
            this.windowEnableResize = this.ini.get("window", "EnableResize", this.windowEnableResize);
            this.windowEnableClose = this.ini.get("window", "EnableClose", this.windowEnableClose);
            this.windowSaveAspect = this.ini.get("window", "SaveAspect", this.windowSaveAspect);
            this.windowUse3Renders = this.ini.get("window", "Use3Renders", this.windowUse3Renders);
            this.windowUse3RendersUI = this.ini.get("window", "Use3RendersUI", this.windowUse3RendersUI);
            this.windowsWideScreenFoV = this.ini.get("window", "WideScreenFoV", this.windowsWideScreenFoV);
            this.windowsUIColor = this.ini.get("window", "UIColor", this.windowsUIColor, 0, 5);
            this.windowsUIDetail = this.ini.get("window", "UIDetail", this.windowsUIDetail, 0, 5);
            this.windowsUIBackground = this.countryToInt(this.ini.get("window", "UIBackground", this.getDefaultCountry()));
            this.bNoHudLog = this.ini.get("game", "NoHudLog", this.bNoHudLog);
            this.bNoSubTitles = this.ini.get("game", "NoSubTitles", this.bNoSubTitles);
            this.bNoChatter = this.ini.get("game", "NoChatter", this.bNoChatter);
            this.bRec = this.ini.get("game", "RecordingIndicator", this.bRec);
            this.screenshotType = this.ini.get("game", "ScreenshotType", this.screenshotType, 0, 2);
            this.mapAlpha = this.ini.get("game", "MapAlpha", this.mapAlpha, 0.0F, 1.0F);
            this.saveTrk = this.ini.get("game", "SaveTrk", this.saveTrk);
            this.checkWindowUse3Renders();
            if (this.windowChangeScreenRes) {
                this.windowFullScreen = true;
            } else {
                ScreenMode screenmode = ScreenMode.startup();
                if (this.windowColourBits != screenmode.colourBits()) {
                    this.windowColourBits = screenmode.colourBits();
                }
            }
            this.loadGlProvider();
            this.loadEngine();
            Provider.GLload(this.glLib);
            if (this.gluLib != null) {
                Provider.GLUload(this.gluLib);
            }
            this.beforeLoadSound();
        }
    }

    public void save() {
        if (isUSE_RENDER()) {
            this.ini.setValue("window", "width", Integer.toString(this.windowWidth));
            this.ini.setValue("window", "height", Integer.toString(this.windowHeight));
            this.ini.setValue("window", "ColourBits", Integer.toString(this.windowColourBits));
            this.ini.setValue("window", "DepthBits", Integer.toString(this.windowDepthBits));
            this.ini.setValue("window", "StencilBits", Integer.toString(this.windowStencilBits));
            this.ini.setValue("window", "FullScreen", this.windowFullScreen ? "1" : "0");
            this.ini.setValue("window", "ChangeScreenRes", this.windowChangeScreenRes ? "1" : "0");
            this.ini.setValue("window", "EnableResize", this.windowEnableResize ? "1" : "0");
            this.ini.setValue("window", "EnableClose", this.windowEnableClose ? "1" : "0");
            this.ini.setValue("window", "SaveAspect", this.windowSaveAspect ? "1" : "0");
            this.ini.setValue("window", "Use3Renders", this.windowUse3Renders ? "1" : "0");
            this.ini.setValue("window", "Use3RendersUI", this.windowUse3RendersUI ? "1" : "0");
            this.ini.setValue("window", "WideScreenFoV", this.windowsWideScreenFoV ? "1" : "0");
            this.ini.setValue("window", "UIColor", Integer.toString(this.windowsUIColor));
            this.ini.setValue("window", "UIDetail", Integer.toString(this.windowsUIDetail));
            this.ini.setValue("window", "UIBackground", this.intToCountry(this.windowsUIBackground));
            this.ini.setValue("game", "ScreenshotType", Integer.toString(this.screenshotType));
            this.ini.setValue("game", "NoHudLog", this.bNoHudLog ? "1" : "0");
            this.ini.setValue("game", "NoSubTitles", this.bNoSubTitles ? "1" : "0");
            this.ini.setValue("game", "NoChatter", this.bNoChatter ? "1" : "0");
            this.ini.setValue("game", "RecordingIndicator", this.bRec ? "1" : "0");
            this.ini.setValue("game", "MapAlpha", Float.toString(this.mapAlpha));
            this.ini.setValue("game", "SaveTrk", this.saveTrk ? "1" : "0");
            this.ini.setValue("game", "IconUnits", Integer.toString(this.iconUnits));
            this.ini.setValue("GLPROVIDER", "GL", this.glLib);
            if (this.gluLib != null) {
                this.ini.setValue("GLPROVIDER", "GLU", this.gluLib);
            }
            this.saveSound();
            this.saveEngine();
        }
        this.saveConsole();
        this.saveNet();
        this.ini.saveFile();
    }

    private void loadNet() {
        this.netLocalPort = this.ini.get("NET", "localPort", this.netLocalPort, 1000, 65000);
        this.netRemotePort = this.ini.get("NET", "remotePort", this.netRemotePort, 1000, 65000);
        this.netSpeed = this.ini.get("NET", "speed", this.netSpeed, 300, 0xf4240);
        this.netLocalHost = this.ini.get("NET", "localHost", this.netLocalHost);
        if (this.netLocalHost != null) {
            try {
                InetAddress inetaddress = InetAddress.getByName(this.netLocalHost);
                DatagramSocket datagramsocket = new DatagramSocket(this.netLocalPort, inetaddress);
                datagramsocket.close();
            } catch (Exception exception) {
                System.out.println("Unknown net address: " + this.netLocalHost);
                this.netLocalHost = null;
            }
        }
        if (isUSE_RENDER()) {
            this.netRemotePort = this.ini.get("NET", "remotePort", this.netRemotePort, 1000, 65000);
            this.netRemoteHost = this.ini.get("NET", "remoteHost", this.netRemoteHost);
            this.netRouteChannels = this.ini.get("NET", "routeChannels", this.netRouteChannels, 0, 16);
            this.netServerChannels = this.ini.get("NET", "serverChannels", this.netServerChannels, 1, 127);
            this.zutiLoadServers();
        } else {
            this.netServerChannels = this.ini.get("NET", "serverChannels", this.netServerChannels, 1, 128);
        }
        this.netSkinDownload = this.ini.get("NET", "SkinDownload", this.netSkinDownload);
        this.netServerName = UnicodeTo8bit.load(this.ini.get("NET", "serverName", ""));
        this.netServerDescription = UnicodeTo8bit.load(this.ini.get("NET", "serverDescription", ""));
        NetChannel.bCheckServerTimeSpeed = this.ini.get("NET", "checkServerTimeSpeed", NetChannel.bCheckServerTimeSpeed);
        NetChannel.bCheckClientTimeSpeed = this.ini.get("NET", "checkClientTimeSpeed", NetChannel.bCheckClientTimeSpeed);
        NetChannel.checkTimeSpeedDifferense = this.ini.get("NET", "checkTimeSpeedDifferense", (float) NetChannel.checkTimeSpeedDifferense, 0.01F, 1000F);
        NetChannel.checkTimeSpeedInterval = this.ini.get("NET", "checkTimeSpeedInterval", NetChannel.checkTimeSpeedInterval, 1, 1000);
        SocksUdpSocket.setProxyHost(this.ini.get("NET", "socksHost", (String) null));
        SocksUdpSocket.setProxyPort(this.ini.get("NET", "socksPort", 1080));
        SocksUdpSocket.setProxyUser(this.ini.get("NET", "socksUser", (String) null));
        SocksUdpSocket.setProxyPassword(this.ini.get("NET", "socksPwd", (String) null));
    }

    private void saveNet() {
        this.ini.setValue("NET", "localPort", "" + this.netLocalPort);
        this.ini.setValue("NET", "speed", "" + this.netSpeed);
        if (isUSE_RENDER()) {
            this.ini.setValue("NET", "remotePort", "" + this.netRemotePort);
            this.ini.setValue("NET", "remoteHost", this.netRemoteHost);
            this.ini.setValue("NET", "routeChannels", "" + this.netRouteChannels);
            for (int i = 0; i < this.zutiServerNames.size(); i++) {
                String s = "";
                if (i < 10) {
                    s = "00" + Integer.toString(i);
                } else if ((i > 9) && (i < 100)) {
                    s = "0" + Integer.toString(i);
                } else if (i > 99) {
                    s = Integer.toString(i);
                }
                this.ini.setValue("NET", "remoteHost_" + s, (String) this.zutiServerNames.get(i));
            }

        }
        this.ini.setValue("NET", "serverChannels", "" + this.netServerChannels);
        this.ini.setValue("NET", "SkinDownload", this.netSkinDownload ? "1" : "0");
        this.ini.setValue("NET", "serverName", UnicodeTo8bit.save(this.netServerName, false));
        this.ini.setValue("NET", "serverDescription", UnicodeTo8bit.save(this.netServerDescription, false));
        if (SocksUdpSocket.getProxyHost() != null) {
            this.ini.setValue("NET", "socksHost", SocksUdpSocket.getProxyHost());
        } else {
            this.ini.setValue("NET", "socksHost", "");
        }
        if (SocksUdpSocket.getProxyPort() != 1080) {
            this.ini.setValue("NET", "socksPort", "" + SocksUdpSocket.getProxyPort());
        } else {
            this.ini.deleteValue("NET", "socksPort");
        }
        if (SocksUdpSocket.getProxyUser() != null) {
            this.ini.setValue("NET", "socksUser", SocksUdpSocket.getProxyUser());
        } else {
            this.ini.deleteValue("NET", "socksUser");
        }
        if (SocksUdpSocket.getProxyPassword() != null) {
            this.ini.setValue("NET", "socksPwd", SocksUdpSocket.getProxyPassword());
        } else {
            this.ini.deleteValue("NET", "socksPwd");
        }
    }

    private void loadGame() {
        this.b3dgunners = this.ini.get("game", "3dgunners", this.b3dgunners);
        this.clear_cache = this.ini.get("game", "ClearCache", this.clear_cache);
        this.newCloudsRender = this.ini.get("game", "TypeClouds", this.newCloudsRender);
        this.Mouse6DOFMove = this.ini.get("game", "Mouse6DOFMove", this.Mouse6DOFMove);
    }

    private void loadConsole() {
        RTSConf.cur.console.bWrap = this.ini.get("Console", "WRAP", 1, 0, 1) == 1;
        RTSConf.cur.console.setMaxHistoryOut(this.ini.get("Console", "HISTORY", 128, 0, 10000));
        RTSConf.cur.console.setMaxHistoryCmd(this.ini.get("Console", "HISTORYCMD", 128, 0, 10000));
        RTSConf.cur.console.setPageHistoryOut(this.ini.get("Console", "PAGE", 20, 1, 100));
        RTSConf.cur.console.setPause(this.ini.get("Console", "PAUSE", 1, 0, 1) == 1);
        RTSConf.cur.console.setLogKeep(this.ini.get("Console", "LOGKEEP", 1, 0, 1) == 1);
        String s = this.ini.getValue("Console", "LOGFILE");
        if (s.length() > 0) {
            RTSConf.cur.console.setLogFileName(s);
        } else {
            RTSConf.cur.console.setLogFileName("log.lst");
        }
        RTSConf.cur.console.log(this.ini.get("Console", "LOG", 0, 0, 1) == 1);
        Console.setTypeTimeInLogFile(this.ini.get("Console", "LOGTIME", 0, 0, 1) == 1);
        s = this.ini.getValue("Console", "LOAD");
        if (s.length() > 0) {
            RTSConf.cur.console.load(s);
        }
    }

    private void saveConsole() {
        if (RTSConf.cur.console.getEnv().levelAccess() == 0) {
            this.ini.setValue("Console", "WRAP", Integer.toString(RTSConf.cur.console.bWrap ? 1 : 0));
            this.ini.setValue("Console", "PAUSE", Integer.toString(RTSConf.cur.console.isPaused() ? 1 : 0));
            this.ini.setValue("Console", "PAGE", Integer.toString(RTSConf.cur.console.pageHistoryOut()));
        }
        this.ini.setValue("Console", "HISTORY", Integer.toString(RTSConf.cur.console.maxHistoryOut()));
        this.ini.setValue("Console", "HISTORYCMD", Integer.toString(RTSConf.cur.console.maxHistoryCmd()));
        this.ini.setValue("Console", "LOGFILE", RTSConf.cur.console.logFileName());
        this.ini.setValue("Console", "LOG", Integer.toString(RTSConf.cur.console.isLog() ? 1 : 0));
        this.ini.setValue("Console", "LOGKEEP", Integer.toString(RTSConf.cur.console.isLogKeep() ? 1 : 0));
        this.ini.setValue("Console", "LOGTIME", Integer.toString(Console.isTypeTimeInLogFile() ? 1 : 0));
        String s = this.ini.getValue("Console", "SAVE");
        if (s.length() > 0) {
            RTSConf.cur.console.save(s);
        }
        RTSConf.cur.console.log(false);
    }

    public void loadEngine() {
        this.loadEngine(this.ini.get("GLPROVIDER", "GL", (String) null));
    }

    public void loadEngine(String s) {
        if (s == null) {
            s = this.glLib;
        }
        String s1 = this.ini.get("GLPROVIDERS", "DirectX", "");
        String s2 = "Render_OpenGL";
        if ((s1 != null) && s.equalsIgnoreCase(s1)) {
            s2 = "Render_DirectX";
        }
        RenderContext.loadConfig(this.ini, s2);
        this.drawGroundTracks = this.ini.get(s2, "DrawGroundTracks", this.drawGroundTracks);
    }

    private void saveEngine() {
        RenderContext.saveConfig();
    }

    public boolean isDebugSound() {
        return this.bDebugSound;
    }

    public boolean isSoundUse() {
        return this.bSoundUse;
    }

    private void beforeLoadSound() {
        this.bSoundUse = this.ini.get("sound", "SoundUse", false);
        this.bDebugSound = this.ini.get("sound", "DebugSound", this.bDebugSound);
    }

    private void saveSound() {
        this.ini.setValue("sound", "SoundUse", this.bSoundUse ? "1" : "0");
        AudioDevice.saveControls(this.ini, "sound");
    }

    public void beginSound() {
        AudioDevice.setMessageMode(this.bDebugSound ? 1 : 0);
        AudioDevice.loadControls(this.ini, "sound");
        if (this.bSoundUse) {
            this.bSoundUse = false;
            if ((RTSConf.cur != null) && (RTSConf.cur instanceof RTSConfWin)) {
                int i = RTSConf.cur.mainWindow.hWnd();
                if (i != 0) {
                    if (AudioDevice.initialize(i, engineDllName() + ".dll")) {
                        this.bSoundUse = true;
                    } else {
                        System.err.println("AudioDevice NOT initialized");
                    }
                }
            }
        } else {
            AudioDevice.initialize(0, engineDllName() + ".dll");
        }
        AudioDevice.resetControls();
    }

    public void endSound() {
        if (this.bSoundUse) {
            AudioDevice.done();
        }
    }

    public GLInitCaps getGLCaps() {
        GLInitCaps glinitcaps = new GLInitCaps();
        glinitcaps.setDoubleBuffered(true);
        glinitcaps.setColourBits(this.windowColourBits);
        glinitcaps.setDepthBits(this.windowDepthBits);
        glinitcaps.setStencilBits(this.windowStencilBits);
        return glinitcaps;
    }

    public void setGLCaps(GLCaps glcaps) {
        this.windowColourBits = glcaps.getColourBits();
        this.windowDepthBits = glcaps.getDepthBits();
        this.windowStencilBits = glcaps.getStencilBits();
    }

    public GLContext createGlContext(GLContext glcontext, boolean flag, boolean flag1, int i, int j, int k, int l, int i1) throws GLContextException {
        ScreenMode screenmode = ScreenMode.readCurrent();
        if (flag) {
            if (ScreenMode.set(i, j, k)) {
                i = ScreenMode.current().width();
                j = ScreenMode.current().height();
                k = ScreenMode.current().colourBits();
            } else {
                throw new GLContextException("ScreenMode NOT changed");
            }
        } else if (ScreenMode.current() != ScreenMode.startup()) {
            ScreenMode.restore();
        }
        boolean flag2 = false;
        if (flag1) {
            flag2 = ((MainWin32) RTSConf.cur.mainWindow).create(this.windowTitle, i, j);
        } else {
            flag2 = ((MainWin32) RTSConf.cur.mainWindow).create(this.windowTitle, this.windowEnableClose, this.windowEnableResize, i, j);
        }
        if (!flag2) {
            ScreenMode.set(screenmode);
            throw new GLContextException("Window NOT created");
        }
        i = RTSConf.cur.mainWindow.width();
        j = RTSConf.cur.mainWindow.height();
        GLInitCaps glinitcaps = new GLInitCaps();
        glinitcaps.setDoubleBuffered(true);
        glinitcaps.setColourBits(k);
        glinitcaps.setDepthBits(l);
        glinitcaps.setStencilBits(i1);
        try {
            if (glcontext != null) {
                glcontext.changeWin32(glinitcaps, RTSConf.cur.mainWindow.hWnd(), true, i, j);
            } else {
                glcontext = new GLContext(glinitcaps);
                glcontext.createWin32(RTSConf.cur.mainWindow.hWnd(), true, i, j);
            }
        } catch (GLContextException glcontextexception) {
            ((MainWin32) RTSConf.cur.mainWindow).destroy();
            ScreenMode.set(screenmode);
            throw glcontextexception;
        }
        this.windowFullScreen = flag1;
        this.windowChangeScreenRes = flag;
        this.windowWidth = RTSConf.cur.mainWindow.width();
        this.windowHeight = RTSConf.cur.mainWindow.height();
        this.windowColourBits = glcontext.getCaps().getColourBits();
        this.windowDepthBits = glcontext.getCaps().getDepthBits();
        this.windowStencilBits = glcontext.getCaps().getStencilBits();
        this.checkWindowUse3Renders();
        return glcontext;
    }

    public GLContext createGlContext(String s) throws GLContextException {
        this.windowTitle = s;
        return this.createGlContext(null, this.windowChangeScreenRes, this.windowFullScreen, this.windowWidth, this.windowHeight, this.windowColourBits, this.windowDepthBits, this.windowStencilBits);
    }

    public static void typeCurrentScreenMode() {
        ScreenMode screenmode = ScreenMode.current();
        System.err.println("Current screen mode: " + screenmode.width() + "x" + screenmode.height() + "x" + screenmode.colourBits());
    }

    public static void typeScreenModes() {
        ScreenMode ascreenmode[] = ScreenMode.all();
        System.err.print("Screen modes: ");
        for (int i = 0; i < ascreenmode.length; i++) {
            if ((i % 4) == 0) {
                System.err.println("");
            }
            System.err.print("\t" + i + " " + ascreenmode[i].width() + "x" + ascreenmode[i].height() + "x" + ascreenmode[i].colourBits());
        }

        System.err.println("");
    }

    public static void typeGLCaps() {
        GLCaps aglcaps[] = Provider.getGLCaps();
        System.err.println("Caps OpenGL library:");
        for (int i = 0; i < aglcaps.length; i++) {
            System.err.print(i);
            System.err.print(aglcaps[i].getDevice() != 1 ? "  DRAW_TO_BITMAP" : "  DRAW_TO_WINDOW");
            System.err.print(aglcaps[i].isDoubleBuffered() ? "  DOUBLEBUFFER" : "  SINGLEBUFFER");
            System.err.print(aglcaps[i].isStereo() ? "  STEREO" : "  NOSTEREO");
            System.err.println(aglcaps[i].getPixelType() != 1 ? "  TYPE_COLOURINDEX" : "  TYPE_RGBA");
            System.err.print("  ColourBits: " + aglcaps[i].getColourBits());
            System.err.print("  AlphaBits: " + aglcaps[i].getAlphaBits());
            System.err.print("  AccumBits: " + aglcaps[i].getAccumBits());
            System.err.print("  DepthBits: " + aglcaps[i].getDepthBits());
            System.err.println("  StencilBits: " + aglcaps[i].getStencilBits());
        }

    }

    public static void typeGlStrings() {
        System.err.println("OpenGL library:");
        System.err.println("  Vendor: " + gl.GetString(7936));
        System.err.println("  Render: " + gl.GetString(7937));
        System.err.println("  Version: " + gl.GetString(7938));
        System.err.println("  Extensions: " + gl.GetString(7939));
    }

    public static void typeProvider() {
        System.err.println("OpenGL provider: " + Provider.GLname());
        try {
            System.err.println("GLU provider: " + Provider.GLUname());
        } catch (Exception exception) {
        }
    }

    public void typeContextSettings(GLContext glcontext) {
        System.err.println("Size: " + glcontext.width() + "x" + glcontext.height());
        System.err.println("ColorBits: " + glcontext.getCaps().getColourBits());
        System.err.println("DepthBits: " + glcontext.getCaps().getDepthBits());
        System.err.println("StencilBits: " + glcontext.getCaps().getStencilBits());
        System.err.println("isDoubleBuffered: " + glcontext.getCaps().isDoubleBuffered());
    }

    public static boolean isAppEditor() {
        return cur.bAppEditor;
    }

    public static String engineDllName() {
        if (CLASS.ser() != 0) {
            if ((Cpu86ID.getVendor() == 1) && Cpu86ID.isSSE2()) {
                return "il2_coreP4";
            } else {
                return "il2_core";
            }
        } else {
            Cpu86ID.getVendor();
            return "il2_server";
        }
    }

    public Config(IniFile inifile, boolean flag) {
        this.ini = null;
        this.mainSection = "il2";
        this.windowTitle = "Il2";
        this.windowWidth = 640;
        this.windowHeight = 480;
        this.windowColourBits = 16;
        this.windowDepthBits = 16;
        this.windowStencilBits = 0;
        this.windowFullScreen = true;
        this.windowChangeScreenRes = true;
        this.windowEnableResize = false;
        this.windowEnableClose = false;
        this.windowSaveAspect = true;
        this.windowUse3Renders = false;
        this.windowUse3RendersUI = false;
        this.windowsWideScreenFoV = true;
        this.windowsUIColor = 0;
        this.windowsUIDetail = 0;
        this.windowsUIBackground = 0;
        this.screenshotType = 0;
        this.bNoHudLog = false;
        this.bNoSubTitles = false;
        this.bNoChatter = false;
        this.bRec = false;
        this.mapAlpha = 1.0F;
        this.saveTrk = true;
        this.iconUnits = 0;
        this.drawGroundTracks = true;
        this.glLib = "opengl32.dll";
        this.gluLib = null;
        this.netLocalPort = 21000;
        this.netRemotePort = 21000;
        this.netLocalHost = null;
        this.netRemoteHost = "";
        this.netSpeed = 5000;
        this.netRouteChannels = 0;
        this.netServerChannels = 8;
        this.netSkinDownload = true;
        this.netServerName = "";
        this.netServerDescription = "";
        this.b3dgunners = false;
        this.clear_cache = true;
        this.newCloudsRender = true;
        this.Mouse6DOFMove = false;
        this.zutiServerNames = null;
        this.bSoundUse = false;
        this.bDebugSound = false;
        this.bAppEditor = false;
        this.zutiServerNames = new ArrayList();
        bUseRender = flag;
        GObj.loadNative();
        this.ini = inifile;
        this.load();
    }

    private void zutiLoadServers() {
        for (int i = 0; i < 255; i++) {
            String s = "";
            if (i < 10) {
                s = "00" + Integer.toString(i);
            } else if ((i > 9) && (i < 100)) {
                s = "0" + Integer.toString(i);
            } else if (i > 99) {
                s = Integer.toString(i);
            }
            String s1 = this.ini.get("NET", "remoteHost_" + s, "");
            if (s1.trim().length() > 0) {
                this.zutiServerNames.add(s1);
            }
        }

    }

    public ArrayList zutiGetServerNames() {
        return this.zutiServerNames;
    }

    public void zutiAddServerName(String s) {
        if (!this.zutiServerNames.contains(s)) {
            this.zutiServerNames.add(s);
        }
    }

    private String getDefaultCountry() {
        String s = RTSConf.cur.locale.getLanguage();
        if ("ru".equalsIgnoreCase(s)) {
            return "ru";
        }
        if ("de".equalsIgnoreCase(s)) {
            return "de";
        } else {
            return "en";
        }
    }

    private int countryToInt(String s) {
        if (s.equals("en")) {
            return 0;
        }
        if (s.equals("ru")) {
            return 1;
        }
        if (s.equals("de")) {
            return 2;
        }
        if (s.equals("jp")) {
            return 3;
        }
        return !s.equals("rnd") ? 0 : 4;
    }

    private String intToCountry(int i) {
        if (i == 0) {
            return "en";
        }
        if (i == 1) {
            return "ru";
        }
        if (i == 2) {
            return "de";
        }
        if (i == 3) {
            return "jp";
        }
        if (i == 4) {
            return "rnd";
        } else {
            return "en";
        }
    }

    public String getBackgroundCountry(int i) {
        if (i == 4) {
            return this.intToCountry((new RangeRandom(System.currentTimeMillis())).nextInt(0, 3));
        } else {
            return this.intToCountry(i);
        }
    }

    public static String getVersion() {
        return _VERSION;
    }

    public static int getTrackSingleVersion() {
        return _TRACK_SINGLE_VERSION;
    }

    public static int           debugEffects                  = 0;
    public static final boolean _MDEBUG_                      = false;
    public static final boolean _MDEBUG_DEV_                  = false;
    public static final String  JRE                           = "JRE";
    public static final boolean AEROMASH                      = false;
    public static final String  PRODUCT                       = "FB_PF";
    private static final String _VERSION                      = "4.13.3";
    private static final int    _TRACK_SINGLE_VERSION         = 37;
    public static final int     TRACK_NET_VERSION             = 109;
    public static final boolean TRACK_CHECK                   = false;
    public static final String  VERSION                       = "4.13.3m";
    public static final String  NET_VERSION                   = "FB_PF_v_4.13.3m";
    public static final int     TRACK_SINGLE_VERSION          = 137;
    public static String        LOCALE                        = "RU";
    public static final boolean bCHECK_LOCALE                 = true;
    public static final String  RELEASE                       = "ON";
    public static final String  PROTECT                       = "OFF";
    public static final boolean bCHECK_EXPIRED                = false;
    public static final int     EXPIRED_YEAR                  = 2005;
    public static final int     EXPIRED_MONTH                 = 11;
    public static final int     EXPIRED_DAY                   = 1;
    public static Config        cur;
    public static final int     ADAPTER_INTERPOLATOR_TICK_POS = -1000;
    public static final int     ADAPTER_COLLISION_TICK_POS    = -999;
    public static final int     ADAPTER_RENDER_TICK_POS       = 0x7fffffff;
    public static final String  CONSOLE                       = "Console";
    public static final String  C_WRAP                        = "WRAP";
    public static final String  C_HISTORY                     = "HISTORY";
    public static final String  C_HISTORYCMD                  = "HISTORYCMD";
    public static final String  C_PAGE                        = "PAGE";
    public static final String  C_PAUSE                       = "PAUSE";
    public static final String  C_LOG                         = "LOG";
    public static final String  C_LOGKEEP                     = "LOGKEEP";
    public static final String  C_LOGTIME                     = "LOGTIME";
    public static final String  C_LOGFILE                     = "LOGFILE";
    public static final String  C_LOAD                        = "LOAD";
    public static final String  C_SAVE                        = "SAVE";
    public static final String  WINDOW                        = "window";
    public static final String  WIDTH                         = "width";
    public static final String  HEIGHT                        = "height";
    public static final String  COLOURBITS                    = "ColourBits";
    public static final String  DEPTHBITS                     = "DepthBits";
    public static final String  STENCILBITS                   = "StencilBits";
    public static final String  FULLSCREEN                    = "FullScreen";
    public static final String  CHANGESCREENRES               = "ChangeScreenRes";
    public static final String  ENABLERESIZE                  = "EnableResize";
    public static final String  ENABLECLOSE                   = "EnableClose";
    public static final String  SAVEASPECT                    = "SaveAspect";
    public static final String  USE3RENDERS                   = "Use3Renders";
    public static final String  USE3RENDERSUI                 = "Use3RendersUI";
    public static final String  WIDESCREENFOV                 = "WideScreenFoV";
    public static final String  UICOLOR                       = "UIColor";
    public static final String  UIDETAIL                      = "UIDetail";
    public static final String  UIBACKGROUND                  = "UIBackground";
    public static final String  SCREENSHOTTYPE                = "ScreenshotType";
    public static final String  MAPALPHA                      = "MapAlpha";
    public static final String  NOHUDLOG                      = "NoHudLog";
    public static final String  NOSUBTITLES                   = "NoSubTitles";
    public static final String  NOCHATTER                     = "NoChatter";
    public static final String  REC                           = "RecordingIndicator";
    public static final String  SAVETRK                       = "SaveTrk";
    public static final String  DRAWGROUNDTRACKS              = "DrawGroundTracks";
    public static final String  ICON_UNITS                    = "IconUnits";
    public static final String  GLPROVIDER                    = "GLPROVIDER";
    public static final String  GL_LIB                        = "GL";
    public static final String  GLU_LIB                       = "GLU";
    public static final String  SOUND                         = "sound";
    public static final String  SOUND_USE                     = "SoundUse";
    public static final String  SOUND_DEBUG                   = "DebugSound";
    public static final String  NET                           = "NET";
    public static final String  NET_LOCAL_PORT                = "localPort";
    public static final String  NET_LOCAL_HOST                = "localHost";
    public static final String  NET_REMOTE_PORT               = "remotePort";
    public static final String  NET_REMOTE_HOST               = "remoteHost";
    public static final String  NET_SPEED                     = "speed";
    public static final String  NET_ROUTE_CHANNELS            = "routeChannels";
    public static final String  NET_SERVER_CHANNELS           = "serverChannels";
    public static final String  NET_SKIN_DOWNLOAD             = "SkinDownload";
    public static final String  NET_SERVER_NAME               = "serverName";
    public static final String  NET_SERVER_DESCRIPTION        = "serverDescription";
    public static final String  NET_SOCKS_HOST                = "socksHost";
    public static final String  NET_SOCKS_PORT                = "socksPort";
    public static final String  NET_SOCKS_USER                = "socksUser";
    public static final String  NET_SOCKS_PWD                 = "socksPwd";
    public static final String  GAME                          = "game";
    public static final String  GAME_3DGUNNERS                = "3dgunners";
    public static final String  GAME_CLEARCACHE               = "ClearCache";
    public static final String  GAME_TYPECLOUDS               = "TypeClouds";
    public static final String  GAME_MOUSE6DOFMOVE            = "Mouse6DOFMove";
    public IniFile              ini;
    public String               mainSection;
    public String               windowTitle;
    public int                  windowWidth;
    public int                  windowHeight;
    public int                  windowColourBits;
    public int                  windowDepthBits;
    public int                  windowStencilBits;
    public boolean              windowFullScreen;
    public boolean              windowChangeScreenRes;
    public boolean              windowEnableResize;
    public boolean              windowEnableClose;
    public boolean              windowSaveAspect;
    public boolean              windowUse3Renders;
    public boolean              windowUse3RendersUI;
    public boolean              windowsWideScreenFoV;
    public int                  windowsUIColor;
    public int                  windowsUIDetail;
    public int                  windowsUIBackground;
    public int                  screenshotType;
    public boolean              bNoHudLog;
    public boolean              bNoSubTitles;
    public boolean              bNoChatter;
    public boolean              bRec;
    public float                mapAlpha;
    public boolean              saveTrk;
    public int                  iconUnits;
    public boolean              drawGroundTracks;
    public String               glLib;
    public String               gluLib;
    public int                  netLocalPort;
    public int                  netRemotePort;
    public String               netLocalHost;
    public String               netRemoteHost;
    public int                  netSpeed;
    public int                  netRouteChannels;
    public int                  netServerChannels;
    public boolean              netSkinDownload;
    public String               netServerName;
    public String               netServerDescription;
    public boolean              b3dgunners;
    public boolean              clear_cache;
    public boolean              newCloudsRender;
    public boolean              Mouse6DOFMove;
    private static boolean      bUseRender                    = false;
    public ArrayList            zutiServerNames;
    protected boolean           bSoundUse;
    private boolean             bDebugSound;
    public boolean              bAppEditor;

}
