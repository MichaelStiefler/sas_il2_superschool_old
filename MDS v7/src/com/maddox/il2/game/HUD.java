/*410 class*/
package com.maddox.il2.game;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.order.Order;
import com.maddox.il2.game.order.OrderAnyone_Help_Me;
import com.maddox.il2.game.order.ZutiOrder_EjectGunner;
import com.maddox.il2.game.order.ZutiOrder_EngineRepair;
import com.maddox.il2.game.order.ZutiOrder_Loadout;
import com.maddox.il2.game.order.ZutiOrder_TransferControls;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.LDRres;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;

public class HUD
{
	private static final int lenLogBuf = 6;
	private static final long logTimeLife = 10000L;
	private static final long logTimeFire = 5000L;
	
	public boolean bDrawAllMessages = true;
	public boolean bDrawVoiceMessages = true;
	private boolean bNoSubTitles = false;
	private int subTitlesLines = 11;
	private boolean bNoHudLog = false;
	private Main3D main3d;
	private int viewDX;
	private int viewDY;
	long timeLoadLimit = 0L;
	int cnt;
	private String[][] renderSpeedSubstrings = {{null, null, null}, {null, null, null}, {null, null, null}, {null, null, null}};
	private int iDrawSpeed = 1;
	private int lastDrawSpeed = -1;
	private Order[] order;
	private String[] orderStr;
	public ResourceBundle resOrder;
	public ResourceBundle resMsg;
	private int msgX0;
	private int msgY0;
	private int msgDX;
	private int msgDY;
	private int msgSIZE;
	private int msgSpaceLen;
	private ArrayList msgLines = new ArrayList();
	private int[][] msgColor = {{-822083329, -822083329, -822083329, -822059105, -822067233, -822083329, -822083329, -822083329, -822083329}, {-805371904, -805371904, -805371904, -811639040, -807452928, -805371904, -805371904, -805371904, -805371904}};
	private int trainingX0;
	private int trainingY0;
	private int trainingDX;
	private int trainingDY;
	private int trainingSIZE;
	private int trainingSpaceLen;
	private ArrayList trainingLines = new ArrayList();
	private static int idLog = 1;
	public ResourceBundle resLog;
	private String logRightBottom;
	private long logRightBottomTime;
	private static final long logCenterTimeLife = 5000L;
	private String logCenter;
	private long logCenterTime;
	private String logIntro;
	private String logIntroESC;
	private TTFont fntCenter;
	private boolean bCoopTimeStart = false;
	private long coopTimeStart;
	private String[] logBuf = new String[lenLogBuf];
	private String[] logBufStr = new String[lenLogBuf];
	private int[] logBufId = new int[lenLogBuf];
	private long[] logTime = new long[lenLogBuf];
	private int logPtr = 0;
	private int logLen = 0;
	private boolean bDrawNetStat = false;
	private int pageNetStat = 0;
	private int pageSizeNetStat = 0;
	ArrayList statUsers = new ArrayList();
	private ArrayList pointers = new ArrayList();
	private int nPointers = 0;
	private Mat spritePointer;
	public boolean bDrawDashBoard = true;
	private Point3d _p = new Point3d();
	private Orient _o = new Orient();
	private Orient _o1 = new Orient();
	private Orient _oNull = new Orient(0.0F, 0.0F, 0.0F);
	private Mat spriteLeft;
	private Mat spriteRight;
	//TODO: Disabled in 4.10.1
	//private Mat spriteG;
	private Mesh meshNeedle1;
	private Mesh meshNeedle2;
	private Mesh meshNeedle3;
	private Mesh meshNeedle4;
	private Mesh meshNeedle5;
	private Mesh meshNeedle6;
	private Mesh meshNeedleMask;
	private TTFont fntLcd;
	
	static class Ptr
	{
		float x;
		float y;
		int color;
		float alpha;
		float angle;
		
		public void set(float f, float f_0_, int i, float f_1_, float f_2_)
		{
			x = f;
			y = f_0_;
			color = i;
			alpha = f_1_;
			angle = (float)((double)(f_2_ * 180.0F) / 3.141592653589793);
		}
		
		public Ptr(float f, float f_3_, int i, float f_4_, float f_5_)
		{
			set(f, f_3_, i, f_4_, f_5_);
		}
	}
	
	static class StatUser
	{
		NetUser user;
		int iNum;
		String sNum;
		int iPing;
		String sPing;
		int iScore;
		String sScore;
		int iArmy;
		String sArmy;
		Aircraft aAircraft;
		String sAircraft;
		String sAircraftType;
	}
	
	static class MsgLine
	{
		String msg;
		int iActor;
		int army;
		long time0;
		int len;
		
		MsgLine(String string, int i, int i_6_, int i_7_, long l)
		{
			msg = string;
			len = i;
			iActor = i_6_;
			army = i_7_;
			time0 = l;
		}
	}
	
	public static int drawSpeed()
	{
		return Main3D.cur3D().hud.iDrawSpeed;
	}
	
	public static void setDrawSpeed(int i)
	{
		Main3D.cur3D().hud.iDrawSpeed = i;
	}
	
