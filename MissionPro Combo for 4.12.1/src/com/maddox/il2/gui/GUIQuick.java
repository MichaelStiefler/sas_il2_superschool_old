///  Final Release 4.111m

/////////////////////////////////////////////////////////////////////////////////////////
//   QMB Full MODded
//   QMBPro 4.101m developed by PAL from TD source
//   lifted to 4.12 by SAS~Storebror
/////////////////////////////////////////////////////////////////////////////////////////

package com.maddox.il2.gui;

import java.io.File;
import java.io.FilenameFilter;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.AirportCarrier;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.builder.PlMission;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.HomePath;
import com.maddox.rts.IniFile;
import com.maddox.rts.LDRres;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SFSInputStream;
import com.maddox.rts.SectFile;
import com.maddox.util.HashMapExt;
import com.maddox.util.NumberTokenizer;
import com.maddox.util.SharedTokenizer;

public class GUIQuick extends GameState
{
	//By PAL, introduced to update countries comboboxes whenever needed?	
	public void updateCountries()
	{
		for(int i3 = 0; i3 < 16; i3++)
        {
        	fillComboCountry(dlg[i3].wCountry, i3);  //By PAL 
        	selectCountries(i3); //By PAL, to select the corresponding Country in combobox
        }
	}
	
    public static class DirFilter
        implements FilenameFilter
    {

        public boolean accept(File file, String s)
        {
            File file1 = new File(file, s);
            return file1.isDirectory();
        }

        public DirFilter()
        {
        }
    }

    public class byI18N_name
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

/* By PAL, from 4.111
    public class IOState
    {

        public void getMap()
        {
            String as[] = {
                "Okinawa", "CoralSeaOnline", "Net8Islands", "Smolensk", "Moscow", "Crimea", "Kuban", "Bessarabia", "MTO", "Slovakia_winter", 
                "Slovakia_summer"
            };
            int i = Integer.parseInt(map);
            String s = as[i];
            map = s;
            wMap.setValue(I18N.map(s));
            mapChanged();
        }

        public void save()
        {
            our = OUR;
            scramble = bScramble;
            situation = wSituation.getSelected();
            map = getMapName();
            target = wTarget.getSelected();
            defence = wDefence.getSelected();
            altitude = wAltitude.getValue();
            pos = wPos.getValue();
            weather = wWeather.getSelected();
            cldheight = wCldHeight.getValue();
            timeH = wTimeHour.getSelected();
            timeM = wTimeMins.getSelected();
            noneTARGET = wLevel.getSelected();
        }

        public void loaded()
        {
            OUR = our;
            wMap.setValue(I18N.map(map));
            mapChanged();
            wSituation.setSelected(situation, true, false);
            wTarget.setSelected(target, true, false);
            wDefence.setSelected(defence, true, false);
            bScramble = scramble;
            wAltitude.setValue(altitude);
            int i = Integer.parseInt(wAltitude.getValue());
            if(i < 100)
            {
                if(situation != 0)
                    pos = "700";
                else
                    pos = "0";
                cldheight = "2000";
                altitude = wAltitude.get(i);
                wAltitude.setValue(altitude);
                scramble = false;
            }
            wPos.setValue(pos);
            validateEditableComboControl(wPos);
            validateEditableComboControl(wAltitude);
            wCldHeight.setValue(cldheight);
            validateEditableComboControl(wCldHeight);
            wWeather.setSelected(weather, true, false);
            wTimeHour.setSelected(timeH, true, false);
            wTimeMins.setSelected(timeM, true, false);
            wLevel.setSelected(noneTARGET, true, false);
            if(target == 0)
                noneTarget = true;
            else
                noneTarget = false;
        }
 */

//By PAL, my own:
    public class IOState
    {

   /*     public void getMap()
        {
            String as[] = {
                "Okinawa", "CoralSeaOnline", "Net8Islands", "Smolensk", "Moscow", "Crimea", "Kuban", "Bessarabia", "MTO", "Slovakia_winter", 
                "Slovakia_summer"
            };
            int i = Integer.parseInt(map);
            String s = as[i];
            map = s;
            wMap.setValue(I18N.map(s));
            //mapChanged();
            mapChanged(false);
        }*/
    	
        public void getMap() //v4101, only missions 4.09 with map specified as number
        {           
            int i = parseIntOwn(map); //Integer.parseInt(map);       
			if (i >= 0 && i < _mapKey.length)
			{
	            //String s = as[i];
		        String s = _mapKey[i];               
	            map = s;
      		    wMap.setValue(I18N.map(s));  
		        mapChanged(false);				
			}
			else					
            {
            	if (client.isActivated())
            	{
					new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, "Error", "The 4.09m legacy mission loaded has a map with code " +
							i + " which is not installed now!" , 3, 0.0F);             		
            	}
            	map = ""; //By PAL, reset to the first one
            }        	 	                    	 
        }
        
        public void save()
        {
            our = OUR;
            scramble = bScramble;
            situation = wSituation.getSelected();
            map = getMapName();
            //target = wTarget.getSelected();
            target = wTargetList.getSelected(); //By PAL, originally wTarget           
            defence = wDefence.getSelected();
            altitude = wAltitude.getValue();
            pos = wPos.getValue();
            weather = wWeather.getSelected();
            cldheight = wCldHeight.getValue();
            timeH = wTimeHour.getSelected();
            timeM = wTimeMins.getSelected();
            //noneTARGET = wLevel.getSelected();
            noneTARGET = noneTargetNum; //By PAL, extra variable to keep reference of noneTarget
        }

        public void loaded()
        {
            OUR = our;
            wMap.setValue(I18N.map(map));

			mapChanged(false); //V4101
			
			updateCountries();			

            wSituation.setSelected(situation, true, false);
            //wTarget.setSelected(target, true, false);
   //By PAL, problem on load
   			if (target < wTargetList.size()) 
            	wTargetList.setSelected(target, true, false); //By PAL, originally wTarget. Pay attention with this!
            else wTargetList.setSelected(-1, false, false);
                       
            wDefence.setSelected(defence, true, false);          
            bScramble = scramble;  //v4101
            wPos.setValue(pos);
            validateEditableComboControl(wPos);
            wAltitude.setValue(altitude);
            int i = parseIntOwn(wAltitude.getValue());
            if(i < 100)
            {
                if(situation != 0)
                    pos = "700";
                else
                    pos = "0";
                cldheight = "2000";
                altitude = wAltitude.get(i);
                wAltitude.setValue(altitude);
                scramble = false;
            }           
            //validateEditableComboControl(wPos);
            //wAltitude.setValue(altitude);
            validateEditableComboControl(wAltitude);
            wCldHeight.setValue(cldheight);
            validateEditableComboControl(wCldHeight);
            wWeather.setSelected(weather, true, false);
            wTimeHour.setSelected(timeH, true, false);
            wTimeMins.setSelected(timeM, true, false);
            //wLevel.setSelected(noneTARGET, true, false); //By PAL, can I?
            /*
            if(target == 0)
                noneTarget = true;
            else
                noneTarget = false;*/ //By PAL
        }
        public boolean our;
        public boolean scramble;
        public int situation;
        public String map;
        public int target;
        public int defence;
        public String altitude;
        public String pos;
        public int weather;
        public String cldheight;
        public int timeH;
        public int timeM;
        public int noneTARGET;

