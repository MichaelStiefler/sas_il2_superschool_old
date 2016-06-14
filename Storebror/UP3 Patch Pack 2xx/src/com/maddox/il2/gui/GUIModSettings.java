/*
 * New Mod Settings GUI by SAS~Storebror
 */

package com.maddox.il2.gui;

import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.DifficultySettings;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.Main;
import com.maddox.rts.RTSConf;
import com.maddox.rts.RTSConfWin;
import com.maddox.sas1946.il2.util.Reflection;

public class GUIModSettings extends GameState {
    public GUIClient     client;
    public DialogClient  dialogClient;
    public GUIInfoMenu   infoMenu;
    public GUIInfoName   infoName;
    public GUISwitchBox3 sStabsForAll;
    public GUISwitchBox3 sNewTIR;
    public GUISwitchBox3 sHighSpeedNet;
    public GUISwitchBox3 sAddDefaultCountryNone;

    public GUIButton     bExit;
    
    private boolean      bUseTrackIR;


    public class DialogClient extends GUIDialogClient {
        public boolean notify(GWindow gwindow, int i, int i_0_) {
            if (i != 2)
                return super.notify(gwindow, i, i_0_);
            if (gwindow == bExit) {
                Main.stateStack().pop();
                return true;
            }
            return super.notify(gwindow, i, i_0_);
        }

        public void render() {
            super.render();
            GUISeparate.draw(this, GColor.Gray, x1024(32.0F), y1024(464.0F), x1024(768.0F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(32.0F), y1024(544.0F), x1024(768.0F), 2.0F);
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(96.0F), y1024(577.0F), x1024(224.0F), y1024(48.0F), 0, GUIModSettings.this.i18n("diff.Back"));
            draw(x1024(128.0F), y1024(32.0F), x1024(272.0F), y1024(48.0F), 0, "Stabilizers on all Aircraft");
            draw(x1024(128.0F), y1024(96.0F), x1024(272.0F), y1024(48.0F), 0, "Use Network File Transfer Boost");
            draw(x1024(128.0F), y1024(160.0F), x1024(272.0F), y1024(48.0F), 0, "Add Default Country \"None\"");
            if (bUseTrackIR) draw(x1024(128.0F), y1024(224.0F), x1024(272.0F), y1024(48.0F), 0, "Use new TrackIR code");
        }

        public void setPosSize() {
            set1024PosSize(92.0F, 72.0F, 832.0F, 656.0F);
            sStabsForAll.setPosC(x1024(88.0F), y1024(56.0F));
            sHighSpeedNet.setPosC(x1024(88.0F), y1024(120.0F));
            sAddDefaultCountryNone.setPosC(x1024(88.0F), y1024(184.0F));
            if (bUseTrackIR) sNewTIR.setPosC(x1024(88.0F), y1024(248.0F));
            bExit.setPosC(x1024(56.0F), y1024(602.0F));
        }
    }

    public void enterPush(GameState gamestate) {
        _enter();
    }

    protected DifficultySettings settings() {
        return World.cur().diffUser;
    }

    public void _enter() {
        reset();
        sStabsForAll.setEnable(true);
        sHighSpeedNet.setEnable(true);
        sAddDefaultCountryNone.setEnable(true);
        if (bUseTrackIR) sNewTIR.setEnable(true);
        client.activateWindow();
    }

    private void reset() {
        sStabsForAll.setChecked(Config.cur.bStabs4All, false);
        sHighSpeedNet.setChecked(Config.cur.bNetBoost, false);
        sAddDefaultCountryNone.setChecked(Config.cur.bAddDefaultCountryNone, false);
        if (bUseTrackIR) sNewTIR.setChecked(Config.cur.bNewTrackIR, false);
    }

    public void _leave() {
        Config.cur.bStabs4All = sStabsForAll.bChecked;
        Config.cur.bNetBoost = sHighSpeedNet.bChecked;
        if (sHighSpeedNet.bChecked)
            Config.cur.netSpeed = Config.NET_SPEED_HIGH;
        Config.cur.bAddDefaultCountryNone = sAddDefaultCountryNone.bChecked;
        if (bUseTrackIR) {
            ((RTSConfWin)RTSConf.cur).trackIRWin.destroy();
            Config.cur.bNewTrackIR = sNewTIR.bChecked;
            ((RTSConfWin)RTSConf.cur).trackIRWin.create();
            Reflection.setBoolean(((RTSConfWin)RTSConf.cur).trackIR, "bExist", ((RTSConfWin)RTSConf.cur).trackIRWin.isCreated());
        }
        client.hideWindow();
    }

    protected void clientInit(GWindowRoot gwindowroot) {
    }

    public GUIModSettings(GWindowRoot gwindowroot) {
        this(gwindowroot, MOD_SETTINGS);
    }

    protected GUIModSettings(GWindowRoot gwindowroot, int i) {
        super(i);
        if (RTSConf.cur.isUseTrackIR()) {
            if (RTSConf.cur instanceof RTSConfWin) {
                this.bUseTrackIR = true;
            }
        }
        client = (GUIClient) gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient) client.create(new DialogClient());
        infoMenu = (GUIInfoMenu) client.create(new GUIInfoMenu());
        infoMenu.info = i18n("diff.info");
        infoName = (GUIInfoName) client.create(new GUIInfoName());
        GTexture gtexture = ((GUILookAndFeel) gwindowroot.lookAndFeel()).buttons2;
        bExit = (GUIButton) dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96.0F, 48.0F, 48.0F));
        sStabsForAll = ((GUISwitchBox3) dialogClient.addControl(new GUISwitchBox3(dialogClient)));
        sHighSpeedNet = ((GUISwitchBox3) dialogClient.addControl(new GUISwitchBox3(dialogClient)));
        if (bUseTrackIR) sNewTIR = ((GUISwitchBox3) dialogClient.addControl(new GUISwitchBox3(dialogClient)));
        sAddDefaultCountryNone = ((GUISwitchBox3) dialogClient.addControl(new GUISwitchBox3(dialogClient)));
        clientInit(gwindowroot);
        dialogClient.activateWindow();
        client.hideWindow();
    }
}