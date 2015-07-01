//////////////////////////////////////////////////////////////////////
// 4.111m By PAL - MODded to have extra-functions in the Briefing, etc.
// lifted to 4.12 by SAS~Storebror
//////////////////////////////////////////////////////////////////////

package com.maddox.il2.gui;

import java.io.BufferedWriter;
import java.io.File; //By PAL
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;				//By PAL
import java.util.ResourceBundle;

import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.GBevel;
import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GFileFilter;
import com.maddox.gwindow.GFileFilterName;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowFileBox;
import com.maddox.gwindow.GWindowFileBoxExec;
import com.maddox.gwindow.GWindowFileSaveAs;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.gwindow.GWindowScrollingDialogClient;
import com.maddox.il2.ai.AirportCarrier;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.Front;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.builder.PlMission;
import com.maddox.il2.builder.Plugin;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.BmpUtils;
import com.maddox.il2.engine.CameraOrtho2D;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.GUIRenders;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Land2DText;
import com.maddox.il2.engine.Land2Dn;
import com.maddox.il2.engine.LightEnvXY;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.Renders;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.HomePath;
import com.maddox.rts.IniFile;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;
import com.maddox.util.UnicodeTo8bit;

public class GUIBriefingGeneric extends GameState
{
		
    public void ShowMessage(String a, String b)
    {
      	new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, a, b, 3, 0.0F);     	
    }	
	
    class DlgFileConfirmSave extends GWindowFileBoxExec
    {
        public boolean isCloseBox()
        {
            return bClose;
        }

        public void exec(GWindowFileBox gwindowfilebox, String s)
        {
            box = gwindowfilebox;
            bClose = true;
            if(s == null || box.files.size() == 0)
            {
                box.endExec();
            } else
            {
                int i = s.lastIndexOf("/");
                if(i >= 0)
                    s = s.substring(i + 1);
                for(int j = 0; j < box.files.size(); j++)
                {
                    String s1 = ((File)box.files.get(j)).getName();
                    if(s.compareToIgnoreCase(s1) == 0)
                    {
                        new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, I18N.gui("warning.Warning"), I18N.gui("warning.ReplaceFile"), 1, 0.0F) {
                            public void result(int k)
                            {
                                if(k != 3)
                                    bClose = false;
                                box.endExec();
                            }
                        }
;
                        return;
                    }
                }
                box.endExec();
            }
        }

        GWindowFileBox box;
        boolean bClose;

        DlgFileConfirmSave()
        {
            bClose = true;
        }
    }	
    
    public void SaveAs()
    {    		
    	missionFileName = OriginalFileName;  //I have to use in first term the last original filename
    	lastOpenFile = OriginalFileName;
    	//File f = new File(OriginalFileName);
        GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(Main3D.cur3D().guiManager.root, true, Plugin.i18n("SaveMission"), "missions/Single/", new GFileFilter[]
         { new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] {"*.mis"})
         	})
         		{
			        public void result(String s)
                    {
                        if(s != null)
                        {
                            s = checkMisExtension(s);
                            missionFileName = s;
                            lastOpenFile = s;                            
                            SaveMissionDescription(OriginalFileName, "missions/Single/" + s); //By PAL, two parameters
                            Main.cur().currentMissionFile.saveFile("missions/Single/" + s);
                        }
                    }
                }
;
                gwindowfilesaveas.exec = new DlgFileConfirmSave();
				gwindowfilesaveas.setSelectFile(missionFileName);
            }
	
    private String checkMisExtension(String s)
    {
        if(!s.toLowerCase().endsWith(".mis"))
            return s + ".mis";
        else
            return s;
    }


    // Removed by SAS~Storebror, unused private method