	private final void renderSpeed()
	{
		if (Actor.isValid(World.getPlayerAircraft()) && iDrawSpeed != 0 && bDrawAllMessages && (Main.cur().netServerParams == null || Main.cur().netServerParams.isShowSpeedBar()))
		{
			TTFont ttfont = TTFont.font[1];
			int i = ttfont.height();
			int i_8_ = -1073741569;
			int i_9_ = (int)(World.getPlayerFM().Or.getYaw() + 0.5F);
			i_9_ = i_9_ > 90 ? 450 - i_9_ : 90 - i_9_;
			boolean bool = false;
			float f = World.getPlayerFM().getLoadDiff();
			if (f <= World.getPlayerFM().getLimitLoad() * 0.25F && f > World.getPlayerFM().getLimitLoad() * 0.1F)
			{
				bool = true;
				cnt = 0;
				timeLoadLimit = 0L;
			}
			else if (f <= World.getPlayerFM().getLimitLoad() * 0.1F && Time.current() < timeLoadLimit)
				bool = false;
			else if (f <= World.getPlayerFM().getLimitLoad() * 0.1F && Time.current() >= timeLoadLimit)
			{
				bool = true;
				cnt++;
				if (cnt == 22)
				{
					timeLoadLimit = 125L + Time.current();
					cnt = 0;
				}
			}
			else
			{
				cnt = 0;
				timeLoadLimit = 0L;
			}
			String string;
			int i_10_;
			int i_11_;
			switch (iDrawSpeed)
			{
				default :
					string = ".si";
					i_10_ = (int)(World.getPlayerFM().getAltitude() * 0.1F) * 10;
					i_11_ = (int)(3.6F * Pitot.Indicator((float)(World.getPlayerFM().Loc.z), World.getPlayerFM().getSpeed()) * 0.1F) * 10;
					break;
				case 2 :
					string = ".gb";
					i_10_ = (int)(3.28084F * World.getPlayerFM().getAltitude() * 0.02F) * 50;
					i_11_ = (int)(1.943845F * Pitot.Indicator((float)(World.getPlayerFM().Loc.z), World.getPlayerFM().getSpeed()) * 0.1F) * 10;
					break;
				case 3 :
					string = ".us";
					i_10_ = (int)(3.28084F * World.getPlayerFM().getAltitude() * 0.02F) * 50;
					i_11_ = (int)(2.236936F * Pitot.Indicator((float)(World.getPlayerFM().Loc.z), World.getPlayerFM().getSpeed()) * 0.1F) * 10;
			}
			if (iDrawSpeed != lastDrawSpeed)
			{
				try
				{
					renderSpeedSubstrings[0][0] = resLog.getString("HDG");
					renderSpeedSubstrings[1][0] = resLog.getString("ALT");
					renderSpeedSubstrings[2][0] = resLog.getString("SPD");
					renderSpeedSubstrings[3][0] = resLog.getString("G");
					renderSpeedSubstrings[0][1] = resLog.getString("HDG" + string);
					renderSpeedSubstrings[1][1] = resLog.getString("ALT" + string);
					renderSpeedSubstrings[2][1] = resLog.getString("SPD" + string);
					renderSpeedSubstrings[3][1] = resLog.getString("Ga");
				}
				catch (Exception exception)
				{
					renderSpeedSubstrings[0][0] = "HDG";
					renderSpeedSubstrings[1][0] = "ALT";
					renderSpeedSubstrings[2][0] = "SPD";
					renderSpeedSubstrings[3][0] = "G";
					renderSpeedSubstrings[0][1] = "";
					renderSpeedSubstrings[1][1] = "";
					renderSpeedSubstrings[2][1] = "";
					renderSpeedSubstrings[3][1] = "";
				}
			}
			ttfont.output(i_8_, 5.0F, 5.0F, 0.0F, (renderSpeedSubstrings[0][0] + " " + i_9_ + " " + renderSpeedSubstrings[0][1]));
			if (!World.cur().diffCur.NoSpeedBar)
			{
				ttfont.output(i_8_, 5.0F, (float)(5 + i), 0.0F, (renderSpeedSubstrings[1][0] + " " + i_10_ + " " + renderSpeedSubstrings[1][1]));
				ttfont.output(i_8_, 5.0F, (float)(5 + i + i), 0.0F, (renderSpeedSubstrings[2][0] + " " + i_11_ + " " + renderSpeedSubstrings[2][1]));
				if (bool)
					ttfont.output(i_8_, 5.0F, (float)(5 + i + i + i), 0.0F, renderSpeedSubstrings[3][0]);
			}
		}
	}
	
	public void clearSpeed()
	{
		iDrawSpeed = 1;
	}
	
	private void initSpeed()
	{
		iDrawSpeed = 1;
	}
	
	public static void order(Order[] orders)
	{
		if (Config.isUSE_RENDER())
			Main3D.cur3D().hud._order(orders);
	}
	
	public void _order(Order[] orders)
	{
		if (orders == null)
			order = null;
		else
		{
			order = new Order[orders.length];
			orderStr = new String[orders.length];
			int i = World.getPlayerArmy();
			
			//TODO: Added by |ZUTI|
			//------------------------------------------------------------------------------------------
			Aircraft netAc = (Aircraft)World.getPlayerAircraft();
			String currentAcName = Property.stringValue(netAc.getClass(), "keyName");
			int playerArmy = World.getPlayerArmy();
			int loadoutId = 0;
			int engineId = 1;
			int gunnerId = 0;
			int transferId = 0;
			Map crew = ZutiAircraftCrewManagement.getAircraftCrew(netAc.name()).getCrewMap();
			//System.out.println("HUD - crew size: " + crew.size());
			int playerPlaneEngines = ZutiSupportMethods_Air.getNuberOfAircraftEngines(netAc);
			ZutiOrder_EjectGunner ejectOrder = null;
			ZutiOrder_TransferControls transferOrder = null;
			boolean refreshNetPositions = true;
			//------------------------------------------------------------------------------------------
			
			for (int i_12_ = 0; i_12_ < order.length; i_12_++)
			{
				order[i_12_] = orders[i_12_];
				if (orders[i_12_] != null && order[i_12_].name(i) != null)
				{
					String string = order[i_12_].name(i);
					String string_13_ = null;
					String string_14_ = World.getPlayerLastCountry();
					
					//TODO: Added by |ZUTI|
					//------------------------------------------------------------------------------------------
					//System.out.println("HUD - " + string + ", " + string_14_);
					//System.out.println("HUD - " + order[i_12_].toString());
					//System.out.println("==================================");
					
					if( order[i_12_] instanceof ZutiOrder_Loadout )
					{
						//System.out.println("Populating loadout id: " + loadoutId);
						string = ZutiSupportMethods.getSelectedLoadoutForAircraft(ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(netAc.FM), currentAcName, netAc.FM.Loc.x, netAc.FM.Loc.y, loadoutId, playerArmy, true);
						loadoutId++;
					}
					else if( order[i_12_] instanceof ZutiOrder_EngineRepair )
					{
						if( engineId > playerPlaneEngines )
							string = "";
						else
							string = "Engine " + engineId;
						
						engineId++;
					}
					else if( order[i_12_] instanceof ZutiOrder_EjectGunner )
					{
						if( refreshNetPositions )
						{
							ZutiSupportMethods_Multicrew.updateNetUsersCrewPlaces();
							refreshNetPositions = false;
						}
						
						if( crew != null )
						{
							string = (String)crew.get(new Integer(gunnerId));
							if( string == null )
								string = "";
							
							//System.out.println("HUD - gunner at >" + gunnerId + "< is >" + string + "<.");
							ejectOrder = (ZutiOrder_EjectGunner)order[i_12_];
							ejectOrder.setName(string);
						}
						gunnerId++;
					}
					else if( order[i_12_] instanceof ZutiOrder_TransferControls )
					{
						if( refreshNetPositions )
						{
							ZutiSupportMethods_Multicrew.updateNetUsersCrewPlaces();
							refreshNetPositions = false;
						}
						
						transferOrder = (ZutiOrder_TransferControls)order[i_12_];
						
						if( transferId < 8 )
						{
							if( crew != null )
								string = (String)crew.get(new Integer(transferId));
							
							if( string == null )
								string = "";
							
							transferOrder.setName(string);
						}
						else
							transferOrder.setIsRetakeCommand(true);
						
						transferId++;
					}
					//------------------------------------------------------------------------------------------
					
					if (string_14_ != null)
					{
						try
						{
							string_13_ = resOrder.getString(string + "_" + string_14_);
						}
						catch (Exception exception)
						{
							/* empty */
						}
					}
					if (string_13_ == null)
					{
						try
						{
							string_13_ = resOrder.getString(string);
						}
						catch (Exception exception)
						{
							string_13_ = string;
						}
					}
					orderStr[i_12_] = string_13_;
				}
			}
		}
	}
	