        public IOState()
        {
        }
    }
    
    private int parseIntOwn(String s)
    {
    	try
    	{
    		int i = Integer.parseInt(s);
    		return i;
    	}
		catch (Exception exception)
		{
			System.out.println("Error on parseInt: " + s);
			return -1;			
		}
    }

	private void UpdateComboNum(int indx) //By PAL
	{
		//if (indx == 0) return;  //By PAL, new to avoid doing anything on the first field (Player)
		int i = parseIntOwn(dlg[indx].wNum.getValue());
   		if(i == 0) //By PAL, to clear all the fields of the empty flights
  		{
			dlg[indx].wPlane.setValue("");
			//dlg[indx].wSkill.setValue("");
			//dlg[indx].wLoadout.setValue("");
			dlg[indx].wSkill.setEnable(false);
			dlg[indx].wLoadout.setEnable(false);
			dlg[indx].wCountry.setEnable(false);										
 		}
 		else
 		{
			dlg[indx].wSkill.setEnable(true);
			dlg[indx].wLoadout.setEnable(true);
			dlg[indx].wCountry.setEnable(true);				             			
    		dlg[indx].wPlane.setSelected(dlg[indx].wPlane.getSelected(), true, false);
			dlg[indx].wSkill.setSelected(dlg[indx].wSkill.getSelected(), true, false);
			dlg[indx].wLoadout.setSelected(dlg[indx].wLoadout.getSelected(), true, false);
			dlg[indx].wCountry.setSelected(dlg[indx].wCountry.getSelected(), true, false);				       					
 		}		
	}

    private int getFirstNonPlaceholder(int k, boolean Player)
    {
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////                    
		//              By PAL, avoid selecting placeholder
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////                         
        ArrayList arraylist = null;
        if(bPlaneArrestor)
            arraylist = Player ? playerPlaneC : aiPlaneC;
        else
            arraylist = Player ? playerPlane : aiPlane;

		int k0 = k;
        ItemAir itemair;            
		itemair = (ItemAir)arraylist.get(k);	            		
        while(itemair.className.equalsIgnoreCase(PlaceholderLabel)) 
        {
        	if(k >= arraylist.size() - 1) break;
        	k++;
            itemair = (ItemAir)arraylist.get(k);
        }
        if(k >= arraylist.size() - 1)
        {
            k = k0; //I start in the original value
            while(itemair.className.equalsIgnoreCase(PlaceholderLabel)) //By PAL, avoid selecting placeholder
            {
            	if(k <= 0) break;
            	k--;
                itemair = (ItemAir)arraylist.get(k);
            }                	
        }
        return k;
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////                    
    } 

    class WComboNum extends GWindowComboControl
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
             	UpdateComboNum(indx);	//By PAL, to clear all the fields of the empty flights           	
                int k = getSelected();
                if(k < 0)
                {
                    return true;
                } else
                {
                    wing[indx].planes = indx == 0 ? k + 1 : k;
                    return true;
                }
            }
            else       
            if(i == 15)
            {	//By PAL, value set, no other change            
           		UpdateComboNum(indx); //By PAL, to clear all the fields of the empty flights	
                int k = getSelected();            	
               	wing[indx].planes = indx == 0 ? k + 1 : k;             	
                return true;
            }
            else
            {
                return super.notify(i, j);
            }
        }

        private int indx;

        public WComboNum(int i, GWindow gwindow, float f, float f1, float f2)
        {
            super(gwindow, f, f1, f2);
            indx = i;
        }
    }

    class WComboSkill extends GWindowComboControl
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                int k = getSelected();
                if(k < 0)
                {
                    return true;
                } else
                {
                    wing[indx].skill = k;
                    return true;
                }
            } else
            {
                return super.notify(i, j);
            }
        }

        private int indx;

        public WComboSkill(int i, GWindow gwindow, float f, float f1, float f2)
        {
            super(gwindow, f, f1, f2);
            indx = i;
        }
    }

    class WComboPlane extends GWindowComboControl
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                int k = getSelected();
                if(k < 0)
                    return true;                   
                    
                int kn = getFirstNonPlaceholder(k, (indx == 0));   //By PAL, general function to retrieve first available 
                if (kn != k)
                {
                	setSelected(kn, true, false);
                	k = kn;
                }    
                    
                ItemAir itemair;
                if(indx == 0)
                    itemair = (ItemAir)(bPlaneArrestor ? playerPlaneC : playerPlane).get(k);
                else
                    itemair = (ItemAir)(bPlaneArrestor ? aiPlaneC : aiPlane).get(k);
            	    
                String s = fillComboWeapon(dlg[indx].wLoadout, itemair, 0);
                wing[indx].setPlane(k);
                boolean flag = false;
                if(indx == 0)
                {
                    wing[indx].fromUserCfg();
                    String as[] = Aircraft.getWeaponsRegistered(wing[indx].plane.clazz);
                    int l = 0;
                    do
                    {
                        if(l >= as.length)
                            break;
                        if(as[l].equals(wing[indx].weapon))
                        {
                            fillComboWeapon(dlg[indx].wLoadout, wing[indx].plane, l);
                            flag = true;
                            break;
                        }
                        l++;
                    } while(true);
                }
 				else
            	if(indx > 0 && dlg[indx].wNum.getSelected() == 0)
           		{
       				dlg[indx].wNum.setSelected(1, true, true);  // By PAL, one plane at least, true, true processes
       				//wing[indx].planes = 1;  // By PAL, is necessary?
					//dlg[indx].wSkill.setEnable(true);
					//dlg[indx].wLoadout.setEnable(true);
					//dlg[indx].wCountry.setEnable(true);						
       				//dlg[indx].wSkill.setSelected(dlg[indx].wSkill.getSelected(), true, false);
       				//dlg[indx].wLoadout.setSelected(dlg[indx].wLoadout.getSelected(), true, false);   			
       				//dlg[indx].wCountry.setSelected(dlg[indx].wCountry.getSelected(), true, false);
            	}                
                if(!flag)
                    wing[indx].weapon = s;
                return true;
            }
			else
            if(i == 15)
            {
//                int k = getSelected();            	
            	if(indx > 0 && dlg[indx].wNum.getSelected() == 0)
               	{ //By PAL, after closing combo and with indx != 0
       				dlg[indx].wNum.setSelected(1, true, true);  // By PAL, one plane at least            			   			
                }
                return true;
            }
            else
            {
                return super.notify(i, j);
            }
        }

        private int indx;

        public WComboPlane(int i, GWindow gwindow, float f, float f1, float f2)
        {
            super(gwindow, f, f1, f2);
            indx = i;
        }
    }

    class WComboLoadout extends GWindowComboControl
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                int k = getSelected();
                if(k < 0)
                    return true;
                wing[indx].setWeapon(k);
                if(indx == 0)
                    wing[indx].toUserCfg();
                return true;
            } else
            {
                return super.notify(i, j);
            }
        }

        private int indx;

        public WComboLoadout(int i, GWindow gwindow, float f, float f1, float f2)
        {
            super(gwindow, f, f1, f2);
            indx = i;
        }
    }


    class WComboCountry extends GWindowComboControl //By PAL, added Country
    {                   
        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                int k = getSelected();
                if (k < 0)
               		return true;                
                String s = OUR ? r01 : g01;
                if (indx < 8)
                {
                	if (OUR)
	                {
	                	if (countryLstRed.size() > k)
	                	{
				            s = (String)countryLstRed.get(k);
			                ArrayList arraylist = (ArrayList)regList.get(s);
							Object obj = arraylist.get(0);
							wing[indx].regiment = ((Regiment)obj).name();
	                	}
	                		//wing[indx].regiment = (String)countryLstRed.get(k) + "01";               		
	                } else
	                {
	                	if (countryLstBlue.size() > k)
	                	{	                	
				            s = (String)countryLstBlue.get(k);
			                ArrayList arraylist = (ArrayList)regList.get(s);
							Object obj = arraylist.get(0);
							wing[indx].regiment = ((Regiment)obj).name();			                
	                	}
	                		//wing[indx].regiment = (String)countryLstBlue.get(k) + "01";
	                }                 	
                } else
                {
                	if (!OUR)
	                {
	                	if (countryLstRed.size() > k)
	                	{
				            s = (String)countryLstRed.get(k);
			                ArrayList arraylist = (ArrayList)regList.get(s);
							Object obj = arraylist.get(0);
							wing[indx].regiment = ((Regiment)obj).name();
	                	}
	                		//wing[indx].regiment = (String)countryLstRed.get(k) + "01";
	                } else
	                {
	                	if (countryLstBlue.size() > k)
	                	{	                	
				            s = (String)countryLstBlue.get(k);
			                ArrayList arraylist = (ArrayList)regList.get(s);
							Object obj = arraylist.get(0);
							wing[indx].regiment = ((Regiment)obj).name();
	                	}
	                		//wing[indx].regiment = (String)countryLstBlue.get(k) + "01";
	                }                 	
                }                 
                return true;
            }
            else            	
            {
                return super.notify(i, j);
            }
        }

        private int indx;

        public WComboCountry(int i, GWindow gwindow, float f, float f1, float f2)
        {
            super(gwindow, f, f1, f2);
            indx = i;
        }
    }

    class WButtonArming extends GUIButton
    {

        public boolean notify(int i, int j)
        {
            if(i == 2)
            {
                if(wing[indx].planes == 0)
                {
                    return true;
                } else
                {
                    indxAirArming = indx;
                    wing[indx].toAirArming();
                    GUIAirArming.stateId = 4;
                    Main.stateStack().push(55);
                    return true;
                }
            } else
            {
                return super.notify(i, j);
            }
        }

        private int indx;

        public WButtonArming(int i, GWindow gwindow, GTexture gtexture, float f, float f1, float f2, 
                float f3)
        {
            super(gwindow, gtexture, f, f1, f2, f3);
            indx = i;
        }
    }

    static class ItemDlg  //v4101
    {
        public WComboNum wNum;
        public WComboSkill wSkill;
        public WComboPlane wPlane;
        public WComboLoadout wLoadout;
        public WComboCountry wCountry;    //Added by PAL                   
        public WButtonArming bArming;

        ItemDlg()
        {
        }
    }

    public class ItemWing
    {

        public String getPlane()
        {
            return plane.name;
        }

   /*     public void setPlane(String s)
        {
            ArrayList arraylist = null;
            if(bPlaneArrestor)
                arraylist = indx == 0 ? playerPlaneC : aiPlaneC;
            else
                arraylist = indx == 0 ? playerPlane : aiPlane;
            int i = 0;
            do
            {
                if(i >= arraylist.size())
                    break;
                plane = (ItemAir)arraylist.get(i);
                if(plane.name.equals(s))
                    break;
                i++;
            } while(true);
        }*/
        
        public void setPlane(String s)
        {
            ArrayList arraylist = null;
            if(bPlaneArrestor)
                arraylist = indx == 0 ? playerPlaneC : aiPlaneC;
            else
                arraylist = indx == 0 ? playerPlane : aiPlane;
            int i = 0;
            do
            {
                if(i >= arraylist.size())
                    break;
                plane = (ItemAir)arraylist.get(i);
                if(plane.name.equals(s))
                {
                	int in = getFirstNonPlaceholder(i, (indx == 0)); //By PAL, be sure not to use a placeholder
                	plane = (ItemAir)arraylist.get(in);
                    //dlg[indx].wPlane.setSelected(i, true, false);                   
                    break;
                }                	
                i++;
            } while(true);
            
            if(i >= arraylist.size()) //By PAL, if I reached the end and didn't find it, it doesn't exist, choose one from 0
            {
            	int in = getFirstNonPlaceholder(0, (indx == 0)); //By PAL, be sure not to use a placeholder
            	plane = (ItemAir)arraylist.get(in);            	
            }            
        }        

        public void loaded()
        {
            dlg[indx].wNum.setSelected(indx == 0 ? planes - 1 : planes, true, false);
            dlg[indx].wSkill.setSelected(skill, true, false);
            ArrayList arraylist = null;
            if(bPlaneArrestor)
                arraylist = indx == 0 ? playerPlaneC : aiPlaneC;
            else
                arraylist = indx == 0 ? playerPlane : aiPlane;
            int i = 0;
            do
            {
                if(i >= arraylist.size())
                    break;
                if(plane == arraylist.get(i))
                {
//                	int in = getFirstNonPlaceholder(i, (indx == 0)); //By PAL, be sure not to use a placeholder
                	//dlg[indx].wPlane.setSelected(in, true, false);
                	//plane = (ItemAir)arraylist.get(in);
                    dlg[indx].wPlane.setSelected(i, true, false);                   
                    break;
                }
                i++;
            } while(true);
        /*    
            if(i >= arraylist.size()) //By PAL, if I reached the end and didn't find it, it doesn't exist, choose one from 0
            {
            	int in = getFirstNonPlaceholder(0, (indx == 0)); //By PAL, be sure not to use a placeholder
            	dlg[indx].wPlane.setSelected(in, true, false);
            	plane = (ItemAir)arraylist.get(in);            	
            }*/
            
            String as[] = Aircraft.getWeaponsRegistered(plane.clazz);
            int j = 0;
            do
            {
                if(j >= as.length)
                    break;
                if(as[j].equals(weapon))
                {
                    fillComboWeapon(dlg[indx].wLoadout, plane, j);
                    break;
                }
                j++;
            } while(true);
            
			UpdateComboNum(indx);  //By PAL, original false but I have to update planes            
            fillComboCountry(dlg[indx].wCountry, indx);// ??
            selectCountries(indx); //By PAL, to select the corresponding Country in combobox
            
            if(indx == 0)
                toUserCfg();
        }

        public void toUserCfg()
        {
            if(indx != 0)
            {
                return;
            } else
            {
                UserCfg usercfg = World.cur().userCfg;
                usercfg.setSkin(plane.name, skin[0]);
                usercfg.setNoseart(plane.name, noseart[0]);
                usercfg.netPilot = pilot[0];
                usercfg.setWeapon(plane.name, weapon);
                usercfg.netNumberOn = numberOn[0];
                return;
            }
        }

        public void fromUserCfg()
        {
            if(indx != 0)
            {
                return;
            } else
            {
                UserCfg usercfg = World.cur().userCfg;
                skin[0] = usercfg.getSkin(plane.name);
                noseart[0] = usercfg.getNoseart(plane.name);
                pilot[0] = usercfg.netPilot;
                weapon = usercfg.getWeapon(plane.name);
                numberOn[0] = usercfg.netNumberOn;
                return;
            }
        }
//By PAL, original version for 8 flights
        public String prepareWing409(SectFile sectfile)
        {
            String s;
            if(indx <= 3)
                s = (OUR ? r010 : g010) + iwing;
            else
                s = (OUR ? g010 : r010) + iwing;
            int i = sectfile.sectionIndex("Wing");
            if(planes == 0)
            {
                sectfile.lineRemove(i, sectfile.varIndex(i, s));
                sectfile.sectionRemove(sectfile.sectionIndex(s));
                sectfile.sectionRemove(sectfile.sectionIndex(s + "_Way"));
                return null;
            }
            String s1 = null;
            int j;
            if(regiment != null)
            {
                s1 = regiment + "0" + iwing;
                sectfile.lineRemove(i, sectfile.varIndex(i, s));
                sectfile.lineAdd(i, s1);
                sectfile.sectionRename(sectfile.sectionIndex(s + "_Way"), s1 + "_Way");
                sectfile.sectionRename(sectfile.sectionIndex(s), s1);
                j = sectfile.sectionIndex(s1);
            } else
            {
                j = sectfile.sectionIndex(s);
                s1 = s;
            }
            sectfile.sectionClear(j);
            sectfile.lineAdd(j, "Planes " + planes);
            sectfile.lineAdd(j, "Skill " + skill);
            sectfile.lineAdd(j, "Class " + plane.className);
            sectfile.lineAdd(j, "Fuel " + fuel);
            if(weapon != null)
                sectfile.lineAdd(j, "weapons " + weapon);
            else
                sectfile.lineAdd(j, "weapons default");
            for(int k = 0; k < planes; k++)
            {
                if(skin[k] != null)
                    sectfile.lineAdd(j, "skin" + k + " " + skin[k]);
                if(noseart[k] != null)
                    sectfile.lineAdd(j, "noseart" + k + " " + noseart[k]);
                if(pilot[k] != null)
                    sectfile.lineAdd(j, "pilot" + k + " " + pilot[k]);
                if(!numberOn[k])
                    sectfile.lineAdd(j, "numberOn" + k + " 0");
            }

            return s1;
        }
//By PAL, original version for 8 flights
        public void prepereWay409(SectFile sectfile, String as[], String as1[])
        {
            int i = sectfile.sectionIndex(as1[indx] + "_Way");
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
            {
                SharedTokenizer.set(sectfile.line(i, k));
                String s = SharedTokenizer.next("");
                String s1 = s + " " + SharedTokenizer.next("") + " " + SharedTokenizer.next("") + " ";
                String s2 = wAltitude.getValue();
                if(wSituation.getSelected() != 0)
                {
                    int l = 500;
                    try
                    {
                        l = parseIntOwn(wAltitude.getValue());
                    }
                    catch(Exception exception) { }
                    if(wSituation.getSelected() == 1)
                    {
                        if(indx <= 3)
                            l += parseIntOwn(wPos.getValue());
                    } else
                    if(indx > 3)
                        l += parseIntOwn(wPos.getValue());
                    s2 = "" + l;
                }
                SharedTokenizer.next("");
                float f = (float)SharedTokenizer.next((plane.speedMin + plane.speedMax) / 2D, plane.speedMin, plane.speedMax);
                if("TAKEOFF".equals(s) || "LANDING".equals(s))
                {
                    s2 = "0";
                    f = 0.0F;
                }
                String s3 = SharedTokenizer.next((String)null);
                String s4 = SharedTokenizer.next((String)null);
            //By PAL, changed in 4.111
                if(s3 != null && (GUIQuick.class$com$maddox$il2$objects$air$TypeTransport != null ? GUIQuick.class$com$maddox$il2$objects$air$TypeTransport : (GUIQuick.class$com$maddox$il2$objects$air$TypeTransport = GUIQuick._mthclass$("com.maddox.il2.objects.air.TypeTransport"))).isAssignableFrom(plane.clazz))
                    s3 = null;                        
                if(s3 != null)
                {
                    for(int i1 = 0; i1 < 8; i1++)
                    {
                        if(!s3.equals(as[i1]))
                            continue;
                        s3 = as1[i1];
                        break;
                    }

                }
                if(s3 != null)
                {
                    if(s4 != null)
                        sectfile.line(i, k, s1 + s2 + " " + f + " " + s3 + " " + s4);
                    else
                        sectfile.line(i, k, s1 + s2 + " " + f + " " + s3);
                } else
                {
                    sectfile.line(i, k, s1 + s2 + " " + f);
                }
            }

        }

        public String prepareWing(SectFile sectfile)
        {
            String s;
            if(indx < 8)
            {
                s = OUR ? r01 : g01;
                if(indx < 4)
                    s = s + "0" + iwing;
                else
                    s = s + "1" + iwing;
            } else
            {
                s = OUR ? g01 : r01;
                if(indx < 12)
                    s = s + "0" + iwing;
                else
                    s = s + "1" + iwing;
            }
            int i = sectfile.sectionIndex("Wing");
            if(planes == 0)
            {
                sectfile.lineRemove(i, sectfile.varIndex(i, s));
                sectfile.sectionRemove(sectfile.sectionIndex(s));
                sectfile.sectionRemove(sectfile.sectionIndex(s + "_Way"));
                return null;
            }
            String s1 = null;
            int j;
            if(regiment != null)
            {
                if(indx > 3 && indx < 8 || indx > 11)
                    s1 = regiment + "1" + iwing;
                else
                    s1 = regiment + "0" + iwing;
                sectfile.lineRemove(i, sectfile.varIndex(i, s));
                sectfile.lineAdd(i, s1);
                sectfile.sectionRename(sectfile.sectionIndex(s + "_Way"), s1 + "_Way");
                sectfile.sectionRename(sectfile.sectionIndex(s), s1);
                j = sectfile.sectionIndex(s1);
            } else
            {
                j = sectfile.sectionIndex(s);
                s1 = s;
            }
            sectfile.sectionClear(j);
            sectfile.lineAdd(j, "Planes " + planes);
            sectfile.lineAdd(j, "Skill " + skill);
            sectfile.lineAdd(j, "Class " + plane.className);
            sectfile.lineAdd(j, "Fuel " + fuel);
            if(weapon != null)
                sectfile.lineAdd(j, "weapons " + weapon);
            else
                sectfile.lineAdd(j, "weapons default");
            for(int k = 0; k < planes; k++)
            {
                if(skin[k] != null)
                    sectfile.lineAdd(j, "skin" + k + " " + skin[k]);
                if(noseart[k] != null)
                    sectfile.lineAdd(j, "noseart" + k + " " + noseart[k]);
                if(pilot[k] != null)
                    sectfile.lineAdd(j, "pilot" + k + " " + pilot[k]);
                if(!numberOn[k])
                    sectfile.lineAdd(j, "numberOn" + k + " 0");
            }

            return s1;
        }

        public void prepereWay(SectFile sectfile, String as[], String as1[])
        {
            int i = sectfile.sectionIndex(as1[indx] + "_Way");
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
            {
                SharedTokenizer.set(sectfile.line(i, k));
                String s = SharedTokenizer.next("");
                if(s.startsWith("TRIGGERS"))
                {
                    String s1 = s + " " + SharedTokenizer.next("") + " " + SharedTokenizer.next("") + " " + SharedTokenizer.next("") + " " + SharedTokenizer.next("") + " ";
                    sectfile.line(i, k, s1);
                    continue;
                }
                String s2 = s + " " + SharedTokenizer.next("") + " " + SharedTokenizer.next("") + " ";
                String s3 = wAltitude.getValue();
                if(wSituation.getSelected() != 0)
                {
                    int l = 500;
                    try
                    {
                        l = Integer.parseInt(wAltitude.getValue());
                    }
                    catch(Exception exception) { }
                    if(wSituation.getSelected() == 1)
                    {
                        if(indx <= 7)
                            l += Integer.parseInt(wPos.getValue());
                    } else
                    if(indx > 7)
                        l += Integer.parseInt(wPos.getValue());
                    s3 = "" + l;
                }
                SharedTokenizer.next("");
                float f = (float)SharedTokenizer.next((plane.speedMin + plane.speedMax) / 2D, plane.speedMin, plane.speedMax);
                if(s.startsWith("TAKEOFF") || s.startsWith("LANDING"))
                {
                    s3 = "0";
                    f = 0.0F;
                }
                String s4 = SharedTokenizer.next((String)null);
                String s5 = SharedTokenizer.next((String)null);
                if(s4 != null && (GUIQuick.class$com$maddox$il2$objects$air$TypeTransport != null ? GUIQuick.class$com$maddox$il2$objects$air$TypeTransport : (GUIQuick.class$com$maddox$il2$objects$air$TypeTransport = GUIQuick._mthclass$("com.maddox.il2.objects.air.TypeTransport"))).isAssignableFrom(plane.clazz))
                    s4 = null;
                if(s4 != null)
                {
                    int i1 = 0;
                    do
                    {
                        if(i1 >= 8)
                            break;
                        if(s4.equals(as[i1]))
                        {
                            s4 = as1[i1];
                            break;
                        }
                        i1++;
                    } while(true);
                }
                if(s4 != null)
                {
                    if(s5 != null)
                        sectfile.line(i, k, s2 + s3 + " " + f + " " + s4 + " " + s5);
                    else
                        sectfile.line(i, k, s2 + s3 + " " + f + " " + s4);
                } else
                {
                    sectfile.line(i, k, s2 + s3 + " " + f);
                }
            }

        }

        public void setPlane(int i)
        {
            ArrayList arraylist = null;
            if(bPlaneArrestor)
                arraylist = indx != 0 ? aiPlaneC : playerPlaneC;
            else
                arraylist = indx != 0 ? aiPlane : playerPlane;
            plane = (ItemAir)arraylist.get(i);
            for(int j = 0; j < 4; j++)
            {
                skin[j] = null;
                noseart[j] = null;
            }

        }

        public void setWeapon(int i)
        {
            String as[] = Aircraft.getWeaponsRegistered(plane.clazz);
            weapon = as[i];
        }

        public void toAirArming()
        {
            GUIAirArming guiairarming = (GUIAirArming)GameState.get(55);
            guiairarming.quikPlayer = indx == 0;
            if(indx <= 7)
                guiairarming.quikArmy = OUR ? 1 : 2;
            else
                guiairarming.quikArmy = OUR ? 2 : 1;
            guiairarming.quikPlanes = planes;
            guiairarming.quikPlane = plane.name;
            guiairarming.quikWeapon = weapon;
            guiairarming.quikCurPlane = 0;
            guiairarming.quikRegiment = regiment;
            guiairarming.quikWing = iwing;
            guiairarming.quikFuel = fuel;
            for(int i = 0; i < 4; i++)
            {
                guiairarming.quikSkin[i] = skin[i];
                guiairarming.quikNoseart[i] = noseart[i];
                guiairarming.quikPilot[i] = pilot[i];
                guiairarming.quikNumberOn[i] = numberOn[i];
            }

            guiairarming.quikListPlane.clear();
            ArrayList arraylist = null;
            if(bPlaneArrestor)
                arraylist = indx != 0 ? aiPlaneC : playerPlaneC;
            else
                arraylist = indx != 0 ? aiPlane : playerPlane;
            for(int j = 0; j < arraylist.size(); j++)
                guiairarming.quikListPlane.add(((ItemAir)arraylist.get(j)).clazz);

        }

        public void fromAirArming()
        {
            GUIAirArming guiairarming = (GUIAirArming)GameState.get(55);
            ItemAir itemair = null;
            ArrayList arraylist = null;
            if(bPlaneArrestor)
                arraylist = indx != 0 ? aiPlaneC : playerPlaneC;
            else
                arraylist = indx != 0 ? aiPlane : playerPlane;
            int i = 0;
            do
            {
                if(i >= arraylist.size())
                    break;
                itemair = (ItemAir)arraylist.get(i);
                if(itemair.name.equals(guiairarming.quikPlane))
                    break;
                i++;
            } while(true);
            plane = itemair;
            weapon = guiairarming.quikWeapon;
            regiment = guiairarming.quikRegiment;
            fuel = guiairarming.quikFuel;
            for(int j = 0; j < 4; j++)
            {
                skin[j] = guiairarming.quikSkin[j];
                noseart[j] = guiairarming.quikNoseart[j];
                pilot[j] = guiairarming.quikPilot[j];
                numberOn[j] = guiairarming.quikNumberOn[j];
            }

            loaded();
        }

        public int indx;
        public int planes;
        public ItemAir plane;
        public String weapon;
        public String regiment;
        public int iwing;
        public String skin[] = {
            null, null, null, null
        };
        public String noseart[] = {
            null, null, null, null
        };
        public String pilot[] = {
            null, null, null, null
        };
        public boolean numberOn[] = {
            true, true, true, true
        };
        public int fuel;
        public int skill;
//By PAL, from 4.111
        public String origPlaneName;

        public ItemWing(int i)
        {
            indx = 0;
            planes = 0;
            plane = null;
            weapon = "default";
            regiment = null;
            iwing = 0;
            fuel = 100;
            skill = 1;
            if(i == 0)
                planes = 1;
            indx = i;
            iwing = i % 4;
            if(indx == 0)
                plane = (ItemAir)(bPlaneArrestor ? playerPlaneC : playerPlane).get(0);
            else
                plane = (ItemAir)(bPlaneArrestor ? aiPlaneC : aiPlane).get(0);
            if(indx <= 7)
                regiment = r01;
            else
                regiment = g01;
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

    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            validateEditableComboControl(wCldHeight);
            validateEditableComboControl(wPos);
            validateEditableComboControl(wAltitude);
            if(i != 2)
                return super.notify(gwindow, i, j);
                
            if(gwindow == bArmy)
            {
                if(OUR)
                    OUR = false;
                else
                    OUR = true;
                onArmyChange();
			    mapChanged(true); //By PAL, to update Missions in TargetList
			    			                                   
                //if(r01.equals("usa01"))
                if(music.equals("usa01"))  //By PAL
                    Main3D.menuMusicPlay(OUR ? "us" : "ja");
                else
                    Main3D.menuMusicPlay(OUR ? "ru" : "de");
                    
       			//By PAL, esta bien?             
                for(int k = 0; k < 16; k++)
                    if(k <= 7)
                        wing[k].regiment = OUR ? r01 : g01;
                    else
                        wing[k].regiment = OUR ? g01 : r01;
                updateCountries(); //By PAL, required because I have different ones                                            
                return true;
            }
            /* By PAL
            if(gwindow == wTarget)
            {
                if(wTarget.getSelected() == 4)
                    bScramble = true;
                else
                    bScramble = false;
                if(wTarget.getSelected() == 0)
                {
                    noneTarget = true;
                    wLevel.showWindow();
                } else
                {
                    noneTarget = false;
                    wLevel.hideWindow();
                }
                return true;
            }*/           
            if(gwindow == wTargetList) //By PAL, new selector
            {
				processTargetList();
                return true;
            }
                         
            if(gwindow == bExit)
            {
                GUIQuick.setQMB(false);
                save(true);
                Main.stateStack().pop();
                return true;
            }
            
            if(gwindow == bDiff)
            {
                Main.stateStack().push(17);
                return true;
            }
            
            if(gwindow == bFly)
                if(bNoAvailableMissions)
                {
                    return true;
                } else
                {
               	    validateEditableComboControl(wCldHeight);
                    validateEditableComboControl(wPos);
                    validateEditableComboControl(wAltitude);
                    startQuickMissionMulti();
                    return true;
                }
                
            if(gwindow == bReset)
            {
            	mapChanged(true); //By PAL
                setDefaultValues();            	           	                
                return true;
            }
            
            if(gwindow == bStat)
            {
                Main.stateStack().push(71);
                return true;
            }
            
            if(gwindow == bNext)
            {
                bFirst = false;
                showHide();
                return true;
            }
            
            if(gwindow == bBack)
            {
                bFirst = true;
                showHide();
                return true;
            }
            
            if(gwindow == bLoad)
            {
                ssect = null;
                Main.stateStack().push(25);
                return true;
            }
            
            if(gwindow == bSave)
            {
                validateEditableComboControl(wCldHeight); //By PAL, necessary?
                validateEditableComboControl(wPos);
                validateEditableComboControl(wAltitude);
                save(false);
                Main.stateStack().push(24);
                return true;
            }
            
            if(gwindow == wPlaneList)
            { //By PAL, routines added to avoid loosing the planes
               /* IniFile inifile = Config.cur.ini;
                inifile.set("QMB", "PlaneList", wPlaneList.getSelected());
                GUIQuick.setPlaneList(wPlaneList.getSelected());
                fillArrayPlanes();
                for(int l = 0; l < 16; l++)
                {
                    fillComboPlane(dlg[l].wPlane, l == 0);
                    int i1 = dlg[l].wPlane.getSelected();
                    if(i1 == 0)
                        dlg[l].wPlane.setSelected(1, false, false);
                    dlg[l].wPlane.setSelected(0, true, true);
                }  */
                //By PAL, first save current
                ArrayList OldPlanes;
                OldPlanes = new ArrayList(16);
                int [] Planes = new int [16];
                int [] Weapons = new int [16];
                for(int l = 0; l < 16; l++)
                {
		            ArrayList arraylist = null;
		            if(bPlaneArrestor)
		                arraylist = l == 0 ? playerPlaneC : aiPlaneC;
		            else
		                arraylist = l == 0 ? playerPlane : aiPlane;
		                
		            ItemAir plane = (ItemAir)arraylist.get(dlg[l].wPlane.getSelected());
		            Planes[l] = parseIntOwn(dlg[l].wNum.getValue());//By PAL, If there is one plane at least 
		            OldPlanes.add(plane.name);	//By PAL, because problem with set
		            Weapons[l] = dlg[l].wLoadout.getSelected();	            
                }
                IniFile inifile = Config.cur.ini;
                inifile.set("QMB", "PlaneList", wPlaneList.getSelected());
                GUIQuick.setPlaneList(wPlaneList.getSelected());                                
                fillArrayPlanes();
                for(int l = 0; l < 16; l++)
                {
                    fillComboPlane(dlg[l].wPlane, l == 0);
                    //int i1 = dlg[l].wPlane.getSelected();
                    //if(i1 == 0)
                    //    dlg[l].wPlane.setSelected(1, false, false);
                    //dlg[l].wPlane.setSelected(0, true, true);
                }                                             
                for(int l = 0; l < 16; l++)
                {
		            ArrayList arraylist = null;
		            if(bPlaneArrestor)
		                arraylist = l == 0 ? playerPlaneC : aiPlaneC;
		            else
		                arraylist = l == 0 ? playerPlane : aiPlane;
		            int it = 0;
		            do
		            {
		                if(it >= arraylist.size())
		                    break;
		                String s = (String)OldPlanes.get(l);
		                ItemAir plane = (ItemAir)arraylist.get(it);
		                if(plane.name.equalsIgnoreCase(s))
		                    break;
		                it++;
		            } while(true);
		            dlg[l].wPlane.setSelected(it, false, false);
		            if (Weapons[l] > -1 && Weapons[l] < dlg[l].wLoadout.size())
		            	dlg[l].wLoadout.setSelected(Weapons[l], false, false);		            
					//By PAL, to avoid wrong assignments after re-sorting
		            dlg[l].wNum.setSelected(Planes[l], false, false);
		            UpdateComboNum(l);			                                   
                }
                return true;
            }
            
            if(gwindow == sParachuteOn)
            {           	
				return true;     	
            }
                       
            if(gwindow == bFMB)
            {
		        //ArrayList arraylist = getAvailableMissionsS(getMapName(), getMapName() + getArmyString() + _targetKey[wTarget.getSelected()] + (wTarget.getSelected() != 0 ? "" : noneTargetSuffix[wLevel.getSelected()]));
		        //Random random = new Random();
		        //int n = (arraylist.size() > 0) ? random.nextInt(arraylist.size()) : 0; //By PAL
 		        //String s = "Missions/Quick/" + getMapName() + "/" + (String)arraylist.get(n);               
                //String s = "Missions/Quick/" + getMapName() + "/" + getMapName() + wTargetList.getValue() + ".mis";        	
       			save(false); //By PAL, to save settings of Mission before leaving    				
        		Main.stateStack().push(18);
       			if(exisstFile(currentMissionName)) //By PAL, it should exist
       				PlMission.doLoadMissionFile(currentMissionName, currentMissionName);        		        			
                return true;
            }
            
            if(gwindow == bBrief)
            {
	            if (sDesc.length() > 2)
	            {
					new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, "Mission Briefing", sDesc, 3, 0.0F);
				}
                return true;
            }           
                        
            if(gwindow == wMap)
            {
/*
            SectFile sectfile = new SectFile(currentMissionName, 0);                
            String s1 = sectfile.get("MAIN", "MAP", (String)null);
            if(s1 != null)
            {
                IniFile inifile = new IniFile("maps/" + s1, 0);
				String s2 = inifile.get("MAP", "COLORMAP", (String)null);
				if(s2 == null)//By PAL, check if the map exists
				{
					GWindowMessageBox gwindowmessagebox =
						new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, "Warning",
							"Map '" + s1 + "' not present in your IL-2 installation!", 3, 0.0F);
				}
				                  
            } */
            	
                onArmyChange();
                mapChanged(false);
                //v4101 defaultRegiments(); //Why? To define if Japan or Germany, etc.                                                           
//BY PAL, last changed: updateCountries();					    	                             
		        if(Main.cur() != null)
		        {
		            Main.cur();
		            if(Main.stateStack() != null)
		            {
		                Main.cur();
		                if(Main.stateStack().peek() != null)
		                {
		                    Main.cur();
		                    if(Main.stateStack().peek().id() == 14)
		                        //if(r01.equals("usa01"))
		                        if(music.equals("usa01"))  //By PAL
		                            Main3D.menuMusicPlay(OUR ? "us" : "ja");
		                        else
		                            Main3D.menuMusicPlay(OUR ? "ru" : "de");
		                }
		            }
		        }		                		        
            	return true;
            } else
            {
                return super.notify(gwindow, i, j);
            }            
        }
