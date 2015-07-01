//////////////////////////////////////////////////////////////////////
//	By PAL - MODded to parse EnterPop for Arming
//	Review to 4.111m
//  lifted to 4.12 by SAS~Storebror
//////////////////////////////////////////////////////////////////////

package com.maddox.il2.gui;

import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.Main;
import com.maddox.rts.CmdEnv;

public class GUISingleBriefing extends GUIBriefing
{

    public void enterPush(GameState gamestate)
    {
        World.cur().diffUser.set(World.cur().userCfg.singleDifficulty);
        clientRender();//By PAL
        super.enterPush(gamestate);
        if(briefSound != null)
        {
            CmdEnv.top().exec("music PUSH");
            CmdEnv.top().exec("music LIST " + briefSound);
            CmdEnv.top().exec("music PLAY");
        }         
    }

    public void enterPop(GameState gamestate)
    {
        if(gamestate.id() == 17)
        {
            World.cur().userCfg.singleDifficulty = World.cur().diffUser.get();
            World.cur().userCfg.saveConf();
        }                
        client.activateWindow();       
        super.enterPop(gamestate); //By PAL, necessary for GUIBriefingGeneric
	/*GWindowMessageBox gwindowmessagebox =
		new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, "Error", "gamestate:" +
			gamestate.id(), 3, 0.0F); */                      
    }

    protected void doNext()
    {
        if(briefSound != null)
        {
            CmdEnv.top().exec("music POP");
            CmdEnv.top().exec("music STOP");
        }
        Main.stateStack().change(5);
    }

    protected void doLoodout()
    {
        Main.stateStack().push(54);
    }

    protected void doDiff()
    {
        Main.stateStack().push(17);
    }

    protected void doBack()
    {
        if(briefSound != null)
        {
            CmdEnv.top().exec("music POP");
            CmdEnv.top().exec("music PLAY");
        }
        Main.stateStack().pop();
    }

    protected void clientRender()
    {
        GUIBriefingGeneric.DialogClient dialogclient = dialogClient;
        dialogclient.draw(dialogclient.x1024(0.0F), dialogclient.y1024(633F), dialogclient.x1024(170F), dialogclient.y1024(48F), 1, i18n("brief.Back"));
        dialogclient.draw(dialogclient.x1024(194F), dialogclient.y1024(633F), dialogclient.x1024(208F), dialogclient.y1024(48F), 1, i18n("brief.Difficulty"));
        dialogclient.draw(dialogclient.x1024(680F), dialogclient.y1024(633F), dialogclient.x1024(176F), dialogclient.y1024(48F), 1, i18n("brief.Arming"));
        super.clientRender();
    }

    protected void clientSetPosSize()
    {
        GUIBriefingGeneric.DialogClient dialogclient = dialogClient;
        bLoodout.setPosC(dialogclient.x1024(768F), dialogclient.y1024(689F));
    }

    public GUISingleBriefing(GWindowRoot gwindowroot)
    {
        super(4);
        init(gwindowroot);
    }
}
