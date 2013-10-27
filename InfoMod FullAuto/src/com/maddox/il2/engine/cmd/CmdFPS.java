// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   CmdFPS.java

package com.maddox.il2.engine.cmd;

import java.util.Map;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.RendersMain;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.WeaponsHelper;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.MsgTimeOut;
import com.maddox.rts.MsgTimeOutListener;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;
import com.maddox.sound.AudioDevice;
import com.maddox.util.NumberTokenizer;

public class CmdFPS extends Cmd
    implements MsgTimeOutListener
{

    public void msgTimeOut(Object obj)
    {
        msg.post();
        if(!bGo)
            return;
        long l = Time.real();
        int i = RendersMain.frame();
        if(l >= timePrev + 250L)
        {
            fpsCur = (1000D * (double)(i - framePrev)) / (double)(l - timePrev);
            if(fpsMin > fpsCur)
                fpsMin = fpsCur;
            if(fpsMax < fpsCur)
                fpsMax = fpsCur;
            framePrev = i;
            timePrev = l;
        }
        if(framePrev == frameStart)
            return;
        if(bShow)
        {
            Render render = (Render)Actor.getByName("renderTextScr");
            if(render == null)
                return;
            TTFont ttfont = TextScr.font();
            int j = render.getViewPortWidth();
            int k = render.getViewPortHeight();
            String s = "fps:" + fpsInfo();
            int i1 = (int)ttfont.width(s);
            int j1 = k - ttfont.height() - 5;
            int k1 = (j - i1) / 2;
            TextScr.output(k1, j1, s);
        }
        if(logPeriod > 0L && l >= logPrintTime + logPeriod)
        {
            System.out.println("fps:" + fpsInfo());
            logPrintTime = l;
        }
    }

    public Object exec(CmdEnv cmdenv, Map map)
    {
        boolean flag = false;
        checkMsg();
        if(map.containsKey("SHOW"))
        {
            bShow = true;
            flag = true;
        } else
        if(map.containsKey("HIDE"))
        {
            bShow = false;
            flag = true;
        }
        if(map.containsKey("LOG"))
        {
            int i = arg(map, "LOG", 0, 5);
            if(i < 0)
                i = 0;
            logPeriod = (long)i * 1000L;
            flag = true;
        }
        if(map.containsKey("PERF"))
        {
            AudioDevice.setPerformInfo(true);
            flag = true;
        }
        if(map.containsKey("START"))
        {
            if(bGo && timeStart != timePrev && logPeriod == 0L)
                INFO_HARD("fps:" + fpsInfo());
            timeStart = Time.real();
            frameStart = RendersMain.frame();
            fpsMin = 1000000D;
            fpsMax = 0.0D;
            fpsCur = 0.0D;
            timePrev = timeStart;
            framePrev = frameStart;
            logPrintTime = timeStart;
            bGo = true;
            flag = true;
        } else
        if(map.containsKey("STOP"))
        {
            if(bGo && timeStart != timePrev && logPeriod == 0L)
                INFO_HARD("fps:" + fpsInfo());
            bGo = false;
            flag = true;
        }
        if(map.containsKey("FMINFO"))
        {
            flag = true;
            if(Mission.isNet() && NetEnv.hosts().size() > 1)
            {
                System.out.println("Network mission detected, disabling fm debug.");
            } else
            {
                if(map.containsKey("SWITCH"))
                    if(!Maneuver.showFM)
                        Maneuver.showFM = true;
                    else
                        Maneuver.showFM = false;
                if(map.containsKey("DUMP") && Actor.isAlive(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.isPlayerDead())
                {
                    com.maddox.il2.fm.FlightModel flightmodel = World.getPlayerFM();
                    if(flightmodel != null)
                        flightmodel.drawData();
                }
                if(map.containsKey("DMPALL") && Actor.isAlive(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.isPlayerDead())
                {
//                    if (nargs(map, "DMPALL") == 0) {
//        				System.out.println("TEST!!!");
//        				System.out.println("1 " + I18N.plane("com.maddox.il2.objects.air.BF_109E4") + " ... ");
//        				System.out.println("2 " + I18N.plane("air.BF_109E4") + " ... ");
//        				System.out.println("3 " + I18N.plane("BF_109E4") + " ... ");
//        				System.out.println("3 " + I18N.plane("Bf-109E-4") + " ... ");
//        				System.out.println("3 " + getAirNameForClassName("BF_109E4") + " ... ");
//                    } else
                	
                    if (nargs(map, "DMPALL") > 0) {
                    	DumpDesc = arg(map, "DMPALL", 0);
                    }
                    DumpStart = 0;
                    if (nargs(map, "DMPALL") > 1) {
                    	DumpStart = arg(map, "DMPALL", 1, 0);
                    }
                    dumpAllFlightModels();
                }
                if(map.containsKey("FMDMP") && Actor.isAlive(World.getPlayerAircraft()) && !World.isPlayerParatrooper() && !World.isPlayerDead())
                {
                    if (nargs(map, "FMDMP") > 0) {
	                	dumpFlightModel(arg(map, "FMDMP", 0));
                    } else {
                    	System.out.println("FMDMP no args!");
                    }
                }
            }
        }
        if(!flag)
        {
            INFO_HARD("  LOG  " + logPeriod / 1000L);
            if(bShow)
                INFO_HARD("  SHOW");
            else
                INFO_HARD("  HIDE");
            if(bGo)
            {
                if(timeStart != timePrev)
                    INFO_HARD("  " + fpsInfo());
            } else
            {
                INFO_HARD("  STOPPED");
            }
        }
        return CmdEnv.RETURN_OK;
    }
    
    private static String getAirNameForClassName(String theClassName) {
    	String sRetVal = theClassName;
        SectFile sectfile = new SectFile("com/maddox/il2/objects/air.ini");
        boolean bFound = false;
        for (int i=0; i<sectfile.sections(); i++) {
        	for (int j=0; j<sectfile.vars(i); j++) {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.value(i, j));
                String s = numbertokenizer.next((String)null);
//                System.out.println("getAirNameForClassName i=" + i + ", j=" + j + " s=" + s);
                if (s.substring(4).equalsIgnoreCase(theClassName)) {
                	sRetVal = sectfile.var(i, j);
//                    System.out.println("getAirNameForClassName sRetVal=" + sRetVal);
                	bFound = true;
                	break;
                }
        	}
        	if (bFound) break;
        }
        
        return sRetVal;
    }

    private static void dumpFlightModel(String theAircraftClass) {
		EnginesInterface.inFmDump = true;
    	HUD.training("dumpFlightModel " + theAircraftClass);
    	try {
    		World.getPlayerAircraft().destroy();
			Class airClass = Class.forName(theAircraftClass);
			World.setPlayerAircraft((Aircraft)airClass.newInstance());
			Aircraft theAircraft = World.getPlayerAircraft();
			String aircraftFM = Property.stringValue(airClass, "FlightModel", null);
			if (aircraftFM != null) {
				theAircraft.interpEnd("FlightModel");
				theAircraft.setFM(aircraftFM, 1, true);
				WeaponsHelper.weaponsLoad(theAircraft, "default");
				FlightModel theFlightModel = theAircraft.FM;
				theFlightModel.M.computeParasiteMass(theFlightModel.CT.Weapons);
				theFlightModel.M.fuel = theFlightModel.M.maxFuel;
				theFlightModel.M.nitro = theFlightModel.M.maxNitro;
//				theFlightModel.M.mass = theFlightModel.M.massEmpty + theFlightModel.M.maxFuel + theFlightModel.M.maxNitro + theFlightModel.M.parasiteMass;
				theFlightModel.M.requestNitro(0);
				
				
				String i18Name = I18N.plane(getAirNameForClassName(airClass.getName().substring(27)));
				
				i18Name = i18Name.replace('<', '_');
				i18Name = i18Name.replace('>', '_');
				i18Name = i18Name.replace(':', '_');
				i18Name = i18Name.replace('"', '_');
				i18Name = i18Name.replace('/', '_');
				i18Name = i18Name.replace('\\', '_');
				i18Name = i18Name.replace('|', '_');
				i18Name = i18Name.replace('?', '_');
				i18Name = i18Name.replace('*', '_');
				i18Name = i18Name.replace('\t', ' ');
				
				System.out.println(i18Name + " Masses: massEmpty=" + theFlightModel.M.massEmpty
													+ ", maxFuel="  + theFlightModel.M.maxFuel
													+ ", maxNitro="  + theFlightModel.M.maxNitro
													+ ", parasiteMass="  + theFlightModel.M.parasiteMass
													+ ", mass="  + theFlightModel.M.mass
													);
				System.out.print("dumping FM data of " + i18Name + " ... ");
				theFlightModel.speedFile = "FlightModels/" + i18Name;
				theFlightModel.turnFile = "FlightModels/" + i18Name;
				theFlightModel.craftFile = "FlightModels/" + i18Name;
				theFlightModel.drawData(iFuelLevel);
				System.out.println("done.");
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		EnginesInterface.inFmDump = false;
    }
    
	private static void dumpAllFlightModels() {
		System.out.println("*** FM DUMP START ! ***");
		iFuelLevel = 100;
		EnginesInterface.inFmDump = true;
		for (int iAirClassIterator = DumpStart; iAirClassIterator < Main.cur().airClasses.size(); iAirClassIterator++) {
//	    for (int iAirClassIterator = DumpStart; iAirClassIterator < 10; iAirClassIterator++) {
			Class airClass = (Class) Main.cur().airClasses.get(iAirClassIterator);
			System.out.println("" + iAirClassIterator + ". dumpFlightModel " + airClass.getName());
			if (airClass.getName().toLowerCase().indexOf("placeholder") != -1) {
				System.out.println("...is PlaceHolder, choose next.");
				continue;
			}
			CmdEnv.top().exec("fps FMINFO FMDMP " + airClass.getName());
		}
		EnginesInterface.inFmDump = false;
		iFuelLevel = -1;
		System.out.println("*** FM DUMP FINISHED ! ***");
		RTSConf.setRequestExitApp(true);
	}

    private String fpsInfo()
    {
        return "" + (int)Math.floor(fpsCur) + " avg:" + (int)Math.floor((1000D * (double)(framePrev - frameStart)) / (double)(timePrev - timeStart)) + " max:" + (int)Math.floor(fpsMax) + " min:" + (int)Math.floor(fpsMin) + " #fr:" + (framePrev - frameStart);
    }

    private void checkMsg()
    {
        if(msg == null)
        {
            msg = new MsgTimeOut();
            msg.setListener(this);
            msg.setNotCleanAfterSend();
            msg.setFlags(72);
        }
        if(!msg.busy())
            msg.post();
    }

    public CmdFPS()
    {
        bGo = false;
        bShow = false;
        logPeriod = 5000L;
        logPrintTime = -1L;
        param.put("LOG", null);
        param.put("START", null);
        param.put("STOP", null);
        param.put("SHOW", null);
        param.put("HIDE", null);
        param.put("PERF", null);
        param.put("FMINFO", null);
        param.put("SWITCH", null);
        param.put("DUMP", null);
        param.put("DMPALL", null);
        param.put("FMDMP", null);
        _properties.put("NAME", "fps");
        _levelAccess = 1;
    }

    private boolean bGo;
    private boolean bShow;
    private long timeStart;
    private int frameStart;
    private double fpsMin;
    private double fpsMax;
    private double fpsCur;
    private long timePrev;
    private int framePrev;
    private long logPeriod;
    private long logPrintTime;
    public static final String LOG = "LOG";
    public static final String START = "START";
    public static final String STOP = "STOP";
    public static final String SHOW = "SHOW";
    public static final String HIDE = "HIDE";
    public static final String PERF = "PERF";
    private MsgTimeOut msg;
    public static String DumpDesc = "InfoMod 4.12.1m"; 
    public static int DumpStart = 0;
    private static int iFuelLevel = -1;
}
