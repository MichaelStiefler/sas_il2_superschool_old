package com.maddox.il2.gui;

import java.util.ArrayList;
import java.util.HashMap;

import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.gwindow.GWindowTable;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.net.NetChannelListener;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.USGS;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.Finger;
import com.maddox.rts.MsgAction;
import com.maddox.rts.MsgAddListener;
import com.maddox.rts.MsgInvokeMethod;
import com.maddox.rts.MsgNetExtListener;
import com.maddox.rts.MsgRemoveListener;
import com.maddox.rts.NetAddress;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetControl;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetSocket;
import com.maddox.rts.Time;
import com.maddox.rts.net.IPAddress;
import com.maddox.util.NumberTokenizer;
import com.maddox.util.UnicodeTo8bit;

public class GUINetNewClient extends GameState implements NetChannelListener, MsgNetExtListener {
    public class DlgServerPassword extends GWindowFramed {

        public void doOk() {
            long l = Finger.incLong(0L, this.publicKey);
            l = Finger.incLong(l, this.pw.getValue());
            ((NetControl) NetEnv.cur().control).doAnswer("SP " + l);
            GUINetNewClient.this.doStartWaitDlg();
        }

        public void doCancel() {
            GUINetNewClient.this.connectMessgeBox = null;
            NetEnv.cur().connect.joinBreak();
        }