	private void renderOrder()
	{
		if (order != null && bDrawAllMessages)
		{
			TTFont ttfont = TTFont.font[1];
			int i = (int)((double)viewDX * 0.05);
			int i_15_ = ttfont.height();
			int i_16_ = viewDY - 2 * ttfont.height();
			String string = null;
			int i_17_ = i_16_;
			int i_18_ = 0;
			int i_19_ = 0;
			boolean bool = false;
			boolean grayedOut = false;
			for (int i_21_ = 0; i_21_ < order.length; i_21_++)
			{
				if (orderStr[i_21_] != null)
				{
					if (order[i_21_] instanceof OrderAnyone_Help_Me)
						bool = true;
					if (Main3D.cur3D().ordersTree.frequency() == null)
						grayedOut = true;
					if (string != null)
						drawOrder(string, i, i_17_, i_19_ == 0 ? -1 : i_19_, i_18_, grayedOut);
					i_19_ = i_21_;
					string = orderStr[i_21_];
					i_17_ = i_16_;
					i_18_ = order[i_21_].attrib();
					
					//TODO: Disabled by |ZUTI|: this makes vectoring options grayed out... why?
					//--------------------------------------------------------------------------
					/*
					if ((order[i_21_] instanceof OrderVector_To_Home_Base || order[i_21_] instanceof OrderVector_To_Target)) //&& Main.cur().mission.zutiRadar_DisableVectoring)
						grayedOut = true;
					else
						grayedOut = false;
					*/
					//--------------------------------------------------------------------------
				}
				i_16_ -= i_15_;
			}
			if (Main3D.cur3D().ordersTree.frequency() == null)
				grayedOut = true;
			if (string != null)
				drawOrder(string, i, i_17_, 0, i_18_, grayedOut);
			if (bool)
			{
				String[] strings = Main3D.cur3D().ordersTree.getShipIDs();
				for (int i_22_ = 0; i_22_ < strings.length; i_22_++)
				{
					if (i_22_ == 0 && strings[i_22_] != null)
					{
						i_16_ -= i_15_;
						i_16_ -= i_15_;
						drawShipIDs(resOrder.getString("ShipIDs"), i, i_16_);
						i_16_ -= i_15_;
					}
					if (strings[i_22_] != null)
					{
						drawShipIDs(strings[i_22_], i, i_16_);
						i_16_ -= i_15_;
					}
				}
			}
		}
	}
	
	private void drawShipIDs(String string, int i, int i_23_)
	{
		int i_24_ = -16776961;
		TTFont ttfont = TTFont.font[1];
		ttfont.output(i_24_, (float)i, (float)i_23_, 0.0F, string);
	}
	
	private void drawOrder(String string, int i, int i_25_, int i_26_, int i_27_, boolean grayedOut)
	{
		int color = -16776961;
		if ((i_27_ & 0x1) != 0)
			color = -16777089;
		else if ((i_27_ & 0x2) != 0)
			color = -16744449;
		TTFont ttfont = TTFont.font[1];
		if (grayedOut)
			color = 2139062143;
		
		if (i_26_ >= 0)
			ttfont.output(color, (float)i, (float)i_25_, 0.0F, "" + i_26_ + ". " + string);
		else
			ttfont.output(color, (float)i, (float)i_25_, 0.0F, string);
	}
	
	public void clearOrder()
	{
		order = null;
	}
	
	private void initOrder()
	{
		clearOrder();
		resOrder = ResourceBundle.getBundle("i18n/hud_order", RTSConf.cur.locale, LDRres.loader());
	}
	
	public static void message(int[] is, int i, int i_29_, boolean bool)
	{
		if (Config.isUSE_RENDER())
			Main3D.cur3D().hud._message(is, i, i_29_, bool);
	}
	
	public void _message(int[] is, int i, int i_30_, boolean bool)
	{
		if (bDrawVoiceMessages && !bNoSubTitles && i >= 1 && i <= 9 && i_30_ >= 1 && i_30_ <= 2 && is != null)
		{
			TTFont ttfont = TTFont.font[1];
			for (int i_31_ = 0; i_31_ < is.length && is[i_31_] != 0; i_31_++)
			{
				String string = Voice.vbStr[is[i_31_]];
				try
				{
					String string_32_ = resMsg.getString(string);
					if (string_32_ != null)
						string = string_32_;
				}
				catch (Exception exception)
				{
					/* empty */
				}
				StringTokenizer stringtokenizer = new StringTokenizer(string);
				while (stringtokenizer.hasMoreTokens())
				{
					String string_33_ = stringtokenizer.nextToken();
					int i_34_ = (int)ttfont.width(string_33_);
					if (msgLines.size() == 0)
						msgLines.add(new MsgLine(string_33_, i_34_, i, i_30_, Time.current()));
					else
					{
						MsgLine msgline = (MsgLine)msgLines.get(msgLines.size() - 1);
						if (msgline.iActor == i && msgline.army == i_30_ && !bool)
						{
							int i_35_ = msgline.len + msgSpaceLen + i_34_;
							if (i_35_ < msgDX)
							{
								msgline.msg = msgline.msg + " " + string_33_;
								msgline.len = i_35_;
							}
							else
								msgLines.add(new MsgLine(string_33_, i_34_, i, i_30_, 0L));
						}
						else
							msgLines.add(new MsgLine(string_33_, i_34_, i, i_30_, 0L));
					}
					bool = false;
				}
			}
			while (msgLines.size() > msgSIZE)
			{
				msgLines.remove(0);
				MsgLine msgline = (MsgLine)msgLines.get(0);
				msgline.time0 = Time.current();
			}
		}
	}
	
	private int msgColor(int i, int i_36_)
	{
		return msgColor[i_36_ - 1][i - 1];
	}
	
	private void renderMsg()
	{
		if (bDrawVoiceMessages && bDrawAllMessages)
		{
			int i = msgLines.size();
			if (i != 0)
			{
				MsgLine msgline = (MsgLine)msgLines.get(0);
				long l = msgline.time0 + (long)(msgline.msg.length() * 250);
				if (l < Time.current())
				{
					msgLines.remove(0);
					if (--i == 0)
						return;
					msgline = (MsgLine)msgLines.get(0);
					msgline.time0 = Time.current();
				}
				TTFont ttfont = TTFont.font[1];
				int i_37_ = msgX0;
				int i_38_ = msgY0 + msgDY;
				for (int i_39_ = 0; i_39_ < i; i_39_++)
				{
					msgline = (MsgLine)msgLines.get(i_39_);
					ttfont.output(msgColor(msgline.iActor, msgline.army), (float)i_37_, (float)i_38_, 0.0F, msgline.msg);
					i_38_ -= ttfont.height();
				}
			}
		}
	}
	