//    private String changeExtension(String s, String extension)
//    {
//        if(s.toLowerCase().endsWith(extension))
//        {
//            return s;        	
//        }
//        else
//        {
//            for(int i = s.length() - 1; i > 0; i--)
//            {
//                char c = s.charAt(i);
//                if(c == '\\' || c == '/')
//                    break;
//                if(c != '.')
//                    continue;
//                s = s.substring(0, i);
//                break;
//            }        	
//            return s + "." + extension;
//        }       
//    }
    
    private String textFileName(String tFileName) //By PAL, from PlMisBrief
    {
        String s = "";
		String s1 = Locale.getDefault().getLanguage();        
        String s3 = RTSConf.cur.locale.getLanguage(); //By PAL, I have to use the one in RTS
        if (s3.equalsIgnoreCase("ru") && !s1.equalsIgnoreCase("ru"))
        	s1 = "us"; //By PAL, if the default Ru is not the language of your system!! 
        if(s1 == null || s1.length() < 2) //By PAL
        	s1 = "us";
        if(!"us".equals(s1))
            s = "_" + s1;    	
        String s2 = tFileName;
        for(int i = s2.length() - 1; i >= 0; i--)
        {
            char c = s2.charAt(i);
            if(c == '/' || c == '\\')
                break;
            if(c == '.')
                return s2.substring(0, i) + s + ".properties";
        }
        return s2 + s + ".properties";
    }
    
	private void SaveMissionDescription(String OldFileName, String NewFileName)
	{   //By PAL, save properties too	
	    String nameProps = "";
        String shortProps = "";
        String descriptionProps = "";
        PrintWriter printwriter = null;
        String s = OldFileName; 
        for(int i = s.length() - 1; i > 0; i--)
        {
            char c = s.charAt(i);
            if(c == '\\' || c == '/')
                break;
            if(c != '.')
                continue;
            s = s.substring(0, i);
            break;
        }                	            
        try
        {
            ResourceBundle resourcebundleN = ResourceBundle.getBundle(s, RTSConf.cur.locale);
            nameProps = resourcebundleN.getString("Name");
        }
        catch(Exception exceptionProps) {}            
        try
        {
            ResourceBundle resourcebundleP = ResourceBundle.getBundle(s, RTSConf.cur.locale);
            shortProps = resourcebundleP.getString("Short");
        }
        catch(Exception exceptionShort) {}
        try
        {
            ResourceBundle resourcebundleD = ResourceBundle.getBundle(s, RTSConf.cur.locale);
            descriptionProps = resourcebundleD.getString("Description");
        }
        catch(Exception exceptionDescription) {}            
        try
        {        					            
        //String fileMission = LastMissionProps; //textFileName(sectfile);
            printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(textFileName(NewFileName), 0))));
     /*       if(nameProps != null && nameProps.length() > 0)
                printwriter.println("Name " + UnicodeTo8bit.save(nameProps, false));                
            if(shortProps != null && shortProps.length() > 0)
                printwriter.println("Short " + UnicodeTo8bit.save(shortProps, false));               
            if(descriptionProps != null && descriptionProps.length() > 0)
            {
            	if (descriptionProps.toLowerCase().indexOf(HeaderDescript.toLowerCase().trim()) == -1) //By PAL, not to duplicate comment
            		descriptionProps = (DescriptString != null ? (DescriptString + "\n" + descriptionProps) : descriptionProps); //By PAL, only if corresponds 
                printwriter.println("Description " + UnicodeTo8bit.save(descriptionProps, false));            	
            }*/
            if(nameProps == null) nameProps = " ";
            printwriter.println("Name " + UnicodeTo8bit.save(nameProps, false));                
            if(shortProps == null) shortProps = " ";
            printwriter.println("Short " + UnicodeTo8bit.save(shortProps, false));               
            if(descriptionProps == null) descriptionProps = " ";
        	if (descriptionProps.toLowerCase().indexOf(HeaderDescript.toLowerCase().trim()) == 0) //By PAL, not to duplicate comment
        		descriptionProps = (DescriptString != null ? (DescriptString + "\n" + descriptionProps) : descriptionProps); //By PAL, only if corresponds 
            printwriter.println("Description " + UnicodeTo8bit.save(descriptionProps, false));            	           
        }
        catch(Exception exception)
        {
           	System.out.println(exception.getMessage());
           	exception.printStackTrace();        	
        }
        if(printwriter != null)
            try
            {
                printwriter.close();
            }
            catch(Exception exception1){}
	}
	
    private int getFirstNonPlaceholder(int k)
    {
    	///////////////////////////////////////////////////////////////////////////////////////////////////////////////                    
		//              By PAL, avoid selecting placeholder
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////                  
		int k0 = k;
        ItemAir itemair;
        itemair = (ItemAir)aiPlane.get(k0);
        while(itemair.className.equalsIgnoreCase(PlaceholderLabel)) 
        {
        	if(k >= wSelectPlane.size() - 1) break;
        	k++;
            itemair = (ItemAir)aiPlane.get(k);
        }
        if(k >= wSelectPlane.size() - 1)
        {
            k = k0; //I start in the original value
            while(itemair.className.equalsIgnoreCase(PlaceholderLabel)) //By PAL, avoid selecting placeholder
            {
            	if(k <= 0) break;
            	k--;
                itemair = (ItemAir)aiPlane.get(k);
            }                	
        }
        return k;               
        //wSelectPlane.setSelected(k, true, false);   //Avoid processing            	    
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
    }	 	

	public class DialogClient extends GUIDialogClient
    {
        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
        	
          //Added by PAL
            if(gwindow == wWeather)
            {
				doUpdateMissionParams();            	
				return true;
            }             
            if(gwindow == wCldHeight)
            {
				doUpdateMissionParams();             	
				return true;
            }
            if(gwindow == wTimeHour)
            {
				doUpdateMissionParams();             	
				return true;
            }             
            if(gwindow == wTimeMins)
            {
				doUpdateMissionParams();             	
				return true;
            }                            
            if(gwindow == wSelectFlight)
            {
            	//SectFile sectfile = Main.cur().currentMissionFile;         
            	//FlightName = sectfile.var(j, wSelectFlight.getSelected());          
				//sectfile.set("MAIN", "player", FlightName);            	
            	doUpdateFlight();
            	return true;
            }
            if(gwindow == wSelectPlane)
            {
                int k = wSelectPlane.getSelected();
                if(k < 0)
                    return true;
                wSelectPlane.setSelected(getFirstNonPlaceholder(k), true, false);   //Avoid processing 
                          	
   /*         	///////////////////////////////////////////////////////////////////////////////////////////////////////////////                    
				//              By PAL, avoid selecting placeholder
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////                  
				int k0 = k;
                while(itemair.className.equalsIgnoreCase(PlaceholderLabel)) 
                {
                	if(k >= wSelectPlane.size() - 1) break;
                	k++;
	                itemair = (ItemAir)aiPlane.get(k);
                }
                if(k >= wSelectPlane.size() - 1)
                {
	                k = k0; //I start in the original value
	                while(itemair.className.equalsIgnoreCase(PlaceholderLabel)) //By PAL, avoid selecting placeholder
	                {
	                	if(k <= 0) break;
	                	k--;
		                itemair = (ItemAir)aiPlane.get(k);
	                }                	
                }               
                wSelectPlane.setSelected(k, true, false);   //Avoid processing            	    
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	*/			
				doUpdatePlane();
				return true;
            }
            if(gwindow == wSelectWeapons)
            {
				doUpdateWeapons();
				return true;
            }
            if(gwindow == wSelectSkin)
            {
				doUpdateSkin();
				return true;
            }                                   
            if(gwindow == wSelectNum)
            {
				doUpdateNum();
				return true;     	
            }
            if(gwindow == sOwnSkinOn)
            {           	
				return true;     	
            }
            if(gwindow == sParachuteOn)
            {           	
				return true;     	
            }
            if(gwindow == bFMB)
            {
            //	String s1 = RTSConf.cur.locale.getLanguage(); //Locale.getDefault().getLanguage(); By PAL, I have to use the one in RTS
			//	ShowMessage("Locale", s1);
            	try
            	{
			       	if (!bSingleMission)
            		{
        				SectFile sectfile = Main.cur().currentMissionFile;
        				sectfile.set("MAIN", "player", originalFlightName); //To avoid changing the plane in a Campaign
            		}
					SaveMissionDescription(OriginalFileName, LastMissionFile);
	       			_leave();	       				       				       			
			        /*Main.stateStack().pop();
	        		Main.stateStack().push(18);*/
	        		Main.stateStack().push(18);
	       			PlMission.doLoadMissionFile(LastMissionFile, OriginalFileName);//Here or before	        					                		
	        	}
	   			catch(Exception exception)
            	{
		           	System.out.println(exception.getMessage());
		           	exception.printStackTrace();
            	}
                //return super.notify(gwindow, i, j);            				
                return true;
            }                                  
            if(gwindow == bSave)
            {
            	SaveAs();
                //Main.stateStack().push(24);            	
                return true;
            }
            if(gwindow == bReset)
            {
//By PAL, new addition, check if it's OK            	
				Main.cur().currentMissionFile = new SectFile(OriginalFileName, 0);  //By PAL, open previous
				OriginalFileName = null; //By PAL, to load it from the beginning		        	
	        	_enter();            	
                return true;
            }                        
             //By PAL, end of added        	
            if(gwindow == bPrev)
            {
		    	if (bNotMultiPlay) //By PAL, or not?
		   		{
		   			if (bSingleMission) Main.cur().currentMissionFile.saveFile(LastMissionFile); //Save last status of mission file
		   		}            	
                doBack();
                return true;
            }
            if(gwindow == bNext)
            {
            	if (bNotMultiPlay)
            	{          		
	            	try
	            	{
						String OldFlightName = FlightName; //By PAL, to let Campaign User know about the different plane	            		         			            		
	            		if (!bSingleMission)
	            		{
            				SectFile sectfile = Main.cur().currentMissionFile;
            				sectfile.set("MAIN", "player", originalFlightName); //To avoid changing the plane in a Campaign
	            		}	            			
						Main.cur().currentMissionFile.saveFile(LastMissionFile); //By PAL, Save last status of mission file			            	            			         				         			

	        			boolean HasCockpit = true;
	        			boolean OnlyAI = false;
	        			String CurrentPlane = null;
						SectFile sectfile = Main.cur().currentMissionFile;
						FlightName = sectfile.get("MAIN", "player", (String)null);	        			
	        			if(FlightName != null)   //If there is a flight and a plane existent
	        			{
					    	PlaneName = sectfile.get(FlightName, "Class", (String)null);
					    	if(PlaneName != null)
					    	{
			            		Class planeClass =  ObjIO.classForName(PlaneName);
		               	        HasCockpit = (Property.value(planeClass, "cockpitClass") != null);
				                OnlyAI = (sectfile.get(FlightName, "OnlyAI", 0, 0, 1) == 1);
				                CurrentPlane = Property.stringValue(planeClass, "keyName", (String)null);				                
					    	}
			                if (!HasCockpit)
			                {	                	
								ShowMessage("Warning", "The plane '" + CurrentPlane +
									"' does not have cockpit to fly it! Please choose a different one.");
			                }
			                if (OnlyAI)
			                {
								ShowMessage("Warning", "The Flight " + FlightName +
									" is A.I. Only for this mission! Please select another one.");	                
			                }					    				                
			                if (HasCockpit && !OnlyAI)
			                {
					   			if((PlaneName != null)&&(!sOwnSkinOn.isChecked()))  //By PAL
					       		{
					            		Class planeClass = ObjIO.classForName(PlaneName);				                		
/*By PAL, rever esto*/             		LastPlaneSkin = Property.stringValue(planeClass, "keyName", (String)null);
				            			LastSkin = World.cur().userCfg.getSkin(LastPlaneSkin);
				            			LastNoseArt = World.cur().userCfg.getNoseart(LastPlaneSkin);         			          
				            			if(FlightName != null)   //If there is a flight and a plane exists
				            			{
				            				int playerNum = sectfile.get("MAIN", "playerNum", 0, 0, 3);  //By PAL, get the Plane of the Player
				            				String NewSkin = sectfile.get(FlightName, "skin" + playerNum, (String)null);
					            			String NewNoseArt = sectfile.get(FlightName, "noseart" + playerNum, (String)null);
					            			String NewPilot = sectfile.get(FlightName, "pilot" + playerNum, i18n("neta.Default"));
	            							boolean NewNumberOn = (sectfile.get(FlightName, "numberOn" + playerNum, 1, 0, 1) == 1);
					            			if (LastPlaneSkin != null)
					            				if (NewSkin != null)
						            			{
						            				World.cur().userCfg.setSkin(LastPlaneSkin, NewSkin);
						            				World.cur().userCfg.setNoseart(LastPlaneSkin, NewNoseArt);
						            				
						            			}
						            			else
						            			{
						            				World.cur().userCfg.setSkin(LastPlaneSkin, i18n("neta.Default"));  //null
						            				World.cur().userCfg.setNoseart(LastPlaneSkin, i18n("neta.Default")); //null
						            			}
						            		World.cur().userCfg.netPilot = NewPilot; //By PAL, recently added
						            		World.cur().userCfg.netNumberOn = NewNumberOn;
				            			}		            			
					       		}             	
					            /*else
					            {
					            	LastPlaneSkin = null;
					            	LastSkin = null;
					            	LastNoseArt = null;
					            }*/
					           /* Why to do this?
					            * if (LastPlaneSkin != null)
					            {
							 		try  //By PAL, Save status of sOwnSkinOn switch, etc
							 		{				   			
							        	IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);  //By PAL, Save last settings
								  		inifile.set("LastSingleMission", "LastPlaneSkin", LastPlaneSkin);
										if (LastSkin != null) inifile.set("LastSingleMission", "LastSkin", LastSkin);
										if (LastNoseArt != null) inifile.set("LastSingleMission", "LastNoseArt", LastNoseArt);   		
								    	inifile.saveFile();  		
							   		}
							    	catch(Exception exception)
							    	{
							            System.out.println(exception.getMessage());
							            exception.printStackTrace();
							    	}
					            }*/
					            
					            if (!sParachuteOn.isChecked())
					            { //By PAL, remove Parachutes if selected
						            int n = sectfile.sectionIndex("Wing");
	        						if (n >= 0)   //if Exists
						        	{
						           		int k = sectfile.vars(n);
						        		for(int i1 = 0; i1 < k; i1++)
						        		{
						            		String s = sectfile.var(n, i1);
						            		if(!s.equalsIgnoreCase(FlightName)) //By PAL, only if it is not the Player one
						            			sectfile.set(s, "Parachute", "0");
						        		}
						        	}					            	
					            }
					            					            
					            if (!bSingleMission && !OldFlightName.equalsIgnoreCase(originalFlightName))
					            {
					            	String Message = "You will be flying the original Flight for this campaign: " + originalFlightName +
					            		((PlaneName.equalsIgnoreCase(OriginalPlaneName))? "\nwith the original plane " : "\nnow with the plane ") + 
					            			CurrentPlane + ".";
	                        		new GWindowMessageBox(client.root, 20F, true, "Reminder", Message, 3, 0.0F)
	                        		{
						                public void result(int i)
						                {
						                    if(i == 2) doNext();
						                }
					            	};		 					
					            } else doNext();
			                }					   					                
	        			}     				   	    		
					}           				
		   			catch(Exception exception)
	            	{
			           	System.out.println(exception.getMessage());
			           	exception.printStackTrace();
	            	}
            	}
            	else
            	{	//By PAL, if MultiPlay
            		doNext();
            	}
            	return true;           	
               /* doNext();
                return true;*/
            }
            if(gwindow == bDifficulty)
            {
                doDiff();
                return true;
            }
            if(gwindow == bLoodout)
            {
            	if (bSingleMission && !sOwnSkinOn.isChecked())
            	{
            		toAirArming();
            	}
            	else
            	{
            		doLoodout();
            	} 
                return true;
            } else
            {
                return super.notify(gwindow, i, j);
            }
        }

        public void render()
        {
            super.render();
            //GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(624F), x1024(924F), 2.5F);
            GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(640F), x1024(960F), 2.0F); //By PAL, a little lower
            
            //+++ TODO: 4.12 changed code +++
            if(bNext.isVisible())
            {
            //--- TODO: 4.12 changed code ---
            	
	            GUISeparate.draw(this, GColor.Gray, x1024(457F), y1024(686F), x1024(30F), 2.0F);
	            GUISeparate.draw(this, GColor.Gray, x1024(537F), y1024(686F), x1024(30F), 2.0F);
	            GUISeparate.draw(this, GColor.Gray, x1024(457F), y1024(640F), 1.0F, y1024(46F)); //x1024
	            GUISeparate.draw(this, GColor.Gray, x1024(567F), y1024(640F), 1.0F, y1024(46F)); //x1024
	        
	        //+++ TODO: 4.12 changed code +++
            }
            //--- TODO: 4.12 changed code ---
            
            setCanvasColorWHITE();
            //GUILookAndFeel guilookandfeel = (GUILookAndFeel)lookAndFeel();
            //guilookandfeel.drawBevel(this, x1024(32F), y1024(32F), x1024(528F), y1024(560F), guilookandfeel.bevelComboDown, guilookandfeel.basicelements);
            GUILookAndFeel guilookandfeel = (GUILookAndFeel)lookAndFeel();
            if (bNotMultiPlay)
            {
    	        guilookandfeel.drawBevel(this, x1024(32F), y1024(32F), x1024(528F), y1024(515F), guilookandfeel.bevelComboDown, guilookandfeel.basicelements);           
            }
            else
            {
	            guilookandfeel.drawBevel(this, x1024(32F), y1024(32F), x1024(528F), y1024(560F), guilookandfeel.bevelComboDown, guilookandfeel.basicelements);           
            }                
            setCanvasFont(0);
            setCanvasColor(GColor.Gray);
			//By PAL, labels for Single Missions
			draw(x1024(458F), y1024(5F), x1024(170F), y1024(32F), 0, i18n(MPVer));				
			if (bNotMultiPlay)
			{
				draw(x1024(32F), y1024(602F), x1024(220F), y1024(32F), 0, i18n("Flight")); //By PAL for DGen 
				draw(x1024(244F), y1024(602F), x1024(220F), y1024(32F), 0, i18n("Plane"));   
				draw(x1024(506F), y1024(602F), x1024(220F), y1024(32F), 0, i18n("Num"));
				draw(x1024(597F), y1024(602F), x1024(220F), y1024(32F), 0, i18n("Player / Mission Skin"));				
				draw(x1024(32F), y1024(558F), x1024(220F), y1024(32F), 0, i18n("Weather")); //By PAL for DGen   
				draw(x1024(234F), y1024(558F), x1024(220F), y1024(32F), 0, i18n("Clouds"));				
				draw(x1024(390F), y1024(558F), x1024(170F), y1024(32F), 0, i18n("Time"));
				draw(x1024(597F), y1024(558F), x1024(170F), y1024(32F), 0, i18n("Parachutes On"));				
				draw(x1024(788F), y1024(558F), x1024(220F), y1024(32F), 0, i18n("Arms"));
				draw(x1024(649F), y1024(642F), x1024(220F), y1024(32F), 0, i18n("Reset"));  //By PAL, new								   
				if (bSingleMission)
				{  //Only if it is a single mission
					draw(x1024(850F), y1024(642F), x1024(220F), y1024(32F), 0, i18n("F.M.B."));
					draw(x1024(950F), y1024(642F), x1024(220F), y1024(32F), 0, i18n("Save"));
				}   		
			}              
            clientRender();            
        }

        public void resized()
        {
            super.resized();
            if(renders != null)
            {
                GUILookAndFeel guilookandfeel = (GUILookAndFeel)lookAndFeel();
                GBevel gbevel = guilookandfeel.bevelComboDown;
                //renders.setPosSize(x1024(32F) + gbevel.L.dx, y1024(32F) + gbevel.T.dy, x1024(528F) - gbevel.L.dx - gbevel.R.dx, y1024(560F) - gbevel.T.dy - gbevel.B.dy);
	            if (bNotMultiPlay) //By PAL
	            {
	                renders.setPosSize(x1024(32F) + gbevel.L.dx, y1024(32F) + gbevel.T.dy, x1024(528F) - gbevel.L.dx - gbevel.R.dx, y1024(515F) - gbevel.T.dy - gbevel.B.dy);
	            }
	            else
	            {
	                renders.setPosSize(x1024(32F) + gbevel.L.dx, y1024(32F) + gbevel.T.dy, x1024(528F) - gbevel.L.dx - gbevel.R.dx, y1024(560F) - gbevel.T.dy - gbevel.B.dy);
	            }
            }
        }

        public void setPosSize()
        {
			set1024PosSize(0.0F, 32F, 1024F, 736F);            
            bPrev.setPosC(x1024(85F), y1024(689F));
            bDifficulty.setPosC(x1024(298F), y1024(689F));
            bLoodout.setPosC(x1024(768F), y1024(689F));
			bReset.setPosC(x1024(666F), y1024(689F)); //By PAL
            bNext.setPosC(x1024(512F), y1024(689F));
            //wScrollDescription.setPosSize(x1024(592F), y1024(32F), x1024(400F), y1024(560F));
            //clientSetPosSize();
    		bFMB.setPosC(x1024(868F), y1024(689F));   //By PAL         
	    	bSave.setPosC(x1024(968F), y1024(689F));            
            if (!bNotMultiPlay)
            {	                       	
            	wScrollDescription.setPosSize(x1024(592F), y1024(32F), x1024(400F), y1024(560)); //y1024(560F));
            }
            else
            {
            	wScrollDescription.setPosSize(x1024(592F), y1024(32F), x1024(400F), y1024(515F)); //By PAL       	         
            	sOwnSkinOn.setPosC(x1024(788F), y1024(618F));
            	sParachuteOn.setPosC(x1024(748F), y1024(574F));
             	wSelectFlight.setPos(x1024(73F), y1024(604F));
            	wSelectPlane.setPos(x1024(282F), y1024(604F));             	
            	wSelectNum.setPos(x1024(539F), y1024(604F));
            	wSelectSkin.setPos(x1024(830F), y1024(604)); //559F
            	
	            wWeather.setPosSize(x1024(92F), y1024(561F), x1024(110F), M(1.7F));
	            wCldHeight.setPosSize(x1024(282F), y1024(561F), x1024(90F), M(1.7F));
	            wTimeHour.setPosSize(x1024(425F), y1024(561F), x1024(60F), M(1.7F));
	            wTimeMins.setPosSize(x1024(490F), y1024(561F), x1024(60F), M(1.7F));
            	wSelectWeapons.setPos(x1024(830F), y1024(561F));	                        	                                                     	
            }
            clientSetPosSize();            
        }

        public DialogClient()
        {
        }
    }

    public class ScrollDescript extends GWindowScrollingDialogClient
    {

        public void created()
        {
            fixed = wDescript = createDescript(this);
            fixed.bNotify = true;
            bNotify = true;
        }

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(super.notify(gwindow, i, j))
            {
                return true;
            } else
            {
                notify(i, j);
                return false;
            }
        }

        public void resized()
        {
            if(wDescript != null)
                wDescript.computeSize();
            super.resized();
            if(vScroll.isVisible())
            {
                GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
                vScroll.setPos(win.dx - lookAndFeel().getVScrollBarW() - gbevel.R.dx, gbevel.T.dy);
                vScroll.setSize(lookAndFeel().getVScrollBarW(), win.dy - gbevel.T.dy - gbevel.B.dy);
            }
        }

        public void render()
        {
            setCanvasColorWHITE();
            GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
            lookAndFeel().drawBevel(this, 0.0F, 0.0F, win.dx, win.dy, gbevel, ((GUILookAndFeel)lookAndFeel()).basicelements, true);
        }

        public ScrollDescript()
        {
        }
    }

    public class Descript extends GWindowDialogClient
    {

        public void render()
        {
            String s = textDescription();
            if(s != null)
            {
                GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
                setCanvasFont(0);
                if (bNotMultiPlay) //By PAL, If Color Active
                {
                 	if (OriginalArmy == 1)
                	  setCanvasColor(colorRed); //Red
                	else 
                	  setCanvasColor(colorBlue); //Blue                		
                } 
                else setCanvasColorBLACK(); //If not singlemission                
                //setCanvasColorBLACK();
                root.C.clip.y += gbevel.T.dy;
                root.C.clip.dy -= gbevel.T.dy + gbevel.B.dy;
                drawLines(gbevel.L.dx + 2.0F, gbevel.T.dy + 2.0F, s, 0, s.length(), win.dx - gbevel.L.dx - gbevel.R.dx - 4F, root.C.font.height);
            }
        }

        public void computeSize()
        {
            String s = textDescription();
            if(s != null)
            {
                win.dx = parentWindow.win.dx;
                GBevel gbevel = ((GUILookAndFeel)lookAndFeel()).bevelComboDown;
                setCanvasFont(0);
                int i = computeLines(s, 0, s.length(), win.dx - gbevel.L.dx - gbevel.R.dx - 4F);
                win.dy = root.C.font.height * (float)i + gbevel.T.dy + gbevel.B.dy + 4F;
                if(win.dy > parentWindow.win.dy)
                {
                    win.dx = parentWindow.win.dx - lookAndFeel().getVScrollBarW();
                    int j = computeLines(s, 0, s.length(), win.dx - gbevel.L.dx - gbevel.R.dx - 4F);
                    win.dy = root.C.font.height * (float)j + gbevel.T.dy + gbevel.B.dy + 4F;
                }
            } else
            {
                win.dx = parentWindow.win.dx;
                win.dy = parentWindow.win.dy;
            }
        }

        public Descript()
        {
        }
    }

    public class RenderMap2D extends Render
    {

        public void preRender()
        {
            if(main.land2D == null)
            {
                return;
            } else
            {
                Front.preRender(false);
                return;
            }
        }

        public void render()
        {
            if(main.land2D == null)
                return;
            main.land2D.render();
            if(main.land2DText != null)
                main.land2DText.render();
            drawGrid2D();
            Front.render(false);
            int i = (int)Math.round((32D * (double)renders.root.win.dx) / 1024D);
            
            //+++ TODO: 4.12 changed code +++
            float f = client.root.win.dx / client.root.win.dy;
            float f1 = 1.333333F / f;
            i = (int)((float)i * f1);
            //--- TODO: 4.12 changed code ---
            
            IconDraw.setScrSize(i, i);
            doRenderMap2D();
            SquareLabels.draw(cameraMap2D, Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.worldOfsX(), Main3D.cur3D().land2D.mapSizeX());
        }

        public RenderMap2D(Renders renders1, float f)
        {
            super(renders1, f);
            useClearDepth(false);
            useClearColor(false);
        }
    }
    
