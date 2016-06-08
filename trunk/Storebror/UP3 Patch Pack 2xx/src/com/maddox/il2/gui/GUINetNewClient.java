/*4.10.1 class*/
package com.maddox.il2.gui;

import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.rts.MsgInvokeMethod;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.net.IPAddress;
import com.maddox.util.NumberTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import com.maddox.gwindow.GWindowComboControl;

public class GUINetNewClient extends com.maddox.il2.game.GameState implements com.maddox.il2.net.NetChannelListener, com.maddox.rts.MsgNetExtListener
{
	public com.maddox.il2.gui.GUIClient client;
	public com.maddox.il2.gui.GUINetNewClient.DialogClient dialogClient;
	public com.maddox.il2.gui.GUIInfoMenu infoMenu;
	public com.maddox.il2.gui.GUIInfoName infoName;
	public com.maddox.il2.gui.GUINetNewClient.Table wTable;
	public com.maddox.gwindow.GWindowEditControl wEdit;
	public com.maddox.il2.gui.GUIButton bSearch;
	public com.maddox.il2.gui.GUIButton bJoin;
	public com.maddox.il2.gui.GUIButton bExit;
	public com.maddox.gwindow.GWindow connectMessgeBox;
	public java.lang.String serverAddress;
	com.maddox.rts.NetChannel serverChannel;
	public boolean bExistSearch;
	private static com.maddox.rts.NetAddress broadcastAdr;
	private static com.maddox.rts.NetMsgInput _netMsgInput = new NetMsgInput();
	
	//TODO: |ZUTI| variables
	//------------------------------------
	GWindowComboControl wZutiServersList;
	GUIButton bZutiRemove;;
	//------------------------------------
	
	public class DlgServerPassword extends com.maddox.gwindow.GWindowFramed
	{

		public void doOk()
		{
			long l = com.maddox.rts.Finger.incLong(0L, publicKey);
			l = com.maddox.rts.Finger.incLong(l, pw.getValue());
			((com.maddox.rts.NetControl) com.maddox.rts.NetEnv.cur().control).doAnswer("SP " + l);
			doStartWaitDlg();
		}

		public void doCancel()
		{
			connectMessgeBox = null;
			com.maddox.rts.NetEnv.cur().connect.joinBreak();
		}