        public void afterCreated() {
            this.clientWindow = this.create(new GWindowDialogClient() {

                public boolean notify(GWindow gwindow, int i, int j) {
                    if (i != 2) {
                        return super.notify(gwindow, i, j);
                    }
                    if (gwindow == DlgServerPassword.this.bOk) {
                        DlgServerPassword.this.doOk();
                        this.close(false);
                        return true;
                    }
                    if (gwindow == DlgServerPassword.this.bCancel) {
                        DlgServerPassword.this.doCancel();
                        this.close(false);
                        return true;
                    } else {
                        return super.notify(gwindow, i, j);
                    }
                }

            });
            GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) this.clientWindow;
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 10F, 1.5F, GUINetNewClient.this.i18n("netnc.Password") + " ", null));
            gwindowdialogclient.addControl(this.pw = new GWindowEditControl(gwindowdialogclient, 12F, 1.0F, 8F, 1.5F, null));
            this.pw.bPassword = true;
            this.pw.bCanEditTab = false;
            gwindowdialogclient.addDefault(this.bOk = new GWindowButton(gwindowdialogclient, 4F, 4F, 6F, 2.0F, GUINetNewClient.this.i18n("netnc.Ok"), null));
            gwindowdialogclient.addEscape(this.bCancel = new GWindowButton(gwindowdialogclient, 12F, 4F, 6F, 2.0F, GUINetNewClient.this.i18n("netnc.Cancel"), null));
            super.afterCreated();
            this.resized();
            this.showModal();
        }

        GWindowEditControl pw;
        GWindowButton      bOk;
        GWindowButton      bCancel;
        String             publicKey;

        public DlgServerPassword(GWindow gwindow, String s) {
            this.bSizable = false;
            this.publicKey = s;
            this.title = GUINetNewClient.this.i18n("netnc.EnterPassword");
            float f = 22F;
            float f1 = 10F;
            float f2 = gwindow.win.dx / gwindow.lookAndFeel().metric();
            float f3 = gwindow.win.dy / gwindow.lookAndFeel().metric();
            float f4 = (f2 - f) / 2.0F;
            float f5 = (f3 - f1) / 2.0F;
            this.doNew(gwindow, f4, f5, f, f1, true);
        }
    }

    public class DialogClient extends GUIDialogClient {

        public boolean notify(GWindow gwindow, int i, int j) {
            if (i != 2) {
                return super.notify(gwindow, i, j);
            }
            if (gwindow == GUINetNewClient.this.bJoin) {
                GUINetNewClient.this.doJoin();
                return true;
            }
            if (gwindow == GUINetNewClient.this.bSearch) {
                if (!GUINetNewClient.this.bExistSearch) {
                    CmdEnv.top().exec("socket LISTENER 0");
                    String s = "socket udp CREATE LOCALPORT " + Config.cur.netLocalPort;
                    if ((Config.cur.netLocalHost != null) && (Config.cur.netLocalHost.length() > 0)) {
                        s = s + " LOCALHOST " + Config.cur.netLocalHost;
                    }
                    CmdEnv.top().exec(s);
                    if ((NetEnv.socketsBlock().size() + NetEnv.socketsNoBlock().size()) <= 0) {
                        return true;
                    }
                    if (GUINetNewClient.broadcastAdr == null) {
                        try {
                            GUINetNewClient.broadcastAdr = new IPAddress();
                            GUINetNewClient.broadcastAdr.create("255.255.255.255");
                        } catch (Exception exception) {
                            GUINetNewClient.broadcastAdr = null;
                            System.out.println(exception.getMessage());
                            exception.printStackTrace();
                            return true;
                        }
                    }
                    GUINetNewClient.this.wTable.showWindow();
                    GUINetNewClient.this.bSearch.hideWindow();
                    GUINetNewClient.this.bExistSearch = true;
                    this.setPosSize();
                    MsgAddListener.post(64, NetEnv.cur(), Main.state(), null);
                    GUINetNewClient.this.onMsgTimeout();
                }
                return true;
            }
            if (gwindow == GUINetNewClient.this.bExit) {
                CmdEnv.top().exec("socket LISTENER 0");
                CmdEnv.top().exec("socket udp DESTROY LOCALPORT " + Config.cur.netLocalPort);
                ((NetUser) NetEnv.host()).reset();
                Main.stateStack().pop();
                return true;
            } else {
                return super.notify(gwindow, i, j);
            }
        }

        public void render() {
            super.render();
            if (GUINetNewClient.this.bExistSearch) {
                GUISeparate.draw(this, GColor.Gray, this.x1024(32F), this.y1024(368F), this.x1024(896F), 2.0F);
            } else {
                GUISeparate.draw(this, GColor.Gray, this.x1024(32F), this.y1024(208F), this.x1024(480F), 2.0F);
            }
            this.setCanvasColor(GColor.Gray);
            this.setCanvasFont(0);
            if (GUINetNewClient.this.bExistSearch) {
                this.draw(this.x1024(304F), this.y1024(256F), this.x1024(352F), this.y1024(32F), 1, GUINetNewClient.this.i18n("netnc.Server"));
                this.draw(this.x1024(672F), this.y1024(400F), this.x1024(192F), this.y1024(48F), 2, GUINetNewClient.this.i18n("netnc.Join"));
                this.draw(this.x1024(96F), this.y1024(400F), this.x1024(160F), this.y1024(48F), 0, GUINetNewClient.this.i18n("netnc.Back"));
            } else {
                if (GUINetNewClient.this.bSearch.isVisible()) {
                    this.draw(this.x1024(96F), this.y1024(32F), this.x1024(416F), this.y1024(48F), 0, GUINetNewClient.this.i18n("netnc.Search"));
                }
                if (!USGS.isUsed() && (Main.cur().netGameSpy == null)) {
                    this.draw(this.x1024(96F), this.y1024(96F), this.x1024(352F), this.y1024(32F), 1, GUINetNewClient.this.i18n("netnc.Server"));
                }
                if (GUINetNewClient.this.bJoin.isVisible()) {
                    this.draw(this.x1024(288F), this.y1024(240F), this.x1024(160F), this.y1024(48F), 2, GUINetNewClient.this.i18n("netnc.Join"));
                }
                this.draw(this.x1024(96F), this.y1024(240F), this.x1024(136F), this.y1024(48F), 0, !USGS.isUsed() && (Main.cur().netGameSpy == null) ? GUINetNewClient.this.i18n("netnc.MainMenu") : GUINetNewClient.this.i18n("main.Quit"));
            }
        }

        public void setPosSize() {
            if (GUINetNewClient.this.bExistSearch) {
                this.set1024PosSize(32F, 144F, 960F, 480F);
            } else {
                this.set1024PosSize(240F, 240F, 544F, 320F);
            }
            if (GUINetNewClient.this.bExistSearch) {
                GUINetNewClient.this.wTable.set1024PosSize(32F, 32F, 892F, 192F);
                GUINetNewClient.this.wZutiServersList.setPosSize(this.x1024(352F), this.y1024(304F), this.x1024(256F), this.M(1.7F));
                GUINetNewClient.this.bJoin.setPosC(this.x1024(904F), this.y1024(424F));
                GUINetNewClient.this.bExit.setPosC(this.x1024(56F), this.y1024(424F));
            } else {
                GUINetNewClient.this.bSearch.setPosC(this.x1024(56F), this.y1024(56F));
                GUINetNewClient.this.wZutiServersList.setPosSize(this.x1024(144F), this.y1024(144F), this.x1024(256F), this.M(1.7F));
                GUINetNewClient.this.bJoin.setPosC(this.x1024(488F), this.y1024(264F));
                GUINetNewClient.this.bExit.setPosC(this.x1024(56F), this.y1024(264F));
            }
        }

        public DialogClient() {
        }
    }

    public class Table extends GWindowTable {

        public int countRows() {
            return this.adrList == null ? 0 : this.adrList.size();
        }

        public void renderCell(int i, int j, boolean flag, float f, float f1) {
            this.setCanvasFont(0);
            if (flag) {
                this.setCanvasColorBLACK();
                this.draw(0.0F, 0.0F, f, f1, this.lookAndFeel().regionWhite);
            }
            String s = (String) this.adrList.get(i);
            Item item = (Item) this.serverMap.get(s);
            String s1 = null;
            int k = 0;
            switch (j) {
                case 0: // '\0'
                    s1 = s;
                    break;

                case 1: // '\001'
                    s1 = item.serverName;
                    break;

                case 2: // '\002'
                    s1 = "" + item.ping;
                    k = 1;
                    break;

                case 3: // '\003'
                    s1 = "" + item.existUsers + "/" + item.maxUsers;
                    k = 1;
                    break;

                case 4: // '\004'
                    if (item.bServer) {
                        if (item.bCoop) {
                            s1 = (item.bProtected ? "* " : "  ") + GUINetNewClient.this.i18n("netnc.Cooperative");
                        } else {
                            s1 = (item.bProtected ? "* " : "  ") + GUINetNewClient.this.i18n("netnc.Dogfight");
                        }
                    } else if (item.bCoop) {
                        s1 = (item.bProtected ? "* " : "  ") + GUINetNewClient.this.i18n("netnc.Cooperative_routing");
                    } else {
                        s1 = (item.bProtected ? "* " : "  ") + GUINetNewClient.this.i18n("netnc.Dogfight_routing");
                    }
                    break;
            }
            if (s1 != null) {
                if (flag) {
                    this.setCanvasColorWHITE();
                    this.draw(0.0F, 0.0F, f, f1, k, s1);
                } else {
                    this.setCanvasColorBLACK();
                    this.draw(0.0F, 0.0F, f, f1, k, s1);
                }
            }
        }

        public void setSelect(int i, int j) {
            super.setSelect(i, j);
            if (this.selectRow >= 0) {
                String s = (String) this.adrList.get(this.selectRow);
                GUINetNewClient.this.wZutiServersList.setValue(s, false);
            }
        }

        public void afterCreated() {
            super.afterCreated();
            this.bColumnsSizable = true;
            this.bSelectRow = true;
            this.addColumn(I18N.gui("netnc.Address"), null);
            this.addColumn(I18N.gui("netnc.Name"), null);
            this.addColumn(I18N.gui("netnc.Ping"), null);
            this.addColumn(I18N.gui("netnc.Users"), null);
            this.addColumn(I18N.gui("netnc.Type"), null);
            this.vSB.scroll = this.rowHeight(0);
            this.getColumn(0).setRelativeDx(8F);
            this.getColumn(1).setRelativeDx(10F);
            this.getColumn(2).setRelativeDx(4F);
            this.getColumn(3).setRelativeDx(4F);
            this.getColumn(4).setRelativeDx(8F);
            this.alignColumns();
            this.bNotify = true;
            this.wClient.bNotify = true;
            this.resized();
        }

        public void resolutionChanged() {
            this.vSB.scroll = this.rowHeight(0);
            super.resolutionChanged();
        }

        public HashMap   serverMap;
        public ArrayList adrList;

        public Table(GWindow gwindow) {
            super(gwindow);
            this.serverMap = new HashMap();
            this.adrList = new ArrayList();
        }
    }

    static class Item {

        public NetAddress adr;
        public int        port;
        public int        ping;
        public String     ver;
        public boolean    bServer;
        public int        iServerType;
        public boolean    bProtected;
        public boolean    bDedicated;
        public boolean    bCoop;
        public boolean    bMissionStarted;
        public int        maxChannels;
        public int        usedChannels;
        public int        maxUsers;
        public int        existUsers;
        public String     serverName;

        Item() {
        }
    }

    public void _enter() {
        this.bExistSearch = false;
        this.wTable.hideWindow();
        this.wTable.adrList.clear();
        this.wTable.serverMap.clear();
        Main.cur().netChannelListener = this;
        if (USGS.isUsed() || (Main.cur().netGameSpy != null)) {
            this.bSearch.hideWindow();
            this.wZutiServersList.hideWindow();
            this.bJoin.hideWindow();
        } else {
            this.bSearch.showWindow();
            this.wZutiServersList.setValue(Config.cur.netRemoteHost + ":" + Config.cur.netRemotePort, false);
            this.wZutiServersList.showWindow();
            this.bJoin.showWindow();
        }
        this.dialogClient.setPosSize();
        this.client.activateWindow();
        if (USGS.isUsed() || (Main.cur().netGameSpy != null)) {
            new MsgAction(64, Time.real() + 500L) {

                public void doAction() {
                    GUINetNewClient.this.doJoin();
                }

            };
        }
        ((NetUser) NetEnv.host()).reset();
    }

    public void _leave() {
        this.client.hideWindow();
        Main.cur().netChannelListener = null;
        if (this.bExistSearch) {
            MsgRemoveListener.post(64, NetEnv.cur(), this, null);
        }
    }

    public void netChannelCanceled(String s) {
        this.serverChannel = null;
        if (this.connectMessgeBox == null) {
            return;
        } else {
            this.connectMessgeBox.hideWindow();
            this.connectMessgeBox = new GWindowMessageBox(this.client.root, 20F, true, this.i18n("netnc.NotConnect"), s, 3, 0.0F) {

                public void result(int i) {
                    GUINetNewClient.this.connectMessgeBox = null;
                    if (USGS.isUsed() || (Main.cur().netGameSpy != null)) {
                        GUINetNewClient.this.bJoin.showWindow();
                    }
                }

            };
            return;
        }
    }

    public void netChannelCreated(NetChannel netchannel) {
        if (this.connectMessgeBox == null) {
            return;
        } else {
            this.serverChannel = netchannel;
            this.onChannelCreated();
            return;
        }
    }

    private void onChannelCreated() {
        this.connectMessgeBox.hideWindow();
        this.connectMessgeBox = new GWindowMessageBox(this.client.root, 20F, true, this.i18n("netnc.Connect"), this.i18n("netnc.ConnectSucc"), 3, 5F) {

            public void result(int i) {
                GUINetNewClient.this.connectMessgeBox = null;
                ((NetUser) NetEnv.host()).onConnectReady(GUINetNewClient.this.serverChannel);
                Main.stateStack().change(36);
                GUI.chatDlg.showWindow();
            }

        };
    }

    public void netChannelRequest(String s) {
        if (this.connectMessgeBox == null) {
            return;
        }
        NumberTokenizer numbertokenizer = new NumberTokenizer(s);
        String s1 = numbertokenizer.next("_");
        if ("SP".equals(s1)) {
            String s2 = numbertokenizer.next("0");
            this.connectMessgeBox.hideWindow();
            this.connectMessgeBox = new DlgServerPassword(this.client.root, s2);
        } else if (USGS.isUsed() && "NM".equals(s1)) {
            ((NetControl) NetEnv.cur().control).doAnswer("NM \"" + NetEnv.host().shortName() + '"');
        }
    }

    public void netChannelDestroying(NetChannel netchannel, String s) {
        this.netChannelCanceled(s);
    }

    public void onMsgTimeout() {
        if (!this.bExistSearch || (Main.state() != this)) {
            return;
        }
        if ((NetEnv.socketsBlock().size() + NetEnv.socketsNoBlock().size()) <= 0) {
            String s = "socket udp CREATE LOCALPORT " + Config.cur.netLocalPort;
            if ((Config.cur.netLocalHost != null) && (Config.cur.netLocalHost.length() > 0)) {
                s = s + " LOCALHOST " + Config.cur.netLocalHost;
            }
            CmdEnv.top().exec(s);
        }
        if ((NetEnv.socketsBlock().size() + NetEnv.socketsNoBlock().size()) <= 0) {
            return;
        }
        NetSocket netsocket = null;
        if (NetEnv.socketsNoBlock().size() > 0) {
            netsocket = (NetSocket) NetEnv.socketsNoBlock().get(0);
        } else {
            netsocket = (NetSocket) NetEnv.socketsBlock().get(0);
        }
        NetEnv.cur().postExtUTF((byte) 32, "rinfo " + Time.currentReal(), netsocket, broadcastAdr, Config.cur.netRemotePort);
        (new MsgInvokeMethod("onMsgTimeout")).post(64, this, 1.0D);
    }

    public void msgNetExt(byte abyte0[], NetSocket netsocket, NetAddress netaddress, int i) {
        if (!this.bExistSearch || (Main.state() != this)) {
            return;
        }
        if ((abyte0 == null) || (abyte0.length < 2)) {
            return;
        }
        if (abyte0[0] != 32) {
            return;
        }
        String s = "";
        try {
            _netMsgInput.setData(null, false, abyte0, 1, abyte0.length - 1);
            s = _netMsgInput.readUTF();
        } catch (Exception exception) {
            return;
        }
        NumberTokenizer numbertokenizer = new NumberTokenizer(s);
        if (!numbertokenizer.hasMoreTokens()) {
            return;
        }
        if (!"ainfo".equals(numbertokenizer.next())) {
            return;
        }
        Item item = new Item();
        item.adr = netaddress;
        item.port = i;
        long l = -1L;
        try {
            l = Long.parseLong(numbertokenizer.next());
        } catch (Exception exception1) {
            return;
        }
        item.ping = (int) (Time.currentReal() - l);
        if (item.ping < 0) {
            return;
        }
        item.ver = numbertokenizer.next("");
        item.bServer = numbertokenizer.next(false);
        item.iServerType = numbertokenizer.next(0);
        item.bProtected = numbertokenizer.next(false);
        item.bDedicated = numbertokenizer.next(false);
        item.bCoop = numbertokenizer.next(false);
        item.bMissionStarted = numbertokenizer.next(false);
        item.maxChannels = numbertokenizer.next(0);
        item.usedChannels = numbertokenizer.next(0);
        item.maxUsers = numbertokenizer.next(0);
        item.existUsers = numbertokenizer.next(0);
        item.serverName = "";
        if (numbertokenizer.hasMoreTokens()) {
            StringBuffer stringbuffer = new StringBuffer(numbertokenizer.next(""));
            for (; numbertokenizer.hasMoreTokens(); stringbuffer.append(numbertokenizer.next(""))) {
                stringbuffer.append(' ');
            }

            item.serverName = stringbuffer.toString();
        }
        String s1 = "" + item.adr.getHostAddress() + ":" + item.port;
        boolean flag = this.wTable.serverMap.containsKey(s1);
        this.wTable.serverMap.put(s1, item);
        if (!flag) {
            this.wTable.adrList.add(s1);
            this.wTable.resized();
        }
    }

    public void doJoin() {
        if (USGS.isUsed()) {
            NetEnv.cur();
            NetEnv.host().setShortName(USGS.name);
            this.serverAddress = USGS.serverIP;
        } else if (Main.cur().netGameSpy != null) {
            NetEnv.cur();
            if (Main.onlineUsernameOverride.length() > 0) {
                NetEnv.host().setShortName(UnicodeTo8bit.load(Main.onlineUsernameOverride));
                // NetEnv.host().setShortName(UnicodeTo8bit.load("ABC"));
            } else {
                NetEnv.host().setShortName(Main.cur().netGameSpy.userName);
            }
            this.serverAddress = Main.cur().netGameSpy.serverIP;
        } else {
            NetEnv.cur();
            if (Main.onlineUsernameOverride.length() > 0) {
                NetEnv.host().setShortName(UnicodeTo8bit.load(Main.onlineUsernameOverride));
                // NetEnv.host().setShortName(UnicodeTo8bit.load("DEF"));
            } else {
                NetEnv.host().setShortName(World.cur().userCfg.callsign);
            }
            this.serverAddress = this.wZutiServersList.getValue();
        }
        String s = this.serverAddress;
        if ((s == null) || (s.length() == 0)) {
            return;
        }
        int i = Config.cur.netRemotePort;
        int j = s.lastIndexOf(":");
        if ((j >= 0) && (j < (s.length() - 1))) {
            String s1 = s.substring(j + 1);
            s = s.substring(0, j);
            try {
                i = Integer.parseInt(s1);
            } catch (Exception exception) {
                s = this.serverAddress;
            }
        }
        CmdEnv.top().exec("socket LISTENER " + (Config.cur.netRouteChannels <= 0 ? 0 : 1));
        int k = Config.cur.netRouteChannels;
        if (k <= 0) {
            k = 1;
        } else {
            k++;
        }
        if (this.bExistSearch) {
            CmdEnv.top().exec("socket udp DESTROY LOCALPORT " + Config.cur.netLocalPort);
        }
        String s2 = "socket udp JOIN LOCALPORT " + Config.cur.netLocalPort + " PORT " + i + " SPEED " + Config.cur.netSpeed + " CHANNELS " + k + " HOST " + s;
        if ((Config.cur.netLocalHost != null) && (Config.cur.netLocalHost.length() > 0)) {
            s2 = s2 + " LOCALHOST " + Config.cur.netLocalHost;
        }
        CmdEnv.top().exec(s2);
        Config.cur.netRemoteHost = s;
        Config.cur.netRemotePort = i;
        Config.cur.zutiAddServerName(this.wZutiServersList.getValue());
        this.doStartWaitDlg();
    }

    private void doStartWaitDlg() {
        if (this.connectMessgeBox != null) {
            this.connectMessgeBox.close(false);
        }
        this.connectMessgeBox = new GWindowMessageBox(this.dialogClient.root, 20F, true, this.i18n("netnc.Connect"), this.i18n("netnc.ConnectWait"), 5, 0.0F) {

            public void result(int i) {
                if ((i == 1) && (GUINetNewClient.this.connectMessgeBox != null)) {
                    GUINetNewClient.this.connectMessgeBox = null;
                    NetEnv.cur().connect.joinBreak();
                    if (GUINetNewClient.this.serverChannel != null) {
                        GUINetNewClient.this.serverChannel.destroy();
                        GUINetNewClient.this.serverChannel = null;
                    }
                    return;
                } else {
                    return;
                }
            }

        };
    }

    public GUINetNewClient(GWindowRoot gwindowroot) {
        super(34);
        this.client = (GUIClient) gwindowroot.create(new GUIClient());
        this.dialogClient = (DialogClient) this.client.create(new DialogClient());
        this.infoMenu = (GUIInfoMenu) this.client.create(new GUIInfoMenu());
        this.infoMenu.info = this.i18n("netnc.info");
        this.infoName = (GUIInfoName) this.client.create(new GUIInfoName());
        this.wTable = new Table(this.dialogClient);
        this.wZutiServersList = (GWindowComboControl) this.dialogClient.addControl(new GWindowComboControl(this.dialogClient, 0.0F, 0.0F, 50F));
        this.wZutiServersList.list = Config.cur.zutiGetServerNames();
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel) gwindowroot.lookAndFeel()).buttons2;
        this.bSearch = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bJoin = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bExit = (GUIButton) this.dialogClient.addEscape(new GUIButton(this.dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        this.dialogClient.activateWindow();
        this.client.hideWindow();
    }

    public GUIClient           client;
    public DialogClient        dialogClient;
    public GUIInfoMenu         infoMenu;
    public GUIInfoName         infoName;
    public Table               wTable;
    public GUIButton           bSearch;
    public GUIButton           bJoin;
    public GUIButton           bExit;
    public GWindow             connectMessgeBox;
    public String              serverAddress;
    NetChannel                 serverChannel;
    GWindowComboControl        wZutiServersList;
    public boolean             bExistSearch;
    private static NetAddress  broadcastAdr;
    private static NetMsgInput _netMsgInput = new NetMsgInput();

}