/*
    public void leavePop(GameState gamestate)
    {
    	if (bSingleMission) Main.cur().currentMissionFile.saveFile(LastMissionFile); //Save last status of mission file

        _leave();
    }      
    
    public void leavePush(GameState gamestate)
    {
        _leave();
    }*/

    public void enterPush(GameState gamestate)
    {  //By PAL to intercept calls from the single mission
        //if (!client.isActivated()) client.activateWindow();
        //ShowMessage(OriginalFileName, "gamestate Push: " + gamestate.id());       
        //bNotMultiPlay = Main.cur().netServerParams.isSingle();
        bSingleMission = false;
        if(gamestate.id() == 3) //By PAL, coming from Selector Single Mission
        {
			bSingleMission = true;
			OriginalFileName = null; //By PAL, to load it from the beginning        	
        }        
        if(gamestate.id() == 18) //By PAL, special case from FMB Play
        {
			bSingleMission = true;
			OriginalFileName = null; //By PAL, to load it from the beginning        	
        }
  	/*  if(gamestate.id() == 14) //By PAL, special case from FMB Play - from QMB
        {
			bSingleMission = true;
			OriginalFileName = null; //By PAL, to load it from the beginning
			Main.stateStack().pop(); //Return, I will not play from here
			return;        	
        }  */
		selectedFlightName = null; //By PAL, to go to the Player Mission                             
        _enter();        
    }

    public void enter(GameState gamestate)
    {
        //if (!client.isActivated()) client.activateWindow(); //By PAL, I need it to avoid hangs from Campaign and Coop        
        //ShowMessage(OriginalFileName, "gamestate Pop: " + gamestate.id());    	
        if(gamestate.id() == 27) //By PAL, campaign
        {
			bSingleMission = false;
			OriginalFileName = null; //By PAL, to load it from the beginning
			selectedFlightName = null; //By PAL, to go to the Player Mission         	
        }    	
    	super.enter(gamestate);
    }    	

    public void enterPop(GameState gamestate) //By PAL, when I return from FMB, etc.
    {
        //if (!client.isActivated()) client.activateWindow(); //By PAL, I need it to avoid hangs from Campaign and Coop        
        //ShowMessage(OriginalFileName, "gamestate Pop: " + gamestate.id());
        if(gamestate.id() == 18) //From FMB Play
        {
			//bSingleMission = true;
			Main.cur().currentMissionFile = new SectFile(OriginalFileName, 0);  //By PAL, open previous
			OriginalFileName = null; //By PAL, to load it from the beginning
			selectedFlightName = null; //By PAL, to go to the Player Mission 					        	
        	_enter(); 
        }
        if(gamestate.id() == 55) //By PAL, from Arming using Quik screen
        {
        	fromAirArming();
        	if (!bSingleMission) bCampaignChanged = false; //To avoid displaying message "Campaign Changed"
			//if (!client.isActivated()) client.activateWindow(); //By PAL, I need it to avoid hangs from Campaign and Coop              	
//By PAL, very important, here I will keep selectedFlightName
        	_enter();
        }            	
        if (!client.isActivated()) client.activateWindow(); //By PAL, I need it to avoid hangs from Campaign and Coop                   	
        if(gamestate.id() == 54) //By PAL, from Arming using Full Screen
        {
//By PAL, very important, here I will keep selectedFlightName
			try
			{
				SectFile sectfile = Main.cur().currentMissionFile;
				//FlightName = sectfile.get("MAIN", "player", (String)null);	        			
				if(FlightName != null)   //If there is a flight and a plane existent
				{
			    	//PlaneName = sectfile.get(FlightName, "Class", (String)null);
			    	if(PlaneName != null)
			    	{
	            		Class planeClass = ObjIO.classForName(PlaneName);				                		
	            		String LastPlane = Property.stringValue(planeClass, "keyName", (String)null);
	            		
	        			String LastSkin = World.cur().userCfg.getSkin(LastPlane);
	        			String LastNoseArt = World.cur().userCfg.getNoseart(LastPlane);
	        			String LastPilot = World.cur().userCfg.netPilot;
	        			boolean NumberOn = World.cur().userCfg.netNumberOn;	        			
	        			if (LastSkin == null || LastSkin.length() == 0) LastSkin = i18n("neta.Default");
	        			sectfile.set(FlightName, "skin" + wSelectNum.getSelected(), LastSkin);       					
	        			if (LastNoseArt == null || LastNoseArt.length() == 0) LastNoseArt = "";
	        			sectfile.set(FlightName, "noseart" + wSelectNum.getSelected() , LastNoseArt);
	        			if (LastPilot == null || LastPilot.length() == 0) LastPilot = "";
	        			sectfile.set(FlightName, "pilot" + wSelectNum.getSelected() , LastPilot);
        				sectfile.set(FlightName, "numberOn" + wSelectNum.getSelected() , NumberOn? 1 : 0);
        					        								
						String LastWeapon = //No, it doesn't change with this: World.cur().userCfg.getWeapon(LastPlane); //By PAL, from Arming
							sectfile.get(FlightName, "weapons", i18n("neta.Default"));  //plane.name					
		            	wSelectWeapons.setSelected(0, false, false);
			            for(int i = 0; i < wSelectWeapons.size(); i++)
			            {
			                String s1 = Aircraft.getWeaponsRegistered(planeClass)[i];
			                if(!s1.equalsIgnoreCase(LastWeapon))
			                    continue;
			                wSelectWeapons.setSelected(i, true, false);
			                break;
			            }	            
			            wSelectWeapons.setValue((String)wSelectWeapons.list.get(wSelectWeapons.getSelected()));			            	
									
			            wSelectSkin.setSelected(0, false, false);
			            for(int i = 0; i < wSelectSkin.size(); i++)
			            {
			                if(!LastSkin.equalsIgnoreCase((String)wSelectSkin.list.get(i)))
			                    continue;
			                wSelectSkin.setSelected(i, true, false);
			                break;
			            }
			            wSelectSkin.setValue((String)wSelectSkin.list.get(wSelectSkin.getSelected())); //Recently added 	        			
			    	}
				} 											
			}
   			catch(Exception exception)
        	{
	           	System.out.println(exception.getMessage());
	           	exception.printStackTrace();
        	}      		        	
        	//doUpdatePlane(); 			      
        }                                       		    	    	
    }
        	
    public void toAirArming() //By PAL, from QMB to Fill the Quik Arming Screen
    {
        GUIAirArming guiairarming = (GUIAirArming)GameState.get(55);
        try
        {
	        guiairarming.quikListPlane.clear();
	        ArrayList arraylist = null;
	        /*if(bPlaneArrestor)
	            arraylist = indx == 0 ? playerPlaneC : aiPlaneC;
	        else
	            arraylist = indx == 0 ? playerPlane : aiPlane;*/
	        arraylist = aiPlane;
	        for(int j = 0; j < arraylist.size(); j++)
	            guiairarming.quikListPlane.add(((ItemAir)arraylist.get(j)).clazz); 
	            	        	    	
	    	SectFile sectfile = Main.cur().currentMissionFile;  	   	
	        //guiairarming.quikPlayer = FlightName.equalsIgnoreCase(originalFlightName); //false; //true; //to have weapons convergence, etc wSelectNum.getSelected(); //indx == 0;
	        guiairarming.quikPlayer = true; //By PAL, I will always change it for the corresponding one
	        guiairarming.quikArmy = sectfile.get("MAIN", "army", 1, 1, 2);
	        guiairarming.quikPlanes = sectfile.get(FlightName, "Planes", 1, 1, 4); //wSelectNum.size();
	        guiairarming.quikCurPlane = sectfile.get("MAIN", "playerNum", 0, 0, 3); //always become #1
	        try
	        {
	        	ItemAir plane = (ItemAir)aiPlane.get(wSelectPlane.getSelected());	
	    		guiairarming.quikPlane = plane.name;       	
	        } catch(Exception exception) {}
	
	        guiairarming.quikWeapon = sectfile.get(FlightName, "weapons", i18n("neta.Default"));
	        guiairarming.quikCurPlane = 0;//wSelectNum.getSelected();  It changes!!!!!!!!!!!!
	
	   		if(FlightName.length() > 2)
	   		{
				guiairarming.quikRegiment = FlightName.substring(0, FlightName.length() - 2);
				guiairarming.quikWing = Integer.parseInt(FlightName.substring(FlightName.length() - 2)); //sectfile.get("MAIN", "player", (String)null); //By PAL, to remember the First One 
	   		}
	        guiairarming.quikFuel = sectfile.get(FlightName, "Fuel", 100);
	        for(int i = 0; i < 4; i++)
	        {
	            guiairarming.quikSkin[i] = sectfile.get(FlightName, "skin" + i, i18n("neta.Default"));
	            guiairarming.quikNoseart[i] = sectfile.get(FlightName, "noseart" + i, i18n("neta.Default"));
	            guiairarming.quikPilot[i] = sectfile.get(FlightName, "pilot" + i, i18n("neta.Default"));
	            guiairarming.quikNumberOn[i] = (sectfile.get(FlightName, "numberOn" + i, 1, 0, 1) == 1);          
	        }       
        	GUIAirArming.stateId = 4; //Quick Mission, = 2 means Net
	        Main.stateStack().push(55);
	        guiairarming.cPlane.setSelected(wSelectNum.getSelected(), true, false); //OK?
	        //guiairarming.quikCurPlane = wSelectNum.getSelected();	        
		}
		catch(Exception exception)
    	{
           	System.out.println(exception.getMessage());
           	exception.printStackTrace();
    	}
    }
    
    
    public void fromAirArming()  //By PAL, from QMB to Retrieve Data from the Quik Arming Screen
    {
    	if (Mission.isNet()) return; // By SAS~Storebror, skip this step for network sessions.
		GUIAirArming guiairarming = (GUIAirArming)GameState.get(55);
		try
		{
			SectFile sectfile = Main.cur().currentMissionFile;
            int prePlanes = sectfile.get(FlightName, "Planes", 1, 1, 4); //wSelectNum.size();
			int preSkill = sectfile.get(FlightName, "Skill", 1, 0, 3);
//			int preFuel = sectfile.get(FlightName, "Fuel", 100, 0, 100);			    		        	
	    	String NewFlightName = guiairarming.quikRegiment +
	    		(guiairarming.quikWing < 10 ? "0" + guiairarming.quikWing : "" + guiairarming.quikWing);
	    	//ShowMessage("FlightNames", "'"+FlightName+"' : '"+NewFlightName+"'");
    		if (NewFlightName.length() > 2)
	        {
	        /*	int mi = sectfile.sectionIndex("Wing");
	            sectfile.lineRemove(mi, sectfile.varIndex(mi, FlightName)); //Remove Old
	            sectfile.lineAdd(mi, NewFlightName); //Add New
	            //int li = sectfile.varIndex(i, FlightName);
	            //sectfile.line(i, li, NewFlightName);
	            int ifn = sectfile.sectionIndex(FlightName);
	            if (ifn > 1) sectfile.sectionRename(ifn, NewFlightName);
	        	sectfile.set("MAIN", "player", NewFlightName); //By PAL, to remember the First One	            
	            //wSelectFlight.setEditable(true);
	            //wSelectFlight.setValue(NewFlightName + "", true);
	            //wSelectFlight.setEditable(false);*/            
	            
		        int i = sectfile.sectionIndex("Wing");
		        if(prePlanes == 0) //Is this necessary?
		        {
		            sectfile.lineRemove(i, sectfile.varIndex(i, NewFlightName));
		            sectfile.sectionRemove(sectfile.sectionIndex(NewFlightName));
		            sectfile.sectionRemove(sectfile.sectionIndex(NewFlightName + "_Way"));
		        }
		        
	    		if (!NewFlightName.equalsIgnoreCase(FlightName)) //If they are a different one, rename sections
	    		{
		            sectfile.lineRemove(i, sectfile.varIndex(i, FlightName));
		            sectfile.lineAdd(i, NewFlightName);
		            sectfile.sectionRename(sectfile.sectionIndex(FlightName + "_Way"), NewFlightName + "_Way");
		            sectfile.sectionRename(sectfile.sectionIndex(FlightName), NewFlightName);
		            //sectfile.set("MAIN", "player", NewFlightName); //By PAL, if I changed it, the player must be this one	    			
	    		}
	    				        
	            int j = sectfile.sectionIndex(NewFlightName);
	            if (j > 0)
	            {
			        sectfile.sectionClear(j);
			        sectfile.lineAdd(j, "Planes " + prePlanes);
			        sectfile.lineAdd(j, "Skill " + preSkill);
			        //sectfile.lineAdd(j, "Class " + plane.className);
			        sectfile.lineAdd(j, "Fuel " + guiairarming.quikFuel);
			        if(guiairarming.quikWeapon != null)
			            sectfile.lineAdd(j, "weapons " + guiairarming.quikWeapon);
			        else
			            sectfile.lineAdd(j, "weapons default");
			        for(int k = 0; k < prePlanes; k++)
			        {
			            if(guiairarming.quikSkin[k] != null)
			                sectfile.lineAdd(j, "skin" + k + " " + guiairarming.quikSkin[k]);
			            if(guiairarming.quikNoseart[k] != null)
			                sectfile.lineAdd(j, "noseart" + k + " " + guiairarming.quikNoseart[k]);
			            if(guiairarming.quikPilot[k] != null)
			                sectfile.lineAdd(j, "pilot" + k + " " + guiairarming.quikPilot[k]);
			            if(!guiairarming.quikNumberOn[k])
			                sectfile.lineAdd(j, "numberOn" + k + " 0");
			        }
			        
			        ItemAir itemair = null;
			        ArrayList arraylist = null;
			        /*if(bPlaneArrestor)
			            arraylist = indx == 0 ? playerPlaneC : aiPlaneC;
			        else
			            arraylist = indx == 0 ? playerPlane : aiPlane;*/
			    	arraylist = aiPlane;
			        i = 0;
			        do
			        {
			            if(i >= arraylist.size())
			                break;
			            itemair = (ItemAir)arraylist.get(i);
			            if(itemair.name.equals(guiairarming.quikPlane))
			                break;
			            i++;
			        } while(true);
			     	sectfile.lineAdd(j, "Class " + itemair.className); //By PAL, the name should start with air.
			        PlaneName = itemair.className;	        			        	            	
	            }
	            if (originalFlightName.equalsIgnoreCase(FlightName))
	            	originalFlightName = NewFlightName; //By PAL, if I changed the original of the mission, update it.
	            selectedFlightName = NewFlightName;	            	
	            FlightName = NewFlightName;
	           /* if (sectfile.get(FlightName, "OnlyAI", 0, 0, 1) == 1) //If it was OnlyAI I cannot set it as the player one
	            	sectfile.set("MAIN", "player", originalFlightName); //By PAL, if I changed it, the player must be this one
	            else*/
	            sectfile.set("MAIN", "player", FlightName); //By PAL, if I changed it, the player must be this one
	        }
	        sectfile.saveFile(LastMissionFile);			
		}
		catch(Exception exception)
    	{
           	System.out.println(exception.getMessage());
           	exception.printStackTrace();
    	}  	
    }

    public void _enter()
    {
        //client.activateWindow();
        if (!client.isActivated()) client.activateWindow();               
        /*try
        {
            SectFile sectfile = Main.cur().currentMissionFile;
            briefSound = sectfile.get("MAIN", "briefSound");
            String s = Main.cur().currentMissionFile.fileName();
            String s1 = sectfile.get("MAIN", "MAP");
            if(!s.equals(curMissionName) || !s1.equals(curMapName) || curMissionNum != Main.cur().missionCounter || main.land2D == null)
            {
                dialogClient.resized();
                fillTextDescription();
                fillMap();
                Front.loadMission(sectfile);
                curMissionName = s;
                curMapName = s1;
                curMissionNum = Main.cur().missionCounter;
            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        Front.setMarkersChanged();
        wScrollDescription.resized();
        if(wScrollDescription.vScroll.isVisible())
            wScrollDescription.vScroll.setPos(0.0F, true);*/
            
        bNotMultiPlay = !Mission.isNet();
	   	try  //By PAL, Restore values of skin and Markings Switch
   		{
        	IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);  //By PAL, Save last settings
			sOwnSkinOn.bChecked = inifile.get("LastSingleMission", "OwnSkinOn", false);
			sParachuteOn.bChecked = inifile.get("LastSingleMission", "ParachuteOn", true);        	
			/*
			String LastPlaneSkin = inifile.get("LastSingleMission", "LastPlaneSkin", (String)null);
			String LastSkin = inifile.get("LastSingleMission", "LastSkin", (String)null);
			String LastNoseArt = inifile.get("LastSingleMission", "LastNoseArt", (String)null);				
			if (LastPlaneSkin != null && LastPlaneSkin.length() > 0)
			{
				if (LastSkin != null) World.cur().userCfg.setSkin(LastPlaneSkin, LastSkin);
				if (LastNoseArt != null) World.cur().userCfg.setNoseart(LastPlaneSkin, LastNoseArt);
			}
			inifile.deleteValue("LastSingleMission", "LastPlaneSkin");	*/				
			inifile.saveFile();    		    		
   		}
    	catch(Exception exception)
    	{
           	System.out.println(exception.getMessage());
           	exception.printStackTrace();
    	}   		    	

        try //By PAL, to record settings of the original mission
        {       	
            SectFile sectfile = Main.cur().currentMissionFile;
            	
			//if(!OriginalFileName.equalsIgnoreCase(Main.cur().currentMissionFile.fileName())) //By PAL, if it is not the same or it is Campaign
			if(selectedFlightName == null)
            	originalFlightName = sectfile.get("MAIN", "player", (String)null); //By PAL, to remember the First One
            selectedFlightName = null;
                        				                        
            OriginalArmy = sectfile.get("MAIN", "army", 1, 1, 2);
        	if(bNotMultiPlay) OriginalPlaneName = sectfile.get(originalFlightName, "Class", (String)null);
            OriginalPlayerNum = sectfile.get("MAIN", "playerNum", 0, 0, 3);		            	
    		if((originalFlightName != null) && (originalFlightName.length() >2))
    		{
	            String FlightName1 = originalFlightName.substring(0, originalFlightName.length() - 1);
	            String FlightName2 = FlightName1.substring(0, FlightName1.length() - 1);	    			
   				Regiment regiment = (Regiment)Actor.getByName(FlightName2);
        		if(regiment.getArmy() == 1)
		    	{
    				OriginalArmy = 1;
    				sectfile.set("MAIN", "army", 1);
    			} else
    			{
    				OriginalArmy = 2;
    				sectfile.set("MAIN", "army", 2);
    			}            
	        }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    	 	      
    	if(bNotMultiPlay)
      	{	//By PAL, SingleMission or Campaign mode
      		fillArrayPlanes(); //By PAL, required here
        	String FormerFileName = null;
      		sOwnSkinOn.showWindow();
      		sParachuteOn.showWindow();
			wWeather.showWindow();
			wCldHeight.showWindow();
			wTimeHour.showWindow();
			wTimeMins.showWindow();      		    		
            if (bSingleMission)
            {
            	bSave.showWindow();
            	bFMB.showWindow();
            	bReset.showWindow();
            }
            wSelectFlight.showWindow();
            wSelectPlane.showWindow();
            wSelectNum.showWindow(); 
            wSelectWeapons.showWindow();
            wSelectSkin.showWindow();               
                                  	   			    	
	    	if (bSingleMission)
	    	{
				FormerFileName = OriginalFileName;	    		
				OriginalFileName = Main.cur().currentMissionFile.fileName(); //By PAL, set the OriginalFileName	
				if(!OriginalFileName.equalsIgnoreCase(FormerFileName)) //By PAL, if it is not the same or it is Campaign
		      	{
			        try
			        {            
		      			Main.cur().currentMissionFile.saveFile(LastMissionFile); //By PAL, new mission file in Missions\Single
		      			Main.cur().currentMissionFile = new SectFile(LastMissionFile, 0); //By PAL, to have the chance of saving, 0 if not
		      			Main.cur().currentMissionFile.setFileName(OriginalFileName);  //By PAL, but keep original name
			        }
			        catch(Exception exception)
			        {
			            System.out.println(exception.getMessage());
			            exception.printStackTrace();
			        }		                        	                     	        	
		      	}
		      	else  //By PAL, load previous
		      	{
		      		try
		      		{
		      			Main.cur().currentMissionFile = new SectFile(LastMissionFile, 0); //By PAL, to have the chance of saving, 0 if not
		      			Main.cur().currentMissionFile.setFileName(OriginalFileName);  //By PAL, but keep original name
		      		}
			        catch(Exception exception)
			        {
			            System.out.println(exception.getMessage());
			            exception.printStackTrace();
			        }		      		
		      	}
		        try
		        {
		        		        
		            Class planeClass = ObjIO.classForName(OriginalPlaneName);		        
		        	DescriptString = HeaderDescript + originalFlightName + " ***\n*** with plane " +
		        		Property.stringValue(planeClass, "keyName", null) + " of the " + (OriginalArmy == 1 ? "Red army ***" : "Blue army ***");                   			        
		        }
		        catch(Exception exception)
		        {
		            System.out.println(exception.getMessage());
		            exception.printStackTrace();
		        } 		      			      			      	
	    	}
	    	else	//By PAL, campaign
	    	{
	    		FormerFileName = OriginalFileName; //By PAL, Necessary? I Think so		    			    		
				OriginalFileName = Main.cur().currentMissionFile.fileName(); //By PAL, set the OriginalFileName	    		
	    		if (curMissionNum != Main.cur().missionCounter || !OriginalFileName.equalsIgnoreCase(FormerFileName)) //By PAL, check if I'm returning
		    	{
			        try
			        {            
		      			Main.cur().currentMissionFile.saveFile(LastMissionFile); //By PAL, new mission file in Missions\Single
		      			Main.cur().currentMissionFile = new SectFile(LastMissionFile, 0); //By PAL, to have the chance of saving, 0 if not
		      			Main.cur().currentMissionFile.setFileName(OriginalFileName);  //By PAL, but keep original name
			        }
			        catch(Exception exception)
			        {
			            System.out.println(exception.getMessage());
			            exception.printStackTrace();
			        }		                        	                     	        	
		      	}
		      	else //By PAL, if I'm returning, me aseguro de volver a la que ya había grabado cambiada
		      	{
		      		try
		      		{
		      			Main.cur().currentMissionFile = new SectFile(LastMissionFile, 0); //By PAL, to have the chance of saving, 0 if not
		      			Main.cur().currentMissionFile.setFileName(OriginalFileName);  //By PAL, but keep original name
		      		}
			        catch(Exception exception)
			        {
			            System.out.println(exception.getMessage());
			            exception.printStackTrace();
			        }
		      	}
		      	if (originalFlightName.equalsIgnoreCase(Main.cur().campaign.originalFlightName()) || !bCampaignChanged)// || (OriginalPlayerNum != Campaign.originalPlayerNum))
		      	{
			        try
			        {		        		        
			            Class planeClass = ObjIO.classForName(OriginalPlaneName);		        
		            	DescriptString = "*** Original Campaign brief for Flight " + originalFlightName +
		            		" ***\n*** with plane " + Property.stringValue(planeClass, "keyName", null) + " of the " +
		            		(OriginalArmy == 1 ? "Red army ***" : "Blue army ***") +
		            		"\n\n --- You will allways fly in this Flight even if you change the plane for it ---";                   		                   			        
			        }
			        catch(Exception exception)
			        {
			            System.out.println(exception.getMessage());
			            exception.printStackTrace();
			        }		      		
		      	}
		      	else
		      	{ //By PAL, if Campaign modified something!!!
			        try
			        {	        		        
                		ResourceBundle resourcebundle = ResourceBundle.getBundle("missions/campaign/" + Main.cur().campaign.branch() + "/" + "rank", RTSConf.cur.locale);
			            Class oriplaneClass = ObjIO.classForName(Main.cur().campaign.originalPlaneName());//OriginalPlaneName);
			            Class planeClass = ObjIO.classForName(OriginalPlaneName);		        
		            	DescriptString = "*** This mission was modified by IL-2 Campaign Manager! ***"
		            		+ "\n*** It was originally for the Flight " + Main.cur().campaign.originalFlightName() +
		            		" with plane " + Property.stringValue(oriplaneClass, "keyName", null) + " of the " +
		            		(OriginalArmy == 1 ? "Red army ***" : "Blue army ***") +
		            		"\n\n Because your rank of '" + resourcebundle.getString("" + Main.cur().campaign.originalRank()) +
		            		"' was not enough for the mission, you will be flying the " + originalFlightName +
		            		" Flight with plane " +	Property.stringValue(planeClass, "keyName", null) +
		            		"\n\n --- You will allways fly in this Flight even if you change the plane for it ---";
		            	ShowMessage("Warning", "*** This mission was modified by IL-2 Campaign Manager! ***" +
		            		"\nThe rank of '" + resourcebundle.getString("" + Main.cur().campaign.originalRank()) +
      			 			"' you have is not enough to fly the " + Property.stringValue(oriplaneClass, "keyName", null) +
      			 			" in the position " + (Main.cur().campaign.originalPlayerNum() + 1) + " of the Flight " +
      			 			Main.cur().campaign.originalFlightName());
			        }
			        catch(Exception exception)
			        {
			            System.out.println(exception.getMessage());
			            exception.printStackTrace();
			        }		      		
		      	} 		    		
	    	}
	    	bCampaignChanged = true;
	    		    			      	
			//Main.cur().currentMissionFile = new SectFile(LastMissionFile, 0); //By PAL, to have the chance of saving, 0 if not
		    		    	        
	        try
	        {     	       	
	        	SectFile sectfile = Main.cur().currentMissionFile;						                    	
	            briefSound = sectfile.get("MAIN", "briefSound");
	            String s = Main.cur().currentMissionFile.fileName();	            
	            String s1 = sectfile.get("MAIN", "MAP");            
	          	fillComboPlane(wSelectPlane, false); //bSingleMission); // True = Player, False = AI, all
	            fillSelectFlight(); //By PAL fill the available flights
	            // By PAL, Mission Properties
	        	int v = sectfile.get("MAIN", "CloudType", 0);
	        	if (v >= 0 && v < 8) wWeather.setSelected(v, true, false);
	        	  else wWeather.setSelected(0, true, false);
				v = (int)sectfile.get("MAIN", "CloudHeight", 2000.0F);
				String sv = v + "m";
				if (wCldHeight.list.indexOf(sv) < 0)
				{
					sv = v + "m";
					wCldHeight.add(0, sv);  //By PAL, add current cloud height in first term and make it selected
					wCldHeight.setSelected(wCldHeight.list.size() - 1, true, false);	//By PAL, I set the last one I added				
				} else
				{
					wCldHeight.setSelected(wCldHeight.list.indexOf(sv), true, false);					
				}
				float t = sectfile.get("MAIN", "TIME", 12.0F, 0.0F, 23.99F);
				int h = (int)t;
				sv = (h < 10 ? "0" : "") + h + "h";			
				v = wTimeHour.list.indexOf(sv);
				if (v < 0) v = 12;  
				wTimeHour.setSelected(v, true, false);
				int m = (int)((t % 1) * 60);	//By PAL, minutes = fraction of 60 minutes							
				sv = (m < 10 ? "0" : "") + m + "m"; //By PAL, < 10 min
				v = wTimeMins.list.indexOf(sv);
				if (v < 0) v = 0;
				wTimeMins.setSelected(v, true, false);
				
	            //SectFile sectfile = Main.cur().currentMissionFile;
	            //briefSound = sectfile.get("MAIN", "briefSound");
	            //String s = Main.cur().currentMissionFile.fileName();
	            //String s1 = sectfile.get("MAIN", "MAP");
            	if(!OriginalFileName.equalsIgnoreCase(FormerFileName) || !s1.equalsIgnoreCase(curMapName) ||
            		curMissionNum != Main.cur().missionCounter || main.land2D == null)
            	{
	                dialogClient.resized();
	                fillTextDescription();
	                fillMap();
	                Front.loadMission(sectfile);
	                curMissionName = s;
	                curMapName = s1;
	                curMissionNum = Main.cur().missionCounter;         					
            	}           	           	        
	        }
	        catch(Exception exception)
	        {
	            System.out.println(exception.getMessage());
	            exception.printStackTrace();
	        }	               	                       	
      	}
        else
        {	//By PAL, NetMission, MultiPlay       	
			sOwnSkinOn.hideWindow();
			sParachuteOn.hideWindow();
			wWeather.hideWindow();
			wCldHeight.hideWindow();
			wTimeHour.hideWindow();
			wTimeMins.hideWindow();
           	wSelectFlight.hideWindow();
           	wSelectPlane.hideWindow();
           	wSelectNum.hideWindow();
           	wSelectWeapons.hideWindow();           	     	
           	wSelectSkin.hideWindow();
           	bSave.hideWindow();
           	bFMB.hideWindow();
           	bReset.hideWindow();         
	        try
	        {
	            SectFile sectfile = Main.cur().currentMissionFile;
	            briefSound = sectfile.get("MAIN", "briefSound");
	            String s = Main.cur().currentMissionFile.fileName();
	            String s1 = sectfile.get("MAIN", "MAP");
	            if(!s.equals(curMissionName) || !s1.equals(curMapName) || curMissionNum != Main.cur().missionCounter || main.land2D == null)
	            {
	                dialogClient.resized();
	                fillTextDescription();
	                fillMap();
	                Front.loadMission(sectfile);
	                curMissionName = s;
	                curMapName = s1;
	                curMissionNum = Main.cur().missionCounter;
	            }
	        }
	        catch(Exception exception)
	        {
	            System.out.println(exception.getMessage());
	            exception.printStackTrace();
	        }                        	     	
        }                
        Front.setMarkersChanged();
        wScrollDescription.resized();
        if(wScrollDescription.vScroll.isVisible())
            wScrollDescription.vScroll.setPos(0.0F, true); 
            
    }

    public void _leave()
    {
 		try  //By PAL, Save status of sOwnSkinOn switch, etc
   		{				   			
        	IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);  //By PAL, Save last settings
        	if (inifile.get("LastSingleMission", "OwnSkinOn", false) != sOwnSkinOn.bChecked)
        		inifile.set("LastSingleMission", "OwnSkinOn", sOwnSkinOn.bChecked? true : false);
        	if (inifile.get("LastSingleMission", "ParachuteOn", true) != sParachuteOn.bChecked)
        		inifile.set("LastSingleMission", "ParachuteOn", sParachuteOn.bChecked? true : false);        		  		
	    	inifile.saveFile();  		
   		}
    	catch(Exception exception)
    	{
            System.out.println(exception.getMessage());
            exception.printStackTrace();
    	}     	
        client.hideWindow();
    }

    private void setPosCamera(float f, float f1)
    {
        float f2 = (float)((double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale);
        cameraMap2D.worldXOffset = f - f2 / 2.0F;
        float f3 = (float)((double)(cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale);
        cameraMap2D.worldYOffset = f1 - f3 / 2.0F;
        clipCamera();
    }

    private void scaleCamera()
    {
        cameraMap2D.worldScale = (scale[curScale] * renders.root.win.dx) / 1024F;
    }

    private void clipCamera()
    {
        if(cameraMap2D.worldXOffset < -Main3D.cur3D().land2D.worldOfsX())
            cameraMap2D.worldXOffset = -Main3D.cur3D().land2D.worldOfsX();
        float f = (float)((double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale);
        if(cameraMap2D.worldXOffset > Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - (double)f)
            cameraMap2D.worldXOffset = Main3D.cur3D().land2D.mapSizeX() - Main3D.cur3D().land2D.worldOfsX() - (double)f;
        if(cameraMap2D.worldYOffset < -Main3D.cur3D().land2D.worldOfsY())
            cameraMap2D.worldYOffset = -Main3D.cur3D().land2D.worldOfsY();
        float f1 = (float)((double)(cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale);
        if(cameraMap2D.worldYOffset > Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - (double)f1)
            cameraMap2D.worldYOffset = Main3D.cur3D().land2D.mapSizeY() - Main3D.cur3D().land2D.worldOfsY() - (double)f1;
    }

    private void computeScales()
    {
        float f = (renders.win.dx * 1024F) / renders.root.win.dx;
        float f1 = (renders.win.dy * 768F) / renders.root.win.dy;
        int i = 0;
        float f2 = 0.064F;
        do
        {
            if(i >= scale.length)
                break;
            scale[i] = f2;
            float f3 = landDX * f2;
            if(f3 < f)
                break;
            float f5 = landDY * f2;
            if(f5 < f1)
                break;
            f2 /= 2.0F;
            i++;
        } while(true);
        scales = i;
        if(scales < scale.length)
        {
            float f4 = f / landDX;
            float f6 = f1 / landDY;
            scale[i] = f4;
            if(f6 > f4)
                scale[i] = f6;
            scales = i + 1;
        }
        curScale = scales - 1;
        curScaleDirect = -1;
    }

    private void drawGrid2D()
    {
        int i = gridStep();
        int j = (int)((cameraMap2D.worldXOffset + Main3D.cur3D().land2D.worldOfsX()) / (double)i);
        int k = (int)((cameraMap2D.worldYOffset + Main3D.cur3D().land2D.worldOfsY()) / (double)i);
        double d = (double)(cameraMap2D.right - cameraMap2D.left) / cameraMap2D.worldScale;
        double d1 = (double)(cameraMap2D.top - cameraMap2D.bottom) / cameraMap2D.worldScale;
        int l = (int)(d / (double)i) + 2;
        int i1 = (int)(d1 / (double)i) + 2;
        float f = (float)(((double)(j * i) - cameraMap2D.worldXOffset - Main3D.cur3D().land2D.worldOfsX()) * cameraMap2D.worldScale + 0.5D);
        float f1 = (float)(((double)(k * i) - cameraMap2D.worldYOffset - Main3D.cur3D().land2D.worldOfsY()) * cameraMap2D.worldScale + 0.5D);
        float f2 = (float)((double)(l * i) * cameraMap2D.worldScale);
        float f3 = (float)((double)(i1 * i) * cameraMap2D.worldScale);
        float f4 = (float)((double)i * cameraMap2D.worldScale);
        _gridCount = 0;
        Render.drawBeginLines(-1);
        for(int j1 = 0; j1 <= i1; j1++)
        {
            float f5 = f1 + (float)j1 * f4;
            char c = (j1 + k) % 10 != 0 ? '\177' : '\300';
            line2XYZ[0] = f;
            line2XYZ[1] = f5;
            line2XYZ[2] = 0.0F;
            line2XYZ[3] = f + f2;
            line2XYZ[4] = f5;
            line2XYZ[5] = 0.0F;
            Render.drawLines(line2XYZ, 2, 1.0F, 0xff000000 | c << 16 | c << 8 | c, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
            if(c == '\300')
                drawGridText(0, (int)f5, (k + j1) * i);
        }

        for(int k1 = 0; k1 <= l; k1++)
        {
            float f6 = f + (float)k1 * f4;
            char c1 = (k1 + j) % 10 != 0 ? '\177' : '\300';
            line2XYZ[0] = f6;
            line2XYZ[1] = f1;
            line2XYZ[2] = 0.0F;
            line2XYZ[3] = f6;
            line2XYZ[4] = f1 + f3;
            line2XYZ[5] = 0.0F;
            Render.drawLines(line2XYZ, 2, 1.0F, 0xff000000 | c1 << 16 | c1 << 8 | c1, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 0);
            if(c1 == '\300')
                drawGridText((int)f6, 0, (j + k1) * i);
        }

        Render.drawEnd();
        drawGridText();
    }

    private int gridStep()
    {
        float f = cameraMap2D.right - cameraMap2D.left;
        float f1 = cameraMap2D.top - cameraMap2D.bottom;
        double d = f;
        if(f1 < f)
            d = f1;
        d /= cameraMap2D.worldScale;
        int i = 0x186a0;
        for(int j = 0; j < 5 && (double)(i * 3) > d; j++)
            i /= 10;

        return i;
    }

    private void drawGridText(int i, int j, int k)
    {
        if(i < 0 || j < 0 || k <= 0 || _gridCount == 20)
        {
            return;
        } else
        {
            _gridX[_gridCount] = i;
            _gridY[_gridCount] = j;
            _gridVal[_gridCount] = k;
            _gridCount++;
            return;
        }
    }

    private void drawGridText()
    {
        for(int i = 0; i < _gridCount; i++)
            gridFont.output(0xffc0c0c0, _gridX[i] + 2, _gridY[i] + 2, 0.0F, _gridVal[i] / 1000 + "." + (_gridVal[i] % 1000) / 100);

        _gridCount = 0;
    }

    protected void doRenderMap2D()
    {
    }

    protected void doMouseButton(int i, boolean flag, float f, float f1)
    {
        if(i == 2) bMPressed = flag;
        if(i == 1) bRPressed = flag;
        if(i == 0)
        {
            bLPressed = flag;
            renders.mouseCursor = bLPressed ? 7 : 3;
        }
    }

 /*   protected void doMouseButton(int i, boolean flag, float f, float f1)
    {
        renders;
        if(i != 0)
            break MISSING_BLOCK_LABEL_48;
        bLPressed = flag;
        renders;
        if(!bLPressed) goto _L2; else goto _L1
_L1:
        renders;
        7;
          goto _L3
_L2:
        renders;
        3;
_L3:
        mouseCursor;
        break MISSING_BLOCK_LABEL_280;
        renders;
        if(i == 1 && scales > 1)
        {
            bRPressed = flag;
            if(bRPressed && !bLPressed)
            {
                float f2 = (float)(cameraMap2D.worldXOffset + (double)f / cameraMap2D.worldScale);
                float f3 = (float)(cameraMap2D.worldYOffset + (double)(renders.win.dy - f1 - 1.0F) / cameraMap2D.worldScale);
                curScale += curScaleDirect;
                if(curScaleDirect < 0)
                {
                    if(curScale < 0)
                    {
                        curScale = 1;
                        curScaleDirect = 1;
                    }
                } else
                if(curScale == scales)
                {
                    curScale = scales - 2;
                    curScaleDirect = -1;
                }
                scaleCamera();
                f2 = (float)((double)f2 - (double)(f - renders.win.dx / 2.0F) / cameraMap2D.worldScale);
                f3 = (float)((double)f3 + (double)(f1 - renders.win.dy / 2.0F) / cameraMap2D.worldScale);
                setPosCamera(f2, f3);
            }
        }
    }*/

    protected void doMouseMove(float f, float f1)
    {
        if(bLPressed && renders.mouseCursor == 7)
        {
            cameraMap2D.worldXOffset -= (double)renders.root.mouseStep.dx / cameraMap2D.worldScale;
            cameraMap2D.worldYOffset += (double)renders.root.mouseStep.dy / cameraMap2D.worldScale;
            clipCamera();
        }
    }
    
    protected void doMouseWheel(float f, float f1, float v)  //By PAL
    {
    	    //bRPressed = flag;
            //if(bRPressed && !bLPressed)
            {
            	f -= ((GWindow) (renders)).win.x;
            	f1 -= ((GWindow) (renders)).win.y;         	
            	if (f < 0) f = ((GWindow) (renders)).win.x;
            	if (f1 < 0) f1 = ((GWindow) (renders)).win.y;             	            	
                float f2 = (float)(cameraMap2D.worldXOffset + (double)f / cameraMap2D.worldScale);
                float f3 = (float)(cameraMap2D.worldYOffset + (double)(((GWindow) (renders)).win.dy - f1 - 1.0F) / cameraMap2D.worldScale);
                /*curScale += curScaleDirect;
                if(curScaleDirect < 0)
                {
                    if(curScale < 0)
                    {
                        curScale = 1;
                        curScaleDirect = 1;
                    }
                } else
                if(curScale == scales)
                {
                    curScale = scales - 2;
                    curScaleDirect = -1;
                }*/
                if (bPALZoomInUp) v = -v;
  		        if(v > 0.0F)
        	    {
            	    if(curScale < scales - 1)
                    curScale++;
            	}
            	if (v < 0.0F)
            	{
                	if(curScale > 0)
                    curScale--;
            	}                
                scaleCamera();
                f2 = (float)((double)f2 - (double)(f - ((GWindow) (renders)).win.dx / 2.0F) / cameraMap2D.worldScale);
                f3 = (float)((double)f3 + (double)(f1 - ((GWindow) (renders)).win.dy / 2.0F) / cameraMap2D.worldScale);
                setPosCamera(f2, f3);
            }
    }   

//By PAL, from 4.111: (they updated my version)
    protected void doMouseRelMove(float f, float f1, float f2)
    {
        if((double)f2 < 0.001D && (double)f2 > -0.001D)
            return;
        if((double)f2 < 0.0D)
            curScaleDirect = 1;
        if((double)f2 > 0.0D)
            curScaleDirect = -1;
        float f3 = renders.root.mousePos.x - renders.win.x - renders.parentWindow.win.x;
        float f4 = renders.root.mousePos.y - renders.win.y - renders.parentWindow.win.y;
        float f5 = (float)(cameraMap2D.worldXOffset + (double)f3 / cameraMap2D.worldScale);
        float f6 = (float)(cameraMap2D.worldYOffset + (double)(renders.win.dy - f4 - 1.0F) / cameraMap2D.worldScale);
        curScale += curScaleDirect;
        if(curScaleDirect < 0)
        {
            if(curScale < 0)
                curScale = 0;
        } else
        if(curScale == scales)
            curScale = scales - 1;
        scaleCamera();
        f5 = (float)((double)f5 - (double)(f3 - renders.win.dx / 2.0F) / cameraMap2D.worldScale);
        f6 = (float)((double)f6 + (double)(f4 - renders.win.dy / 2.0F) / cameraMap2D.worldScale);
        setPosCamera(f5, f6);
    }

    protected void createRenderWindow(GWindow gwindow)
    {
        renders = new GUIRenders(gwindow, 0.0F, 0.0F, 1.0F, 1.0F, false) {

            public void mouseButton(int i, boolean flag, float f, float f1)
            {
                doMouseButton(i, flag, f, f1);
            }

            public void mouseMove(float f, float f1)
            {
                doMouseMove(f, f1);
            }
            
            //by PAL, 4.111 from TD:
            public void mouseRelMove(float f, float f1, float f2)
            {
                doMouseRelMove(f, f1, f2);
            }           
/*            
            // By PAL, mouse Wheel
        	public void mouseRelMove(float f, float f1, float f2)
        	{
            //super.mouseRelMove(f, f1, f2);
            	//if(f2 != 0.0F && isKeyFocus())
            	if (f2 != 0.0F)
            	{
            		GPoint MPos = getMouseXY();
            		doMouseWheel(MPos.x, MPos.y, f2);
                //doKey(530, true);
                //doKey(530, false, true, f2 < 0.0F);
            	}
        	}  */
        }
;
        renders.mouseCursor = 3;
        renders.bNotify = true;
        cameraMap2D = new CameraOrtho2D();
        cameraMap2D.worldScale = scale[curScale];
        renderMap2D = new RenderMap2D(renders.renders, 1.0F);
        renderMap2D.setCamera(cameraMap2D);
        renderMap2D.setShow(true);
        LightEnvXY lightenvxy = new LightEnvXY();
        renderMap2D.setLightEnv(lightenvxy);
        lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
        Vector3f vector3f = new Vector3f(1.0F, -2F, -1F);
        vector3f.normalize();
        lightenvxy.sun().set(vector3f);
        gridFont = TTFont.font[1];

        //+++ TODO: 4.12 changed code +++
        if(World.cur().smallMapWPLabels)
        {
            waypointFont = TTFont.font[0];
            bigFontMultip = 1.0F;
        } else
        {
            waypointFont = TTFont.font[1];
            bigFontMultip = 2.0F;
        }
        //--- TODO: 4.12 changed code ---
        
        emptyMat = Mat.New("icons/empty.mat");
        main = Main3D.cur3D();
    }

/////////// By PAL, Special Routines //////////

    protected void doUpdateMissionParams()
    {
        try
        {   
            SectFile sectfile = Main.cur().currentMissionFile;    
            sectfile.set("MAIN", "CloudType", wWeather.getSelected());
            String s = wCldHeight.getValue();
            if(s.charAt(s.length() - 1) == 'm')
            	s = s.substring(0, s.length() - 1);					            
			sectfile.set("MAIN", "CloudHeight", s + ".0"); //By PAL, float for CloudHeight
			//sectfile.set("MAIN", "TIME", wTimeHour.getSelected() + "." + wTimeMins.getSelected() * 25);
			int m = (int)(wTimeMins.getSelected() / 0.6);
			s = (m < 10 ? "0" : "") + m;
			sectfile.set("MAIN", "TIME", wTimeHour.getSelected() + "." + s); //By PAL, fractions of 60
        }
        catch(Exception exception)
        {
           	System.out.println(exception.getMessage());
           	exception.printStackTrace();
        }
    }			    
    
    protected void doUpdateFlight()
    {
        try
        {   
            SectFile sectfile = Main.cur().currentMissionFile;
            int playerNum = sectfile.get("MAIN", "playerNum", 0, 0, 3);  //Select the Plane of the Player
            FlightName = originalFlightName;
            int j = sectfile.sectionIndex("Wing");            
			if(j >= 0)   //if Exists
			{
	           	if(wSelectFlight.getSelected() > -1)
	           		FlightName = sectfile.var(j, wSelectFlight.getSelected());      //By PAL, update FlightName from Combo    
				sectfile.set("MAIN", "player", FlightName);			
			}        
			PlaneName = sectfile.get(FlightName, "Class", (String)null);
			int NoPlanes = sectfile.get(FlightName, "Planes", 1, 1, 4);  //Theorically not less than 1
			wSelectNum.clear();
			for(int l = 0; l < NoPlanes; l++)
        		wSelectNum.add(Integer.toString(l + 1)); //By PAL, fill wSelectNum from 1 to 4
        	if (playerNum >= 0 && playerNum < NoPlanes)
        	{
        		wSelectNum.setSelected(playerNum, true, false);  //Select the Plane of the Player to be 0 in first term
        	}
        	else
        	{
				wSelectNum.setSelected(-1, false, false);  //Select the Plane of the Player to be the last in first term
        	}
        	doUpdateNum(); //Added, necessary
       		wSelectNum.setEditable(false);        	       	
       		if((FlightName != null) && (FlightName.length() >2))
       		{
				String FlightName1 = FlightName.substring(0, FlightName.length() - 1);
	            String FlightName2 = FlightName1.substring(0, FlightName1.length() - 1);	    			
   				Regiment regiment = (Regiment)Actor.getByName(FlightName2);
            	if(regiment.getArmy() == 1)
    	    	{
        			wSelectFlight.setEditTextColor(colorRed);
        			wSelectPlane.setEditTextColor(colorRed);	
        			wSelectNum.setEditTextColor(colorRed);
        			wSelectWeapons.setEditTextColor(colorRed);
        			wSelectSkin.setEditTextColor(colorRed);
        			sectfile.set("MAIN", "army", 1);
        		} else
        		{
        			wSelectFlight.setEditTextColor(colorBlue);
        			wSelectPlane.setEditTextColor(colorBlue);
        			wSelectNum.setEditTextColor(colorBlue);
        			wSelectWeapons.setEditTextColor(colorBlue);
        			wSelectSkin.setEditTextColor(colorBlue);
        			sectfile.set("MAIN", "army", 2);
        		}
       		}
       		 		      
	        wSelectPlane.setSelected(0, false, false); //By PAL, originally it said -1
	       	ItemAir plane; 	        
	    	for(int k = 0; k < aiPlane.size(); k++)
	    	{
	        	plane = (ItemAir)aiPlane.get(k);
	        	if (!plane.className.equalsIgnoreCase(PlaneName))
	        		continue;
	        	wSelectPlane.setSelected(k, true, false);
	            break;
	    	}
	    /*	if(wSelectPlane.getSelected() == -1)
	    	{
				ShowMessage("Warning", "The plane " + PlaneName + " doesn't exist in your IL-2 game. The " +
      			 (String)wSelectPlane.list.get(0) + " will be used instead of the original.");
	    		wSelectPlane.setSelected(0, true, false); //By PAL, if the plane didn't exist
	    	}*/	    		
			doUpdatePlane();
			{
                dialogClient.resized();
                fillMap();
			}						          
        }
        catch(Exception exception)
        {
           	System.out.println(exception.getMessage());
           	exception.printStackTrace();
        }
    }
    
    protected void doUpdatePlane()
    {
        try
        {      	  	
            SectFile sectfile = Main.cur().currentMissionFile;             	
        	//int Aux = sectfile.sectionIndex("Wing");     
//FlightName = sectfile.get("MAIN", "player", (String)null);            
        	if(FlightName != null && wSelectPlane.getSelected() > -1)   //If there is a flight and a plane existent
        	{
            	ItemAir plane = (ItemAir)aiPlane.get(wSelectPlane.getSelected());
        		sectfile.set(FlightName, "Class", plane.className); //By PAL, the name should start with air.
        		PlaneName = plane.className; 
				String LastWeapon = sectfile.get(FlightName, "weapons", i18n("neta.Default"));        		
        		   			
                boolean HasCockpit = (Property.value(plane.clazz, "cockpitClass") != null);
                boolean OnlyAI = (sectfile.get(FlightName, "OnlyAI", 0, 0, 1) == 1);
                boolean CampaignOther = !bSingleMission && !FlightName.equalsIgnoreCase(originalFlightName); 	   			
		
				if (!HasCockpit || OnlyAI || CampaignOther)
				{
					wSelectPlane.setEditTextColor(0xc0202020); // By PAL, Grayed
        			wSelectNum.setEnable(false);
				}
				else
				{
					String FlightName1 = FlightName.substring(0, FlightName.length() - 1);
		            String FlightName2 = FlightName1.substring(0, FlightName1.length() - 1);	    			
	   				Regiment regiment = (Regiment)Actor.getByName(FlightName2);
            		if(regiment.getArmy() == 1)
    		    	{
        				wSelectPlane.setEditTextColor(colorRed);
        			} else
        			{
        				wSelectPlane.setEditTextColor(colorBlue);
        			}
        			wSelectNum.setEnable(true);					
				}

        		wSelectWeapons.setEnable(true);				
				wSelectSkin.setEnable(true);			
				fillComboWeapon(wSelectWeapons, plane, 0);
				fillComboSkin(wSelectSkin, plane);			
				if (bSingleMission)
 				{
					if (FlightName.equalsIgnoreCase(originalFlightName))  //By PAL, because in Campaign I will not fly other flight 
					{
						wSelectFlight.list.set(wSelectFlight.getSelected(), ("> " + FlightName + " (" + plane.name +")"));  //By PAL, Regiment + (Name of the plane)
						wSelectFlight.setValue("> " + FlightName + " (" + plane.name +")");//By PAL, to update visual on combobox						
					}
					else
					{
						wSelectFlight.list.set(wSelectFlight.getSelected(), (FlightName + " (" + plane.name +")"));  //By PAL, Regiment + (Name of the plane)
						wSelectFlight.setValue(FlightName + " (" + plane.name +")");//By PAL, to update visual on combobox						
					} 					
				}
				else
				{
					if (FlightName.equalsIgnoreCase(originalFlightName))  //By PAL, because in Campaign I will not fly other flight 
					{
						wSelectFlight.list.set(wSelectFlight.getSelected(), ("Your Flight: " + FlightName + " (" + plane.name +")"));  //By PAL, Regiment + (Name of the plane) 
						wSelectFlight.setValue(("Your Flight: " + FlightName + " (" + plane.name +")"));						
					}
					else
					{
						wSelectFlight.list.set(wSelectFlight.getSelected(), (FlightName + " (" + plane.name +")"));  //By PAL, Regiment + (Name of the plane)
						wSelectFlight.setValue(FlightName + " (" + plane.name +")");//By PAL, to update visual on combobox
					}
				}				
				LastWeapon = sectfile.get(FlightName, "weapons", i18n("neta.Default"));  //plane.name
				
            	wSelectWeapons.setSelected(0, false, false);
	            for(int i = 0; i < wSelectWeapons.size(); i++)
	            {
	                String s1 = //(String)wSelectWeapons.list.get(i);
	                			Aircraft.getWeaponsRegistered(plane.clazz)[i];
	                if(!s1.equalsIgnoreCase(LastWeapon))
	                    continue;
	                wSelectWeapons.setSelected(i, true, false);
	                break;
	            }	            
	            wSelectWeapons.setValue((String)wSelectWeapons.list.get(wSelectWeapons.getSelected()));
	            doUpdateWeapons();

	            String CurrentSkin = sectfile.get(FlightName, "skin" + wSelectNum.getSelected(), (String)null);
	            /*if (CurrentSkin == null)
					for (int i = 0; i < wSelectNum.size(); i++)
					{
						CurrentSkin = sectfile.get(FlightName, "skin" + i, (String)null);
						if (CurrentSkin != null) break; 		
					}*/ //Why to take the one of other flight?	            	
				if (CurrentSkin == null) CurrentSkin = i18n("neta.Default");			
	            wSelectSkin.setSelected(0, false, false);
	            for(int i = 0; i < wSelectSkin.size(); i++)
	            {
	                if(!CurrentSkin.equalsIgnoreCase((String)wSelectSkin.list.get(i)))
	                    continue;
	                wSelectSkin.setSelected(i, true, false);
	                break;
	            }
	            wSelectSkin.setValue((String)wSelectSkin.list.get(wSelectSkin.getSelected())); //Recently added
	            sectfile.set(FlightName, "skin" + wSelectNum.getSelected(), wSelectSkin.getValue()); //By PAL, to save the one
	            //doUpdateSkin(); //To have all uniform	            	            	            	            
        	}
        	else
        	{
        		wSelectNum.setEnable(false);
        		wSelectSkin.setEnable(false);
        		wSelectWeapons.setEnable(false);
        	}
        }
        catch(Exception exception)
        {
           	System.out.println(exception.getMessage());
           	exception.printStackTrace();
        }          	
    }
    
    protected void doUpdateNum()
    {
        try
        {   
           	SectFile sectfile = Main.cur().currentMissionFile;
			sectfile.set("MAIN", "playerNum", wSelectNum.getSelected());
        }
        catch(Exception exception)
        {
           	System.out.println(exception.getMessage());
           	exception.printStackTrace();
        } 
    }  

    protected void doUpdateWeapons()
    {
        try
        {     	  	
		    SectFile sectfile = Main.cur().currentMissionFile;             	
			//int Aux = sectfile.sectionIndex("Wing");     
		    //FlightName = sectfile.var(Aux, wSelectFlight.getSelected());
			if(FlightName != null)   //Number of Flights
			{
				ItemAir plane = (ItemAir)aiPlane.get(wSelectPlane.getSelected());
				String CurrentWeapon =
					Aircraft.getWeaponsRegistered(plane.clazz)[wSelectWeapons.getSelected()];
				sectfile.set(FlightName, "weapons", CurrentWeapon);        		 	   			
			}
        }
        catch(Exception exception)
        {
           	System.out.println(exception.getMessage());
           	exception.printStackTrace();
        } 
    }
    
    protected void doUpdateSkin()
    {
        try
        {     	  	
		    SectFile sectfile = Main.cur().currentMissionFile;             	
//			int Aux = sectfile.sectionIndex("Wing");     
		    //FlightName = sectfile.var(Aux, wSelectFlight.getSelected());
			if(FlightName != null)   //Number of Flights
			{
			    int NoPlanes = sectfile.get(FlightName, "Planes", 1, 1, 4);
			    //if (sOwnSkinOn.isChecked())
			    {
					for (int i =0; i < NoPlanes; i++)
					//int i = wSelectNum.getSelected();  //Only this one
					{  //By PAL, set all the skins for the Flight in the defined
						//if (sectfile.get(FlightName, "skin" + i, (String)null) != null)
						sectfile.set(FlightName, "skin" + i, (String)wSelectSkin.list.get(wSelectSkin.getSelected()));		
					}			    	
			    }
			}
        }
        catch(Exception exception)
        {
           	System.out.println(exception.getMessage());
           	exception.printStackTrace();
        } 
    }    
     
    protected void fillSelectFlight()
    {
		int playerSlot = 0;
//        int numPlanes = 0;
//        int playerNum = 0;
        PlaneName = ""; //By PAL, becasue I don't know exactly what could happen 
      	wSelectFlight.clear();      
        try
        {       	
            SectFile sectfile = Main.cur().currentMissionFile;
            FlightName = sectfile.get("MAIN", "player", (String)null);
    		if((FlightName != null) && (FlightName.length() >2))
    		{
				String FlightName1 = FlightName.substring(0, FlightName.length() - 1);
	            String FlightName2 = FlightName1.substring(0, FlightName1.length() - 1);	    			
   				Regiment regiment = (Regiment)Actor.getByName(FlightName2);
        		if(regiment.getArmy() == 1)
		    	{
    				wSelectFlight.setEditTextColor(colorRed);
    				wSelectPlane.setEditTextColor(colorRed);
    				wSelectNum.setEditTextColor(colorRed);
    				wSelectWeapons.setEditTextColor(colorRed);
    				wSelectSkin.setEditTextColor(colorRed);
    			} else
    			{
    				wSelectFlight.setEditTextColor(colorBlue);
    				wSelectPlane.setEditTextColor(colorBlue);
    				wSelectNum.setEditTextColor(colorBlue);
    				wSelectWeapons.setEditTextColor(colorBlue);
    				wSelectSkin.setEditTextColor(colorBlue);
    			}
    		}            
            int i = sectfile.get("MAIN", "WEAPONSCONSTANT", 0, 0, 1);
            World.cur().setWeaponsConstant(i == 1);
//            playerNum = sectfile.get("MAIN", "playerNum", 0, 0, 3);  //Select the Plane of the Player
       		int j = sectfile.sectionIndex("Wing");
        	if(j >= 0)   //Number of Flights
        	{
           		int k = sectfile.vars(j);
        		if(wSelectFlight.posEnable == null || wSelectFlight.posEnable.length < k)
             	  wSelectFlight.posEnable = new boolean[k]; //By PAL to solve problem with enabling / disabling items 
        		for(int i1 = 0; i1 < k; i1++)
        		{      			
            		String s = sectfile.var(j, i1);
//                    boolean HasCockpit = false;
                    boolean StartsLater = (sectfile.get(s, "StartTime", 0) > 0);
//                    boolean OnlyAI = (sectfile.get(s, "OnlyAI", 0, 0, 1) == 1);             		
            		String s1 = sectfile.get(s, "Class", (String)null);
            	/*	if(s1 == null)
                		continue;*/
            		Class planeClass = null;
            		try
            		{
                		planeClass = ObjIO.classForName(s1);
//                		HasCockpit = (Property.value(planeClass, "cockpitClass") != null);
            		}
            		catch(Exception exception)
            		{
		      			ItemAir plane = (ItemAir)aiPlane.get(getFirstNonPlaceholder(0));  //By PAL, search first available
			    		String s2 = plane.className; //By PAL, if the plane didn't exist
            		    sectfile.set(s, "Class", s2);
	            		try //By PAL, to reassign if I didn't find it
	            		{
	                		planeClass = ObjIO.classForName(s2);
//	                		HasCockpit = (Property.value(planeClass, "cockpitClass") != null);
							ShowMessage("Warning", "The air class '" + s1 + "' doesn't exist in your IL-2 game installation." +
							"\nThe '" + s2 + "' plane will be used instead of the original one.");	                			                		
	            		}
	            		catch(Exception exc){}            		               		    			    		            			
            		}

//            		int k1 = sectfile.get(s, "Planes", 0, 0, 4);            		
                    if(s.equalsIgnoreCase(originalFlightName)) //By PAL, must be originalFlightName
                    {
	                    if (bSingleMission)
	                    {
		                    wSelectFlight.add("> " + s + " (" +
		                    	Property.stringValue(planeClass, "keyName", null)  + ")");  //By PAL, Regiment + (Name of the plane)              	
	                    }
	                    else
	                    {	                    	
	                    	wSelectFlight.add(">Your Flight: " + s + " (" +
		                    	Property.stringValue(planeClass, "keyName", null)  + ")");  //By PAL, Regiment + (Name of the plane) 
	                    }                     
                    }
                    else
                    {
	                    wSelectFlight.add(s + " (" +
	                    	Property.stringValue(planeClass, "keyName", null)  + ")");  //By PAL, Regiment + (Name of the plane)              	
                    }
                    if(s.equalsIgnoreCase(FlightName)) //By PAL, must be the FlightName read before
                    {
                    	playerSlot = i1;
//                    	numPlanes = k1;
                    	PlaneName = s1;	                    	
                    }                                                           
                  	wSelectFlight.posEnable[i1] = (!StartsLater); //I don't mind the rest (HasCockpit && (!OnlyAI))
        		}      			
        	}            
            /*
            for(int l = 0; l < k; l++)  //For all the flights
            {
                String s3 = sectfile.var(j, l);
                //if(s3.startsWith(s1))
                {
                    int j1 = s3.charAt(s3.length() - 1) - 48;
                    int k1 = sectfile.get(s3, "Planes", 0, 0, 4);
                    if(s3.equals(s))
                    {
                    	playerSlot = j1;
                    	numPlanes = k1;
                    }
                    Slot slot2 = new Slot();
                    slot[j1] = slot2;
                    slot2.wingName = s3;
                    slot2.players = k1;
                    slot2.fuel = sectfile.get(s3, "Fuel", 100, 0, 100);
                    String s4 = sectfile.get(s3, "Class", (String)null);
                    Class planeClass = ObjIO.classForName(s4);
                    if(l != playerSlot)
                        wSelectFlight.color = 255;
                    else
                        wSelectFlight.color = 0;
                    //wSelectFlight.setEditTextColor(255);
                    wSelectFlight.add(Property.stringValue(planeClass, "keyName", null) 
                    	+ " (" + s3 + ")");  //By PAL, Name of the plane + (Regiment)
                    boolean HasCockpit = (Property.value(planeClass, "cockpitClass") != null);
                    boolean OnlyAI = (sectfile.get(s, "OnlyAI", 0, 0, 1) == 1); 
                    wSelectFlight.posEnable[l] = (HasCockpit && !OnlyAI); //By PAL, it must have Cockpit and not being only AI                               
                    String s5 = sectfile.get(s3, "weapons", (String)null);
                    slot2.weapons = Aircraft.getWeaponsRegistered(slot2.planeClass);
                }               
            }*/ 

           /*	String LastWeapon = sectfile.get(FlightName, "weapons", (String)null); 	          
	        int k;               
	        wSelectNum.clear();        
	        if (playerSlot > -1)
	        	for(k = 0; k < numPlanes; k++)
	        	{
	        		wSelectNum.add(Integer.toString(k));
	        	}*/
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        wSelectFlight.setEditable(false);
        wSelectPlane.setEditable(false);
        wSelectNum.setEditable(false);
        wSelectWeapons.setEditable(false);
        wSelectSkin.setEditable(false);
        
        wSelectFlight.setSelected(-1, false, false); //By PAL, reset        
        if (wSelectFlight.posEnable[playerSlot])
        {    	
        	wSelectFlight.setSelected(playerSlot, true, false);
        }
        else
        {
        	for (int k = 0; k < wSelectFlight.size(); k++)
        		if (wSelectFlight.posEnable[k])
        		{
        			wSelectFlight.setSelected(k, true, false);
        			break;
        		}
        }
        doUpdateFlight();        		        
    }

/////////// By PAL, Return to standard Code //////////

    protected void fillMap()
        throws Exception
    {
        SectFile sectfile = Main.cur().currentMissionFile;
        String s = sectfile.get("MAIN", "MAP");
        if(s == null)
            throw new Exception("No MAP in mission file ");
        SectFile sectfile1 = new SectFile("maps/" + s);
        String s1 = sectfile1.get("MAP", "TypeMap", (String)null);
        if(s1 == null)
            throw new Exception("Bad MAP description in mission file ");
        NumberTokenizer numbertokenizer = new NumberTokenizer(s1);
        if(numbertokenizer.hasMoreTokens())
        {
            numbertokenizer.next();
            if(numbertokenizer.hasMoreTokens())
                s1 = numbertokenizer.next();
        }
        s1 = HomePath.concatNames("maps/" + s, s1);
        int ai[] = new int[3];
        if(!Mat.tgaInfo(s1, ai))
            throw new Exception("Bad MAP description in mission file ");
        landDX = (float)ai[0] * 200F;
        landDY = (float)ai[1] * 200F;
        if(main.land2D != null)
        {
            if(!main.land2D.isDestroyed())
                main.land2D.destroy();
            main.land2D = null;
        }
        s1 = null;
        int i = sectfile1.sectionIndex("MAP2D");
        if(i >= 0)
        {
            int j = sectfile1.vars(i);
            if(j > 0)
            {
                main.land2D = new Land2Dn(s, landDX, landDY);
                landDX = (float)main.land2D.mapSizeX();
                landDY = (float)main.land2D.mapSizeY();
            }
        }
        if(main.land2DText == null)
            main.land2DText = new Land2DText();
        else
            main.land2DText.clear();
        int k = sectfile1.sectionIndex("text");
        if(k >= 0 && sectfile1.vars(k) > 0)
        {
            String s2 = sectfile1.var(k, 0);
            main.land2DText.load(HomePath.concatNames("maps/" + s, s2));
        }
        computeScales();
        scaleCamera();
        setPosCamera(landDX / 2.0F, landDY / 2.0F);
    }

    protected void prepareTextDescription(int i)
    {
        if(textDescription == null)
            return;
        if(textArmyDescription == null || textArmyDescription.length != i)
            textArmyDescription = new String[i];
        for(int j = 0; j < i; j++)
        {
            textArmyDescription[j] = null;
            prepareTextDescriptionArmy(j);
        }

    }

    private void prepareTextDescriptionArmy(int i)
    {
        String s = (Army.name(i) + ">").toUpperCase();
        int j = 0;
        int k = textDescription.length();
        StringBuffer stringbuffer = new StringBuffer();
        do
        {
            if(j >= k)
                break;
            int l = textDescription.indexOf("<ARMY", j);
            if(l >= j)
            {
                if(l > j)
                    subString(stringbuffer, textDescription, j, l);
                int i1 = textDescription.indexOf("</ARMY>", l);
                if(i1 == -1)
                    i1 = k;
                for(l += "<ARMY".length(); l < k && Character.isSpaceChar(textDescription.charAt(l)); l++);
                if(l == k)
                {
                    j = k;
                    break;
                }
                if(textDescription.startsWith(s, l))
                {
                    l += s.length();
                    if(l < i1 && textDescription.charAt(l) == '\n')
                        l++;
                    subString(stringbuffer, textDescription, l, i1);
                }
                j = i1 + "</ARMY>".length();
                if(j < k && textDescription.charAt(j) == '\n')
                    j++;
            } else
            {
                subString(stringbuffer, textDescription, j, k);
                j = k;
            }
        } while(true);
        textArmyDescription[i] = new String(stringbuffer);
    }

    private void subString(StringBuffer stringbuffer, String s, int i, int j)
    {
        while(i < j) 
            stringbuffer.append(s.charAt(i++));
    }

//By PAL, from 4.111    
    private void setMissionDate(String s)
    {
        String s1 = s;
        if(Main.cur().currentMissionFile == null)
            return;
        if(Main.cur().currentMissionFile.sectionExist("SEASON"))
            return;
        try
        {
            s = s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf("."));
            int i = Integer.parseInt(s);
            int j = i / 10000 + 1940;
            i %= 10000;
            int k = i / 100;
            i %= 100;
            int l = i;
            Main.cur().currentMissionFile.sectionAdd("SEASON");
            int i1 = Main.cur().currentMissionFile.sectionIndex("SEASON");
            Main.cur().currentMissionFile.lineAdd(i1, "Year", "" + j);
            Main.cur().currentMissionFile.lineAdd(i1, "Month", "" + k);
            Main.cur().currentMissionFile.lineAdd(i1, "Day", "" + l);
            Main.cur().currentMissionFile.saveFile(s1);
        }
        catch(Exception exception) { }
    }   

    protected void fillTextDescription()
    {
        try
        {        	
           /* String s = Main.cur().currentMissionFile.fileName();
            int i = s.length() - 1;
            do
            {
                if(i <= 0)
                    break;
                char c = s.charAt(i);
                if(c == '\\' || c == '/')
                    break;
                if(c == '.')
                {
                    s = s.substring(0, i);
                    break;
                }
                i--;
            } while(true);
            ResourceBundle resourcebundle = ResourceBundle.getBundle(s, RTSConf.cur.locale);
            textDescription = resourcebundle.getString("Description");*/
	        if (bNotMultiPlay) //By PAL
			{
				String s = Main.cur().currentMissionFile.fileName(); //By PAL, to load only original name
				
	        	//By PAL, from 4.111
	            if(Main.cur().campaign != null && Main.cur().campaign.isDGen())
	                setMissionDate(s);
                				
				//By PAL v4101 
	            int i = s.length() - 1;
	            do
	            {
	                if(i <= 0)
	                    break;
	                char c = s.charAt(i);
	                if(c == '\\' || c == '/')
	                    break;
	                if(c == '.')
	                {
	                    s = s.substring(0, i);
	                    break;
	                }
	                i--;
	            } while(true);
	            			
	            ResourceBundle resourcebundle = ResourceBundle.getBundle(s, RTSConf.cur.locale);
	            textDescription = (DescriptString != null ? DescriptString : "")
	            	+ "\n\n" + resourcebundle.getString("Description");			
			}
			else
			{
				String s = Main.cur().currentMissionFile.fileName(); //By PAL, to load only original name 
	            for(int i = s.length() - 1; i > 0; i--)
	            {
	                char c = s.charAt(i);
	                if(c == '\\' || c == '/')
	                    break;
	                if(c != '.')
	                    continue;
	                s = s.substring(0, i);
	                break;
	            }
	            ResourceBundle resourcebundle = ResourceBundle.getBundle(s, RTSConf.cur.locale);
	            textDescription = resourcebundle.getString("Description");
	        }            
        }
        catch(Exception exception)
        {
            textDescription = null;
            textArmyDescription = null;
        }
    }

    protected String textDescription()
    {
        return textDescription;
    }

    protected Descript createDescript(GWindow gwindow)
    {
        return (Descript)gwindow.create(new Descript());
    }

/////// By PAL, new block /////
    public static String validateFileName(String s)
    {
        if(s.indexOf('\\') >= 0)
            s = s.replace('\\', '_');
        if(s.indexOf('/') >= 0)
            s = s.replace('/', '_');
        if(s.indexOf('?') >= 0)
            s = s.replace('?', '_');
        return s;
    }   
    
    static class Slot  //Added by PAL, to know planes
    {
        boolean bEnable;
        String wingName;
        int players;
        int fuel;
        Class planeClass;
        String planeKey;
        String weapons[];
        int weapon;

        Slot()
        {
        }
    }    

    static class ItemAir
    {

        public String name;
        public String className;
        public Class clazz;
        public boolean bEnablePlayer;
        public double speedMin;
        public double speedMax;

        public ItemAir(String s, Class class1, String s1)
        {
            speedMin = 200D;
            speedMax = 500D;
            name = s;
            clazz = class1;
            className = s1;
            bEnablePlayer = Property.containsValue(class1, "cockpitClass");
            String s2 = Property.stringValue(class1, "FlightModel", null);
            if(s2 != null)
            {
                SectFile sectfile = FlightModelMain.sectFile(s2);
                speedMin = sectfile.get("Params", "Vmin", (float)speedMin);
                speedMax = sectfile.get("Params", "VmaxH", (float)speedMax);
            }
        }
    } 

    // Removed by SAS~Storebror, unused private functions
//    private static int getPlaneList() //By PAL, adapted from QMB4.10
//    {
//        pl = Config.cur.ini.get("QMB", "PlaneList", 0, 0, 3); //By PAL from QMB 4.10, I only want values 0 (not sorted) or 1
//		if (pl > 1)
//			pl = 0; //I don't want for this special lists    	
//        return pl;
//    }
//    
//    private boolean checkCustomAirIni(String s) //By PAL, adapted from QMB4.10
//    {
//        SectFile sectfile = new SectFile(s);
//        if(sectfile.sections() <= 0)
//            return false;
//        SectFile sectfile1 = new SectFile("com/maddox/il2/objects/air.ini");
//        int i = sectfile.vars(0);
//        for(int j = 0; j < i; j++)
//            if(sectfile1.varExist(0, sectfile.var(0, j)))
//                return true;
//
//        return false;
//    }    
    
    public class byI18N_name //By PAL, adapted from QMB4.10
        implements Comparator
    {

        public int compare(Object obj, Object obj1)
        {
            if(RTSConf.cur.locale.getLanguage().equals("ru"))
            {
                return collator.compare(I18N.plane(((ItemAir)obj).name), I18N.plane(((ItemAir)obj1).name));
            } else
            {
                Collator collator1 = Collator.getInstance(RTSConf.cur.locale);
                collator1.setStrength(1);
                collator1.setDecomposition(2);
                return collator1.compare(I18N.plane(((ItemAir)obj).name), I18N.plane(((ItemAir)obj1).name));
            }
        }

        public byI18N_name()
        {
        }
    }    

    private void dumpFullPlaneList() //By PAL, adapted from QMB4.10
    {
        SectFile sectfile = new SectFile("com/maddox/il2/objects/air.ini");
        SectFile sectfile1 = new SectFile("./Missions/Quick/FullPlaneList.dump", 1);
        sectfile1.sectionAdd("AIR");
        int i = sectfile.vars(sectfile.sectionIndex("AIR"));
        for(int j = 0; j < i; j++)
            sectfile1.varAdd(0, sectfile.var(0, j));

    }
    
    public void fillArrayPlanes() //By PAL, adapted from QMB4.10
    {
        playerPlane.clear();
        playerPlaneC.clear();
        aiPlane.clear();
        aiPlaneC.clear();
        pl = Config.cur.ini.get("QMB", "PlaneList", 0, 0, 3); //By PAL from QMB 4.10, I only want values 0 (not sorted) or 1
		if (pl > 1)	pl = 0; //I don't want for this special lists
		boolean flag = (pl == 1);                
        String s = "com/maddox/il2/objects/air.ini";
        SectFile sectfile = new SectFile(s, 0);
        SectFile sectfile1 = new SectFile("com/maddox/il2/objects/air.ini");
        int j = sectfile.sections();
        if(j <= 0)
            throw new RuntimeException("GUIQuick: file '" + s + "' is empty");
        for(int k = 0; k < j; k++)
        {
            int l = sectfile.vars(k);
            for(int i1 = 0; i1 < l; i1++)
            {
                String s1 = sectfile.var(k, i1);
                if(!sectfile1.varExist(0, s1))
                    continue;
                int i = sectfile1.varIndex(0, s1);
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile1.value(k, i));
                String s2 = numbertokenizer.next((String)null);
                boolean flag2 = true;
                do
                {
                    if(!numbertokenizer.hasMoreTokens())
                        break;
                    if(!"NOQUICK".equals(numbertokenizer.next()))
                        continue;
//By PAL, what to do with NOQUICK                        
                    flag2 = false;
                    break;
                } while(true);
                if(!flag2)
                    continue;
                Class class1 = null;
                try
                {
                    class1 = ObjIO.classForName(s2);
                }
                catch(Exception exception)
                {
                    System.out.println("GUIQuick: class '" + s2 + "' not found");
                    break;
                }
                ItemAir itemair = new ItemAir(s1, class1, s2);
                if(itemair.bEnablePlayer)
                {
                    playerPlane.add(itemair);
                    if(AirportCarrier.isPlaneContainsArrestor(class1))
                        playerPlaneC.add(itemair);
                }
                aiPlane.add(itemair);
                if(AirportCarrier.isPlaneContainsArrestor(class1))
                    aiPlaneC.add(itemair);
            }

        }

        if(flag)
        {
            Collections.sort(playerPlane, new byI18N_name());
            Collections.sort(playerPlaneC, new byI18N_name());
            Collections.sort(aiPlane, new byI18N_name());
            Collections.sort(aiPlaneC, new byI18N_name());
        }
    }       
    
    public void fillComboPlane(GWindowComboControl gwindowcombocontrol, boolean flag)
    {
        gwindowcombocontrol.clear();
        ArrayList arraylist = null;
        if(bPlaneArrestor)
            arraylist = flag ? playerPlaneC : aiPlaneC;
        else
            arraylist = flag ? playerPlane : aiPlane;
        int i = arraylist.size();
        for(int j = 0; j < i; j++)
        {
            ItemAir itemair = (ItemAir)arraylist.get(j);
            gwindowcombocontrol.add((itemair.bEnablePlayer ? "" : "! ") + I18N.plane(itemair.name));
        }
        gwindowcombocontrol.setSelected(0, true, false);
    }
    
    public String fillComboWeapon(GWindowComboControl gwindowcombocontrol, ItemAir itemair, int i)
    {
        gwindowcombocontrol.clear();
        Class class1 = itemair.clazz;
        String as[] = Aircraft.getWeaponsRegistered(class1);
        if(as != null && as.length > 0)
        {
            for(int j = 0; j < as.length; j++)
            {
                String s = as[j];
                gwindowcombocontrol.add(I18N.weapons(itemair.name, s));
            }
            gwindowcombocontrol.setSelected(i, true, false);
        }
        return as[i];
    }
    
    private void fillComboSkin(GWindowComboControl gwindowcombocontrol, ItemAir itemair)
    {
        gwindowcombocontrol.clear();
        gwindowcombocontrol.add(i18n("neta.Default"));
        try
        {
            //int i = cAircraft.getSelected();
            String s = Main.cur().netFileServerSkin.primaryPath();
            String s1 = validateFileName(itemair.name);//(String)airNames.get(i));
            File file = new File(HomePath.toFileSystemName(s + "/" + s1, 0));
            File afile[] = file.listFiles();
            if(afile != null)
            {
                for(int j = 0; j < afile.length; j++)
                {
                    File file1 = afile[j];
                    if(file1.isFile())
                    {
                        String s2 = file1.getName();
                        String s3 = s2.toLowerCase();
                        if(s3.endsWith(".bmp") && s3.length() + s1.length() <= 122)
                        {
                            int k = BmpUtils.squareSizeBMP8Pal(s + "/" + s1 + "/" + s2);
                            if(k == 512 || k == 1024)
                                gwindowcombocontrol.add(s2);
                            else
                                System.out.println("Skin " + s + "/" + s1 + "/" + s2 + " NOT loaded");
                        }
                    }
                }

            }
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        gwindowcombocontrol.setSelected(0, true, false);
    }

/////////// By PAL, end of block ////////////////

    protected void doNext()
    {
    }

    protected void doDiff()
    {
    }

    protected void doBack()
    {
    }

    protected void doLoodout()
    {
    }

    protected void clientRender()
    {
    }

    protected void clientSetPosSize()
    {
    }

    protected void clientInit(GWindowRoot gwindowroot)
    {
    }

    protected String infoMenuInfo()
    {
        return "????????";
    }  

    protected void init(GWindowRoot gwindowroot)
    {
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = infoMenuInfo();
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        createRenderWindow(dialogClient);
        dialogClient.create(wScrollDescription = new ScrollDescript());
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        bPrev = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bDifficulty = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bLoodout = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bNext = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));        
        bReset = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 144F, 48F, 48F));

        
        //By PAL extra lines and comboboxes
   			sOwnSkinOn = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));  //By PAL
   			sParachuteOn = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));  //By PAL   			
	        bSave = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
	        bFMB = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));	        		
   			wSelectFlight = (GWindowComboControl)dialogClient.addControl(new 
   	 			GWindowComboControl(dialogClient, 2.0F, 2.0F, 10.5F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
   			wSelectNum = (GWindowComboControl)dialogClient.addControl(new 
   	 			GWindowComboControl(dialogClient, 2.0F, 2.0F, 2.0F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
   			wSelectPlane = (GWindowComboControl)dialogClient.addControl(new 
   	 			GWindowComboControl(dialogClient, 2.0F, 2.0F, 14.5F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
   			wSelectWeapons = (GWindowComboControl)dialogClient.addControl(new 
   	 			GWindowComboControl(dialogClient, 2.0F, 2.0F, 10.5F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
			wSelectSkin = (GWindowComboControl)dialogClient.addControl(new 
   	 			GWindowComboControl(dialogClient, 2.0F, 2.0F, 10.5F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));

	        wWeather = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
	        wCldHeight = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
	        wTimeHour = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
	        wTimeMins = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));


			for(int j = 0; j < 24; j++)
			{
				wTimeHour.add(j < 10? "0" + j +"h": "" + j + "h"); //By PAL, add from 0 to 23
			}
	        wTimeHour.setEditable(false);
	        wTimeHour.setSelected(0, true, false);									
		/*	for(int j = 0; j < 12; j++)
			{
				wTimeMins.add((j*5) < 10? "0" + j * 5 + "m" : "" + j * 5 + "m"); //By PAL, add from 0 to 23
			}*/
			for(int j = 0; j < 60; j++)
			{
				wTimeMins.add(j < 10 ? "0" + j + "m" : "" + j + "m"); //By PAL, add from 0 to 60
			}			
	        wTimeMins.setEditable(false);
	        wTimeMins.setSelected(0, true, false);						

            /*wCldHeight.add("500");  //This are taken from FMB
            wCldHeight.add("600");
            wCldHeight.add("700");
            wCldHeight.add("800");
            wCldHeight.add("900");
            wCldHeight.add("1000");
            wCldHeight.add("1100");
            wCldHeight.add("1200");
            wCldHeight.add("1300");
            wCldHeight.add("1400");
            wCldHeight.add("1500");*/
            
	        wCldHeight.add("500m");	//Taken from QMB4.10
	        wCldHeight.add("750m");
	        wCldHeight.add("1000m");
	        wCldHeight.add("1250m");
	        wCldHeight.add("1500m");
	        wCldHeight.add("1750m");
	        wCldHeight.add("2000m");
	        wCldHeight.add("2250m");
	        wCldHeight.add("2500m");
	        wCldHeight.add("2750m");
	        wCldHeight.add("3000m");                    	        
	        wCldHeight.setEditable(false);       
         
	        wWeather.add(I18N.gui("quick.CLE"));
	        wWeather.add(I18N.gui("quick.GOO"));
	        wWeather.add(I18N.gui("quick.HAZ"));
	        wWeather.add(I18N.gui("quick.POO"));
	        wWeather.add(I18N.gui("quick.BLI"));
	        wWeather.add(I18N.gui("quick.RAI"));
	        wWeather.add(I18N.gui("quick.THU"));
	        wWeather.setEditable(false);        

			sOwnSkinOn.hideWindow();
			sParachuteOn.hideWindow();
			wWeather.hideWindow();
			wCldHeight.hideWindow();
			wTimeHour.hideWindow();
			wTimeMins.hideWindow();
			
			wSelectFlight.hideWindow();
			wSelectPlane.hideWindow();
			wSelectNum.hideWindow();
			wSelectWeapons.hideWindow();
			wSelectSkin.hideWindow();
			bSave.hideWindow();
			bFMB.hideWindow();		
			
			wSelectPlane.listVisibleLines = 30;
			wSelectFlight.listVisibleLines = 16;
			wSelectNum.listVisibleLines = 4;
			wSelectWeapons.listVisibleLines = 16;
			wSelectSkin.listVisibleLines = 16;
			
        russianMixedRules = "< ',' < '.' < '-' <\u0430,\u0410< a,A <\u0431,\u0411< b,B <\u0432,\u0412< v,V <\u0433,\u0413< g,G <\u0434,\u0414< d,D <\u0435,\u0415 < \u0451,\u0401 < \u0436,\u0416 < \u0437,\u0417< z,Z <\u0438,\u0418< i,I <\u0439,\u0419< j,J <\u043A,\u041A< k,K <\u043B,\u041B< l,L <\u043C,\u041C< m,M <\u043D,\u041D< n,N <\u043E,\u041E< o,O <\u043F,\u041F< p,P <\u0440,\u0420< r,R <\u0441,\u0421< s,S <\u0442,\u0422< t,T <\u0443,\u0423< u,U <\u0444,\u0424< f,F <\u0445,\u0425< h,H <\u0446,\u0426< c,C <\u0447,\u0427 < \u0448,\u0428 < \u0449,\u0429 < \u044A,\u042A < \u044B,\u042B< i,I <\u044C,\u042C < \u044D,\u042D< e,E <\u044E,\u042E < \u044F,\u042F< q,Q < x,X < y,Y";
        try //By PAL from QMB4.10
        {
            collator = new RuleBasedCollator(russianMixedRules);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }      
        if(Config.cur.ini.get("QMB", "DumpPlaneList", 0, 0, 1) > 0)
            dumpFullPlaneList();
        /*File file = new File("Missions/Quick/QMBair_" + pl + ".ini");
        if(!file.exists() && pl > 1)
            pl = 0;
        else
        if(!checkCustomAirIni("Missions/Quick/QMBair_" + pl + ".ini"))
            pl = 0;  */      
        playerPlane = new ArrayList();
        aiPlane = new ArrayList();
        playerPlaneC = new ArrayList();
        aiPlaneC = new ArrayList();
        bPlaneArrestor = false;
        //fillArrayPlanes();  I will use it on enter      
        if (!bUseColor) //By PAL, don't use colors
        {
        	colorRed = 0x000000;
        	colorBlue = 0x000000;	
        }       
			         
        clientInit(gwindowroot);          
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public GUIBriefingGeneric(int i)
    {
        super(i);
        curMissionNum = -1;
        briefSound = null;
        
        //+++ TODO: 4.12 changed code +++
        bigFontMultip = 1.0F;
        //--- TODO: 4.12 changed code ---
        
        scales = scale.length;
        curScale = scales - 1;
        curScaleDirect = -1;
        line2XYZ = new float[6];
        _gridX = new int[20];
        _gridY = new int[20];
        _gridVal = new int[20];
        bLPressed = false;
        bRPressed = false;
        bMPressed = false; //By PAL      
    }    

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public ScrollDescript wScrollDescription;
    public Descript wDescript;
    public GUIButton bLoodout;
    public GUIButton bReset; //By PAL, new to return to saved mission
    public GUIButton bDifficulty;
    public GUIButton bPrev;
    public GUIButton bNext;
    public String textDescription;
    public String textArmyDescription[];
    public String curMissionName;
    public int curMissionNum;
    public String curMapName;
    protected String briefSound;
    protected Main3D main;
    protected GUIRenders renders;
    protected RenderMap2D renderMap2D;
    protected CameraOrtho2D cameraMap2D;
    protected TTFont gridFont;
    
    //+++ TODO: 4.12 changed code +++
    protected TTFont waypointFont;
    protected float bigFontMultip;
    //--- TODO: 4.12 changed code ---
    
    protected Mat emptyMat;
    protected float scale[] = {
        0.064F, 0.032F, 0.016F, 0.008F, 0.004F, 0.002F, 0.001F, 0.0005F, 0.00025F
    };
    protected int scales;
    protected int curScale;
    protected int curScaleDirect;
    protected float landDX;
    protected float landDY;
    private float line2XYZ[];
    private int _gridCount;
    private int _gridX[];
    private int _gridY[];
    private int _gridVal[];
    protected boolean bLPressed;
    protected boolean bRPressed;
    
    
    //By PAL, required variables for MOD
        public GUIButton bSave, bFMB;
        public GWindowComboControl wSelectPlane, wSelectWeapons, wSelectSkin;        
   	    private ArrayList playerPlane;
    	private ArrayList aiPlane;
    	private ArrayList playerPlaneC;
    	private ArrayList aiPlaneC;
    	private boolean bPlaneArrestor;
    
    	public String FlightName, PlaneName, LastPlaneSkin, LastSkin, LastNoseArt = null;
    	public int OriginalArmy = 1;
    	//public int CurrentArmy = 1;
    	public String originalFlightName = null;
    	public String selectedFlightName = null; //By PAL, new to remember which one was selected
	    public String OriginalPlaneName = null;
	    public int OriginalPlayerNum = 0;   	
    	private boolean bNotMultiPlay = false;
    	private boolean	bSingleMission = false;
    	private boolean	bCampaignChanged = true; //To tell campaign if I modified something in Briefing
        public GUISwitchBox3 sOwnSkinOn; //By PAL
        public GUISwitchBox3 sParachuteOn; //By PAL        
        public GWindowComboControl wSelectFlight;  //By PAL
        public GWindowComboControl wSelectNum;  //By PAL
//        private Slot slot[];  //By PAL
        private String LastMissionFile = "Missions/Single/LastMission.mis";
        //private String LastMissionProps = "Missions/Single/LastMission.properties";
        private String HeaderDescript = "*** Original Single Mission brief for Flight ";
        private String PlaceholderLabel = "air.Placeholder";
        //private String FMBMissionFile = "Missions/Single/FromMissionPro.mis";
        //private String FMBMissionProps = "Missions/Single/FromMissionPro.properties";        
    	//ListCellRenderer renderer = new LockableListCellRenderer();  //By PAL
    	//wSelectFlight .setRenderer(renderer);     
        //this.wSelectFlight.drawLines()+= new System.Windows.Forms.DrawItemEventHandler(this.comboBox1_DrawItem);
        public String OriginalFileName; //By PAL, to store original FileName
        protected String missionFileName, lastOpenFile;
        //By PAL, to remember Mouse Position, etc.
        boolean bMPressed = false; //By PAL
        
        int lastGameState;
        
        public String DescriptString;
		private int colorRed = 0xc01010dd; //By PAL, color Red
		private int colorBlue = 0xc0dd1010; //By PAL, coolor Blue
		private boolean bUseColor = Config.cur.ini.get("Mods", "PALMODsColor", true);
		private boolean bPALZoomInUp = Config.cur.ini.get("Mods", "PALZoomInUp", false);
		private String MPVer = "MissionPro V4.111 (by P.A.L.)";		
        private static int pl; //From QMB4.10
	    private String russianMixedRules; //From QMB4.10
	    private RuleBasedCollator collator; //From QMB4.10
	    //private boolean ComingFromFMB = false; //By PAL       		
		
	    public GWindowComboControl wWeather;
	    public GWindowComboControl wCldHeight;
	    public GWindowComboControl wTimeHour;
	    public GWindowComboControl wTimeMins;				          		         
    

}