		public void afterCreated()
		{
			clientWindow = create(new com.maddox.gwindow.GWindowDialogClient()
			{

				public boolean notify(com.maddox.gwindow.GWindow gwindow, int i, int j)
				{
					if (i != 2) return super.notify(gwindow, i, j);
					if (gwindow == bOk)
					{
						doOk();
						close(false);
						return true;
					}
					if (gwindow == bCancel)
					{
						doCancel();
						close(false);
						return true;
					}
					else
					{
						return super.notify(gwindow, i, j);
					}
				}

			});
			com.maddox.gwindow.GWindowDialogClient gwindowdialogclient = (com.maddox.gwindow.GWindowDialogClient) clientWindow;
			gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 10F, 1.5F, i18n("netnc.Password") + " ", null));
			gwindowdialogclient.addControl(pw = new GWindowEditControl(gwindowdialogclient, 12F, 1.0F, 8F, 1.5F, null));
			pw.bPassword = true;
			pw.bCanEditTab = false;
			gwindowdialogclient.addDefault(bOk = new GWindowButton(gwindowdialogclient, 4F, 4F, 6F, 2.0F, i18n("netnc.Ok"), null));
			gwindowdialogclient.addEscape(bCancel = new GWindowButton(gwindowdialogclient, 12F, 4F, 6F, 2.0F, i18n("netnc.Cancel"), null));
			super.afterCreated();
			resized();
			showModal();
		}

		com.maddox.gwindow.GWindowEditControl pw;
		com.maddox.gwindow.GWindowButton bOk;
		com.maddox.gwindow.GWindowButton bCancel;
		java.lang.String publicKey;

		public DlgServerPassword(com.maddox.gwindow.GWindow gwindow, java.lang.String s)
		{
			bSizable = false;
			publicKey = s;
			title = i18n("netnc.EnterPassword");
			float f = 22F;
			float f1 = 10F;
			float f2 = gwindow.win.dx / gwindow.lookAndFeel().metric();
			float f3 = gwindow.win.dy / gwindow.lookAndFeel().metric();
			float f4 = (f2 - f) / 2.0F;
			float f5 = (f3 - f1) / 2.0F;
			doNew(gwindow, f4, f5, f, f1, true);
		}
	}

	public class DialogClient extends com.maddox.il2.gui.GUIDialogClient
	{

		public boolean notify(com.maddox.gwindow.GWindow gwindow, int i, int j)
		{
			if (i != 2) return super.notify(gwindow, i, j);
			//TODO: Added by |ZUTI|
			//------------------------------------
			if (gwindow == bZutiRemove)
			{
				if( wZutiServersList.list.size() < 2 )
					return true;
			
				int index = wZutiServersList.getSelected();
				ZutiSupportMethods_GUI.removeDropDownServer( wZutiServersList.getValue() );
				index--;
				if( index < 0 )
					index = 0;
				wZutiServersList.list.clear();
				wZutiServersList.list = (ArrayList)ZutiSupportMethods_GUI.getDropDownServersList();
				wZutiServersList.setSelected(index, true, false);
				return true;
			}
			//------------------------------------
			if (gwindow == bJoin)
			{
				doJoin();
				return true;
			}
			if (gwindow == bSearch)
			{
				if (!bExistSearch)
				{
					com.maddox.rts.CmdEnv.top().exec("socket LISTENER 0");
					java.lang.String s = "socket udp CREATE LOCALPORT " + com.maddox.il2.engine.Config.cur.netLocalPort;
					if (com.maddox.il2.engine.Config.cur.netLocalHost != null && com.maddox.il2.engine.Config.cur.netLocalHost.length() > 0) s = s + " LOCALHOST " + com.maddox.il2.engine.Config.cur.netLocalHost;
					com.maddox.rts.CmdEnv.top().exec(s);
					if (com.maddox.rts.NetEnv.socketsBlock().size() + com.maddox.rts.NetEnv.socketsNoBlock().size() <= 0) return true;
					if (com.maddox.il2.gui.GUINetNewClient.broadcastAdr == null) try
					{
						com.maddox.il2.gui.GUINetNewClient.broadcastAdr = new IPAddress();
						com.maddox.il2.gui.GUINetNewClient.broadcastAdr.create("255.255.255.255");
					}
					catch (java.lang.Exception exception)
					{
						com.maddox.il2.gui.GUINetNewClient.broadcastAdr = null;
						java.lang.System.out.println(exception.getMessage());
						exception.printStackTrace();
						return true;
					}
					wTable.showWindow();
					bSearch.hideWindow();
					bExistSearch = true;
					setPosSize();
					com.maddox.rts.MsgAddListener.post(64, com.maddox.rts.NetEnv.cur(), com.maddox.il2.game.Main.state(), null);
					onMsgTimeout();
				}
				return true;
			}
			if (gwindow == bExit)
			{
				com.maddox.rts.CmdEnv.top().exec("socket LISTENER 0");
				com.maddox.rts.CmdEnv.top().exec("socket udp DESTROY LOCALPORT " + com.maddox.il2.engine.Config.cur.netLocalPort);
				((com.maddox.il2.net.NetUser) com.maddox.rts.NetEnv.host()).reset();
				com.maddox.il2.game.Main.stateStack().pop();
				return true;
			}
			else
			{
				return super.notify(gwindow, i, j);
			}
		}

		public void render()
		{
			super.render();
			if (bExistSearch)
				com.maddox.il2.gui.GUISeparate.draw(this, com.maddox.gwindow.GColor.Gray, x1024(32F), y1024(368F), x1024(896F), 2.0F);
			else
				com.maddox.il2.gui.GUISeparate.draw(this, com.maddox.gwindow.GColor.Gray, x1024(32F), y1024(208F), x1024(480F), 2.0F);
			setCanvasColor(com.maddox.gwindow.GColor.Gray);
			setCanvasFont(0);
			if (bExistSearch)
			{
				draw(x1024(304F), y1024(256F), x1024(352F), y1024(32F), 1, i18n("netnc.Server"));
				draw(x1024(672F), y1024(400F), x1024(192F), y1024(48F), 2, i18n("netnc.Join"));
				draw(x1024(96F), y1024(400F), x1024(160F), y1024(48F), 0, i18n("netnc.Back"));
				
				//TODO: Added by |ZUTI|
				//------------------------------------------------------------------------------
				draw(x1024(672F), y1024(296F), x1024(192F), y1024(48F), 2, i18n("mds.remove"));
				//------------------------------------------------------------------------------
			}
			else
			{
				if (bSearch.isVisible()) draw(x1024(96F), y1024(32F), x1024(416F), y1024(48F), 0, i18n("netnc.Search"));
				if (!com.maddox.il2.net.USGS.isUsed() && com.maddox.il2.game.Main.cur().netGameSpy == null) draw(x1024(96F), y1024(96F), x1024(352F), y1024(32F), 1, i18n("netnc.Server"));
				if (bJoin.isVisible()) draw(x1024(288F), y1024(240F), x1024(160F), y1024(48F), 2, i18n("netnc.Join"));
				draw(x1024(96F), y1024(240F), x1024(136F), y1024(48F), 0, !com.maddox.il2.net.USGS.isUsed() && com.maddox.il2.game.Main.cur().netGameSpy == null ? i18n("netnc.MainMenu") : i18n("main.Quit"));
				
				//TODO: Added by |ZUTI|
				//------------------------------------------------------------------------------
				if (bJoin.isVisible()) 
					draw(x1024(258F), y1024(136F), x1024(192F), y1024(48F), 2, i18n("mds.remove"));
				//------------------------------------------------------------------------------
			}
		}

		public void setPosSize()
		{
			if (bExistSearch)
				set1024PosSize(32F, 144F, 960F, 480F);
			else
				set1024PosSize(240F, 240F, 544F, 320F);
			if (bExistSearch)
			{
				wTable.set1024PosSize(32F, 32F, 892F, 192F);
				//wEdit.setPosSize(x1024(352F), y1024(304F), x1024(256F), y1024(32F));
				
				//TODO: Added by |ZUTI|
				//--------------------------------------------------
				wZutiServersList.setPosSize(x1024(35F), y1024(304F), x1024(700F), y1024(32F));
				bZutiRemove.setPosC(x1024(904F), y1024(320F));
				//--------------------------------------------------
				
				bJoin.setPosC(x1024(904F), y1024(424F));
				bExit.setPosC(x1024(56F), y1024(424F));
			}
			else
			{
				bSearch.setPosC(x1024(56F), y1024(56F));
				
				//wEdit.setPosSize(x1024(144F), y1024(144F), x1024(256F), y1024(32F));
				//TODO: Added by |ZUTI|
				//--------------------------------------------------
				wZutiServersList.setPosSize(x1024(35F), y1024(144F), x1024(295F), y1024(32F));
				bZutiRemove.setPosC(x1024(488F), y1024(160F));
				//--------------------------------------------------
				
				bJoin.setPosC(x1024(488F), y1024(264F));
				bExit.setPosC(x1024(56F), y1024(264F));
			}
		}

		public DialogClient()
		{}
	}

	public class Table extends com.maddox.gwindow.GWindowTable
	{

		public int countRows()
		{
			return adrList == null ? 0 : adrList.size();
		}

		public void renderCell(int i, int j, boolean flag, float f, float f1)
		{
			setCanvasFont(0);
			if (flag)
			{
				setCanvasColorBLACK();
				draw(0.0F, 0.0F, f, f1, lookAndFeel().regionWhite);
			}
			java.lang.String s = (java.lang.String) adrList.get(i);
			com.maddox.il2.gui.GUINetNewClient.Item item = (com.maddox.il2.gui.GUINetNewClient.Item) serverMap.get(s);
			java.lang.String s1 = null;
			int k = 0;
			switch (j)
			{
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
				if (item.bServer)
				{
					if (item.bCoop)
						s1 = (item.bProtected ? "* " : "  ") + i18n("netnc.Cooperative");
					else
						s1 = (item.bProtected ? "* " : "  ") + i18n("netnc.Dogfight");
				}
				else if (item.bCoop)
					s1 = (item.bProtected ? "* " : "  ") + i18n("netnc.Cooperative_routing");
				else
					s1 = (item.bProtected ? "* " : "  ") + i18n("netnc.Dogfight_routing");
				break;
			}
			if (s1 != null) if (flag)
			{
				setCanvasColorWHITE();
				draw(0.0F, 0.0F, f, f1, k, s1);
			}
			else
			{
				setCanvasColorBLACK();
				draw(0.0F, 0.0F, f, f1, k, s1);
			}
		}

		public void setSelect(int i, int j)
		{
			super.setSelect(i, j);
			if (selectRow >= 0)
			{
				java.lang.String s = (java.lang.String) adrList.get(selectRow);
				//wEdit.setValue(s, false);
				
				//TODO: Added by |ZUTI|
				//--------------------------------------------------
				wZutiServersList.setValue(s, false);
				//--------------------------------------------------
			}
		}

		public void afterCreated()
		{
			super.afterCreated();
			bColumnsSizable = true;
			bSelectRow = true;
			addColumn(com.maddox.il2.game.I18N.gui("netnc.Address"), null);
			addColumn(com.maddox.il2.game.I18N.gui("netnc.Name"), null);
			addColumn(com.maddox.il2.game.I18N.gui("netnc.Ping"), null);
			addColumn(com.maddox.il2.game.I18N.gui("netnc.Users"), null);
			addColumn(com.maddox.il2.game.I18N.gui("netnc.Type"), null);
			vSB.scroll = rowHeight(0);
			getColumn(0).setRelativeDx(8F);
			getColumn(1).setRelativeDx(10F);
			getColumn(2).setRelativeDx(4F);
			getColumn(3).setRelativeDx(4F);
			getColumn(4).setRelativeDx(8F);
			alignColumns();
			bNotify = true;
			wClient.bNotify = true;
			resized();
		}

		public void resolutionChanged()
		{
			vSB.scroll = rowHeight(0);
			super.resolutionChanged();
		}

		public java.util.HashMap serverMap;
		public java.util.ArrayList adrList;

		public Table(com.maddox.gwindow.GWindow gwindow)
		{
			super(gwindow);
			serverMap = new HashMap();
			adrList = new ArrayList();
		}
	}

	static class Item
	{

		public com.maddox.rts.NetAddress adr;
		public int port;
		public int ping;
		public java.lang.String ver;
		public boolean bServer;
		public int iServerType;
		public boolean bProtected;
		public boolean bDedicated;
		public boolean bCoop;
		public boolean bMissionStarted;
		public int maxChannels;
		public int usedChannels;
		public int maxUsers;
		public int existUsers;
		public java.lang.String serverName;

		Item()
		{}
	}

	public void _enter()
	{
		bExistSearch = false;
		wTable.hideWindow();
		wTable.adrList.clear();
		wTable.serverMap.clear();
		com.maddox.il2.game.Main.cur().netChannelListener = this;
		if (com.maddox.il2.net.USGS.isUsed() || com.maddox.il2.game.Main.cur().netGameSpy != null)
		{
			bSearch.hideWindow();
			//wEdit.hideWindow();
			
			//TODO: Added by |ZUTI|
			//--------------------------------------------------
			wZutiServersList.hideWindow();
			bZutiRemove.hideWindow();
			//--------------------------------------------------
			
			bJoin.hideWindow();
		}
		else
		{
			bSearch.showWindow();
			//wEdit.setValue(com.maddox.il2.engine.Config.cur.netRemoteHost + ":" + com.maddox.il2.engine.Config.cur.netRemotePort, false);
			//wEdit.showWindow();
			
			//Added by |ZUTI|
			//--------------------------------------------------
			wZutiServersList.setValue(com.maddox.il2.engine.Config.cur.netRemoteHost + ":" + com.maddox.il2.engine.Config.cur.netRemotePort, false);
			wZutiServersList.showWindow();
			bZutiRemove.showWindow();
			//--------------------------------------------------
			
			bJoin.showWindow();
		}
		dialogClient.setPosSize();
		client.activateWindow();
		if (com.maddox.il2.net.USGS.isUsed() || com.maddox.il2.game.Main.cur().netGameSpy != null) new com.maddox.rts.MsgAction(64, com.maddox.rts.Time.real() + 500L)
		{

			public void doAction()
			{
				doJoin();
			}

		};
		((com.maddox.il2.net.NetUser) com.maddox.rts.NetEnv.host()).reset();
	}

	public void _leave()
	{
		client.hideWindow();
		com.maddox.il2.game.Main.cur().netChannelListener = null;
		if (bExistSearch) com.maddox.rts.MsgRemoveListener.post(64, com.maddox.rts.NetEnv.cur(), this, null);
	}

	public void netChannelCanceled(java.lang.String s)
	{
		serverChannel = null;
		if (connectMessgeBox == null)
		{
			return;
		}
		else
		{
			connectMessgeBox.hideWindow();
			connectMessgeBox = new com.maddox.gwindow.GWindowMessageBox(client.root, 20F, true, i18n("netnc.NotConnect"), s, 3, 0.0F)
			{

				public void result(int i)
				{
					connectMessgeBox = null;
					if (com.maddox.il2.net.USGS.isUsed() || com.maddox.il2.game.Main.cur().netGameSpy != null) bJoin.showWindow();
				}

			};
			return;
		}
	}

	public void netChannelCreated(com.maddox.rts.NetChannel netchannel)
	{
		if (connectMessgeBox == null)
		{
			return;
		}
		else
		{
			serverChannel = netchannel;
			onChannelCreated();
			return;
		}
	}

	private void onChannelCreated()
	{
		connectMessgeBox.hideWindow();
		connectMessgeBox = new com.maddox.gwindow.GWindowMessageBox(client.root, 20F, true, i18n("netnc.Connect"), i18n("netnc.ConnectSucc"), 3, 5F)
		{

			public void result(int i)
			{
				connectMessgeBox = null;
				((com.maddox.il2.net.NetUser) com.maddox.rts.NetEnv.host()).onConnectReady(serverChannel);
				com.maddox.il2.game.Main.stateStack().change(36);
				com.maddox.il2.gui.GUI.chatDlg.showWindow();
			}

		};
	}

	public void netChannelRequest(java.lang.String s)
	{
		if (connectMessgeBox == null) return;
		com.maddox.util.NumberTokenizer numbertokenizer = new NumberTokenizer(s);
		java.lang.String s1 = numbertokenizer.next("_");
		if ("SP".equals(s1))
		{
			java.lang.String s2 = numbertokenizer.next("0");
			connectMessgeBox.hideWindow();
			connectMessgeBox = new DlgServerPassword(client.root, s2);
		}
		else if (com.maddox.il2.net.USGS.isUsed() && "NM".equals(s1)) ((com.maddox.rts.NetControl) com.maddox.rts.NetEnv.cur().control).doAnswer("NM \"" + com.maddox.rts.NetEnv.host().shortName() + '"');
	}

	public void netChannelDestroying(com.maddox.rts.NetChannel netchannel, java.lang.String s)
	{
		netChannelCanceled(s);
	}

	public void onMsgTimeout()
	{
		if (!bExistSearch || com.maddox.il2.game.Main.state() != this) return;
		if (com.maddox.rts.NetEnv.socketsBlock().size() + com.maddox.rts.NetEnv.socketsNoBlock().size() <= 0)
		{
			java.lang.String s = "socket udp CREATE LOCALPORT " + com.maddox.il2.engine.Config.cur.netLocalPort;
			if (com.maddox.il2.engine.Config.cur.netLocalHost != null && com.maddox.il2.engine.Config.cur.netLocalHost.length() > 0) s = s + " LOCALHOST " + com.maddox.il2.engine.Config.cur.netLocalHost;
			com.maddox.rts.CmdEnv.top().exec(s);
		}
		if (com.maddox.rts.NetEnv.socketsBlock().size() + com.maddox.rts.NetEnv.socketsNoBlock().size() <= 0) return;
		com.maddox.rts.NetSocket netsocket = null;
		if (com.maddox.rts.NetEnv.socketsNoBlock().size() > 0)
			netsocket = (com.maddox.rts.NetSocket) com.maddox.rts.NetEnv.socketsNoBlock().get(0);
		else
			netsocket = (com.maddox.rts.NetSocket) com.maddox.rts.NetEnv.socketsBlock().get(0);
		com.maddox.rts.NetEnv.cur().postExtUTF((byte) 32, "rinfo " + com.maddox.rts.Time.currentReal(), netsocket, broadcastAdr, com.maddox.il2.engine.Config.cur.netRemotePort);
		(new MsgInvokeMethod("onMsgTimeout")).post(64, this, 1.0D);
	}

	public void msgNetExt(byte abyte0[], com.maddox.rts.NetSocket netsocket, com.maddox.rts.NetAddress netaddress, int i)
	{
		if (!bExistSearch || com.maddox.il2.game.Main.state() != this) return;
		if (abyte0 == null || abyte0.length < 2) return;
		if (abyte0[0] != 32) return;
		java.lang.String s = "";
		try
		{
			_netMsgInput.setData(null, false, abyte0, 1, abyte0.length - 1);
			s = _netMsgInput.readUTF();
		}
		catch (java.lang.Exception exception)
		{
			return;
		}
		com.maddox.util.NumberTokenizer numbertokenizer = new NumberTokenizer(s);
		if (!numbertokenizer.hasMoreTokens()) return;
		if (!"ainfo".equals(numbertokenizer.next())) return;
		com.maddox.il2.gui.GUINetNewClient.Item item = new Item();
		item.adr = netaddress;
		item.port = i;
		long l = -1L;
		try
		{
			l = java.lang.Long.parseLong(numbertokenizer.next());
		}
		catch (java.lang.Exception exception1)
		{
			return;
		}
		item.ping = (int) (com.maddox.rts.Time.currentReal() - l);
		if (item.ping < 0) return;
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
		if (numbertokenizer.hasMoreTokens())
		{
			java.lang.StringBuffer stringbuffer = new StringBuffer(numbertokenizer.next(""));
			for (; numbertokenizer.hasMoreTokens(); stringbuffer.append(numbertokenizer.next("")))
				stringbuffer.append(' ');

			item.serverName = stringbuffer.toString();
		}
		java.lang.String s1 = "" + item.adr.getHostAddress() + ":" + item.port;
		boolean flag = wTable.serverMap.containsKey(s1);
		wTable.serverMap.put(s1, item);
		if (!flag)
		{
			wTable.adrList.add(s1);
			wTable.resized();
		}
	}

	public void doJoin()
	{
		if (com.maddox.il2.net.USGS.isUsed())
		{
			com.maddox.rts.NetEnv.cur();
			com.maddox.rts.NetEnv.host().setShortName(com.maddox.il2.net.USGS.name);
			serverAddress = com.maddox.il2.net.USGS.serverIP;
		}
		else if (com.maddox.il2.game.Main.cur().netGameSpy != null)
		{
			com.maddox.rts.NetEnv.cur();
			com.maddox.rts.NetEnv.host().setShortName(com.maddox.il2.game.Main.cur().netGameSpy.userName);
			serverAddress = com.maddox.il2.game.Main.cur().netGameSpy.serverIP;
		}
		else
		{
			com.maddox.rts.NetEnv.cur();
			com.maddox.rts.NetEnv.host().setShortName(com.maddox.il2.ai.World.cur().userCfg.callsign);
			//serverAddress = wEdit.getValue();
			
			//TODO: Added by |ZUTI|
			//--------------------------------------------------
			serverAddress = wZutiServersList.getValue();
			//--------------------------------------------------
		}
		java.lang.String s = serverAddress;
		if (s == null || s.length() == 0)
			return;
		int i = com.maddox.il2.engine.Config.cur.netRemotePort;
		int j = s.lastIndexOf(":");
		if (j >= 0 && j < s.length() - 1)
		{
			java.lang.String s1 = s.substring(j + 1);
			s = s.substring(0, j);
			try
			{
				i = java.lang.Integer.parseInt(s1);
			}
			catch (java.lang.Exception exception)
			{
				s = serverAddress;
			}
		}
		com.maddox.rts.CmdEnv.top().exec("socket LISTENER " + (com.maddox.il2.engine.Config.cur.netRouteChannels <= 0 ? 0 : 1));
		int k = com.maddox.il2.engine.Config.cur.netRouteChannels;
		if (k <= 0)
			k = 1;
		else
			k++;
		if (bExistSearch) 
			com.maddox.rts.CmdEnv.top().exec("socket udp DESTROY LOCALPORT " + com.maddox.il2.engine.Config.cur.netLocalPort);
		java.lang.String s2 = "socket udp JOIN LOCALPORT " + com.maddox.il2.engine.Config.cur.netLocalPort + " PORT " + i + " SPEED " + com.maddox.il2.engine.Config.cur.netSpeed + " CHANNELS " + k + " HOST " + s;
		if (com.maddox.il2.engine.Config.cur.netLocalHost != null && com.maddox.il2.engine.Config.cur.netLocalHost.length() > 0)
			s2 = s2 + " LOCALHOST " + com.maddox.il2.engine.Config.cur.netLocalHost;
		com.maddox.rts.CmdEnv.top().exec(s2);
		com.maddox.il2.engine.Config.cur.netRemoteHost = s;
		com.maddox.il2.engine.Config.cur.netRemotePort = i;

		//TODO: Added by |ZUTI|
		//----------------------------------------------------------------
		ZutiSupportMethods_GUI.addDropDownServer(wZutiServersList.getValue());
		//----------------------------------------------------------------
		
		doStartWaitDlg();
	}

	private void doStartWaitDlg()
	{
		if (connectMessgeBox != null) connectMessgeBox.close(false);
		connectMessgeBox = new com.maddox.gwindow.GWindowMessageBox(dialogClient.root, 20F, true, i18n("netnc.Connect"), i18n("netnc.ConnectWait"), 5, 0.0F)
		{

			public void result(int i)
			{
				if (i == 1 && connectMessgeBox != null)
				{
					connectMessgeBox = null;
					com.maddox.rts.NetEnv.cur().connect.joinBreak();
					if (serverChannel != null)
					{
						serverChannel.destroy();
						serverChannel = null;
					}
					return;
				}
				else
				{
					return;
				}
			}

		};
	}

	public GUINetNewClient(com.maddox.gwindow.GWindowRoot gwindowroot)
	{
		super(34);
		client = (com.maddox.il2.gui.GUIClient) gwindowroot.create(new GUIClient());
		dialogClient = (com.maddox.il2.gui.GUINetNewClient.DialogClient) client.create(new DialogClient());
		infoMenu = (com.maddox.il2.gui.GUIInfoMenu) client.create(new GUIInfoMenu());
		infoMenu.info = i18n("netnc.info");
		infoName = (com.maddox.il2.gui.GUIInfoName) client.create(new GUIInfoName());
		wTable = new Table(dialogClient);
		
		//wEdit = (com.maddox.gwindow.GWindowEditControl) dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));

		com.maddox.gwindow.GTexture gtexture = ((com.maddox.il2.gui.GUILookAndFeel) gwindowroot.lookAndFeel()).buttons2;
		
		//TODO: Added by |ZUTI|
		//----------------------------------------------------------------
		wZutiServersList = (com.maddox.gwindow.GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 50F ));
		wZutiServersList.list = (ArrayList)ZutiSupportMethods_GUI.getDropDownServersList();
		bZutiRemove = (com.maddox.il2.gui.GUIButton) dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
		//----------------------------------------------------------------
		
		bSearch = (com.maddox.il2.gui.GUIButton) dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
		bJoin = (com.maddox.il2.gui.GUIButton) dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
		bExit = (com.maddox.il2.gui.GUIButton) dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
		dialogClient.activateWindow();
		client.hideWindow();
	}
}