	public void clearMsg()
	{
		msgLines.clear();
	}
	
	public void resetMsgSizes()
	{
		clearMsg();
		TTFont ttfont = TTFont.font[1];
		msgX0 = (int)((double)viewDX * 0.3);
		msgDX = (int)((double)viewDX * 0.6);
		msgDY = ttfont.height() * subTitlesLines;
		if (msgDY > (int)((double)viewDY * 0.9))
			msgDY = (int)((double)viewDY * 0.9);
		int i = msgDY / ttfont.height();
		if (i == 0)
			i = 1;
		msgDY = ttfont.height() * i;
		msgSIZE = i;
		msgY0 = (int)((double)viewDY * 0.95) - msgDY;
		msgSpaceLen = Math.round(ttfont.width(" "));
	}
	
	private void initMsg()
	{
		resetMsgSizes();
		resMsg = ResourceBundle.getBundle("i18n/hud_msg", RTSConf.cur.locale, LDRres.loader());
	}
	
	public static void training(String string)
	{
		if (Config.isUSE_RENDER())
			Main3D.cur3D().hud._training(string);
	}
	
	public void _training(String string)
	{
		trainingLines.clear();
		if (string != null)
		{
			TTFont ttfont = TTFont.font[2];
			StringTokenizer stringtokenizer = new StringTokenizer(string);
			while (stringtokenizer.hasMoreTokens())
			{
				String string_40_ = stringtokenizer.nextToken();
				int i = (int)ttfont.width(string_40_);
				if (trainingLines.size() == 0)
					trainingLines.add(string_40_);
				else
				{
					String string_41_ = (String)trainingLines.get(trainingLines.size() - 1);
					int i_42_ = (int)ttfont.width(string_41_);
					int i_43_ = i_42_ + trainingSpaceLen + i;
					if (i_43_ < trainingDX)
						trainingLines.set(trainingLines.size() - 1, string_41_ + " " + string_40_);
					else
					{
						if (trainingLines.size() >= trainingSIZE)
							break;
						trainingLines.add(string_40_);
					}
				}
			}
		}
	}
	
	private void renderTraining()
	{
		int i = trainingLines.size();
		if (i != 0)
		{
			TTFont ttfont = TTFont.font[2];
			int i_44_ = trainingX0;
			int i_45_ = trainingY0 + trainingDY;
			for (int i_46_ = 0; i_46_ < i; i_46_++)
			{
				String string = (String)trainingLines.get(i_46_);
				ttfont.output(-16776961, (float)i_44_, (float)i_45_, 0.0F, string);
				i_45_ -= ttfont.height();
			}
		}
	}
	
	public void clearTraining()
	{
		trainingLines.clear();
	}
	
	public void resetTrainingSizes()
	{
		clearTraining();
		TTFont ttfont = TTFont.font[2];
		trainingX0 = (int)((double)viewDX * 0.3);
		trainingDX = (int)((double)viewDX * 0.5);
		trainingY0 = (int)((double)viewDY * 0.5);
		trainingDY = (int)((double)viewDY * 0.45);
		int i = trainingDY / ttfont.height();
		if (i == 0)
			i = 1;
		trainingDY = ttfont.height() * i;
		trainingSIZE = i;
		trainingSpaceLen = Math.round(ttfont.width(" "));
	}
	
	private void initTraining()
	{
		resetTrainingSizes();
	}
	
	public static void intro(String string)
	{
		if (Config.isUSE_RENDER())
			Main3D.cur3D().hud._intro(string);
	}
	
	public void _intro(String string)
	{
		if (string == null)
			logIntro = null;
		else
			logIntro = string;
	}
	
	public static void introESC(String string)
	{
		if (Config.isUSE_RENDER())
			Main3D.cur3D().hud._introESC(string);
	}
	
	public void _introESC(String string)
	{
		if (string == null)
			logIntroESC = null;
		else
			logIntroESC = string;
	}
	
	public static int makeIdLog()
	{
		return idLog++;
	}
	
	public static void log(String string, Object[] objects)
	{
		if (Config.isUSE_RENDER())
			log(0, string, objects);
	}
	
	public static void log(int i, String string, Object[] objects)
	{
		if (Config.isUSE_RENDER() && Main3D.cur3D().gameTrackPlay() == null)
		{
			if (Main3D.cur3D().gameTrackRecord() != null && objects != null && objects.length == 1 && objects[0] instanceof Integer)
			{
				try
				{
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
					netmsgguaranted.writeByte(0);
					netmsgguaranted.writeInt(i);
					netmsgguaranted.write255(string);
					netmsgguaranted.writeInt(((Integer)objects[0]).intValue());
					Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
				}
				catch (Exception exception)
				{
					/* empty */
				}
			}
			Main3D.cur3D().hud._log(i, string, objects);
		}
	}
	
	public void _log(int i, String string, Object[] objects)
	{
		if (!bNoHudLog)
		{
			int i_47_ = __log(i, string);
			String string_48_;
			try
			{
				string_48_ = resLog.getString(string);
			}
			catch (Exception exception)
			{
				string_48_ = string;
			}
			logBufStr[i_47_] = MessageFormat.format(string_48_, objects);
		}
	}
	
	public static void log(String string)
	{
		if (Config.isUSE_RENDER())
			log(0, string);
	}
	
	public static void log(int i, String string)
	{
		log(i, string, true);
	}
	
	public static void log(int i, String string, boolean bool)
	{
		if (Config.isUSE_RENDER())
		{
			if (bool)
			{
				if (Main3D.cur3D().gameTrackPlay() != null)
					return;
				if (Main3D.cur3D().gameTrackRecord() != null)
				{
					try
					{
						NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
						netmsgguaranted.writeByte(1);
						netmsgguaranted.writeInt(i);
						netmsgguaranted.write255(string);
						Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
					}
					catch (Exception exception)
					{
						/* empty */
					}
				}
			}
			Main3D.cur3D().hud._log(i, string);
		}
	}
	
	public void _log(int i, String string)
	{
		if (!bNoHudLog)
		{
			int i_49_ = __log(i, string);
			try
			{
				logBufStr[i_49_] = resLog.getString(string);
			}
			catch (Exception exception)
			{
				logBufStr[i_49_] = string;
			}
		}
	}
	
	public static void logRightBottom(String string)
	{
		if (Config.isUSE_RENDER() && Main3D.cur3D().gameTrackPlay() == null)
		{
			if (Main3D.cur3D().gameTrackRecord() != null)
			{
				try
				{
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
					netmsgguaranted.writeByte(2);
					netmsgguaranted.write255(string == null ? "" : string);
					Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
				}
				catch (Exception exception)
				{
					/* empty */
				}
			}
			Main3D.cur3D().hud._logRightBottom(string);
		}
	}
	