/*
        public void render()
        {
            super.render();
            setCanvasColorWHITE();
            GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(630F), x1024(924F), 2.5F);
            GUISeparate.draw(this, GColor.Gray, x1024(150F), y1024(640F), 1.0F, x1024(80F));
            GUISeparate.draw(this, GColor.Gray, x1024(720F), y1024(640F), 1.0F, x1024(80F));
            GUISeparate.draw(this, GColor.Gray, x1024(875F), y1024(640F), 1.0F, x1024(80F));
            GUISeparate.draw(this, GColor.Gray, x1024(457F), y1024(640F), 1.0F, x1024(46F));
            GUISeparate.draw(this, GColor.Gray, x1024(567F), y1024(640F), 1.0F, x1024(46F));
            GUISeparate.draw(this, GColor.Gray, x1024(457F), y1024(686F), x1024(30F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(537F), y1024(686F), x1024(30F), 2.0F);
            setCanvasFont(0);
            draw(x1024(0.0F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.BAC"));
            draw(x1024(143F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.LOD"));
            draw(x1024(285F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.SAV"));
            if(!bNoAvailableMissions)
                draw(x1024(427F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.FLY"));
            draw(x1024(569F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.DIF"));
            if(bFirst)
            {
                GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(500F), x1024(924F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(285F), x1024(924F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(864F), y1024(130F), x1024(94F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(864F), y1024(317F), x1024(94F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(958F), y1024(110F), 2.0F, x1024(56F));
                GUISeparate.draw(this, GColor.Gray, x1024(958F), y1024(317F), 2.0F, x1024(28F));
                draw(x1024(710F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.RES"));
                draw(x1024(853F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.GFC"));
                setCanvasFont(1);
                draw(x1024(38F), y1024(13F), x1024(602F), M(2.0F), 0, localize("quick.YOU"));
                draw(x1024(38F), y1024(108F), x1024(602F), M(2.0F), 0, localize("quick.FRI"));
                draw(x1024(38F), y1024(291F), x1024(602F), M(2.0F), 0, localize("quick.ENM"));
                setCanvasFont(0);
                draw(x1024(48F), y1024(38F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                draw(x1024(144F), y1024(38F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                draw(x1024(318F), y1024(38F), x1024(274F), M(2.0F), 0, localize("quick.PLA"));
                draw(x1024(606F), y1024(38F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
                draw(x1024(48F), y1024(143F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                draw(x1024(144F), y1024(143F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                draw(x1024(318F), y1024(143F), x1024(274F), M(2.0F), 0, localize("quick.PLA"));
                draw(x1024(606F), y1024(143F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
                draw(x1024(48F), y1024(320F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                draw(x1024(144F), y1024(320F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                draw(x1024(318F), y1024(320F), x1024(274F), M(2.0F), 0, localize("quick.PLA"));
                draw(x1024(606F), y1024(320F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
                draw(x1024(606F), y1024(508F), x1024(192F), M(2.0F), 0, localize("quick.MAP"));
                draw(x1024(606F), y1024(542F), x1024(192F), M(2.0F), 0, localize("quick.PLALST"));
                draw(x1024(318F), y1024(508F), x1024(100F), M(2.0F), 0, localize("quick.ALT"));
                draw(x1024(318F), y1024(542F), x1024(100F), M(2.0F), 0, localize("quick.SIT"));
                draw(x1024(318F), y1024(576F), x1024(100F), M(2.0F), 0, localize("quick.POS"));
                draw(x1024(48F), y1024(508F), x1024(100F), M(2.0F), 0, localize("quick.TAR"));
                draw(x1024(48F), y1024(576F), x1024(100F), M(2.0F), 0, localize("quick.DEF"));
                if(noneTarget)
                    draw(x1024(48F), y1024(542F), x1024(100F), M(2.0F), 0, localize("quick.+/-"));
                draw(x1024(320F), y1024(118F), x1024(528F), y1024(32F), 2, localize("quick.ASET"));
                draw(x1024(320F), y1024(298F), x1024(528F), y1024(32F), 2, localize("quick.ASET"));
                setCanvasFont(0);
                if(OUR)
                    draw(x1024(566F), y1024(21F), x1024(362F), M(2.0F), 2, localize("quick.SEL_Allies"));
                else
                    draw(x1024(566F), y1024(21F), x1024(362F), M(2.0F), 2, localize("quick.SEL_Axis"));
            } else
            {
                GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(500F), x1024(924F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(215F), x1024(924F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(864F), y1024(288F), x1024(94F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(958F), y1024(232F), 2.0F, x1024(112F));
                setCanvasFont(0);
                draw(x1024(318F), y1024(508F), x1024(120F), M(2.0F), 0, localize("quick.WEA"));
                draw(x1024(606F), y1024(508F), x1024(120F), M(2.0F), 0, localize("quick.CLD"));
                draw(x1024(48F), y1024(508F), x1024(100F), M(2.0F), 0, localize("quick.TIM"));
                draw(x1024(213F), y1024(508F), x1024(8F), M(2.0F), 1, ":");
                draw(x1024(853F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.STAT"));
                draw(x1024(48F), y1024(37F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                draw(x1024(144F), y1024(37F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                draw(x1024(318F), y1024(37F), x1024(274F), M(2.0F), 0, localize("quick.PLA"));
                draw(x1024(606F), y1024(37F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
                draw(x1024(48F), y1024(320F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                draw(x1024(144F), y1024(320F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                draw(x1024(318F), y1024(320F), x1024(274F), M(2.0F), 0, localize("quick.PLA"));
                draw(x1024(606F), y1024(320F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
                draw(x1024(320F), y1024(274F), x1024(528F), y1024(32F), 2, localize("quick.ASET"));
                setCanvasFont(1);
                draw(x1024(38F), y1024(13F), x1024(602F), M(2.0F), 0, localize("quick.ADDFRI"));
                draw(x1024(38F), y1024(291F), x1024(602F), M(2.0F), 0, localize("quick.ADDENM"));
            }
        }

        public void setPosSize()
        {
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            bExit.setPosC(x1024(85F), y1024(689F));
            bBack.setPosC(x1024(85F), y1024(689F));
            bLoad.setPosC(x1024(228F), y1024(689F));
            bSave.setPosC(x1024(370F), y1024(689F));
            bFly.setPosC(x1024(512F), y1024(689F));
            bStat.setPosC(x1024(939F), y1024(689F));
            bReset.setPosC(x1024(796F), y1024(689F));
            bArmy.setPosC(x1024(966F), y1024(39F));
            bNext.setPosC(x1024(939F), y1024(689F));
            bDiff.setPosC(x1024(654F), y1024(689F));
            wAltitude.setPosSize(x1024(432F), y1024(508F), x1024(160F), M(1.7F));
            wSituation.setPosSize(x1024(432F), y1024(542F), x1024(160F), M(1.7F));
            wPos.setPosSize(x1024(432F), y1024(576F), x1024(160F), M(1.7F));
            wTarget.setPosSize(x1024(142F), y1024(508F), x1024(160F), M(1.7F));
            wLevel.setPosSize(x1024(142F), y1024(542F), x1024(160F), M(1.7F));
            wDefence.setPosSize(x1024(142F), y1024(576F), x1024(160F), M(1.7F));
            wMap.setPosSize(x1024(688F), y1024(508F), x1024(256F), M(1.7F));
            wPlaneList.setPosSize(x1024(688F), y1024(542F), x1024(256F), M(1.7F));
            wWeather.setPosSize(x1024(432F), y1024(508F), x1024(160F), M(1.7F));
            wCldHeight.setPosSize(x1024(784F), y1024(508F), x1024(160F), M(1.7F));
            wTimeHour.setPosSize(x1024(132F), y1024(508F), x1024(80F), M(1.7F));
            wTimeMins.setPosSize(x1024(222F), y1024(508F), x1024(80F), M(1.7F));
            dlg[0].wNum.setPosSize(x1024(48F), y1024(70F), x1024(80F), M(1.7F));
            dlg[0].wSkill.setPosSize(x1024(144F), y1024(70F), x1024(160F), M(1.7F));
            dlg[0].wPlane.setPosSize(x1024(318F), y1024(70F), x1024(274F), M(1.7F));
            dlg[0].wLoadout.setPosSize(x1024(609F), y1024(70F), x1024(332F), M(1.7F));
            dlg[0].bArming.setPosC(x1024(959F), y1024(84F));
            for(int i = 0; i < 7; i++)
                if(i < 3)
                {
                    dlg[i + 1].wNum.setPosSize(x1024(48F), y1024(175 + 34 * i), x1024(80F), M(1.7F));
                    dlg[i + 1].wSkill.setPosSize(x1024(144F), y1024(175 + 34 * i), x1024(160F), M(1.7F));
                    dlg[i + 1].wPlane.setPosSize(x1024(318F), y1024(175 + 34 * i), x1024(274F), M(1.7F));
                    dlg[i + 1].wLoadout.setPosSize(x1024(609F), y1024(175 + 34 * i), x1024(332F), M(1.7F));
                    dlg[i + 1].bArming.setPosC(x1024(959F), y1024((175 + 34 * i + 16) - 2));
                } else
                {
                    dlg[i + 1].wNum.setPosSize(x1024(48F), y1024(104 + 34 * (i - 4)), x1024(80F), M(1.7F));
                    dlg[i + 1].wSkill.setPosSize(x1024(144F), y1024(104 + 34 * (i - 4)), x1024(160F), M(1.7F));
                    dlg[i + 1].wPlane.setPosSize(x1024(318F), y1024(104 + 34 * (i - 4)), x1024(274F), M(1.7F));
                    dlg[i + 1].wLoadout.setPosSize(x1024(609F), y1024(104 + 34 * (i - 4)), x1024(332F), M(1.7F));
                    dlg[i + 1].bArming.setPosC(x1024(959F), y1024((104 + 34 * (i - 4) + 16) - 2));
                }

            for(int j = 0; j < 8; j++)
                if(j < 4)
                {
                    dlg[j + 8].wNum.setPosSize(x1024(48F), y1024(352 + 34 * j), x1024(80F), M(1.7F));
                    dlg[j + 8].wSkill.setPosSize(x1024(144F), y1024(352 + 34 * j), x1024(160F), M(1.7F));
                    dlg[j + 8].wPlane.setPosSize(x1024(318F), y1024(352 + 34 * j), x1024(274F), M(1.7F));
                    dlg[j + 8].wLoadout.setPosSize(x1024(609F), y1024(352 + 34 * j), x1024(332F), M(1.7F));
                    dlg[j + 8].bArming.setPosC(x1024(959F), y1024((352 + 34 * j + 16) - 2));
                } else
                {
                    dlg[j + 8].wNum.setPosSize(x1024(48F), y1024(352 + 34 * (j - 4)), x1024(80F), M(1.7F));
                    dlg[j + 8].wSkill.setPosSize(x1024(144F), y1024(352 + 34 * (j - 4)), x1024(160F), M(1.7F));
                    dlg[j + 8].wPlane.setPosSize(x1024(318F), y1024(352 + 34 * (j - 4)), x1024(274F), M(1.7F));
                    dlg[j + 8].wLoadout.setPosSize(x1024(609F), y1024(352 + 34 * (j - 4)), x1024(332F), M(1.7F));
                    dlg[j + 8].bArming.setPosC(x1024(959F), y1024((352 + 34 * (j - 4) + 16) - 2));
                }

        }*/

        public void render() //By PAL, new GUI
        {
            super.render();
            setCanvasColorWHITE();
            GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(633F/*630F*/), x1024(924F), 2.0F); //2.5F);
             //By PAL, lower it a little for the label
            GUISeparate.draw(this, GColor.Gray, x1024(150F), y1024(640F), 1.0F, y1024(80F));//x1024
            GUISeparate.draw(this, GColor.Gray, x1024(720F), y1024(640F), 1.0F, y1024(80F));//x1024
            GUISeparate.draw(this, GColor.Gray, x1024(875F), y1024(640F), 1.0F, y1024(80F));//x1024
            GUISeparate.draw(this, GColor.Gray, x1024(457F), y1024(640F), 1.0F, y1024(46F));//x1024
            GUISeparate.draw(this, GColor.Gray, x1024(567F), y1024(640F), 1.0F, y1024(46F));//x1024
            GUISeparate.draw(this, GColor.Gray, x1024(457F), y1024(686F), x1024(30F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(537F), y1024(686F), x1024(30F), 2.0F);            
            setCanvasFont(0);
            draw(x1024(0.0F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.BAC"));
            draw(x1024(143F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.LOD"));
            draw(x1024(285F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.SAV"));
            if(!bNoAvailableMissions)
                draw(x1024(427F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.FLY"));
            draw(x1024(569F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.DIF"));

            draw(x1024(10F), y1024(10F), x1024(590F), M(2.0F), 2, localize(QMBVer)); //By PAL
            //draw(x1024(870F), y1024(542F), x1024(170F), M(2.0F), 0, localize(BriefLabel));//quick.BLD")); //By PAL, FMB
            //draw(x1024(959F), y1024(560F), x1024(170F), M(2.0F), 0, localize("Brief")); //By PAL              
            //draw(x1024(960F), y1024(587F), x1024(170F), M(2.0F), 0, localize(FMBLabel)); //By PAL
            draw(x1024(870F), y1024(584F), x1024(170F), M(2.0F), 0, localize(FMBLabel));//quick.BLD")); //By PAL, FMB             
            
            draw(x1024(711F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.RES"));    //By PAL, reset position                      
            if(bFirst)
            {
           	    GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(500F), x1024(924F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(285F), x1024(924F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(864F), y1024(130F), x1024(94F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(864F), y1024(317F), x1024(94F), 2.0F);
                GUISeparate.draw(this, GColor.Gray, x1024(958F), y1024(110F), 2.0F, y1024(56F));//x1024
                GUISeparate.draw(this, GColor.Gray, x1024(958F), y1024(317F), 2.0F, y1024(28F));//x1024
                draw(x1024(853F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.GFC"));
                setCanvasFont(1);
                draw(x1024(38F), y1024(13F), x1024(602F), M(2.0F), 0, localize("quick.YOU"));
                draw(x1024(38F), y1024(108F), x1024(602F), M(2.0F), 0, localize("quick.FRI"));
                draw(x1024(38F), y1024(291F), x1024(602F), M(2.0F), 0, localize("quick.ENM"));
                setCanvasFont(0);
                /*draw(x1024(48F), y1024(38F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                draw(x1024(144F), y1024(38F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                draw(x1024(318F), y1024(38F), x1024(274F), M(2.0F), 0, localize("quick.PLA"));
                draw(x1024(606F), y1024(38F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));               
                draw(x1024(48F), y1024(143F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                draw(x1024(144F), y1024(143F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                draw(x1024(318F), y1024(143F), x1024(274F), M(2.0F), 0, localize("quick.PLA"));
                draw(x1024(606F), y1024(143F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
                draw(x1024(48F), y1024(320F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                draw(x1024(144F), y1024(320F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                draw(x1024(318F), y1024(320F), x1024(274F), M(2.0F), 0, localize("quick.PLA"));
                draw(x1024(606F), y1024(320F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
                draw(x1024(606F), y1024(508F), x1024(192F), M(2.0F), 0, localize("quick.MAP"));
                draw(x1024(606F), y1024(542F), x1024(192F), M(2.0F), 0, localize("quick.PLALST"));
                draw(x1024(318F), y1024(508F), x1024(100F), M(2.0F), 0, localize("quick.ALT"));
                draw(x1024(318F), y1024(542F), x1024(100F), M(2.0F), 0, localize("quick.SIT"));
                draw(x1024(318F), y1024(576F), x1024(100F), M(2.0F), 0, localize("quick.POS"));
                draw(x1024(48F), y1024(508F), x1024(100F), M(2.0F), 0, localize("quick.TAR"));
                draw(x1024(48F), y1024(576F), x1024(100F), M(2.0F), 0, localize("quick.DEF"));*/
//By PAL                     
            draw(x1024(48F), y1024(38F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
            draw(x1024(120F), y1024(38F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
            draw(x1024(247F), y1024(38F), x1024(274F), M(2.0F), 0, localize("quick.PLA"));
            draw(x1024(510F), y1024(38F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
            draw(x1024(752F), y1024(38F), x1024(196F), M(2.0F), 0, localize("Country")); //By PAL, to show Country Label
            draw(x1024(48F), y1024(143F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
            draw(x1024(120F), y1024(143F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
            draw(x1024(247F), y1024(143F), x1024(274F), M(2.0F), 0, localize("quick.PLA"));
            draw(x1024(510F), y1024(143F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
            draw(x1024(752F), y1024(143F), x1024(196F), M(2.0F), 0, localize("Country"));            
            draw(x1024(48F), y1024(320F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
            draw(x1024(120F), y1024(320F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
            draw(x1024(247F), y1024(320F), x1024(274F), M(2.0F), 0, localize("quick.PLA"));
            draw(x1024(510F), y1024(320F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
            draw(x1024(752F), y1024(320F), x1024(196F), M(2.0F), 0, localize("Country"));
                        
                draw(x1024(606F), y1024(508F), x1024(192F), M(2.0F), 0, localize("quick.MAP"));
                //draw(x1024(606F), y1024(542F), x1024(192F), M(2.0F), 0, localize("quick.PLALST"));
                draw(x1024(606F), y1024(542F), x1024(192F), M(2.0F), 0, localize("Missions"));
                //draw(x1024(606F), y1024(576F), x1024(192F), M(2.0F), 0, localize("quick.PLALST"));                
                draw(x1024(606F), y1024(576F), x1024(192F), M(2.0F), 0, localize("quick.DEF")); //By PAL, moved to the right
                draw(x1024(318F), y1024(508F), x1024(100F), M(2.0F), 0, localize("quick.ALT"));
                draw(x1024(318F), y1024(542F), x1024(100F), M(2.0F), 0, localize("quick.SIT"));
                draw(x1024(318F), y1024(576F), x1024(100F), M(2.0F), 0, localize("quick.POS"));
                draw(x1024(48F), y1024(508F), x1024(100F), M(2.0F), 0, localize("quick.TAR"));
                //draw(x1024(48F), y1024(576F), x1024(100F), M(2.0F), 0, localize("quick.DEF"));
                draw(x1024(48F), y1024(576F), x1024(100F), M(2.0F), 0, localize("quick.PLALST")); //By PAL, moved to the left
                /*if(noneTarget)
                    draw(x1024(48F), y1024(542F), x1024(100F), M(2.0F), 0, localize("quick.+/-"));*/
                if (sDesc.length() > 2)
                	draw(x1024(48F), y1024(542F), x1024(100F), M(2.0F), 0, localize(BriefLabel)); //By PAL, replace the other one
				else
                	draw(x1024(48F), y1024(542F), x1024(100F), M(2.0F), 0, localize(NoBriefLabel)); //By PAL, replace the other one
                
                draw(x1024(320F), y1024(118F), x1024(528F), y1024(32F), 2, localize("quick.ASET"));
                draw(x1024(320F), y1024(298F), x1024(528F), y1024(32F), 2, localize("quick.ASET"));
                setCanvasFont(0);
                if(OUR)
                    draw(x1024(566F), y1024(21F), x1024(362F), M(2.0F), 2, localize("quick.SEL_Allies"));
                else
                    draw(x1024(566F), y1024(21F), x1024(362F), M(2.0F), 2, localize("quick.SEL_Axis"));
                
                
                //if (sShort == "")
                { //By PAL, now show always this info
	            	if (b16Flights) //By PAL
	            		draw(x1024(48F), y1024(609F), x1024(196F), M(2.0F), 0, localize("This is a 16 Flights Mission (v4.10m native)."));   //By PAL 
	            	else	
	            		draw(x1024(48F), y1024(609F), x1024(196F), M(2.0F), 0, localize("This is an 8 Flights Mission only (v4.09m legacy)."));   //By PAL                	
                }    
               /* else
           		draw(x1024(48F), y1024(609F), x1024(196F), M(2.0F), 0, localize("Mission: " + sShort));   //By PAL */               	               	    
            } else
            {
            	if (b16Flights) //By PAL, only if 16 Flights
            	{
	                GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(215F), x1024(924F), 2.0F);
	                GUISeparate.draw(this, GColor.Gray, x1024(864F), y1024(288F), x1024(94F), 2.0F);
	                GUISeparate.draw(this, GColor.Gray, x1024(958F), y1024(232F), 2.0F, y1024(112F)); //x1024
	                
                draw(x1024(48F), y1024(37F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                draw(x1024(120F), y1024(37F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                draw(x1024(247F), y1024(37F), x1024(274F), M(2.0F), 0, localize("quick.PLA"));
                draw(x1024(510F), y1024(37F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
            	draw(x1024(752F), y1024(37F), x1024(196F), M(2.0F), 0, localize("Country"));   //By PAL                   
                draw(x1024(48F), y1024(320F), x1024(82F), M(2.0F), 0, localize("quick.NUM"));
                draw(x1024(120F), y1024(320F), x1024(160F), M(2.0F), 0, localize("quick.SKI"));
                draw(x1024(247F), y1024(320F), x1024(274F), M(2.0F), 0, localize("quick.PLA"));
                draw(x1024(510F), y1024(320F), x1024(196F), M(2.0F), 0, localize("quick.TNT"));
            	draw(x1024(752F), y1024(320F), x1024(196F), M(2.0F), 0, localize("Country"));     //By PAL            
                draw(x1024(320F), y1024(274F), x1024(528F), y1024(32F), 2, localize("quick.ASET"));
                	                            		
            	}
	            GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(500F), x1024(924F), 2.0F);            	
                setCanvasFont(0);
                draw(x1024(318F), y1024(508F), x1024(120F), M(2.0F), 0, localize("quick.WEA"));
                draw(x1024(606F), y1024(508F), x1024(120F), M(2.0F), 0, localize("quick.CLD"));
                draw(x1024(48F), y1024(508F), x1024(100F), M(2.0F), 0, localize("quick.TIM"));
                draw(x1024(213F), y1024(508F), x1024(8F), M(2.0F), 1, ":");             
                draw(x1024(853F), y1024(633F), x1024(170F), M(2.0F), 1, localize("quick.STAT"));
                
				draw(x1024(/*595*/48F), y1024(/*560*/556F), x1024(170F), y1024(32F), 0, localize("Parachutes On")); //By PAL                

            	if (b16Flights) //By PAL, only if 16 Flights
            	{               
	                setCanvasFont(1);
	                draw(x1024(38F), y1024(13F), x1024(602F), M(2.0F), 0, localize("quick.ADDFRI"));
	                draw(x1024(38F), y1024(291F), x1024(602F), M(2.0F), 0, localize("quick.ADDENM"));
            	}
            	setCanvasFont(0);
            	if (b16Flights)
            		draw(x1024(48F), y1024(609F), x1024(196F), M(2.0F), 0, localize("This is a 16 Flights Mission (v4.10m native)."));   //By PAL 
            	else	
            		draw(x1024(48F), y1024(609F), x1024(196F), M(2.0F), 0, localize("This is an 8 Flights Mission only (v4.09m legacy). That's why you don't see other planes in this screen."));   //By PAL 
            }
        }

        public void setPosSize()
        {
        	//+++ TODO: 4.12 changed code +++
            float f = 16F;
            float f1 = root.win.dx / root.win.dy;
            if(f1 < 1.0F)
                f /= 2.0F;
            //--- TODO: 4.12 changed code ---
            
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            bExit.setPosC(x1024(85F), y1024(689F));
            bBack.setPosC(x1024(85F), y1024(689F));
            bLoad.setPosC(x1024(228F), y1024(689F));
            bSave.setPosC(x1024(370F), y1024(689F));
            bFly.setPosC(x1024(512F), y1024(689F));
            bStat.setPosC(x1024(939F), y1024(689F));
            bReset.setPosC(x1024(796F), y1024(689F));
            
            //+++ TODO: 4.12 changed code +++
//            bArmy.setPosC(x1024(966F), y1024(39F));
            bArmy.setPosC(x1024(966F), y1024(23F + f));
            //--- TODO: 4.12 changed code ---
            
            bDiff.setPosC(x1024(654F), y1024(689F));
            bNext.setPosC(x1024(939F), y1024(689F));
			bFMB.setPosC(x1024(939F), y1024(600F)); //By PAL
            //bBrief.setPosC(x1024(939F), y1024(/*537*/553F)); //By PAL
            
            bBrief.setPosC(x1024(120F), y1024(555F)); //By PAL
            			
			sParachuteOn.setPosC(x1024(/*748*/201F), y1024(/*576*/572F)); //By PAL, Parachutes
            //sParachuteOn.setPosC(x1024(748F), y1024(574F)); //By PAL, Parachutes
            	           						
            wAltitude.setPosSize(x1024(432F), y1024(508F), x1024(160F), M(1.7F));
            wSituation.setPosSize(x1024(432F), y1024(542F), x1024(160F), M(1.7F));
            wPos.setPosSize(x1024(432F), y1024(576F), x1024(160F), M(1.7F));
            wTarget.setPosSize(x1024(142F), y1024(508F), x1024(160F), M(1.7F));
            wLevel.setPosSize(x1024(142F), y1024(542F), x1024(160F), M(1.7F));
            //wDefence.setPosSize(x1024(142F), y1024(576F), x1024(160F), M(1.7F));            
            wDefence.setPosSize(x1024(688F), y1024(576F), x1024(85F), M(1.7F)); //By PAL, changed to the right, narrow
            wMap.setPosSize(x1024(688F), y1024(508F), x1024(261F), M(1.7F));            
            //wPlaneList.setPosSize(x1024(688F), y1024(542F), x1024(256F), M(1.7F));
            wPlaneList.setPosSize(x1024(142F), y1024(576F), x1024(110F), M(1.7F)); //By PAL, changed to the left
            wTargetList.setPosSize(x1024(688F), y1024(542F), x1024(/*256*/166F), M(1.7F));
            
            wWeather.setPosSize(x1024(432F), y1024(508F), x1024(160F), M(1.7F));
            wCldHeight.setPosSize(x1024(784F), y1024(508F), x1024(160F), M(1.7F));
            wTimeHour.setPosSize(x1024(132F), y1024(508F), x1024(80F), M(1.7F));
            wTimeMins.setPosSize(x1024(222F), y1024(508F), x1024(80F), M(1.7F));
            dlg[0].wNum.setPosSize(x1024(48F), y1024(70F), x1024(48F), M(1.7F));
            dlg[0].wSkill.setPosSize(x1024(120F), y1024(70F), x1024(92F), M(1.7F));
            dlg[0].wPlane.setPosSize(x1024(247F), y1024(70F), x1024(227F), M(1.7F));
            dlg[0].wLoadout.setPosSize(x1024(510F), y1024(70F), x1024(215F), M(1.7F));
        	dlg[0].wCountry.setPosSize(x1024(752F), y1024(70F), x1024(165F), M(1.7F)); 
        	
        	//+++ TODO: 4.12 changed code +++
//            dlg[0].bArming.setPosC(x1024(959F), y1024(84F));
            dlg[0].bArming.setPosC(x1024(959F), y1024(68F + f));
            //--- TODO: 4.12 changed code ---
            
            /*for(int i = 0; i < 7; i++)
                if(i < 3)
                {
                    dlg[i + 1].wNum.setPosSize(x1024(48F), y1024(175 + 34 * i), x1024(80F), M(1.7F));
                    dlg[i + 1].wSkill.setPosSize(x1024(144F), y1024(175 + 34 * i), x1024(160F), M(1.7F));
                    dlg[i + 1].wPlane.setPosSize(x1024(318F), y1024(175 + 34 * i), x1024(274F), M(1.7F));
                    dlg[i + 1].wLoadout.setPosSize(x1024(609F), y1024(175 + 34 * i), x1024(332F), M(1.7F));
                    dlg[i + 1].bArming.setPosC(x1024(959F), y1024((175 + 34 * i + 16) - 2));
                } else
                {
                    dlg[i + 1].wNum.setPosSize(x1024(48F), y1024(104 + 34 * (i - 4)), x1024(80F), M(1.7F));
                    dlg[i + 1].wSkill.setPosSize(x1024(144F), y1024(104 + 34 * (i - 4)), x1024(160F), M(1.7F));
                    dlg[i + 1].wPlane.setPosSize(x1024(318F), y1024(104 + 34 * (i - 4)), x1024(274F), M(1.7F));
                    dlg[i + 1].wLoadout.setPosSize(x1024(609F), y1024(104 + 34 * (i - 4)), x1024(332F), M(1.7F));
                    dlg[i + 1].bArming.setPosC(x1024(959F), y1024((104 + 34 * (i - 4) + 16) - 2));
                }

            for(int j = 0; j < 8; j++)
                if(j < 4)
                {
                    dlg[j + 8].wNum.setPosSize(x1024(48F), y1024(352 + 34 * j), x1024(80F), M(1.7F));
                    dlg[j + 8].wSkill.setPosSize(x1024(144F), y1024(352 + 34 * j), x1024(160F), M(1.7F));
                    dlg[j + 8].wPlane.setPosSize(x1024(318F), y1024(352 + 34 * j), x1024(274F), M(1.7F));
                    dlg[j + 8].wLoadout.setPosSize(x1024(609F), y1024(352 + 34 * j), x1024(332F), M(1.7F));
                    dlg[j + 8].bArming.setPosC(x1024(959F), y1024((352 + 34 * j + 16) - 2));
                } else
                {
                    dlg[j + 8].wNum.setPosSize(x1024(48F), y1024(352 + 34 * (j - 4)), x1024(80F), M(1.7F));
                    dlg[j + 8].wSkill.setPosSize(x1024(144F), y1024(352 + 34 * (j - 4)), x1024(160F), M(1.7F));
                    dlg[j + 8].wPlane.setPosSize(x1024(318F), y1024(352 + 34 * (j - 4)), x1024(274F), M(1.7F));
                    dlg[j + 8].wLoadout.setPosSize(x1024(609F), y1024(352 + 34 * (j - 4)), x1024(332F), M(1.7F));
                    dlg[j + 8].bArming.setPosC(x1024(959F), y1024((352 + 34 * (j - 4) + 16) - 2));
                }*/
			for(int i = 0; i < 7; i++)					
            if(i < 3)
            {
                dlg[i + 1].wNum.setPosSize(x1024(48F), y1024(175 + 34 * i), x1024(48F), M(1.7F));
                dlg[i + 1].wSkill.setPosSize(x1024(120F), y1024(175 + 34 * i), x1024(92F), M(1.7F));
                dlg[i + 1].wPlane.setPosSize(x1024(247F), y1024(175 + 34 * i), x1024(227F), M(1.7F));
                dlg[i + 1].wLoadout.setPosSize(x1024(510F), y1024(175 + 34 * i), x1024(215F), M(1.7F));
    			dlg[i + 1].wCountry.setPosSize(x1024(752F), y1024(175 + 34 * i), x1024(165F), M(1.7F));   
    			
    			//+++ TODO: 4.12 changed code +++
//                dlg[i + 1].bArming.setPosC(x1024(959F), y1024((175 + 34 * i + 16) - 2));
                dlg[i + 1].bArming.setPosC(x1024(959F), y1024(((float)(175 + 34 * i) + f) - 2.0F));
                //--- TODO: 4.12 changed code ---
            } else
            {
                dlg[i + 1].wNum.setPosSize(x1024(48F), y1024(104 + 34 * (i - 4)), x1024(48F), M(1.7F));
                dlg[i + 1].wSkill.setPosSize(x1024(120F), y1024(104 + 34 * (i - 4)), x1024(92F), M(1.7F));
                dlg[i + 1].wPlane.setPosSize(x1024(247F), y1024(104 + 34 * (i - 4)), x1024(227F), M(1.7F));
                dlg[i + 1].wLoadout.setPosSize(x1024(510F), y1024(104 + 34 * (i - 4)), x1024(215F), M(1.7F));
    			dlg[i + 1].wCountry.setPosSize(x1024(752F), y1024(104 + 34 * (i - 4)), x1024(165F), M(1.7F));
    			
    			//+++ TODO: 4.12 changed code +++
//                dlg[i + 1].bArming.setPosC(x1024(959F), y1024((104 + 34 * (i - 4) + 16) - 2));
                dlg[i + 1].bArming.setPosC(x1024(959F), y1024(((float)(104 + 34 * (i - 4)) + f) - 2.0F));
                //--- TODO: 4.12 changed code ---
            }
        	for(int j = 0; j < 8; j++)
            if(j < 4)
            {
                dlg[j + 8].wNum.setPosSize(x1024(48F), y1024(352 + 34 * j), x1024(48F), M(1.7F));
                dlg[j + 8].wSkill.setPosSize(x1024(120F), y1024(352 + 34 * j), x1024(92F), M(1.7F));
                dlg[j + 8].wPlane.setPosSize(x1024(247F), y1024(352 + 34 * j), x1024(227F), M(1.7F));
                dlg[j + 8].wLoadout.setPosSize(x1024(510F), y1024(352 + 34 * j), x1024(215F), M(1.7F));
    			dlg[j + 8].wCountry.setPosSize(x1024(752F), y1024(352 + 34 * j), x1024(165F), M(1.7F));  

    			//+++ TODO: 4.12 changed code +++
//                dlg[j + 8].bArming.setPosC(x1024(959F), y1024((352 + 34 * j + 16) - 2));
                dlg[j + 8].bArming.setPosC(x1024(959F), y1024(((float)(352 + 34 * j) + f) - 2.0F));
                //--- TODO: 4.12 changed code ---
            } else
            {
                dlg[j + 8].wNum.setPosSize(x1024(48F), y1024(352 + 34 * (j - 4)), x1024(48F), M(1.7F));
                dlg[j + 8].wSkill.setPosSize(x1024(120F), y1024(352 + 34 * (j - 4)), x1024(92F), M(1.7F));
                dlg[j + 8].wPlane.setPosSize(x1024(247F), y1024(352 + 34 * (j - 4)), x1024(227F), M(1.7F));
                dlg[j + 8].wLoadout.setPosSize(x1024(510F), y1024(352 + 34 * (j - 4)), x1024(215F), M(1.7F));
    			dlg[j + 8].wCountry.setPosSize(x1024(752F), y1024(352 + 34 * (j - 4)), x1024(165F), M(1.7F)); 
    			
    			//+++ TODO: 4.12 changed code +++
//                dlg[j + 8].bArming.setPosC(x1024(959F), y1024((352 + 34 * (j - 4) + 16) - 2));
                dlg[j + 8].bArming.setPosC(x1024(959F), y1024(((float)(352 + 34 * (j - 4)) + f) - 2.0F));
                //--- TODO: 4.12 changed code ---
            }                
        }

        public DialogClient()
        {
        }
    }

 /* BY PAL, stock 4.111   
    private void defaultRegiments()
    {
        r01 = "r01";
        r010 = "r010";
        g01 = "g01";
        g010 = "g010";
        try
        {
            SectFile sectfile = new SectFile(currentMissionName, 0);
            if(sectfile.sectionIndex("r0100") < 0)
            {
                r01 = "usa01";
                r010 = "usa010";
                g01 = "ja01";
                g010 = "ja010";
            }
            for(int i = 0; i < 8; i++)
                if(OUR)
                {
                    wing[i].regiment = r01;
                    wing[i + 8].regiment = g01;
                } else
                {
                    wing[i].regiment = g01;
                    wing[i + 8].regiment = r01;
                }

        }
        catch(Exception exception)
        {
            System.out.println("WARNING: No quick missions in Missions folder.");
        }
    }  */  


	private void defaultRegiments(boolean cleanRegs)
	{
		boolean r0100 = false;
        try
        {		
			SectFile sectfile = new SectFile(currentMissionName, 0); 
		    int n = 16; //By PAL, default number of missions
			int a = sectfile.sectionIndex("Wing");
		    if (a >= 0) n = sectfile.vars(a); //By PAL, number of Wings, theorically 8 or 16
		    b16Flights = (n == 16);  //By PAL, original Mission 4.09 or 16 Flights Mission 4.10
		    showHide();
		    if (!b16Flights) n = 8; //By PAL, 16 or 8, nothing else
		    for (int i = 0; i < n; i++) //By PAL, I will check it for all (16 and 8 flights)
		    {
				String s = sectfile.var(a, i);
				if(s.equalsIgnoreCase("r0100")) r0100 = true; //By PAL, only if is not the Player one
			}
        }
        catch(Exception exception)
        {
            System.out.println("WARNING: No quick missions in Missions folder.");
        }
		//By PAL, v4101
        //SectFile sectfile = new SectFile(currentMissionName, 0);
        //if(sectfile.sectionIndex("r0100") >= 0)
        if(r0100)
        {
            r01 = "r01";
            r010 = "r010";
            g01 = "g01";
            g010 = "g010";
            /*if (cleanRegs) //By PAL
            for(int k = 0; k < 16; k++)
                if(k < 8)
                {
                    if(wing[k].regiment.equals("usa01"))
                        wing[k].regiment = "r01";
                } else
                if(wing[k].regiment.equals("ja01"))
                    wing[k].regiment = "g01";*/

        } else
        {
            r01 = "usa01";
            r010 = "usa010";
            g01 = "ja01";
            g010 = "ja010";
            /*if (cleanRegs) //By PAL	            
            for(int l = 0; l < 16; l++)
            {
                if(l < 8)
                {
                    if(wing[l].regiment.equals("r01"))
                        wing[l].regiment = "usa01";
                    continue;
                }
                if(wing[l].regiment.equals("g01"))
                    wing[l].regiment = "ja01";
            }*/
        }
        if (cleanRegs) //By PAL  from 4.111, I clean all of them, no problem
        	for(int i = 0; i < 8; i++)
	            if(OUR)
	            {
	                wing[i].regiment = r01;
	                wing[i + 8].regiment = g01;
	            } else
	            {
	                wing[i].regiment = g01;
	                wing[i + 8].regiment = r01;
	            }         			
		music = r01;    //I set the music just copying the value
	}
/*
    private void mapChanged()
    {
        int i = wMap.getSelected();
        if(i >= 0 && i < _mapKey.length && !bNoAvailableMissions)
        {
            ArrayList arraylist = getAvailableMissionsS(getMapName(), getMapName() + getArmyString() + _targetKey[wTarget.getSelected()] + (wTarget.getSelected() != 0 ? "" : noneTargetSuffix[wLevel.getSelected()]));
            Random random = new Random();
            if(arraylist.size() < 1)
                return;
            int j = random.nextInt(arraylist.size());
            String s = "Missions/Quick/" + getMapName() + "/" + (String)arraylist.get(j);
            currentMissionName = s;
            SectFile sectfile = new SectFile(currentMissionName, 0);
            if(sectfile.sectionIndex("r0100") >= 0)
            {
                r01 = "r01";
                r010 = "r010";
                g01 = "g01";
                g010 = "g010";
                for(int k = 0; k < 16; k++)
                {
                    if(wing[k].regiment.equals("usa01"))
                        wing[k].regiment = "r01";
                    if(wing[k].regiment.equals("ja01"))
                        wing[k].regiment = "g01";
                }

            } else
            {
                r01 = "usa01";
                r010 = "usa010";
                g01 = "ja01";
                g010 = "ja010";
                for(int l = 0; l < 16; l++)
                {
                    if(wing[l].regiment.equals("r01"))
                        wing[l].regiment = "usa01";
                    if(wing[l].regiment.equals("g01"))
                        wing[l].regiment = "ja01";
                }

            }
            String s1 = sectfile.get("MAIN", "MAP", (String)null);
            if(s1 != null)
            {
                IniFile inifile = new IniFile("maps/" + s1, 0);
                String s2 = inifile.get("WORLDPOS", "CAMOUFLAGE", "SUMMER");
                if(World.cur() != null)
                    World.cur().setCamouflage(s2);
            }
        }
    } */
      
    private void mapChanged(boolean Reset) //By PAL, to avoid problem reseting countries
    {
        int i = wMap.getSelected();      
        if(i >= 0 && i < _mapKey.length && !bNoAvailableMissions)
        {
            //ArrayList arraylist = getAvailableMissionsS(getMapName(), getMapName() + getArmyString() + _targetKey[wTarget.getSelected()] + (wTarget.getSelected() != 0 ? "" : noneTargetSuffix[wLevel.getSelected()]));
            //Random random = new Random();
            //int j = random.nextInt(arraylist.size()); //By PAL
            //String s = "Missions/Quick/" + getMapName() + "/" + (String)arraylist.get(j);        

		    updateMissionsList(); //By PAL, moved here
			processTargetList(); //By PAL, update data 

            String s = "";
            if(wTargetList.getSelected() >= 0)	
            	s = /*"Missions/Quick/"*/MissionsFolder + getMapName() + "/" + getMapName() +
            	((String)filesTargetList.get(wTargetList.getSelected())).trim() + ".mis"; // wTargetList.getValue().trim() + ".mis";  //By PAL, to use new ComboBox      	
            currentMissionName = s;

       	//By PAL, to note that there is no map loaded
       		if (client.isActivated())
       		{
				s = null;
		        try
		        {
			        String Result = null;		        	
		            SectFile sectfile = new SectFile(currentMissionName, 0);            	
			        s = sectfile.get("MAIN", "MAP");
			        if(s == null) Result = "No map defined in mission file!\n'MAP' section is missing.";
			        SectFile sectfile1 = new SectFile("maps/" + s);
			        String s1 = sectfile1.get("MAP", "TypeMap", (String)null);
			        if(s1 == null) Result = "Bad map in mission file!\nThe map '" + s + "' definition is not correct.";
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
			        	Result = "Bad map in mission file!\nThe map '" + s + "' definition is not correct.";            	
					if(Result != null)//By PAL, check if the map exists
					{
						new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, "Warning", Result, 3, 0.0F);
					}
		        }
		        catch(Exception exception)
		        {
		            //GWindowMessageBox gwindowmessagebox = new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, "Error", "Data file corrupt", 3, 0.0F);
		            new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, "Warning", "The map '" + s +
		            		"' doesn't seem to be installed in your IL-2 game.\nThis mission cannot be played!", 3, 0.0F);
		        }
       		}
            
	        //By PAL, try / catch to avoid problems when starting (nullpointerexception)
	        try
	        {		                       
	            SectFile sectfile = new SectFile(currentMissionName, 0);
				defaultRegiments(Reset); //By PAL, process regiments, it changes b16Flights, etc and music, performs ShowHide
		        //showHide();
		        //Replaces:	            
	            /*
		        if(Reset)
		        {	            
		            if(sectfile.sectionIndex("r0100") >= 0)
		            {
		                r01 = "r01";
		                r010 = "r010";
		                g01 = "g01";
		                g010 = "g010";
		                for(int k = 0; k < 16; k++)
		                    if(k < 8)
		                    {
		                        if(wing[k].regiment.equals("usa01"))
		                            wing[k].regiment = "r01";
		                    } else
		                    if(wing[k].regiment.equals("ja01"))
		                        wing[k].regiment = "g01";
		
		            } else
		            {
		                r01 = "usa01";
		                r010 = "usa010";
		                g01 = "ja01";
		                g010 = "ja010";
		                for(int l = 0; l < 16; l++)
		                {
		                    if(l < 8)
		                    {
		                        if(wing[l].regiment.equals("r01"))
		                            wing[l].regiment = "usa01";
		                        continue;
		                    }
		                    if(wing[l].regiment.equals("g01"))
		                        wing[l].regiment = "ja01";
		                }
		
		            }
		        }*/           	                   
	            String s1 = sectfile.get("MAIN", "MAP", (String)null);
	            if(s1 != null)
	            {
	                IniFile inifile = new IniFile("maps/" + s1, 0);
	                String s2 = inifile.get("WORLDPOS", "CAMOUFLAGE", "SUMMER");						
	                if(World.cur() != null)
	                    World.cur().setCamouflage(s2);                    
	            }
	        }
	        catch(Exception exception)
	        {
	            //GWindowMessageBox gwindowmessagebox = new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, "Error", "Data file corrupt", 3, 0.0F);
	            new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, "Warning", "The map '" + s +
	            		"' doesn't seem to be installed in your IL-2 game.\nThis mission cannot be played!", 3, 0.0F);
	        }                        	
        }
//By PAL, last changes:        
		updateCountries();          
    }   

    public void enterPush(GameState gamestate)
    {
        if(bNoAvailableMissions)
        { //By PAL, to avoid ANY problems
            System.out.println(">> No Quick Missions available!");
            String Title = "QMBPro Error";
            String Msg = "No Quick Missions found!!!\n\n"
            		+ "Either the folder '" + MissionsFolder + "' is missing\n\n"
            		+ "or there are no Quick Missions in the folder '" + MissionsFolder + "'.\n\n"
            		+ "Your settings or folder structure are incorrect!";
            new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, Title, Msg, 3, 0.0F) {
	            /*public void afterCreated()
	            {
	                bModal = true;
	            }*/
            	//gwindowmessagebox.BUTTONS_OK
                public void result(int k)
                {
                   	Main.stateStack().pop();
                }
            }
;            
        	return;
        }   	
        World.cur().diffUser.set(World.cur().userCfg.singleDifficulty);
        if(exisstFile("quicks/.last.quick"))
        {
            GUIQuickLoad guiquickload = (GUIQuickLoad)GameState.get(25);
            guiquickload.execute(".last", true);
            load();
           	mapChanged(false); //By PAL, from 4.111 idea, does it hang everything?
        }
        else
		{
       		//setDefaultValues();
			mapChanged(true); //By PAL, Initialize everything
 		}   
        wing[0].fromUserCfg();        
        //mapChanged(false);
//By PAL, last change updateCountries()	       
        //defaultRegiments();  //By PAL, to avoid loading errors, problem
        //By PAL     
        //if(r01.equals("usa01"))
        if(music.equals("usa01"))  //By PAL
            Main3D.menuMusicPlay(OUR ? "us" : "ja");
        else
            Main3D.menuMusicPlay(OUR ? "ru" : "de");
        _enter();   		        
    }

    public void enterPop(GameState gamestate)
    {
        if(gamestate.id() == 17)
        {
            World.cur().userCfg.singleDifficulty = World.cur().diffUser.get();
            World.cur().userCfg.saveConf();
        } else
        if(gamestate.id() == 55) //By PAL, from Arming
        {	
            wing[indxAirArming].fromAirArming();
		//By PAL, Important:
           	selectCountries(indxAirArming); //By PAL, to select the corresponding Country in combobox            
        }
        else
        if(gamestate.id() == 25)
        {
        	/*if (pl == 0)
            	wPlaneList.setSelected(0, true, false);
            else*/        	
            wPlaneList.setSelected(0, true, false);
            setPlaneList(wPlaneList.getSelected());
            fillArrayPlanes();
            for(int i = 0; i < 16; i++)
            {
                fillComboPlane(dlg[i].wPlane, i == 0);
                int j = dlg[i].wPlane.getSelected();
                if(j == 0)
                    dlg[i].wPlane.setSelected(1, false, false);
                dlg[i].wPlane.setSelected(0, true, true);
            }

            load();
        }
        _enter();
    }

    public void _enter()
    {    	
        Main.cur().currentMissionFile = null;
        setQMB(true);
        String s = "users/" + World.cur().userCfg.sId + "/QMB.ini";
        if(!exisstFile(s))
            initStat();
        client.activateWindow();
//By PAL, from 4.111
        Mission.resetDate();
	   	try  //By PAL, recover ParachuteOn status
   		{
        	IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);  //By PAL, Save last settings
			sParachuteOn.bChecked = inifile.get("LastSingleMission", "ParachuteOn", true);			
			//inifile.saveFile();    		    		
   		}
    	catch(Exception exception)
    	{
           	System.out.println(exception.getMessage());
           	exception.printStackTrace();
    	}        
    }

    public void _leave()
    {
 		try  //By PAL, Save status of sMarkingsOn switch, etc
   		{				   			
        	IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);  //By PAL, Save last settings
        	if (inifile.get("LastSingleMission", "ParachuteOn", true) != sParachuteOn.bChecked)
        		inifile.set("LastSingleMission", "ParachuteOn", sParachuteOn.bChecked);        		  		
	    	inifile.saveFile();  		
   		}
    	catch(Exception exception)
    	{
            System.out.println(exception.getMessage());
            exception.printStackTrace();
    	}    	
        World.cur().userCfg.saveConf();
        client.hideWindow();
    }

    private static int getPlaneList()
    {
        return pl;
    }

    private static void setPlaneList(int i)
    {
        pl = i;
    }

    static boolean isQMB()
    {
        return bIsQuick;
    }

    static void setQMB(boolean flag)
    {
        bIsQuick = flag;
    }

    static void initStat()
    {
    	try
    	{
    	    String s = "users/" + World.cur().userCfg.sId + "/QMB.ini";
	        SectFile sectfile = new SectFile(s, 1, false, World.cur().userCfg.krypto());
	        String s1 = "MAIN";
	        sectfile.sectionAdd(s1);
	        int i = sectfile.sectionIndex(s1);
	        float f = 0.0F;
	        int j = 0;
	        sectfile.lineAdd(i, "qmbTotalScore", "" + j);
	        sectfile.lineAdd(i, "qmbTotalAirKill", "" + j);
	        sectfile.lineAdd(i, "qmbTotalGroundKill", "" + j);
	        sectfile.lineAdd(i, "qmbTotalBulletsFired", "" + j);
	        sectfile.lineAdd(i, "qmbTotalBulletsHitAir", "" + j);
	        sectfile.lineAdd(i, "qmbTotalBulletsFiredHitGround", "" + j);
	        sectfile.lineAdd(i, "qmbTotalPctAir", "" + f);
	        sectfile.lineAdd(i, "qmbTotalPctGround", "" + f);
	        sectfile.lineAdd(i, "qmbTotalTankKill", "" + j);
	        sectfile.lineAdd(i, "qmbTotalCarKill", "" + j);
	        sectfile.lineAdd(i, "qmbTotalArtilleryKill", "" + j);
	        sectfile.lineAdd(i, "qmbTotalAAAKill", "" + j);
	        sectfile.lineAdd(i, "qmbTotalTrainKill", "" + j);
	        sectfile.lineAdd(i, "qmbTotalShipKill", "" + j);
	        sectfile.lineAdd(i, "qmbTotalAirStaticKill", "" + j);
	        sectfile.lineAdd(i, "qmbTotalBridgeKill", "" + j);
	        sectfile.lineAdd(i, "qmbTotalPara", "" + j);
	        sectfile.lineAdd(i, "qmbTotalDead", "" + j);
	        sectfile.lineAdd(i, "qmbTotalBombsFired", "" + j);
	        sectfile.lineAdd(i, "qmbTotalBombsHit", "" + j);
	        sectfile.lineAdd(i, "qmbTotalRocketsFired", "" + j);
	        sectfile.lineAdd(i, "qmbTotalRocketsHit", "" + j);
	        sectfile.lineAdd(i, "qmbTotalPctBomb", "" + f);
	        sectfile.lineAdd(i, "qmbTotalPctRocket", "" + f);
	        sectfile.saveFile();
    	}
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            System.out.println("Cannot start Statistics!");
            return;
        }        
    }

    private static boolean exisstFile(String s)
    {
        try
        {
            SFSInputStream sfsinputstream = new SFSInputStream(s);
            sfsinputstream.close();
        }
        catch(Exception exception)
        {
            return false;
        }
        return true;
    }

    private void validateEditableComboControl(GWindowComboControl gwindowcombocontrol)
    {
    	if (gwindowcombocontrol.size() == 0) //By PAL, added to avoid selecting inexistent
    	{
    		gwindowcombocontrol.setSelected(-1, false, false);
    		return;	
    	}
        String s = gwindowcombocontrol.getValue();
        if(s.equals(""))
        {
            gwindowcombocontrol.setSelected(0, true, false);
            s = gwindowcombocontrol.get(0);
        }
        int i = parseIntOwn(s);
        if(i < parseIntOwn(gwindowcombocontrol.get(0)))
        {
            gwindowcombocontrol.setSelected(0, true, false);
            i = parseIntOwn(gwindowcombocontrol.getValue());
        }
        if(i > parseIntOwn(gwindowcombocontrol.get(gwindowcombocontrol.size() - 1)))
        {
            gwindowcombocontrol.setSelected(gwindowcombocontrol.size() - 1, true, false);
            i = parseIntOwn(gwindowcombocontrol.getValue());
        }
        for(int j = 0; j < gwindowcombocontrol.size(); j++)
            if(i == parseIntOwn(gwindowcombocontrol.get(j)))
                gwindowcombocontrol.setSelected(j, true, false);

    }

    public void startQuickMissionMulti()
    {
        //ArrayList arraylist = getAvailableMissionsS(getMapName(), getMapName() + getArmyString() + _targetKey[wTarget.getSelected()] + (wTarget.getSelected() != 0 ? "" : noneTargetSuffix[wLevel.getSelected()]));
        //Random random = new Random();
        //int i = random.nextInt(arraylist.size());
        //String s = "Missions/Quick/" + getMapName() + "/" + (String)arraylist.get(i);
		String s = currentMissionName;
        try
        {
            SectFile sectfile = new SectFile(s, 0);
            sectfile.set("MAIN", "TIME", wTimeHour.getValue() + "." + wTimeMins.getSelected() * 25);

			int n = 16; //By PAL, default number of missions
			int a = sectfile.sectionIndex("Wing");
	        if (a >= 0) n = sectfile.vars(a); //By PAL, number of Wings, theorically 8 or 16
	        if (n < 16)  //By PAL, original Mission 4.09
	        {
	            for(int j = 0; j < 4; j++)
	            {
	                String s1 = r010 + Character.forDigit(j, 10);
	                if(!sectfile.exist("Wing", s1))
                      throw new Exception("Section Red " + s1 + " not found");
	            }
	            
	            for(int k = 0; k < 4; k++)
	            {
	                String s2 = g010 + Character.forDigit(k, 10);
	                if(!sectfile.exist("Wing", s2))
	                  throw new Exception("Section Blue " + s2 + " not found");
	            }
	
	            sectfile.set("MAIN", "CloudType", wWeather.getSelected());
	            sectfile.set("MAIN", "CloudHeight", wCldHeight.getValue());
	            String as[] = new String[8];
	            String as1[] = new String[8];
	            for(int l = 0; l < 8; l++) //By PAL, to 8
	            {
	                if(l <= 3)
	                    as[l] = (OUR ? r010 : g010) + l;
	                else
	                    as[l] = (OUR ? g010 : r010) + l;
	                if(l <= 3)
	                	as1[l] = wing[l].prepareWing409(sectfile);  //By PAL, 0..3
	                else
	                	as1[l] = wing[l+4].prepareWing409(sectfile);  //By PAL, 8..11
	            }
            
	            if(as1[0] != null)
	                sectfile.set("MAIN", "player", as1[0]);
	            else
	                sectfile.set("MAIN", "player", as[0]);
	                
	            for(int i1 = 0; i1 < 8; i1++) //By PAL, to 8
	                if(as1[i1] != null)
	                /*{
	                	if(i1 <= 3)
	                		wing[i1].prepereWay409(sectfile, as, as1);
	                	else
	                		wing[i1+8].prepereWay409(sectfile, as, as1);	
	                }*/
	                    wing[i1].prepereWay409(sectfile, as, as1); //By PAL, all of them corridos, the sectfile only has 8 flights

/*  By PAL, original 409
            String as[] = new String[8];
            String as1[] = new String[8];
            for(int l = 0; l < 8; l++)
            {
                if(l <= 3)
                    as[l] = (OUR ? r010 : g010) + l;
                else
                    as[l] = (OUR ? g010 : r010) + l;
                as1[l] = wing[l].prepareWing(sectfile);
            }

            if(as1[0] != null)
                sectfile.set("MAIN", "player", as1[0]);
            else
                sectfile.set("MAIN", "player", as[0]);
            for(int i1 = 0; i1 < 8; i1++)
                if(as1[i1] != null)
                    wing[i1].prepereWay(sectfile, as, as1);*/

	                    	
	            if(wDefence.getSelected() == 0)
	            {
	                for(int j1 = 0; j1 < 2; j1++)
	                {
	                    String s3 = j1 == 0 ? "Stationary" : "NStationary";
	                    int l1 = sectfile.sectionIndex(s3);
	                    if(l1 >= 0)
	                    {
	                        sectfile.sectionRename(l1, "Stationary_Temp");
	                        sectfile.sectionAdd(s3);
	                        int j2 = sectfile.sectionIndex(s3);
	                        int l2 = sectfile.vars(l1);
	                        for(int j3 = 0; j3 < l2; j3++)
	                        {
	                            SharedTokenizer.set(sectfile.line(l1, j3));
	                            String s5 = null;
	                            if(j1 == 1)
	                                s5 = SharedTokenizer.next("");
	                            String s7 = SharedTokenizer.next("");
	                            int k3 = SharedTokenizer.next(0);
	                            String s9 = null;
	                            if(s5 != null)
	                                s9 = s5 + " " + s7 + " " + k3 + " " + SharedTokenizer.getGap();
	                            else
	                                s9 = s7 + " " + k3 + " " + SharedTokenizer.getGap();
	                            if(k3 == 0)
	                                sectfile.lineAdd(j2, s9);
	                            else
	                            if(k3 == 1 && OUR && !bScramble)
	                                sectfile.lineAdd(j2, s9);
	                            else
	                            if(k3 == 2 && !OUR && !bScramble)
	                                sectfile.lineAdd(j2, s9);
	                            else
	                                try
	                                {
	                                    Class class1 = ObjIO.classForName(s7);
	                                    if(!(com.maddox.il2.objects.vehicles.artillery.AAA.class).isAssignableFrom(class1))
	                                        if(s7.startsWith("ships."))
	                                        {
	                                            SharedTokenizer.set(sectfile.line(l1, j3));
	                                            if(j1 == 1)
	                                                s5 = SharedTokenizer.next("");
	                                            String s12 = SharedTokenizer.next("");
	                                            String s13 = SharedTokenizer.next("");
	                                            String s14 = SharedTokenizer.next("");
	                                            String s15 = SharedTokenizer.next("");
	                                            String s16 = SharedTokenizer.next("");
	                                            String s17 = SharedTokenizer.next("");
	                                            SharedTokenizer.next("");
	                                            String s19 = SharedTokenizer.next("");
	                                            if(j1 == 1)
	                                                sectfile.lineAdd(j2, s5 + " " + s12 + " " + s13 + " " + s14 + " " + s15 + " " + s16 + " " + s17 + " " + 5940 + " " + s19);
	                                            else
	                                                sectfile.lineAdd(j2, s12 + " " + s13 + " " + s14 + " " + s15 + " " + s16 + " " + s17 + " " + 5940 + " " + s19);
	                                        } else
	                                        {
	                                            sectfile.lineAdd(j2, s9);
	                                        }
	                                }
	                                catch(Throwable throwable) { }
	                        }
	
	                        sectfile.sectionRemove(l1);
	                    }
	                }
	
	                int k1 = sectfile.sectionIndex("Chiefs");
	                if(k1 >= 0)
	                {
	                    sectfile.sectionRename(k1, "Chiefs_Temp");
	                    sectfile.sectionAdd("Chiefs");
	                    int i2 = sectfile.sectionIndex("Chiefs");
	                    int k2 = sectfile.vars(k1);
	                    for(int i3 = 0; i3 < k2; i3++)
	                    {
	                        String s4 = sectfile.line(k1, i3);
	                        SharedTokenizer.set(s4);
	                        String s6 = SharedTokenizer.next("");
	                        String s8 = SharedTokenizer.next("");
	                        if(s8.startsWith("Ships."))
	                        {
	                            int l3 = SharedTokenizer.next(0);
	                            if(l3 == 0)
	                                sectfile.lineAdd(i2, s4);
	                            else
	                            if(l3 == 1 && OUR && !bScramble)
	                                sectfile.lineAdd(i2, s4);
	                            else
	                            if(l3 == 2 && !OUR && !bScramble)
	                            {
	                                sectfile.lineAdd(i2, s4);
	                            } else
	                            {
	                                SharedTokenizer.next("");
	                                String s11 = SharedTokenizer.next("");
	                                sectfile.lineAdd(i2, s6 + " " + s8 + " " + l3 + " " + 5940 + " " + s11);
	                            }
	                        } else
	                        {
	                            sectfile.lineAdd(i2, s4);
	                        }
	                    }
	
	                    sectfile.sectionRemove(k1);
	                }	                
	            }
	        }
	        else // should be 16
	        { //By PAL, original Mission 4.10  
	            for(int j = 0; j < 8; j++)
	            {
	                String s1;
	                if(j < 4)
	                    s1 = r010 + Character.forDigit(j, 10);
	                else
	                    s1 = r01 + "1" + Character.forDigit(j - 4, 10);
	                if(!sectfile.exist("Wing", s1))
	                    throw new Exception("Section " + s1 + " not found");
	            }
	
	            for(int k = 0; k < 8; k++)
	            {
	                String s2;
	                if(k < 4)
	                    s2 = g010 + Character.forDigit(k, 10);
	                else
	                    s2 = g01 + "1" + Character.forDigit(k - 4, 10);
	                if(!sectfile.exist("Wing", s2))
	                    throw new Exception("Section " + s2 + " not found");
	            }
	
	            sectfile.set("MAIN", "CloudType", wWeather.getSelected());
	            sectfile.set("MAIN", "CloudHeight", parseIntOwn(wCldHeight.getValue()));
	            String as[] = new String[16];
	            String as1[] = new String[16];
	            for(int l = 0; l < 16; l++)
	            {
	                if(l < 8)
	                    as[l] = (OUR ? r01 : g01) + (l >= 4 ? "1" : "0") + l;
	                else
	                    as[l] = (OUR ? g01 : r01) + (l >= 12 ? "1" : "0") + l;
	                as1[l] = wing[l].prepareWing(sectfile);
	            }
	
	            if(as1[0] != null)
	                sectfile.set("MAIN", "player", as1[0]);
	            else
	                sectfile.set("MAIN", "player", as[0]);
	            for(int i1 = 0; i1 < 16; i1++)
	                if(as1[i1] != null)
	                    wing[i1].prepereWay(sectfile, as, as1);
	
	            if(wDefence.getSelected() == 0)
	            {
	                for(int j1 = 0; j1 < 2; j1++)
	                {
	                    String s3 = j1 == 0 ? "Stationary" : "NStationary";
	                    int i2 = sectfile.sectionIndex(s3);
	                    if(i2 < 0)
	                        continue;
	                    sectfile.sectionRename(i2, "Stationary_Temp");
	                    sectfile.sectionAdd(s3);
	                    int k2 = sectfile.sectionIndex(s3);
	                    int i3 = sectfile.vars(i2);
	                    for(int j3 = 0; j3 < i3; j3++)
	                    {
	                        SharedTokenizer.set(sectfile.line(i2, j3));
	                        String s6 = null;
	                        if(j1 == 1)
	                            s6 = SharedTokenizer.next("");
	                        String s8 = SharedTokenizer.next("");
	                        int l3 = SharedTokenizer.next(0);
	                        String s10 = null;
	                        if(s6 != null)
	                            s10 = s6 + " " + s8 + " " + l3 + " " + SharedTokenizer.getGap();
	                        else
	                            s10 = s8 + " " + l3 + " " + SharedTokenizer.getGap();
	                        if(l3 == 0)
	                        {
	                            sectfile.lineAdd(k2, s10);
	                            continue;
	                        }
	                        if(l3 == 1 && OUR && !bScramble)
	                        {
	                            sectfile.lineAdd(k2, s10);
	                            continue;
	                        }
	                        if(l3 == 2 && !OUR && !bScramble)
	                        {
	                            sectfile.lineAdd(k2, s10);
	                            continue;
	                        }
	                        try
	                        {
	                            Class class1 = ObjIO.classForName(s8);
	                            if((com.maddox.il2.objects.vehicles.artillery.AAA.class).isAssignableFrom(class1))
	                                continue;
	                            if(s8.startsWith("ships."))
	                            {
	                                SharedTokenizer.set(sectfile.line(i2, j3));
	                                if(j1 == 1)
	                                    s6 = SharedTokenizer.next("");
	                                String s12 = SharedTokenizer.next("");
	                                String s13 = SharedTokenizer.next("");
	                                String s14 = SharedTokenizer.next("");
	                                String s15 = SharedTokenizer.next("");
	                                String s16 = SharedTokenizer.next("");
	                                String s17 = SharedTokenizer.next("");
	                                SharedTokenizer.next("");
	                                String s19 = SharedTokenizer.next("");
	                                if(j1 == 1)
	                                    sectfile.lineAdd(k2, s6 + " " + s12 + " " + s13 + " " + s14 + " " + s15 + " " + s16 + " " + s17 + " " + 5940 + " " + s19);
	                                else
	                                    sectfile.lineAdd(k2, s12 + " " + s13 + " " + s14 + " " + s15 + " " + s16 + " " + s17 + " " + 5940 + " " + s19);
	                            } else
	                            {
	                                sectfile.lineAdd(k2, s10);
	                            }
	                        }
	                        catch(Throwable throwable) { }
	                    }
	
	                    sectfile.sectionRemove(i2);
	                }
	
	                int k1 = sectfile.sectionIndex("Chiefs");
	                if(k1 >= 0)
	                {
	                    sectfile.sectionRename(k1, "Chiefs_Temp");
	                    sectfile.sectionAdd("Chiefs");
	                    int l1 = sectfile.sectionIndex("Chiefs");
	                    int j2 = sectfile.vars(k1);
	                    for(int l2 = 0; l2 < j2; l2++)
	                    {
	                        String s4 = sectfile.line(k1, l2);
	                        SharedTokenizer.set(s4);
	                        String s5 = SharedTokenizer.next("");
	                        String s7 = SharedTokenizer.next("");
	                        if(s7.startsWith("Ships."))
	                        {
	                            int k3 = SharedTokenizer.next(0);
	                            if(k3 == 0)
	                            {
	                                sectfile.lineAdd(l1, s4);
	                                continue;
	                            }
	                            if(k3 == 1 && OUR && !bScramble)
	                            {
	                                sectfile.lineAdd(l1, s4);
	                                continue;
	                            }
	                            if(k3 == 2 && !OUR && !bScramble)
	                            {
	                                sectfile.lineAdd(l1, s4);
	                            } else
	                            {
	                                SharedTokenizer.next("");
	                                String s11 = SharedTokenizer.next("");
	                                sectfile.lineAdd(l1, s5 + " " + s7 + " " + k3 + " " + 5940 + " " + s11);
	                            }
	                        } else
	                        {
	                            sectfile.lineAdd(l1, s4);
	                        }
	                    }
	
	                    sectfile.sectionRemove(k1);
	                }
	            }
	        }  //By PAL, end version 410
	        
            //By PAL, remove Parachutes if necessary. if Exists
            if(!sParachuteOn.bChecked)
            {	                	
                String sp = sectfile.get("MAIN", "player", (String)null);
				int z = sectfile.sectionIndex("Wing");
				if (z >= 0)
	        	{
	           		int k = sectfile.vars(z);
	        		for(int i1 = 0; i1 < k; i1++)
	        		{
	            		String sa = sectfile.var(z, i1);
	            		if (!sa.equalsIgnoreCase(sp))
	            			sectfile.set(sa, "Parachute", "0");
	        		}
	        	}	                	
            }
            	            
            GUIQuickStats.resetMissionStat();
            Main.cur().currentMissionFile = sectfile;
            Main.stateStack().push(5);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            System.out.println(">> no file: " + s + "");
            new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20F, true, "Error", "Data file corrupt", 3, 0.0F);
            return;
        }
    }

    public void save(boolean flag)
    {
        try
        {
            ssect = new SectFile();
            ssect.sectionAdd("states");
            ioState.save();
            ssect.set("states", "head", ioState, false);
            for(int i = 0; i < 16; i++)
                ssect.set("states", "wing" + i, wing[i], false);

        }
        catch(Exception exception)
        {
            System.out.println("sorry, cant save");
            new GWindowMessageBox(client, 20F, true, "Error", "Can't save data file", 3, 0.0F);
        }
        GUIQuickSave guiquicksave = (GUIQuickSave)GameState.get(24);
        guiquicksave.sect = ssect;
        if(flag)
            guiquicksave.execute(".last", false);
    }

//By PAL, added in v4.111
    private void fillMissingWingRegiments(boolean flag)
    {
        for(int i = 0; i < 16; i++)
            if(i <= 7)
                wing[i].regiment = flag ? r01 : g01;
            else
                wing[i].regiment = flag ? g01 : r01;

    }

//By PAL, old load

    public void load()
    {
        setDefaultValues();
        byte byte0 = 0;
        byte byte1 = 16;
        if(ssect == null)
            return;           
        try
        {
            ssect.get("states", "head", ioState);
            if(ssect.get("states", "wing8", wing[8]) == null)
            {
                fillMissingWingRegiments(ioState.our); //By PAL, from v4.111
                byte0 = 4;
                byte1 = 8;
                ioState.getMap(); //v4101, in case of old 4.09 map is numeric
            }
            for(int i = 0; i < byte1; i++)
            {
                byte byte2 = 0;
                if(byte0 > 0 && i > 3)
                    byte2 = 4;
                ssect.get("states", "wing" + i, wing[i + byte2]);
            }
            onArmyChange();
            //By PAL, from v4.111
			try
			{	
	            SectFile sectfile = new SectFile("PaintSchemes/regiments.ini", 0);
	            for(int j = 0; j < 8; j++)
	            {
	                byte byte3 = 2;
	                if(ioState.our)
	                {
	                    if(j > 7)
	                        byte3 = 3;
	                } else
	                if(j < 8)
	                    byte3 = 3;
	                if(!sectfile.varExist(byte3, wing[j].regiment))
	                {
	                    String s = wing[j].regiment;
	                    String s1 = wing[j + 4].regiment;
	                    wing[j].regiment = wing[j + 8].regiment;
	                    wing[j + 4].regiment = s;
	                    wing[j + 8].regiment = s1;
	                }
	            }
			}
	        catch(Exception exceptionNew2)
	        {
	            System.out.println("sorry, Error on paintscheme regiments");
	            new GWindowMessageBox(client, 20F, true, "Error", "Data file corrupt", 3, 0.0F);
	        }            

        }
        catch(Exception exception)
        {
            //System.out.println("sorry, data corrupt");
            //GWindowMessageBox gwindowmessagebox = new GWindowMessageBox(client, 20F, true, "Error", "Data file corrupt", 3, 0.0F);
        	new GWindowMessageBox(client, 20F, true, "Error", "Data file corrupt 2", 3, 0.0F);
        }
        //onArmyChange();
        showHide(); //Added by PAL
    }


//By PAL, original load from v4.111
    public void load411()
    {
        setDefaultValues();
        byte byte0 = 0;
        byte byte1 = 16;
        if(ssect == null)
            return;
        try
        {
            ssect.get("states", "head", ioState);
            if(ssect.get("states", "wing8", wing[8]) == null)
            {
                fillMissingWingRegiments(ioState.our);
                byte0 = 4;
                byte1 = 8;
                ioState.getMap();
            }
			try
			{            
	            for(int i = 0; i < byte1; i++)
	            {
	                byte byte2 = 0;
	                if(byte0 > 0 && i > 3)
	                    byte2 = 4;
	                ssect.get("states", "wing" + i, wing[i + byte2]);
	                if(wing[i + byte2].origPlaneName.equals(wing[i + byte2].getPlane()))
	                    continue;
	                setPlaneList(0);
	                wPlaneList.setSelected(0, true, false);
	                fillArrayPlanes();
	                for(int k = 0; k < 16; k++)
	                    fillComboPlane(dlg[k].wPlane, k == 0);
	
	                i = -1;
	            }
			}
	        catch(Exception exceptionNew1)
	        {
	            System.out.println("sorry, Error on paintscheme regiments");
	            new GWindowMessageBox(client, 20F, true, "Error", "Data file corrupt", 3, 0.0F);
	        }            
			try
			{	
	            SectFile sectfile = new SectFile("PaintSchemes/regiments.ini", 0);
	            for(int j = 0; j < 8; j++)
	            {
	                byte byte3 = 2;
	                if(ioState.our)
	                {
	                    if(j > 7)
	                        byte3 = 3;
	                } else
	                if(j < 8)
	                    byte3 = 3;
	                if(!sectfile.varExist(byte3, wing[j].regiment))
	                {
	                    String s = wing[j].regiment;
	                    String s1 = wing[j + 4].regiment;
	                    wing[j].regiment = wing[j + 8].regiment;
	                    wing[j + 4].regiment = s;
	                    wing[j + 8].regiment = s1;
	                }
	            }
			}
	        catch(Exception exceptionNew2)
	        {
	            System.out.println("sorry, Error on paintscheme regiments");
	            new GWindowMessageBox(client, 20F, true, "Error", "Data file corrupt", 3, 0.0F);
	        }
        }
        catch(Exception exception)
        {
            System.out.println("sorry, data corrupt");
            new GWindowMessageBox(client, 20F, true, "Error", "Data file corrupt", 3, 0.0F);
        }
        onArmyChange();
        setShow(bFirst && noneTarget, wLevel);
    }

    private boolean checkCustomAirIni(String s)
    {
    	try
    	{
	        SectFile sectfile = new SectFile(s);
	        if(sectfile.sections() <= 0)
	            return false;
	        SectFile sectfile1 = new SectFile("com/maddox/il2/objects/air.ini");
	        int i = sectfile.vars(0);
	        for(int j = 0; j < i; j++)
	            if(sectfile1.varExist(0, sectfile.var(0, j)))
	                return true;    		
    	}
    	catch(Exception e) {}
        return false;
    }

    public void fillArrayPlanes()
    {
        playerPlane.clear();
        playerPlaneC.clear();
        aiPlane.clear();
        aiPlaneC.clear();
        boolean flag = false;
        if(getPlaneList() < 2)
        {
            planeListName = "com/maddox/il2/objects/air.ini";
            if(getPlaneList() == 1)
                flag = true;
        } else
        if(checkCustomAirIni(/*"Missions/Quick/"*/MissionsFolder + "QMBair_" + getPlaneList() + ".ini"))
        {
            planeListName = /*"Missions/Quick/"*/MissionsFolder + "QMBair_" + getPlaneList() + ".ini";
        } else
        {
            planeListName = "com/maddox/il2/objects/air.ini";
            wPlaneList.setSelected(0, true, false);
        }
        SectFile sectfile = new SectFile(planeListName, 0);
        SectFile sectfile1 = new SectFile("com/maddox/il2/objects/air.ini");
//        boolean flag1 = false;
        int j = sectfile.sections();
        if(j <= 0)
            throw new RuntimeException("GUIQuick: file '" + planeListName + "' is empty");
        int k = 0;
        do
        {
            if(k >= j)
                break;
            int l = sectfile.vars(k);
            for(int i1 = 0; i1 < l; i1++)
            {
                String s = sectfile.var(k, i1);
                if(!sectfile1.varExist(0, s))
                    continue;
                int i = sectfile1.varIndex(0, s);
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile1.value(0, i));
                String s1 = numbertokenizer.next((String)null);
                boolean flag2 = true;
                do
                {
                    if(!numbertokenizer.hasMoreTokens())
                        break;
                    if(!"NOQUICK".equals(numbertokenizer.next()))
                        continue;
                    flag2 = false;
                    break;
                } while(true);
                if(!flag2)
                    continue;
                Class class1 = null;
                try
                {
                    class1 = ObjIO.classForName(s1);
                }
                catch(Exception exception)
                {
                    throw new RuntimeException("PlMisAir(GUIQuick): class '" + s1 + "' not found");
                }
                ItemAir itemair = new ItemAir(s, class1, s1);                if(itemair.bEnablePlayer)
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

            k++;
        } while(true);

        if(flag)
        {
            Collections.sort(playerPlane, new byI18N_name());
            Collections.sort(playerPlaneC, new byI18N_name());
            Collections.sort(aiPlane, new byI18N_name());
            Collections.sort(aiPlaneC, new byI18N_name());
        }
    }
        
    public void setColors()
    {
        if (OUR) //Change Colors
        {
        	wTargetList.setEditTextColor(colorRed);
        	wDefence.setEditTextColor(colorRed);
        	wTarget.setCanvasColor(colorRed);
        	wLevel.setCanvasColor(colorRed);
       		for(int i=0; i < 8; i++)        	
       		{//8 Reds
        		dlg[i].wNum.setEditTextColor(colorRed);
            	dlg[i].wSkill.setEditTextColor(colorRed);
            	dlg[i].wPlane.setEditTextColor(colorRed);
            	dlg[i].wLoadout.setEditTextColor(colorRed);
            	dlg[i].wCountry.setEditTextColor(colorRed);
       		} 
       		for(int i=8; i < 16; i++)
       		{//8 Blues
       			dlg[i].wNum.setEditTextColor(colorBlue);
            	dlg[i].wSkill.setEditTextColor(colorBlue);
            	dlg[i].wPlane.setEditTextColor(colorBlue);
            	dlg[i].wLoadout.setEditTextColor(colorBlue);
            	dlg[i].wCountry.setEditTextColor(colorBlue);
       		}         	
        }
        else
        {
        	wTargetList.setEditTextColor(colorBlue);
        	wDefence.setEditTextColor(colorBlue);
        	wTarget.setCanvasColor(colorBlue);
        	wLevel.setCanvasColor(colorBlue);
       		for(int i=0; i < 8; i++)
       		{//8 Blues
       			dlg[i].wNum.setEditTextColor(colorBlue);
            	dlg[i].wSkill.setEditTextColor(colorBlue);
            	dlg[i].wPlane.setEditTextColor(colorBlue);
            	dlg[i].wLoadout.setEditTextColor(colorBlue);
            	dlg[i].wCountry.setEditTextColor(colorBlue);
       		}
       		for(int i=8; i < 16; i++)
       		{//8 Reds
        		dlg[i].wNum.setEditTextColor(colorRed);
            	dlg[i].wSkill.setEditTextColor(colorRed);
            	dlg[i].wPlane.setEditTextColor(colorRed);
            	dlg[i].wLoadout.setEditTextColor(colorRed);
            	dlg[i].wCountry.setEditTextColor(colorRed);       			
       		}
        }    	
    }   	

    public void fillArrayCountry() //By PAL
    { 
        if(resCountry == null) //By PAL
            resCountry = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());                    
        countryLstRed.clear();
        countryLstBlue.clear();
        List list = Regiment.getAll();
        TreeMap treemap = new TreeMap();
        int k1 = list.size();
        for(int i2 = 0; i2 < k1; i2++)
        {
            Regiment regiment1 = (Regiment)list.get(i2);
            if(regiment1.getArmy() == 2) //!= (OUR ? 1 : 2))
                continue;
            String s9 = regiment1.name();
            if(regHash.containsKey(s9))
                continue;
            regHash.put(s9, regiment1);
            ArrayList arraylist5 = (ArrayList)regList.get(regiment1.branch());
            if(arraylist5 == null)
            {
                String s15 = null;
                try
                {
                    s15 = resCountry.getString(regiment1.branch());
                }
                catch(Exception exception4)
                {
                    continue;
                }
                arraylist5 = new ArrayList();
                regList.put(regiment1.branch(), arraylist5);
                treemap.put(s15, regiment1.branch());
            }
            arraylist5.add(regiment1);
        }
		String s10;
        for(Iterator iterator = treemap.keySet().iterator(); iterator.hasNext(); countryLstRed.add(treemap.get(s10)))
        {	        	
            s10 = (String)iterator.next();	            	            
        }
        treemap.clear();
        for(int i2 = 0; i2 < k1; i2++)
        {
            Regiment regiment1 = (Regiment)list.get(i2);
            if(regiment1.getArmy() == 1) //!= (OUR ? 1 : 2))
                continue;
            String s9 = regiment1.name();
            if(regHash.containsKey(s9))
                continue;
            regHash.put(s9, regiment1);
            ArrayList arraylist5 = (ArrayList)regList.get(regiment1.branch());
            if(arraylist5 == null)
            {
                String s15 = null;
                try
                {
                    s15 = resCountry.getString(regiment1.branch());
                }
                catch(Exception exception4)
                {
                    continue;
                }
                arraylist5 = new ArrayList();
                regList.put(regiment1.branch(), arraylist5);
                treemap.put(s15, regiment1.branch());
            }
            arraylist5.add(regiment1);
        }
        for(Iterator iterator = treemap.keySet().iterator(); iterator.hasNext(); countryLstBlue.add(treemap.get(s10)))
        {	        	
            s10 = (String)iterator.next();	            	            
        }
        treemap.clear();                           
    }
    
 /*   public void fillCombosCountry() //By PAL
    {
        if(resCountry == null) //By PAL
            resCountry = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());     	
		for(int i3 = 0; i3 < 16; i3++)
			dlg[i3].wCountry.clear(); //Limpiar
		int lr = countryLstRed.size();
		for(int i=0; i < lr; i++)
		{	
			if (OUR)
			{
				for(int i3 = 0; i3 < 8; i3++)
					try
					{
						String s = resCountry.getString((String)countryLstRed.get(i));
						dlg[i3].wCountry.add(s);
					}		
	                catch(Exception exception)
	                {
						dlg[i3].wCountry.add("" + i3);
	                }					
			} else
			{
				for(int i3 = 8; i3 < 16; i3++)
					try
					{
						String s = resCountry.getString((String)countryLstRed.get(i));
						dlg[i3].wCountry.add(s);
					}		
	                catch(Exception exception)
	                {
						dlg[i3].wCountry.add("" + i3);
	                }
			}
  		}				
		int lb = countryLstBlue.size();
		for(int i=0; i < lb; i++)
		{
			if (OUR)
			{
				for(int i3 = 8; i3 < 16; i3++)
					try
					{
						String s = resCountry.getString((String)countryLstBlue.get(i));
						dlg[i3].wCountry.add(s);
					}		
	                catch(Exception exception)
	                {
						dlg[i3].wCountry.add("" + i3);
	                }					
			} else
			{
				for(int i3 = 0; i3 < 8; i3++)
					try
					{
						String s = resCountry.getString((String)countryLstBlue.get(i));
						dlg[i3].wCountry.add(s);
					}		
	                catch(Exception exception)
	                {
						dlg[i3].wCountry.add("" + i3);
	                }
			}
			
		}			 	
    }*/
    
    public void fillComboCountry(GWindowComboControl gwindowcombocontrol, int Num)
    {
    	try
    	{
	        if(resCountry == null) //By PAL
	            resCountry = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());    	
	        gwindowcombocontrol.clear();
	        if (Num < 8) //By PAL, Player Group
	        {
				if (OUR)
				{
					int lr = countryLstRed.size();
					for(int i=0; i < lr; i++)
					{
						try
						{
							String s = resCountry.getString((String)countryLstRed.get(i));
							gwindowcombocontrol.add(s);
						}		
		                catch(Exception exception)
		                {
							gwindowcombocontrol.add("" + i);
		                }
					}				
				}
				else
				{
					int lb = countryLstBlue.size();
					for(int i=0; i < lb; i++)
					{
						try
						{
							String s = resCountry.getString((String)countryLstBlue.get(i));
							gwindowcombocontrol.add(s);
						}		
		                catch(Exception exception)
		                {
							gwindowcombocontrol.add("" + i);
		                }					
					}
				}
	        }
	        else // Num >= 8
	        {
				if (!OUR)
				{
					int lr = countryLstRed.size();
					for(int i=0; i < lr; i++)
					{
						try
						{
							String s = resCountry.getString((String)countryLstRed.get(i));
							gwindowcombocontrol.add(s);
						}		
		                catch(Exception exception)
		                {
							gwindowcombocontrol.add("" + i);
		                }
					}				
				}
				else
				{
					int lb = countryLstBlue.size();
					for(int i=0; i < lb; i++)
					{
						try
						{
							String s = resCountry.getString((String)countryLstBlue.get(i));
							gwindowcombocontrol.add(s);
						}		
		                catch(Exception exception)
		                {
							gwindowcombocontrol.add("" + i);
		                }					
					}
				}
	        }    		
    	}
    	catch (Exception e) {};			 	
        gwindowcombocontrol.setSelected(0, true, false);
    }    

	public void selectCountries(int indx) //By PAL
	{
		ArrayList countryLst;
		if (indx < 8) countryLst = (OUR ? countryLstRed : countryLstBlue);
			else countryLst = (OUR ? countryLstBlue : countryLstRed);
		String s = wing[indx].regiment;
        if(s != null)
        {
            Object obj = regHash.get(s);
            if(obj != null)
            {
                String s13 = ((Regiment)obj).branch();
                for(int k3 = 0; k3 < countryLst.size(); k3++)
                    if(s13.equals(countryLst.get(k3)))
                    {
                    	dlg[indx].wCountry.setSelected(k3, true, false);
                    	break;
                    }
            }
        }		
	}
     
    public void fillComboPlane(GWindowComboControl gwindowcombocontrol, boolean flag)
    {
    //gwindowcombocontrol.setEditable(true);    	
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
            //By PAL
				boolean HasCockpit = (Property.value(itemair.clazz, "cockpitClass") != null);                                  
		     	if((gwindowcombocontrol != dlg[0].wPlane)&&(!HasCockpit)) //By PAL
		     		gwindowcombocontrol.add("! " + I18N.plane(itemair.name));  //By PAL
		        else                         
            gwindowcombocontrol.add(I18N.plane(itemair.name));
        }
	//gwindowcombocontrol.setEditable(false);
        //gwindowcombocontrol.setSelected(0, true, false); //By PAL, original
        gwindowcombocontrol.setSelected(getFirstNonPlaceholder(0, flag), true, false); //By PAL, to avoid placeholders
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

    public String localize(String s)
    {
        return I18N.gui(s);
    }
 
    public GUIQuick(GWindowRoot gwindowroot)
    {
        super(14);
        russianMixedRules = "< ',' < '.' < '-' <\u0430,\u0410< a,A <\u0431,\u0411< b,B <\u0432,\u0412< v,V <\u0433,\u0413< g,G <\u0434,\u0414< d,D <\u0435,\u0415 < \u0451,\u0401 < \u0436,\u0416 < \u0437,\u0417< z,Z <\u0438,\u0418< i,I <\u0439,\u0419< j,J <\u043A,\u041A< k,K <\u043B,\u041B< l,L <\u043C,\u041C< m,M <\u043D,\u041D< n,N <\u043E,\u041E< o,O <\u043F,\u041F< p,P <\u0440,\u0420< r,R <\u0441,\u0421< s,S <\u0442,\u0422< t,T <\u0443,\u0423< u,U <\u0444,\u0424< f,F <\u0445,\u0425< h,H <\u0446,\u0426< c,C <\u0447,\u0427 < \u0448,\u0428 < \u0449,\u0429 < \u044A,\u042A < \u044B,\u042B< i,I <\u044C,\u042C < \u044D,\u042D< e,E <\u044E,\u042E < \u044F,\u042F< q,Q < x,X < y,Y";
        currentMissionName = "";
        bNoAvailableMissions = false;
        try
        {
            collator = new RuleBasedCollator(russianMixedRules);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        bFirst = true;
        b16Flights = true; //By PAL, default
        OUR = true;
        bScramble = false;
        noneTarget = true;
        pl = Config.cur.ini.get("QMB", "PlaneList", 0, 0, 3);
        if(Config.cur.ini.get("QMB", "DumpPlaneList", 0, 0, 1) > 0)
            dumpFullPlaneList();
        try
        {
	        File file = new File(/*"Missions/Quick/"*/MissionsFolder + "QMBair_" + pl + ".ini");
	        
	 //v4101       
	 /*           if(pl > 1)
	            if(!file.exists())
	                pl = 0;
	            else
	            if(!checkCustomAirIni("Missions/Quick/QMBair_" + pl + ".ini"))
	                pl = 0;
	*/
	//v410m 
	        
	        /*if(!file.exists() && pl > 1) //By PAL, if it was 2 or 3 and didn't exist the file
	            pl = 0;
	        else
	        if(!checkCustomAirIni("Missions/Quick/QMBair_" + pl + ".ini"))
	            pl = 0;*/
	            
	        //by PAL 
	            
	        if(file.exists())
	        {
	        	if (pl > 1)
	        		if(!checkCustomAirIni(/*"Missions/Quick/"*/MissionsFolder + "QMBair_" + pl + ".ini"))
	        			pl = 0;
	        }
	        else if (pl>1) pl = 0;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }        		
        r01 = "r01";
        r010 = "r010";
        g01 = "g01";
        g010 = "g010";
        if (!UseColor) //By PAL, don't use colors
        {
        	colorRed = 0x000000;
        	colorBlue = 0x000000;	
        }
        filesTargetList = new ArrayList();
                
        playerPlane = new ArrayList();
        aiPlane = new ArrayList();
        playerPlaneC = new ArrayList();
        aiPlaneC = new ArrayList();
        wing = new ItemWing[16];
        dlg = new ItemDlg[16];
        ioState = new IOState();
        bPlaneArrestor = false;
        fillArrayPlanes();
        for(int i = 0; i < 16; i++)
            wing[i] = new ItemWing(i);
                     
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = localize("quick.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        GTexture gtexture1 = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons;
        bArmy = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 0.0F, 48F, 48F));
        bReset = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 144F, 48F, 48F));
        bExit = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bBack = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bLoad = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bSave = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bFly = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        bNext = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bStat = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        bDiff = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F));

     bFMB = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48F, 48F, 48F)); //From QMBPlus
     bBrief = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture1, 64F, 48F, 32F, 32F)); //Flashing light            

        wSituation = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wMap = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wPlaneList = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
///gwindowdialogclient.addControl(wFuel = new GWindowEditControl(gwindowdialogclient, 9F, 11F, 7F, 1.3F, "") {
        wTarget = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric(), 2.0F, ""));
     wTargetList = (GWindowComboControl)dialogClient.addControl(((com.maddox.gwindow.GWindowDialogControl) (new GWindowComboControl(((GWindow) (dialogClient)), 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()))));
        wPos = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wDefence = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wAltitude = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wWeather = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wCldHeight = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wTimeHour = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wTimeMins = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wLevel = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric(), 2.0F, ""));
        sParachuteOn = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));  //By PAL
        /*wLevel.add("=");
        wLevel.add("+");
        wLevel.add("-");
        wLevel.setEditable(false);
        wLevel.setSelected(0, true, false);
        wLevel.posEnable = (new boolean[] {
            true, true, true
        });*/
        wLevel.setEditable(false); //By PAL        
        wAltitude.add("100");
        wAltitude.add("150");
        wAltitude.add("200");
        wAltitude.add("250");
        wAltitude.add("300");
        wAltitude.add("400");
        wAltitude.add("500");
        wAltitude.add("750");
        wAltitude.add("1000");
        wAltitude.add("1500");
        wAltitude.add("2000");
        wAltitude.add("3000");
        wAltitude.add("5000");
        wAltitude.add("7500");
        wAltitude.add("10000");
        wAltitude.setEditable(true);
        wAltitude.setNumericOnly(true);
        wAltitude.setSelected(8, true, false);
        wPos.add("0");
        wPos.add("500");
        wPos.add("1000");
        wPos.add("2000");
        wPos.add("3000");
        wPos.setEditable(true);
        wPos.setNumericOnly(true);
        wPos.setSelected(0, true, false);
        wTimeHour.add("00");
        wTimeHour.add("01");
        wTimeHour.add("02");
        wTimeHour.add("03");
        wTimeHour.add("04");
        wTimeHour.add("05");
        wTimeHour.add("06");
        wTimeHour.add("07");
        wTimeHour.add("08");
        wTimeHour.add("09");
        wTimeHour.add("10");
        wTimeHour.add("11");
        wTimeHour.add("12");
        wTimeHour.add("13");
        wTimeHour.add("14");
        wTimeHour.add("15");
        wTimeHour.add("16");
        wTimeHour.add("17");
        wTimeHour.add("18");
        wTimeHour.add("19");
        wTimeHour.add("20");
        wTimeHour.add("21");
        wTimeHour.add("22");
        wTimeHour.add("23");
        wTimeHour.setEditable(false);
        wTimeHour.setSelected(12, true, false);
        wTimeMins.add("00");
        wTimeMins.add("15");
        wTimeMins.add("30");
        wTimeMins.add("45");
        wTimeMins.setEditable(false);
        wTimeMins.setSelected(0, true, false);
        wWeather.add(localize("quick.CLE"));
        wWeather.add(localize("quick.GOO"));
        wWeather.add(localize("quick.HAZ"));
        wWeather.add(localize("quick.POO"));
        wWeather.add(localize("quick.BLI"));
        wWeather.add(localize("quick.RAI"));
        wWeather.add(localize("quick.THU"));
        wWeather.setEditable(false);
        wWeather.setSelected(0, true, false);
        wCldHeight.add("500");
        wCldHeight.add("750");
        wCldHeight.add("1000");
        wCldHeight.add("1250");
        wCldHeight.add("1500");
        wCldHeight.add("1750");
        wCldHeight.add("2000");
        wCldHeight.add("2250");
        wCldHeight.add("2500");
        wCldHeight.add("2750");
        wCldHeight.add("3000");
        wCldHeight.setEditable(true);
        wCldHeight.setSelected(6, true, false);
        wCldHeight.setNumericOnly(true);
        wPlaneList.add(localize("quick.STD"));
        wPlaneList.add(localize("quick.ABC"));
        try
        {
        	File file;
	        file = new File(/*"Missions/Quick/"*/MissionsFolder + "QMBair_2.ini");
	        if(file.exists() && checkCustomAirIni(/*"Missions/Quick/"*/MissionsFolder + "QMBair_2.ini"))
	            wPlaneList.add(localize("quick.CUS1"));
	        file = new File(/*"Missions/Quick/"*/MissionsFolder + "QMBair_3.ini");
	        if(file.exists() && checkCustomAirIni(/*"Missions/Quick/"*/MissionsFolder + "QMBair_3.ini"))
	            wPlaneList.add(localize("quick.CUS2"));        	
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }        
        wPlaneList.setEditable(false);
        wPlaneList.setSelected(getPlaneList(), true, false);
        wSituation.add(localize("quick.NON"));
        wSituation.add(localize("quick.ADV"));
        wSituation.add(localize("quick.DIS"));
        wSituation.setEditable(false);
        wSituation.setSelected(0, true, false);
        /*boolean aflag[] = new boolean[_targetKey.length];
        
        for(int j = 0; j < _targetKey.length; j++)
        {
            wTarget.add(localize("quick." + _targetKey[j]));
            aflag[j] = true;
        }
        wTarget.setEditable(false);
        wTarget.setSelected(0, true, false);
        wTarget.posEnable = aflag;*/       
        wTarget.setEditable(false); //By PAL, I use it       
        wDefence.add(localize("quick.NOND"));
        wDefence.add(localize("quick.AAA"));
        wDefence.setEditable(false);
        wDefence.setSelected(1, true, false);
        star = GTexture.New("GUI/Game/QM/star.mat");
        cross = GTexture.New("GUI/Game/QM/cross.mat");
        
        regList = new HashMapExt();  //By PAL, for countries            	
        regHash = new HashMapExt();
		countryLstRed = new ArrayList();  //By PAL
		countryLstBlue = new ArrayList();  //By PAL
        fillArrayCountry(); //By PAL, for countries 
               
        for(int k = 0; k < 16; k++)
        {
            dlg[k] = new ItemDlg();
            dlg[k].wNum = (WComboNum)dialogClient.addControl(new WComboNum(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
            dlg[k].wSkill = (WComboSkill)dialogClient.addControl(new WComboSkill(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
            dlg[k].wPlane = (WComboPlane)dialogClient.addControl(new WComboPlane(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
            dlg[k].wLoadout = (WComboLoadout)dialogClient.addControl(new WComboLoadout(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
            dlg[k].wCountry = (WComboCountry)dialogClient.addControl(new WComboCountry(k, dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
            dlg[k].bArming = (WButtonArming)dialogClient.addControl(new WButtonArming(k, dialogClient, gtexture1, 0.0F, 48F, 32F, 32F));
            dlg[k].wNum.setEditable(false);
            if(k == 0)
            {
                for(int l = 1; l < 5; l++)
                    dlg[k].wNum.add("" + l);

            } else
            {
                for(int i1 = 0; i1 < 5; i1++)
                    dlg[k].wNum.add("" + i1);

            }
            dlg[k].wNum.setSelected(0, true, false);
            dlg[k].wSkill.setEditable(false);
            dlg[k].wSkill.add(localize("quick.ROO"));
            dlg[k].wSkill.add(localize("quick.EXP"));
            dlg[k].wSkill.add(localize("quick.VET"));
            dlg[k].wSkill.add(localize("quick.ACE"));
            dlg[k].wSkill.setSelected(1, true, false);
            dlg[k].wPlane.setEditable(false);
            //dlg[k].wPlane.listVisibleLines = 16;
            fillComboPlane(dlg[k].wPlane, k == 0);
            dlg[k].wLoadout.setEditable(false);
            fillComboWeapon(dlg[k].wLoadout, wing[k].plane, 0);
            fillComboCountry(dlg[k].wCountry, k); //By PAL
        }
        
        //+++ TODO: 4.12 changed code +++
        // Adjusted by SAS~Storebror: Wide/Narrow Screen Proportions taken into consideration
//		dlg[0].wPlane.listVisibleLines = Config.cur.ini.get("Mods", "PALQMBLinesPl0", 30); //By PAL, CirX to expand default list//By PAL requested by CirX, original 16;
		dlg[0].wPlane.listVisibleLines = Config.cur.ini.get("Mods", "PALQMBLinesPl0", ((GUIRoot)gwindowroot.root).isWide() ? 26 : 30); //By PAL, CirX to expand default list//By PAL requested by CirX, original 16;
		//--- TODO: 4.12 changed code ---
		
		for (int i = 1; i < 16; i++)
			dlg[i].wPlane.listVisibleLines = Config.cur.ini.get("Mods", "PALQMBLinesPl" + i, 18); //By PAL, CirX to expand default list//By PAL requested by CirX, original 16;
        wMap.listVisibleLines = Config.cur.ini.get("Mods", "PALQMBLinesMap", 28); //By PAL, CirX to expand default list          
        fillMapKey();
        if (_mapKey.length > 0)
        	wMap.setSelected(0, true, false); //Original
        else
        	wMap.setSelected(-1, false, false); //By PAL, considered the chance that there are no maps.
        wMap.setEditable(false);
        onArmyChange();
// ****************Check it here ******************        
     //mapChanged(true); //By PAL, is that correct?                                                                                             
        defaultRegiments(true); //By PAL, to avoid cleaning, before it was false
        updateCountries();       
        dialogClient.activateWindow();
        //showHide();
        client.hideWindow();
    }

    private void dumpFullPlaneList()
    {
    	try
    	{ //By PAL
	        SectFile sectfile = new SectFile("com/maddox/il2/objects/air.ini");
	        SectFile sectfile1 = new SectFile(/*"./Missions/Quick/"*/MissionsFolder + "FullPlaneList.dump", 1);
	        sectfile1.sectionAdd("AIR");
	        int i = sectfile.vars(sectfile.sectionIndex("AIR"));
	        for(int j = 0; j < i; j++)
	            sectfile1.varAdd(0, sectfile.var(0, j));    		
    	}
    	catch (Exception e)
    	{
			System.out.println("Error creating FullPlaneList.dump: probably Mission Folder doesn't exist");
    	}


    }

    private void fillMapKey()
    {
    	try //By PAL
    	{
	        DirFilter dirfilter = new DirFilter();
	        folderNames = (new File(MissionsFolder/*"Missions/Quick/"*/)).list(dirfilter);
	        if(folderNames != null)
	        {
	            _mapKey = new String[folderNames.length];
	            for(int i = 0; i < _mapKey.length; i++)
	                _mapKey[i] = folderNames[i];
	
	        } else
	        {
	            _mapKey = new String[1];
	            _mapKey[0] = localize("quick.NOMISSIONS");
	            bNoAvailableMissions = true;
	        }    		
    	}
    	catch (Exception e)
    	{
    		//bNoAvailableMissions = true;
    	}
        resetwMap();
    }

    //+++ TODO: 4.12 SAS~Storebror new methods +++
    //
    // Make sure to list only missions where maps are available!
//    private ArrayList getAvailableMissionsS(String s, String s1)
//    {
//        File file = new File(MissionsFolder/*"Missions/Quick/"*/ + s);
//        String as[] = file.list();
//        ArrayList arraylist = new ArrayList();
//        arraylist.clear();
//        if(as == null)
//            return arraylist;
//        for(int i = 0; i < as.length; i++)
//            if(as[i].startsWith(s1) && as[i].endsWith(".mis"))
//                arraylist.add(as[i]);
//
//        return arraylist;
//    }

    private static boolean existSFSFile(String s)
    {
    	try {
	        SFSInputStream sfsinputstream = new SFSInputStream(s);
	        sfsinputstream.close();
	        return true;
    	} catch (Exception e) {}
        return false;
    }

    private ArrayList getAvailableMissionsS(String s, String s1)
    {
        File file = new File(MissionsFolder + s);
        String as[] = file.list();
        ArrayList arraylist = new ArrayList();
        arraylist.clear();
        if(as == null)
            return arraylist;
        String s2;
        for(int i = 0; i < as.length; i++) {
            if(as[i].startsWith(s1) && as[i].endsWith(".mis")) {
            	if (aKnownGoodMissions.contains(as[i])) {
                    arraylist.add(as[i]);
                    continue;
            	}
            	if (aKnownBadMissions.contains(as[i])) {
                    continue;
            	}
            	String currentMissionName = MissionsFolder + s + "/" + as[i];
                try
                {
                    SectFile sectfile = new SectFile(currentMissionName, 0, false);
                    if (sectfile.sections() == 0) {
                    	continue;
                    }
                    s2 = sectfile.get("MAIN", "MAP");
                    if(s2 == null) {
                    	continue;
                    }
                    if (aKnownBadMaps.contains(s2)) {
//                    	System.out.println("Can't use QMBPro mission " + as[i] + " since the regarding map file " + s2 + " is missing.");
                    	aKnownBadMissions.add(as[i]);
                    	continue; 
                    }
                    if (!existSFSFile("maps/" + s2)) {
//                    	System.out.println("Can't use QMBPro mission " + as[i] + " since the regarding map file " + s2 + " is missing.");
                    	aKnownBadMaps.add(s2);
                    	aKnownBadMissions.add(as[i]);
                    	continue;
                    }
                    aKnownGoodMissions.add(as[i]);
                    arraylist.add(as[i]);
                }
                catch(Exception exception)
                {
                    continue;
                }
            }
        }
        return arraylist;
    }
    //--- TODO: 4.12 SAS~Storebror new methods ---
    
    private String getArmyString()
    {
        if(OUR)
            return "Red";
        else
            return "Blue";
    }

    private void resetwMap()
    {
        wMap.clear();
        if(bNoAvailableMissions) //By PAL, no missions at all
        {
            wMap.add(localize("quick.NOMISSIONS"));
            return;
        }
        for(int i = 0; i < _mapKey.length; i++)
        {
            if(getAvailableMissionsS(_mapKey[i], _mapKey[i] + getArmyString()).size() <= 0)
                continue;
            String s = _mapKey[i];
            String s1 = I18N.map(s);
            if(!s.equals(s1))
                wMap.add(s1);
            else
                wMap.add(I18N.map(_mapKey[i]));
        }
    }

    private String getMapName()
    {
        String s = wMap.getValue();
        for(int i = 0; i < _mapKey.length; i++)
        {
            String s1 = _mapKey[i];
            String s2 = I18N.map(s1);
            if(s2.equals(s))
                return s1;
        }

        return s;
    }

   /* private void onArmyChange()
    {
        String s = getMapName();
        String s1 = wMap.getValue();
        resetwMap();
        int i = wMap.list.indexOf(s1);
        if(getAvailableMissionsS(s, s + getArmyString()).size() > 0)
            wMap.setSelected(i, true, false);
        else
            wMap.setSelected(0, true, false);
        onTargetChange();
    }*/


    private void onArmyChange() //By PAL, checks if there are missions for new map, if there are, accepts the map
    {
    	//String prevTarget = wTargetList.getValue();
        String s = getMapName();
        String s1 = wMap.getValue();
        resetwMap();
        int i = wMap.list.indexOf(s1); //By PAL, I let know in the other sentence
        if(getAvailableMissionsS(s, s + getArmyString()).size() > 0)
        {
            wMap.setSelected(i, true, false);        	
	        updateMissionsList(); //By PAL
	        processTargetList();
        }
        else
        {
            wMap.setSelected(-1, false, false); //By PAL Error!, it cannot be 0 because there is no mission at All, so -1
			//bNoAvailableMissions = true;
        }
        //onTargetChange();       		        
        setColors();
    }

	private void updateMissionsList()
	{		
		int firstNone = -1;
        int newIndex = -1;
        int newTypeIndex = -1;           
		String lastMission = "";
		if (wTargetList.getSelected() >= 0)
			lastMission = (String)filesTargetList.get(wTargetList.getSelected());//By PAL, file name - wTargetList.getValue(); //By PAL, todavía dice la última que hubo seleccionada
		String lastTypeMis = "";
            
        if (!(lastMission.toLowerCase()).startsWith(getArmyString().toLowerCase())) //By PAL, invert the army to find equivalent mission
        {
        	if ((lastMission.toLowerCase()).startsWith("red"))
        		lastMission = "Blue" + lastMission.substring("red".length());
        	else
        	if ((lastMission.toLowerCase()).startsWith("blue"))
        		lastMission = "Red" + lastMission.substring("blue".length());         	        	
        }
        		
		if (lastMission.length() >= 2)
			lastTypeMis = lastMission.substring(0, lastMission.length() - 2); //By PAL, to remove the number		
		ArrayList arraylist = getAvailableMissionsS(getMapName(), getMapName() + getArmyString()); //By PAL
        if (arraylist.size() == 0)
        {
       		wTargetList.setValue("(No missions for " + getMapName() + ")");        	
        }
       	else
       	{
	        wTargetList.clear();
	        filesTargetList.clear();       		
	        for (int j=0; j < arraylist.size(); j++)
	        {
	        	String sm = (String)arraylist.get(j);
	        	String nToAdd = sm.substring(getMapName().length(), (sm.toLowerCase()).lastIndexOf(".mis"));
	        	filesTargetList.add(nToAdd);
	        	String s = getName(MissionsFolder + getMapName() + "/" + sm);
	        	if (s != "") wTargetList.add(s);  //By PAL, now I add the name of the missions if they exist
	        		else wTargetList.add(nToAdd);
	        	if (nToAdd.equalsIgnoreCase(lastMission)) newIndex = j;
	        	if (newTypeIndex == -1 && nToAdd.length() >= 2)
	        		if ((nToAdd.substring(0, nToAdd.length() - 2)).equalsIgnoreCase(lastTypeMis)) newTypeIndex = j;
	        	if (firstNone == -1)
	        		if ((sm.toLowerCase()).lastIndexOf("none") > -1) firstNone = j; //Dejar el primero que sea None por si acaso
	        }
	        if (newIndex > -1)
	        {
	        	wTargetList.setSelected(newIndex, true, false);
	        }
	        else
	        if (newTypeIndex > -1)
	        {
	        	wTargetList.setSelected(newTypeIndex, true, false);
	        }	        	
	        else
	        if (firstNone > -1)	        	
	        { 
				wTargetList.setSelected(firstNone, true, false);
	        }
	        else wTargetList.setSelected(0, true, false);       		
       	}       			    		
	}

	private String getName(String s) //By PAL, to fetch Mission Title
	{				
	    for(int i1 = s.length() - 1; i1 > 0; i1--)
	    {
	        char c = s.charAt(i1);
	        if(c == '\\' || c == '/')
	            break;
	        if(c != '.')
	            continue;
	        s = s.substring(0, i1);
	        break;
	    }
	    String dP = "";
	    try
	    {
	        ResourceBundle resourcebundle1 = ResourceBundle.getBundle(s, RTSConf.cur.locale);
	        String st = resourcebundle1.getString("Name");
		    for(int i1 = 0; i1 < st.length(); i1++)
		    {
		        char c = st.charAt(i1);
		        dP = (c == '\n') ? dP + " " : dP + c;
		    }
		    if (dP.length() < 2) dP = "";	                    	
	    }
	    catch(Exception exception) {}	            	            					                           
	    return dP;
	}

	private String getShort(String s) //By PAL, to fetch Mission Short Description
	{			
	    for(int i1 = s.length() - 1; i1 > 0; i1--)
	    {
	        char c = s.charAt(i1);
	        if(c == '\\' || c == '/')
	            break;
	        if(c != '.')
	            continue;
	        s = s.substring(0, i1);
	        break;
	    }
	    String dP = "";
	    try
	    {
	        ResourceBundle resourcebundle1 = ResourceBundle.getBundle(s, RTSConf.cur.locale);
	        String st = resourcebundle1.getString("Short");
		    for(int i1 = 0; i1 < st.length(); i1++)
		    {
		        char c = st.charAt(i1);
		        dP = (c == '\n') ? dP + " " : dP + c;
		    }
		    if (dP.length() < 2) dP = "";			    	                    	
	    }
	    catch(Exception exception) {}	            	            					                           
	    return dP;
	}

	private String getDesc(String s) //By PAL, to fetch Mission Porperties
	{				
	    for(int i1 = s.length() - 1; i1 > 0; i1--)
	    {
	        char c = s.charAt(i1);
	        if(c == '\\' || c == '/')
	            break;
	        if(c != '.')
	            continue;
	        s = s.substring(0, i1);
	        break;
	    }
	    String dP = "";
	    try
	    {
	        ResourceBundle resourcebundle1 = ResourceBundle.getBundle(s, RTSConf.cur.locale);
	        String st = resourcebundle1.getString("Description");
		    if (dP.length() < 2 || dP == null) dP = st;	        	            	
	    }
	    catch(Exception exception) {}	            	            					                           
	    return dP;
	}

	private void processTargetList()
	{
		if(bNoAvailableMissions)
		{
			wLevel.setValue("(No Missions)");
            wTarget.setValue("");
            noneTarget = false; 
            bScramble = false;			
			return;
		}
		noneTargetNum = 0;	
    	String ar = getArmyString().toLowerCase();
    	String s = "";
    	if(wTargetList.getSelected() >= 0)
    		s = ((String)filesTargetList.get(wTargetList.getSelected())).toLowerCase();
        if(s.startsWith(ar + "nonen"))
        {
            noneTarget = true;
            wTarget.setValue("None, Neutral");
            bScramble = false;
        }
        else
        if(s.startsWith(ar + "nonea"))
        {
            noneTarget = true;
            noneTargetNum = 1;
            wTarget.setValue("None, Adv.");
            bScramble = false;            
        }
        else
        if(s.startsWith(ar + "noned"))
        {
            noneTarget = true;
            noneTargetNum = -1;            
            wTarget.setValue("None, Dis.");
            bScramble = false;            
        }
        else
        if(s.startsWith(ar + "none"))
        {
            noneTarget = true;
            wTarget.setValue(_targetKey[NONE]);                    	
            bScramble = false; 
        }
        else        
        if(s.startsWith(ar + "armor"))
        {
            wTarget.setValue(_targetKey[ARMOR]);
            noneTarget = false;      	
            bScramble = false;                            	
        }
        else
        if(s.startsWith(ar + "bridge"))
        {
            wTarget.setValue(_targetKey[BRIDGE]);
            noneTarget = false;        	
            bScramble = false;                             	
        }
        else
        if(s.startsWith(ar + "airbase"))
        {
            wTarget.setValue(_targetKey[AIRBASE]);
            noneTarget = false;      	
            bScramble = false;                             	
        }
        else                                                            	
        if(s.startsWith(ar + "scramble"))
        {
            wTarget.setValue(_targetKey[SCRAMBLE]);                	                               	
            bScramble = true;
            noneTarget = false;                           	
        }
        else
        if(s.startsWith(ar + "strike")) //By PAL, no originally in TargetKey
        {
            wTarget.setValue(_targetKey[STRIKE]);                	                               	
            bScramble = false;
            noneTarget = false;                            	
        }        
        else
        {
        	s = "";
	    	if(wTargetList.getSelected() >= 0)
	    	{
	    		s = ((String)filesTargetList.get(wTargetList.getSelected()));
	    		if (s.length() > 0)       	       		 
			    	for(int i = s.length() - 1; i >= ar.length(); i--)
			    	{
			    	    if (Character.isDigit(s.charAt(i))) continue; //By PAL, go to the first Alpha
			    	    s = s.substring(ar.length(), i + 1);
			    	    break; 
		         	}	         	    	        	 	    		
	    	}
        	wTarget.setValue(s);
            noneTarget = false;       	
            bScramble = false;	    	                           	
        }
        currentMissionName = /*"Missions/Quick/"*/MissionsFolder + getMapName() + "/" + getMapName() + s + ".mis";  //By PAL, to use new ComboBox 
        sShort = getShort(currentMissionName); //By PAL, get Properties
		if (sShort.length() < 2)
		{
			wLevel.setValue("(No Description)");
			wLevel.setEnable(false);
		}	
        else
        {
        	wLevel.setValue(sShort); //By PAL, show Short here
        	wLevel.setEnable(true);
        }       	
        sDesc = getDesc(currentMissionName);
        setShow(bFirst && (sDesc.length() > 2), (bBrief)); //By PAL             	
	}    

 /*   private void onTargetChange()
    {
        int i = wTarget.getSelected();
        int j = 0;
        for(int k = wTarget.size() - 1; k > -1; k--)
            if(getAvailableMissionsS(getMapName(), getMapName() + getArmyString() + _targetKey[k]).size() > 0)
            {
                wTarget.posEnable[k] = true;
                j = k;
            } else
            {
                wTarget.posEnable[k] = false;
            }

        if(wTarget.posEnable[i])
            wTarget.setSelected(i, true, false);
        else
            wTarget.setSelected(j, true, false);
        checkNoneTarget();
    }*/

//By PAL, I don't require this anymor    
/*    private void checkNoneTarget()
    {
        boolean flag = false;
        int j = 0;
        if(wTarget.getSelected() == 0)
        {
            noneTarget = true;
            int i = wLevel.getSelected();
            for(int k = wLevel.size() - 1; k > -1; k--)
                if(getAvailableMissionsS(getMapName(), getMapName() + getArmyString() + _targetKey[0] + noneTargetSuffix[k]).size() > 0)
                {
                    wLevel.posEnable[k] = true;
                    j = k;
                } else
                {
                    wLevel.posEnable[k] = false;
                }

            if(wLevel.posEnable[i])
                wLevel.setSelected(i, true, false);
            else
                wLevel.setSelected(j, true, false);
            wLevel.showWindow();
        } else
        {
            noneTarget = false;
            wLevel.hideWindow();
        }
    }*/

    private void showHide()
    {
        for(int i = 0; i < 4; i++)
        {
            setShow(bFirst, dlg[i].wNum);
            setShow(bFirst, dlg[i].wSkill);
            setShow(bFirst, dlg[i].wPlane);
            setShow(bFirst, dlg[i].wLoadout);
            setShow(bFirst, dlg[i].wCountry); //By PAL            
            setShow(bFirst, dlg[i].bArming);
            setShow(bFirst, dlg[i + 8].wNum);
            setShow(bFirst, dlg[i + 8].wSkill);
            setShow(bFirst, dlg[i + 8].wPlane);
            setShow(bFirst, dlg[i + 8].wLoadout);
            setShow(bFirst, dlg[i + 8].wCountry);
            setShow(bFirst, dlg[i + 8].bArming);
        }

        for(int j = 4; j < 8; j++)
        { //By PAL, b16Flights to hide comboboxes if an 8 Flights mission
            setShow(!bFirst && b16Flights, dlg[j].wNum);
            setShow(!bFirst && b16Flights, dlg[j].wSkill);
            setShow(!bFirst && b16Flights, dlg[j].wPlane);
            setShow(!bFirst && b16Flights, dlg[j].wLoadout);
            setShow(!bFirst && b16Flights, dlg[j].wCountry);  //By PAL           
            setShow(!bFirst && b16Flights, dlg[j].bArming);
            setShow(!bFirst && b16Flights, dlg[j + 8].wNum);
            setShow(!bFirst && b16Flights, dlg[j + 8].wSkill);
            setShow(!bFirst && b16Flights, dlg[j + 8].wPlane);
            setShow(!bFirst && b16Flights, dlg[j + 8].wLoadout);
            setShow(!bFirst && b16Flights, dlg[j + 8].wCountry);            
            setShow(!bFirst && b16Flights, dlg[j + 8].bArming);
        }

        setShow(bFirst, (wPlaneList));
        setShow(bFirst, (wTarget));
        setShow(bFirst, (wTargetList)); //By PAL        
        setShow(bFirst, (wMap));
        setShow(bFirst, (bNext));
        setShow(bFirst, (bExit));
        setShow(bFirst, (bArmy));
        setShow(bFirst, (wAltitude));
        setShow(bFirst, (wDefence));
        setShow(bFirst, (wSituation));
        setShow(bFirst, (wPos));
        setShow(bFirst && (sDesc.length() > 2), (bBrief)); //By PAL
        //setShow(bFirst, (bReset));
        setShow(bFirst, (wLevel)); //By PAL        
        //setShow(bFirst && noneTarget, (wLevel));
        setShow(!bFirst, (bBack));
        setShow(!bFirst, (bStat));
        setShow(!bFirst, (wWeather));
        setShow(!bFirst, (wTimeHour));
        setShow(!bFirst, (wTimeMins));
        setShow(!bFirst, (wCldHeight));
        setShow(!bNoAvailableMissions, (bFly));
        setShow(!bFirst, (sParachuteOn)); //By PAL        
        dialogClient.doResolutionChanged();
        dialogClient.setPosSize();
    }

    private void setShow(boolean flag, GWindow gwindow)
    {
        if(flag)
            gwindow.showWindow();
        else
            gwindow.hideWindow();
    }

    private void setDefaultValues()
    {
        wAltitude.setSelected(8, true, false);
        wPos.setSelected(0, true, false);
        wTimeHour.setSelected(12, true, false);
        wTimeMins.setSelected(0, true, false);
        wWeather.setSelected(0, true, false);
        wCldHeight.setSelected(6, true, false);
        wDefence.setSelected(1, true, false);
        wSituation.setSelected(0, true, false);
        for(int i = 0; i < 16; i++)
        {       	
        	//dlg[i].wNum.setSelected(0, true, false);
            dlg[i].wSkill.setSelected(1, true, false);
            dlg[i].wPlane.setSelected(0, true, false);
            dlg[i].wLoadout.setSelected(0, true, false);
			//dlg[i].wCountry.setSelected(0, true, false); //By PAL            
            dlg[i].wSkill.notify(2, 0);
            dlg[i].wPlane.notify(2, 0);
            dlg[i].wLoadout.notify(2, 0);
            //dlg[i].wCountry.notify(2, 0); //By PAL
            dlg[i].wNum.setSelected(0, true, false); //By PAL, last to avoid problems
            dlg[i].wNum.notify(2, 0);            
            UpdateComboNum(i); //By PAL, it should disable the fields
        }
        for(int k = 0; k < 16; k++)
            if(k <= 7)
                wing[k].regiment = OUR ? r01 : g01;
            else
                wing[k].regiment = OUR ? g01 : r01;        
        for(int i = 0; i < 16; i++) //By PAL, is it necessary here?
        {
        	fillComboCountry(dlg[i].wCountry, i);  //By PAL 
        	selectCountries(i); //By PAL, to select the corresponding Country in combobox
        }        
    }

    static Class _mthclass$(String s)
    {
        Class class1;
        try
        {
            class1 = Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
        return class1;
    }

		private int colorRed = 0xc01010dd; //By PAL, color Red
		private int colorBlue = 0xc0dd1010; //By PAL, coolor Blue		                  
        private boolean UseColor = Config.cur.ini.get("Mods", "PALMODsColor", true);
        private String MissionsFolder = Config.cur.ini.get("Mods", "PALQMBMissions", "Missions/QuickQMBPro/");
        public ArrayList countryLstRed, countryLstBlue;
    	public ResourceBundle resCountry;
    	public HashMapExt regList;
    	public HashMapExt regHash; 
        private String QMBVer = "QMBPro V4.12m (by P.A.L.)";    		   
    	private String FMBLabel = "F.M.B.";
    	private String BriefLabel = "Brief:";
    	private String NoBriefLabel = "(No Brief)";
    	
    	private String PlaceholderLabel = "air.Placeholder";	
    	private String music; //By PAL, to store the corresponding music label
    	private String sDesc, sShort; //By PAL, Derscription and short    	  	

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public SectFile ssect;
    public GTexture cross;
    public GTexture star;
    public boolean OUR;
    public boolean bScramble;
    public GUIButton bArmy;
    public GUIButton bNext;
    public GUIButton bStat;
    public GUIButton bExit;
    public GUIButton bBack;
    public GUIButton bLoad;
    public GUIButton bSave;
    public GUIButton bFly;
    public GUIButton bDiff;
    public GUIButton bReset;
    public GUIButton bFMB; //By PAL
    public GUIButton bBrief; //By PAL
    public GUISwitchBox3 sParachuteOn; //By PAL       
    public GWindowComboControl wSituation;
    public GWindowComboControl wMap;
    public GWindowEditControl wTarget; //By PAL
    public GWindowComboControl wTargetList; //By PAL      
    public GWindowComboControl wPos;
    public GWindowComboControl wDefence;
    public GWindowComboControl wAltitude;
    public GWindowComboControl wWeather;
    public GWindowComboControl wCldHeight;
    public GWindowComboControl wTimeHour;
    public GWindowComboControl wTimeMins;
    public GWindowEditControl wLevel;
    public GWindowComboControl wPlaneList;
    private String r01;
    private String r010;
    private String g01;
    private String g010;
    private String _mapKey[];
    private String folderNames[];
    private String _targetKey[] = {
        "None", "Armor", "Bridge", "Airbase", "Scramble", "Strike"
    };
    private ArrayList playerPlane;
    private ArrayList aiPlane;
    private ArrayList playerPlaneC;
    private ArrayList aiPlaneC;
    private ItemWing wing[];
    private ItemDlg dlg[];
    private IOState ioState;
    private int indxAirArming;
    private boolean bPlaneArrestor;
    private boolean bFirst;
    private boolean b16Flights; //By PAL
    private ArrayList filesTargetList;  //By PAL
    static boolean bIsQuick;
    private static int pl;
    //private static final String PREFIX = "Missions/Quick/";
    private boolean noneTarget;
    private int noneTargetNum = 0; //By PAL
//    private String noneTargetSuffix[] = {
//        "N", "A", "D"
//    };
    private static final int NONE = 0;
    private static final int ARMOR = 1;
    private static final int BRIDGE = 2;
    private static final int AIRBASE = 3;
    private static final int SCRAMBLE = 4;
    private static final int STRIKE = 4;
//    private static final int ADVANTAGE = 1;
    private String currentMissionName;
    private boolean bNoAvailableMissions; //By PAL, made static
    private String planeListName;
    private String russianMixedRules;
    private RuleBasedCollator collator;
    static Class class$com$maddox$il2$objects$air$TypeTransport; /* synthetic field */

    //+++ TODO: 4.12 SAS~Storebror new properties +++
    private static ArrayList aKnownGoodMissions = new ArrayList();
    private static ArrayList aKnownBadMissions = new ArrayList();
    private static ArrayList aKnownBadMaps = new ArrayList();
    //--- TODO: 4.12 SAS~Storebror new properties ---
    
    static 
    {
        ObjIO.fields(com.maddox.il2.gui.GUIQuick.IOState.class, new String[] {
            "our", "situation", "map", "target", "defence", "altitude", "weather", "timeH", "timeM", "pos", 
            "cldheight", "scramble", "noneTARGET"
        });
        ObjIO.validate(com.maddox.il2.gui.GUIQuick.IOState.class, "loaded");
        ObjIO.fields(com.maddox.il2.gui.GUIQuick.ItemWing.class, new String[] {
            "planes", "weapon", "regiment", "skin", "noseart", "pilot", "numberOn", "fuel", "skill"
        });
        ObjIO.accessStr(com.maddox.il2.gui.GUIQuick.ItemWing.class, "plane", "getPlane", "setPlane");
        ObjIO.validate(com.maddox.il2.gui.GUIQuick.ItemWing.class, "loaded");
        //+++ TODO: 4.12 SAS~Storebror new properties +++
        aKnownGoodMissions.clear();
        aKnownBadMissions.clear();
        aKnownBadMaps.clear();
        //--- TODO: 4.12 SAS~Storebror new properties ---
    }
}