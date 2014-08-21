/*
 * GUINetClientDBrief - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package com.maddox.il2.gui;

import java.util.ResourceBundle;

import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.AirportCarrier;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.ZutiSupportMethods_Multicrew;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.sound.AudioDevice;

public class GUINetClientDBrief extends GUIBriefing
{
	public void enter(GameState gamestate)
	{
		super.enter(gamestate);
		if (gamestate != null && (gamestate.id() == 43 || gamestate.id() == 36) && briefSound != null)
		{
			String string = Main.cur().currentMissionFile.get("MAIN", ("briefSound" + ((NetUser) NetEnv.host()).getArmy()));
			if (string != null)
				briefSound = string;
			CmdEnv.top().exec("music PUSH");
			CmdEnv.top().exec("music LIST " + briefSound);
			CmdEnv.top().exec("music PLAY");
		}
	}

	public void leave(GameState gamestate)
	{
		if (gamestate != null && (gamestate.id() == 36 || gamestate.id() == 43) && briefSound != null)
		{
			CmdEnv.top().exec("music POP");
			CmdEnv.top().exec("music STOP");
			briefSound = null;
		}
		super.leave(gamestate);
	}

	public void leavePop(GameState gamestate)
	{
		if (gamestate != null && gamestate.id() == 2 && briefSound != null)
		{
			CmdEnv.top().exec("music POP");
			CmdEnv.top().exec("music PLAY");
		}
		super.leavePop(gamestate);
	}

	protected void fillTextDescription()
	{
		/* empty */
	}

	public boolean isExistTextDescription()
	{
		return textDescription != null;
	}

	public void clearTextDescription()
	{
		textDescription = null;
	}

	public void setTextDescription(String string)
	{
		try
		{
			ResourceBundle resourcebundle = ResourceBundle.getBundle(string, RTSConf.cur.locale);
			textDescription = resourcebundle.getString("Description");
			prepareTextDescription(Army.amountNet());
		}
		catch (Exception exception)
		{
			textDescription = null;
			textArmyDescription = null;
		}
		wScrollDescription.resized();
	}

	protected String textDescription()
	{
		if (textArmyDescription == null)
			return null;
		NetUser netuser = (NetUser) NetEnv.host();
		int i = netuser.getBornPlace();
		if (i < 0 || World.cur().bornPlaces == null)
			return textArmyDescription[0];
		BornPlace bornplace = (BornPlace) World.cur().bornPlaces.get(i);
		return textArmyDescription[bornplace.army];
	}

	private boolean isValidBornPlace()
	{
		NetUser netuser = (NetUser) NetEnv.host();
		int i = netuser.getBornPlace();
		if (i < 0 || i >= World.cur().bornPlaces.size())
		{
			GUINetClientGuard guinetclientguard = (GUINetClientGuard) Main.cur().netChannelListener;
			guinetclientguard.curMessageBox = new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, i18n("brief.BornPlace"), i18n("brief.BornPlaceSelect"), 3, 0.0F);
			return false;
		}
		BornPlace bp = (BornPlace)World.cur().bornPlaces.get(i);
		int airdromeStay = netuser.getAirdromeStay();
		//TODO: Added by |ZUTI|: perform this check only on home bases that are not assigned to carriers
		if (bp != null && !bp.zutiAlreadyAssigned && airdromeStay < 0)
		{
			GUINetClientGuard guinetclientguard = (GUINetClientGuard) Main.cur().netChannelListener;
			guinetclientguard.curMessageBox = new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, i18n("brief.StayPlace"), i18n("brief.StayPlaceWait"), 3, 0.0F);
			return false;
		}
		return true;
	}

	//TODO: This method is rewritten by |ZUTI|. After all the checks are done, method forwards to original methd.
	protected void doNext()
	{
		//TODO: Modified by |ZUTI|: check if aircraft is available
		int result = ZutiSupportMethods_GUI.isValidArming();
		if (result != -1)
		{
			String strReason = "";
			switch (result)
			{
				case 0:
					strReason = i18n("mds.info.various");
				break;
				case 1:
					strReason = i18n("mds.info.fuel");
				break;
				case 2:
					strReason = i18n("mds.info.weapons");
				break;
				case 3:
					strReason = i18n("mds.info.country");
				break;
				case 4:
					strReason = i18n("mds.info.homeBase");
				break;
			}
			if( result != 4 )
				Main.stateStack().push(55);
			new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, i18n("mds.info.bornPlace"), i18n("mds.info.selectValidOptions") + " - " + strReason, 3, 0.0F);
			return;
		}

		//Request aircraft
		ZutiSupportMethods_GUI.CLIENT_BRIEFING_SCREEN = this;
		String acName = ZutiSupportMethods_Air.getAircraftI18NName((Class) Property.value(World.cur().userCfg.netAirName, "airClass", null));
		ZutiSupportMethods_NetSend.requestAircraft(acName);
	}
	
	protected void doNext_original()
	{
		//TODO: reset that object
		ZutiSupportMethods_GUI.CLIENT_BRIEFING_SCREEN = null;
		
		if (isValidBornPlace())
		{
			AirportCarrier airportcarrier = getCarrier((NetUser) NetEnv.host());
			BornPlace bornplace = ((BornPlace) World.cur().bornPlaces.get(((NetUser) NetEnv.host()).getBornPlace()));
			if (airportcarrier != null && !bornplace.zutiAirspawnOnly && World.cur().diffCur.Takeoff_N_Landing)
			{
				UserCfg usercfg = World.cur().userCfg;
				Class var_class = (Class) Property.value(usercfg.netAirName, "airClass", null);
				airportcarrier.setGuiCallback(this);
				airportcarrier.ship().requestLocationOnCarrierDeck((NetUser) NetEnv.host(), var_class.getName());
				GUINetClientGuard guinetclientguard = ((GUINetClientGuard) Main.cur().netChannelListener);
				guinetclientguard.curMessageBox = new GWindowMessageBox((Main3D.cur3D().guiManager.root), 20.0F, true, i18n("brief.StayPlace"), i18n("miss.ReFlyWait"), 4, 30.0F);
			}
			else
				fly(false);
		}
	}

	public void flyFromCarrier(boolean bool)
	{
		GUINetClientGuard guinetclientguard = (GUINetClientGuard) Main.cur().netChannelListener;
		guinetclientguard.curMessageBox.close(false);
		if (bool)
			fly(false);
		else
		{
			//TODO: Added by |ZUTI|: if the deck is full but we have airspawn enabled in that case, airstart
			//----------------------------------------------------------------------------------------------
			NetUser netuser = (NetUser) NetEnv.host();
			if( netuser != null )
			{
				int i = netuser.getBornPlace();
				if (i >= 0 && i < World.cur().bornPlaces.size())
				{
					BornPlace bp = (BornPlace)World.cur().bornPlaces.get(i);
					if( bp.zutiAirspawnIfQueueFull )
						fly(true);
				}
			}
			//----------------------------------------------------------------------------------------------
			guinetclientguard.curMessageBox = new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, i18n("brief.CarrierDeckFull"), i18n("brief.CarrierDeckFullWait"), 3, 0.0F);
		}
	}

	//TODO: Modified by |ZUTI|: added airstart parameter
	protected void fly(boolean airstart)
	{
		Main.cur().resetUser();
		NetUser netuser = (NetUser) NetEnv.host();
		int i = netuser.getBornPlace();
		int i_1_ = netuser.getAirdromeStay();
		UserCfg usercfg = World.cur().userCfg;
		netuser.checkReplicateSkin(usercfg.netAirName);
		netuser.checkReplicateNoseart(usercfg.netAirName);
		netuser.checkReplicatePilot();
		String string;
		if (usercfg.netTacticalNumber < 10)
			string = "0" + usercfg.netTacticalNumber;
		else
			string = "" + usercfg.netTacticalNumber;
		
		//TODO: Disabled - Original code
		/*
		CmdEnv.top().exec(
				"spawn " + ((Class) Property.value(usercfg.netAirName, "airClass", null)).getName() + " PLAYER NAME "
						+ (((NetUser) NetEnv.host()).netUserRegiment.isEmpty() ? usercfg.netRegiment : "") + usercfg.netSquadron + "0" + string + " WEAPONS " + usercfg.getWeapon(usercfg.netAirName)
						+ " BORNPLACE " + i + " STAYPLACE " + i_1_ + " FUEL " + usercfg.fuel + " OVR");
		*/
		//TODO: My code, with multicrew enabled
		CmdEnv.top().exec(
				"spawn " + ((Class)Property.value(usercfg.netAirName, "airClass", null)).getName() + " PLAYER NAME " + (((NetUser)NetEnv.host()).netUserRegiment.isEmpty() ? usercfg.netRegiment : "") + usercfg.netSquadron + "0" + string
						+ " WEAPONS " + usercfg.getWeapon(usercfg.netAirName) + " BORNPLACE " + i + " STAYPLACE " + i_1_ + " FUEL " + usercfg.fuel + (airstart ? " Z_AIRSTART " : " ") + (usercfg.bZutiMultiCrew ? " Z_MULTICREW " : " ") + (usercfg.bZutiMultiCrewAnytime ? "Z_MULTICREW_ANYTIME" : "") + " OVR");
		
		com.maddox.il2.objects.air.Aircraft aircraft = World.getPlayerAircraft();
		if (Actor.isValid(aircraft))
		{
			GUI.unActivate();
			HotKeyCmd.exec("aircraftView", "CockpitView");
			ForceFeedback.startMission();
			AudioDevice.soundsOn();
			Main.stateStack().change(43);
			
			//TODO: Added by |ZUTI|: sync your position with other crew members
			//---------------------------------------------------------------------------------------					 
			//Enable joystick!
			ZutiSupportMethods_Multicrew.setJojstickState();
			//---------------------------------------------------------------------
		}
	}

	protected void doLoodout()
	{
		if (isValidBornPlace())
		{
			GUIAirArming.stateId = 2;
			Main.stateStack().push(55);
		}
	}

	protected void doDiff()
	{
		Main.stateStack().push(41);
	}

	protected void doBack()
	{
		GUINetClientGuard guinetclientguard = (GUINetClientGuard) Main.cur().netChannelListener;
		if (guinetclientguard != null)
			guinetclientguard.dlgDestroy(new GUINetClientGuard.DestroyExec()
			{
				public void destroy(GUINetClientGuard guinetclientguard_3_)
				{
					guinetclientguard_3_.destroy(true);
				}
			});
	}

	protected void clientRender()
	{
		GUIBriefingGeneric.DialogClient dialogclient = dialogClient;
		GUIBriefingGeneric.DialogClient dialogclient_4_ = dialogclient;
		float f = dialogclient.x1024(5.0F);
		float f_5_ = dialogclient.y1024(633.0F);
		float f_6_ = dialogclient.x1024(160.0F);
		float f_7_ = dialogclient.y1024(48.0F);
		if (dialogclient != null)
		{
			/* empty */
		}
		dialogclient_4_.draw(f, f_5_, f_6_, f_7_, 1, i18n("brief.Disconnect"));
		GUIBriefingGeneric.DialogClient dialogclient_8_ = dialogclient;
		float f_9_ = dialogclient.x1024(194.0F);
		float f_10_ = dialogclient.y1024(633.0F);
		float f_11_ = dialogclient.x1024(208.0F);
		float f_12_ = dialogclient.y1024(48.0F);
		if (dialogclient != null)
		{
			/* empty */
		}
		dialogclient_8_.draw(f_9_, f_10_, f_11_, f_12_, 1, i18n("brief.Difficulty"));
		GUIBriefingGeneric.DialogClient dialogclient_13_ = dialogclient;
		float f_14_ = dialogclient.x1024(680.0F);
		float f_15_ = dialogclient.y1024(633.0F);
		float f_16_ = dialogclient.x1024(176.0F);
		float f_17_ = dialogclient.y1024(48.0F);
		if (dialogclient != null)
		{
			/* empty */
		}
		dialogclient_13_.draw(f_14_, f_15_, f_16_, f_17_, 1, i18n("brief.Arming"));
		super.clientRender();
	}

	protected void clientSetPosSize()
	{
		GUIBriefingGeneric.DialogClient dialogclient = dialogClient;
		bLoodout.setPosC(dialogclient.x1024(768.0F), dialogclient.y1024(689.0F));
	}

	public GUINetClientDBrief(GWindowRoot gwindowroot)
	{
		super(40);
		init(gwindowroot);
	}
}