	public void _logRightBottom(String string)
	{
		if (!bNoHudLog)
		{
			if (string == null)
				logRightBottom = null;
			else
			{
				try
				{
					logRightBottom = resLog.getString(string);
				}
				catch (Exception exception)
				{
					logRightBottom = string;
				}
				logRightBottomTime = Time.current();
			}
		}
	}
	
	public static void logCenter(String string)
	{
		if (Config.isUSE_RENDER() && Main3D.cur3D().gameTrackPlay() == null)
		{
			if (Main3D.cur3D().gameTrackRecord() != null)
			{
				try
				{
					NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
					netmsgguaranted.writeByte(3);
					netmsgguaranted.write255(string == null ? "" : string);
					Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
				}
				catch (Exception exception)
				{
					/* empty */
				}
			}
			Main3D.cur3D().hud._logCenter(string);
		}
	}
	
	public void _logCenter(String string)
	{
		if (string == null)
			logCenter = null;
		else
		{
			try
			{
				logCenter = resLog.getString(string);
			}
			catch (Exception exception)
			{
				logCenter = string;
			}
			logCenterTime = Time.current();
		}
	}
	
	public static void logCoopTimeStart(long l)
	{
		if (Config.isUSE_RENDER())
			Main3D.cur3D().hud._logCoopTimeStart(l);
	}
	
	public void _logCoopTimeStart(long l)
	{
		bCoopTimeStart = true;
		coopTimeStart = l;
	}
	
	public boolean netInputLog(int i, NetMsgInput netmsginput) throws IOException
	{
		switch (i)
		{
			case 0 :
			{
				int i_50_ = netmsginput.readInt();
				String string = netmsginput.read255();
				Object[] objects = new Object[1];
				objects[0] = new Integer(netmsginput.readInt());
				_log(i_50_, string, objects);
				break;
			}
			case 1 :
			{
				int i_51_ = netmsginput.readInt();
				String string = netmsginput.read255();
				_log(i_51_, string);
				break;
			}
			case 2 :
			{
				String string = netmsginput.read255();
				if ("".equals(string))
					string = null;
				_logRightBottom(string);
				break;
			}
			case 3 :
			{
				String string = netmsginput.read255();
				if ("".equals(string))
					string = null;
				_logCenter(string);
				break;
			}
		}
		return true;
	}
	
	public void clearLog()
	{
		logRightBottom = null;
		logPtr = 0;
		logLen = 0;
		logCenter = null;
		logIntro = null;
		logIntroESC = null;
		bCoopTimeStart = false;
	}
	
	private void initLog()
	{
		clearLog();
		resLog = ResourceBundle.getBundle("i18n/hud_log", RTSConf.cur.locale, LDRres.loader());
		fntCenter = TTFont.font[2];
	}
	
	private void renderLog()
	{
		long l = Time.current();
		if (bCoopTimeStart)
		{
			int i = (int)(coopTimeStart - Time.currentReal());
			if (i < 0)
				bCoopTimeStart = false;
			else if (bDrawAllMessages)
			{
				TTFont ttfont = fntCenter;
				String string = "" + (i + 500) / 1000;
				float f = ttfont.width(string);
				ttfont.output(-16711681, ((float)viewDX - f) / 2.0F, (float)viewDY * 0.75F, 0.0F, string);
			}
		}
		else if (logIntro != null)
		{
			TTFont ttfont = fntCenter;
			float f = ttfont.width(logIntro);
			int i = -16777216;
			ttfont.output(i, ((float)viewDX - f) / 2.0F, (float)viewDY * 0.75F, 0.0F, logIntro);
		}
		else if (logCenter != null)
		{
			if (l > logCenterTime + logCenterTimeLife)
				logCenter = null;
			else if (bDrawAllMessages)
			{
				TTFont ttfont = fntCenter;
				float f = ttfont.width(logCenter);
				int i = -16776961;
				int i_48_ = 255 - (int)((double)(l - logCenterTime) / 5000.0 * 255.0);
				i |= i_48_ << 8;
				ttfont.output(i, ((float)viewDX - f) / 2.0F, (float)viewDY * 0.75F, 0.0F, logCenter);
			}
		}
		if (logIntroESC != null)
		{
			TTFont ttfont = TTFont.font[0];
			float f = ttfont.width(logIntroESC);
			int i = -1;
			ttfont.output(i, ((float)viewDX - f) / 2.0F, (float)viewDY * 0.05F, 0.0F, logIntroESC);
		}
		if (!Main3D.cur3D().aircraftHotKeys.isAfterburner())
			logRightBottom = null;
		if (logRightBottom != null && bDrawAllMessages)
		{
			TTFont ttfont = TTFont.font[1];
			int i = (int)((double)viewDX * 0.95);
			int i_49_ = (int)ttfont.width(logRightBottom);
			int i_50_ = (int)((double)viewDY * 0.45 - (double)(3 * ttfont.height()));
			int i_51_ = -16776961;
			int i_52_ = (int)(510.0F * (float)((Time.current() - logRightBottomTime) % logTimeFire) / 5000.0F);
			i_52_ -= 255;
			if (i_52_ < 0)
				i_52_ = -i_52_;
			ttfont.output(i_51_ | i_52_ << 8, (float)(i - i_49_), (float)i_50_, 0.0F, logRightBottom);
		}
		if (logLen != 0)
		{
			for (/**/; logLen > 0; logLen--)
			{
				if (l < logTime[logPtr] + logTimeLife)
					break;
				logPtr = (logPtr + 1) % lenLogBuf;
			}
			if (logLen != 0)
			{
				TTFont ttfont = TTFont.font[1];
				int i = (int)((double)viewDX * 0.95);
				int i_53_ = ttfont.height();
				int i_54_ = (int)((double)viewDY * 0.45) - (lenLogBuf - logLen) * i_53_;
				for (int i_55_ = 0; i_55_ < logLen; i_55_++)
				{
					int i_56_ = (logPtr + i_55_) % lenLogBuf;
					int i_57_ = -65536;
					if (l < logTime[i_56_] + logCenterTimeLife)
					{
						int i_58_ = (int)((double)(logTime[i_56_] + logCenterTimeLife - l) / 5000.0 * 255.0);
						i_57_ |= i_58_ | i_58_ << 8;
					}
					float f = ttfont.width(logBufStr[i_56_]);
					if (bDrawAllMessages)
						ttfont.output(i_57_, (float)i - f, (float)i_54_, 0.0F, logBufStr[i_56_]);
					i_54_ -= i_53_;
				}
			}
		}
	}
	
	private int __log(int i, String string)
	{
		if (logLen > 0 && i != 0)
		{
			int i_59_ = (logPtr + logLen - 1) % lenLogBuf;
			if (logBufId[i_59_] == i)
			{
				logTime[i_59_] = Time.current();
				logBuf[i_59_] = string;
				return i_59_;
			}
		}
		if (logLen >= lenLogBuf)
		{
			logPtr = (logPtr + 1) % lenLogBuf;
			logLen = 2;
		}
		int i_60_ = (logPtr + logLen) % lenLogBuf;
		logBuf[i_60_] = string;
		logBufId[i_60_] = i;
		logTime[i_60_] = Time.current();
		logLen++;
		return i_60_;
	}
	
	private void syncStatUser(int i, NetUser netuser)
	{
		if (i == statUsers.size())
			statUsers.add(new StatUser());
		StatUser statuser = (StatUser)statUsers.get(i);
		statuser.user = netuser;
		if (statuser.iNum != i + 1 || statuser.sNum == null)
		{
			statuser.iNum = i + 1;
			statuser.sNum = statuser.iNum + ".";
		}
		if (statuser.iPing != netuser.ping || statuser.sPing == null)
		{
			statuser.iPing = netuser.ping;
			statuser.sPing = "(" + statuser.iPing + ")";
		}
		int i_65_ = (int)netuser.stat().score;
		if (statuser.iScore != i_65_ || statuser.sScore == null)
		{
			statuser.iScore = i_65_;
			statuser.sScore = "" + statuser.iScore;
		}
		if (statuser.iArmy != netuser.getArmy() || statuser.sArmy == null)
		{
			statuser.iArmy = netuser.getArmy();
			statuser.sArmy = ("(" + statuser.iArmy + ")" + I18N.army(Army.name(statuser.iArmy)));
		}
		if (!Actor.isAlive(statuser.aAircraft) || statuser.aAircraft.netUser() != netuser || statuser.sAircraft == null)
		{
			Aircraft aircraft = netuser.findAircraft();
			statuser.aAircraft = aircraft;
			if (aircraft == null)
			{
				statuser.sAircraft = "";
				statuser.sAircraftType = "";
			}
			else
			{
				statuser.sAircraft = aircraft.typedName();
				statuser.sAircraftType = I18N.plane(Property.stringValue(aircraft.getClass(), "keyName"));
			}
		}
	}
	
	private void syncNetStat()
	{
		syncStatUser(0, (NetUser)NetEnv.host());
		for (int i = 0; i < NetEnv.hosts().size(); i++)
			syncStatUser(i + 1, (NetUser)NetEnv.hosts().get(i));
		while (statUsers.size() > NetEnv.hosts().size() + 1)
			statUsers.remove(statUsers.size() - 1);
	}
	
	private int x1024(float f)
	{
		return (int)((float)viewDX / 1024.0F * f);
	}
	
	private int y1024(float f)
	{
		return (int)((float)viewDY / 768.0F * f);
	}
	
	public void startNetStat()
	{
		if (!bDrawNetStat && Mission.isPlaying() && !Mission.isSingle())
		{
			syncNetStat();
			TTFont ttfont = TTFont.font[1];
			int i = ttfont.height() - ttfont.descender();
			int i_66_ = y1024(740.0F);
			int i_67_ = 2 * i;
			pageSizeNetStat = (i_66_ - i_67_) / i;
			bDrawNetStat = true;
		}
	}
	
	public void stopNetStat()
	{
		if (bDrawNetStat)
		{
			statUsers.clear();
			bDrawNetStat = false;
			pageNetStat = 0;
		}
	}
	
	public boolean isDrawNetStat()
	{
		return bDrawNetStat;
	}
	
	public void pageNetStat()
	{
		if (bDrawNetStat)
		{
			pageNetStat++;
			if (pageSizeNetStat * pageNetStat > statUsers.size())
				pageNetStat = 0;
		}
	}
	
	public void renderNetStat()
	{
		//TODO: Changed by |ZUTI|: added last condition
		if (bDrawNetStat && Mission.isPlaying() && !Mission.isSingle() && !Mission.MDS_VARIABLES().zutiHud_DisableHudStatistics)
		{		
			TTFont ttfont = TTFont.font[1];
			TTFont ttfont_64_ = TTFont.font[3];
			int i = ttfont.height() - ttfont.descender();
			int i_65_ = y1024(740.0F);
			int i_66_ = 0;
			int i_67_ = 0;
			int i_68_ = 0;
			int i_69_ = 0;
			int i_70_ = 0;
			int i_71_ = 0;
			
			for (int i_72_ = pageSizeNetStat * pageNetStat; (i_72_ < pageSizeNetStat * (pageNetStat + 1) && i_72_ < statUsers.size()); i_72_++)
			{
				//TODO: Changed by |ZUTI|
				StatUser statuser = (StatUser)statUsers.get(i_72_);
				int i_73_ = 0;
				
				if( Mission.MDS_VARIABLES().zutiHud_ShowPilotNumber )
				{
					i_73_ = (int)ttfont.width(statuser.sNum);
					if (i_66_ < i_73_)
						i_66_ = i_73_;
				}
				if( Mission.MDS_VARIABLES().zutiHud_ShowPilotPing )
				{
					i_73_ = (int)ttfont_64_.width(statuser.sPing);
					if (i_67_ < i_73_)
						i_67_ = i_73_;
				}
				if( Mission.MDS_VARIABLES().zutiHud_ShowPilotName )
				{
					i_73_ = (int)ttfont.width(statuser.user.uniqueName());
					if (i_68_ < i_73_)
						i_68_ = i_73_;
				}
				if( Mission.MDS_VARIABLES().zutiHud_ShowPilotScore )
				{
					i_73_ = (int)ttfont.width(statuser.sScore);
					if (i_69_ < i_73_)
						i_69_ = i_73_;
				}
				if( Mission.MDS_VARIABLES().zutiHud_ShowPilotArmy )
				{
					i_73_ = (int)ttfont.width(statuser.sArmy);
					if (i_70_ < i_73_)
						i_70_ = i_73_;
				}
				if( Mission.MDS_VARIABLES().zutiHud_ShowPilotACDesignation )
				{
					i_73_ = (int)ttfont.width(statuser.sAircraft);
					if (i_71_ < i_73_)
						i_71_ = i_73_;
				}
			}
			
			int i_74_ = x1024(40.0F) + i_66_;
			int i_75_ = i_74_ + i_67_ + x1024(16.0F);
			int i_76_ = i_75_ + i_68_ + x1024(16.0F);
			int i_77_ = i_76_ + i_69_ + x1024(16.0F);
			if (Mission.isCoop())
				i_77_ = i_76_;
			int i_78_ = i_77_ + i_70_ + x1024(16.0F);
			int i_79_ = i_78_ + i_71_ + x1024(16.0F);
			int i_80_ = i_65_;
			for (int i_81_ = pageSizeNetStat * pageNetStat; (i_81_ < pageSizeNetStat * (pageNetStat + 1) && i_81_ < statUsers.size()); i_81_++)
			{
				StatUser statuser = (StatUser)statUsers.get(i_81_);
				i_80_ -= i;
				int i_82_ = Army.color(statuser.iArmy);
				if( Mission.MDS_VARIABLES().zutiHud_ShowPilotNumber )
					ttfont.output(i_82_, (float)i_74_ - ttfont.width(statuser.sNum), (float)i_80_, 0.0F, statuser.sNum);
				if( Mission.MDS_VARIABLES().zutiHud_ShowPilotPing )
					ttfont_64_.output(-1, ((float)i_75_ - ttfont_64_.width(statuser.sPing) - (float)x1024(4.0F)), (float)i_80_, 0.0F, statuser.sPing);
				if( Mission.MDS_VARIABLES().zutiHud_ShowPilotName )
					ttfont.output(i_82_, (float)i_75_, (float)i_80_, 0.0F, statuser.user.uniqueName());
				if (!Mission.isCoop() &&Mission.MDS_VARIABLES().zutiHud_ShowPilotScore)
					ttfont.output(i_82_, (float)i_76_, (float)i_80_, 0.0F, statuser.sScore);
				if( Mission.MDS_VARIABLES().zutiHud_ShowPilotArmy )
					ttfont.output(i_82_, (float)i_77_, (float)i_80_, 0.0F, statuser.sArmy);
				if( Mission.MDS_VARIABLES().zutiHud_ShowPilotACDesignation )
					ttfont.output(i_82_, (float)i_78_, (float)i_80_, 0.0F, statuser.sAircraft);
				if( Mission.MDS_VARIABLES().zutiHud_ShowPilotACType )
					ttfont.output(i_82_, (float)i_79_, (float)i_80_, 0.0F, statuser.sAircraftType);
			}
		}
	}
	
	public static void addPointer(float f, float f_92_, int i, float f_93_, float f_94_)
	{
		Main3D.cur3D().hud._addPointer(f, f_92_, i, f_93_, f_94_);
	}
	
	private void _addPointer(float f, float f_95_, int i, float f_96_, float f_97_)
	{
		if (nPointers == pointers.size())
			pointers.add(new Ptr(f, f_95_, i, f_96_, f_97_));
		else
		{
			Ptr ptr = (Ptr)pointers.get(nPointers);
			ptr.set(f, f_95_, i, f_96_, f_97_);
		}
		nPointers++;
	}
	
	private void renderPointers()
	{
		if (nPointers != 0)
		{
			float f = (float)viewDX / 1024.0F;
			int i = IconDraw.scrSizeX();
			int i_98_ = IconDraw.scrSizeY();
			for (int i_99_ = 0; i_99_ < nPointers; i_99_++)
			{
				Ptr ptr = (Ptr)pointers.get(i_99_);
				int i_100_ = (int)(64.0F * f * ptr.alpha);
				IconDraw.setScrSize(i_100_, i_100_);
				IconDraw.setColor(ptr.color & 0xffffff | (int)(ptr.alpha * 255.0F) << 24);
				IconDraw.render(spritePointer, ptr.x, ptr.y, 90.0F - ptr.angle);
			}
			IconDraw.setScrSize(i, i_98_);
			nPointers = 0;
		}
	}
	
	public void clearPointers()
	{
		nPointers = 0;
	}
	
	private void initPointers()
	{
		spritePointer = Mat.New("gui/game/hud/pointer.mat");
	}
	
	private void preRenderDashBoard()
	{
		if (Actor.isValid(World.getPlayerAircraft()) && Actor.isValid(main3d.cockpitCur))
		{
			if (main3d.isViewOutside())
			{
				if (main3d.viewActor() != World.getPlayerAircraft() || !main3d.cockpitCur.isNullShow())
					return;
			}
			else if (main3d.isViewInsideShow())
				return;
			if (bDrawDashBoard)
			{
				spriteLeft.preRender();
				spriteRight.preRender();
				//TODO: Disabled in 4.10.1
				//spriteG.preRender();
				meshNeedle1.preRender();
				meshNeedle2.preRender();
				meshNeedle3.preRender();
				meshNeedle4.preRender();
				meshNeedle5.preRender();
				meshNeedle6.preRender();
				meshNeedleMask.preRender();
			}
		}
	}
	
	private void renderDashBoard()
	{
		if (Actor.isValid(World.getPlayerAircraft()) && Actor.isValid(main3d.cockpitCur))
		{
			if (main3d.isViewOutside())
			{
				if (main3d.viewActor() != World.getPlayerAircraft() || !main3d.cockpitCur.isNullShow())
					return;
			}
			else if (main3d.isViewInsideShow())
				return;
			if (bDrawDashBoard)
			{
				float f = (float)viewDX;
				float f_101_ = (float)viewDY;
				float f_102_ = f / 1024.0F;
				float f_103_ = f_101_ / 768.0F;
				Render.drawTile(0.0F, 0.0F, 256.0F * f_102_, 256.0F * f_103_, 0.0F, spriteLeft, -1, 0.0F, 1.0F, 1.0F, -1.0F);
				Render.drawTile(768.0F * f_102_, 0.0F, 256.0F * f_102_, 256.0F * f_103_, 0.0F, spriteRight, -1, 0.0F, 1.0F, 1.0F, -1.0F);
				//TODO: Disabled in 4.10.1
				//Render.drawTile(200.0F * f_102_, 168.0F * f_103_, 64.0F * f_102_, 64.0F * f_103_, 0.0F, spriteG, -1, 0.0F, 1.0F, 1.0F, -1.0F);
				Point3d point3d = World.getPlayerAircraft().pos.getAbsPoint();
				Orient orient = World.getPlayerAircraft().pos.getAbsOrient();
				float f_104_ = (float)(point3d.z - World.land().HQ(point3d.x, point3d.y));
				_p.x = (double)(172.0F * f_102_);
				_p.y = (double)(84.0F * f_103_);
				_o.set(cvt(f_104_, 0.0F, 10000.0F, 0.0F, 3600.0F), 0.0F, 0.0F);
				meshNeedle2.setPos(_p, _o);
				meshNeedle2.render();
				_o.set(cvt(f_104_, 0.0F, 10000.0F, 0.0F, 360.0F), 0.0F, 0.0F);
				meshNeedle1.setPos(_p, _o);
				meshNeedle1.render();
				String string = "" + (int)((double)f_104_ + 0.5);
				float f_105_ = fntLcd.width(string);
				fntLcd.output(-1, 208.0F * f_102_ - f_105_, 70.0F * f_103_, 0.0F, string);
				if (f_104_ > 90.0F)
					meshNeedle5.setScale(90.0F * f_102_);
				else
					meshNeedle5.setScaleXYZ(90.0F * f_102_, 90.0F * f_102_, cvt(f_104_, 0.0F, 90.0F, 13.0F, 90.0F) * f_102_);
				f_104_ = (float)World.getPlayerAircraft().getSpeed(null);
				f_104_ *= 3.6F;
				_o.set(cvt(f_104_, 0.0F, 900.0F, 0.0F, 270.0F) + 180.0F, 0.0F, 0.0F);
				_p.x = (double)(83.0F * f_102_);
				_p.y = (double)(167.0F * f_103_);
				meshNeedle2.setPos(_p, _o);
				meshNeedle2.render();
				string = "" + (int)((double)f_104_ + 0.5);
				f_105_ = fntLcd.width(string);
				fntLcd.output(-1, 104.0F * f_102_ - f_105_, 135.0F * f_103_, 0.0F, string);
				for (f_104_ = orient.azimut() + 90.0F; f_104_ < 0.0F; f_104_ += 360.0F)
				{
					/* empty */
				}
				f_104_ %= 360.0F;
				_o.set(f_104_, 0.0F, 0.0F);
				_p.x = (double)(939.0F * f_102_);
				_p.y = (double)(167.0F * f_103_);
				meshNeedle3.setPos(_p, _o);
				meshNeedle3.render();
				string = "" + (int)((double)f_104_ + 0.5);
				f_105_ = fntLcd.width(string);
				fntLcd.output(-1, 960.0F * f_102_ - f_105_, 216.0F * f_103_, 0.0F, string);
				Orient orient_106_ = main3d.camera3D.pos.getAbsOrient();
				_p.x = (double)(511.0F * f_102_);
				_p.y = (double)(96.0F * f_103_);
				if (orient_106_.tangage() < 0.0F)
				{
					_o1.set(orient_106_);
					_o1.increment(0.0F, 0.0F, 90.0F);
					_o1.increment(0.0F, 90.0F, 0.0F);
					_o.sub(_oNull, _o1);
					meshNeedle5.setPos(_p, _o);
					meshNeedle5.render();
				}
				_o1.set(orient_106_);
				_o1.increment(0.0F, 0.0F, 90.0F);
				_o1.increment(0.0F, 90.0F, 0.0F);
				_o.sub(orient, _o1);
				meshNeedle4.setPos(_p, _o);
				meshNeedle4.render();
				if (orient_106_.tangage() >= 0.0F)
				{
					_o1.set(orient_106_);
					_o1.increment(0.0F, 0.0F, 90.0F);
					_o1.increment(0.0F, 90.0F, 0.0F);
					_o.sub(_oNull, _o1);
					meshNeedle5.setPos(_p, _o);
					meshNeedle5.render();
				}
				_p.x = (double)(851.0F * f_102_);
				_p.y = (double)(84.0F * f_103_);
				_o1.set(orient);
				_o1.set(0.0F, -_o1.tangage(), _o1.kren());
				_o1.increment(0.0F, 0.0F, 90.0F);
				_o1.increment(0.0F, 90.0F, 0.0F);
				_o.sub(_oNull, _o1);
				meshNeedle6.setPos(_p, _o);
				meshNeedle6.render();
				_o.set(0.0F, 0.0F, 0.0F);
				meshNeedleMask.setPos(_p, _o);
				meshNeedleMask.render();
				int i = (int)(World.getPlayerFM().getOverload() * 10.0F);
				float f_107_ = (float)i / 10.0F;
				String string_108_ = "" + f_107_;
				float f_109_ = fntLcd.width(string_108_);
				if (World.getPlayerFM().getLoadDiff() < World.getPlayerFM().getLimitLoad() * 0.25F)
					fntLcd.output(-16776961, 215.0F * f_102_ - f_109_, 182.0F * f_103_, 0.0F, string_108_);
				else if (i < 0)
					fntLcd.output(-16777216, 215.0F * f_102_ - f_109_, 182.0F * f_103_, 0.0F, string_108_);
				else
					fntLcd.output(-1, 215.0F * f_102_ - f_109_, 182.0F * f_103_, 0.0F, string_108_);
			}
		}
	}
	
	private float cvt(float f, float f_110_, float f_111_, float f_112_, float f_113_)
	{
		f = Math.min(Math.max(f, f_110_), f_111_);
		return f_112_ + (f_113_ - f_112_) * (f - f_110_) / (f_111_ - f_110_);
	}
	
	private void initDashBoard()
	{
		spriteLeft = Mat.New("gui/game/hud/hudleft.mat");
		spriteRight = Mat.New("gui/game/hud/hudright.mat");
		meshNeedle1 = new Mesh("gui/game/hud/needle1/mono.sim");
		meshNeedle2 = new Mesh("gui/game/hud/needle2/mono.sim");
		meshNeedle3 = new Mesh("gui/game/hud/needle3/mono.sim");
		meshNeedle4 = new Mesh("gui/game/hud/needle4/mono.sim");
		meshNeedle5 = new Mesh("gui/game/hud/needle5/mono.sim");
		meshNeedle6 = new Mesh("gui/game/hud/needle6/mono.sim");
		meshNeedleMask = new Mesh("gui/game/hud/needlemask/mono.sim");
		//TODO: Disabled in 4.10.1
		//spriteG = Mat.New("gui/game/hud/hudg.mat");
		fntLcd = TTFont.get("lcdnova");
		setScales();
	}
	
	private void setScales()
	{
		float f = (float)viewDX;
		float f_114_ = f / 1024.0F;
		meshNeedle1.setScale(140.0F * f_114_);
		meshNeedle2.setScale(140.0F * f_114_);
		meshNeedle3.setScale(75.0F * f_114_);
		meshNeedle4.setScale(100.0F * f_114_);
		meshNeedle5.setScale(90.0F * f_114_);
		meshNeedle6.setScale(150.0F * f_114_);
		meshNeedleMask.setScale(150.0F * f_114_);
	}
	
	public void render()
	{
		renderSpeed();
		renderOrder();
		renderMsg();
		renderTraining();
		renderLog();
		renderDashBoard();
		renderPointers();
		renderNetStat();
	}
	
	public void preRender()
	{
		preRenderDashBoard();
	}
	
	public void resetGame()
	{
		setScales();
		clearSpeed();
		clearOrder();
		clearMsg();
		clearTraining();
		clearLog();
		clearPointers();
		stopNetStat();
	}
	
	public void contextResize(int i, int i_115_)
	{
		viewDX = main3d.renderHUD.getViewPortWidth();
		viewDY = main3d.renderHUD.getViewPortHeight();
		setScales();
		resetMsgSizes();
		resetTrainingSizes();
	}
	
	public HUD()
	{
		main3d = Main3D.cur3D();
		viewDX = main3d.renderHUD.getViewPortWidth();
		viewDY = main3d.renderHUD.getViewPortHeight();
		initSpeed();
		initOrder();
		initMsg();
		initTraining();
		initLog();
		initDashBoard();
		initPointers();
		bNoSubTitles = Config.cur.ini.get("game", "NoSubTitles", bNoSubTitles);
		subTitlesLines = Config.cur.ini.get("game", "SubTitlesLines", subTitlesLines);
		if (subTitlesLines < 1)
			subTitlesLines = 1;
		bNoHudLog = Config.cur.ini.get("game", "NoHudLog", bNoHudLog);
	}
}
