//////////////////////////////////////////////////////////////////////
//	By PAL - MODded to remember last Directories / Files
//	Review for 4.111m
//  lifted to 4.12 by SAS~Storebror
//////////////////////////////////////////////////////////////////////

package com.maddox.il2.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.gwindow.GWindowTable;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.HomePath;
import com.maddox.rts.IniFile;
import com.maddox.rts.LDRres;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.il2.ai.World;


public class GUISingleSelect extends GameState
{
    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == wPrev)
            {      		
                Main.stateStack().pop();
                return true;
            }
            if(gwindow == wCountry)
            {
                fillDirs();
                int k = wCountry.getSelected();
                if(k >= 0)
                {
                    Main3D.menuMusicPlay((String)countryLst.get(k));
                    ((GUIRoot)root).setBackCountry("single", (String)countryLst.get(k), wDirs.getValue());
                    //By PAL, old: ((GUIRoot)root).setBackCountry("single", (String)countryLst.get(k));
                }
                return true;
            }
            if(gwindow == wDirs)
            {
                fillFiles();
//By PAL, new in 4.111:
                int l = wCountry.getSelected();
                if(l >= 0)
                    ((GUIRoot)root).setBackCountry("single", (String)countryLst.get(l), wDirs.getValue());

                return true;
            }
            if(gwindow == wNext)
            {
                if(wDirs.getValue() == null)
                    return true;
                int l = wTable.selectRow;
                if(l < 0 || l >= wTable.files.size())
                {
                    return true;
                } else
                {
                    FileMission filemission = (FileMission)wTable.files.get(l);
//                    int i1 = wCountry.getSelected();
                    Main.cur().currentMissionFile = new SectFile("missions/single/" + country + "/" + wDirs.getValue() + "/" + filemission.fileName, 0);
                    Main.stateStack().push(4);
                    return true;
                }
            } else
            {
                return super.notify(gwindow, i, j);
            }
        }

        public void render()
        {
            super.render();
            GUISeparate.draw(this, GColor.Gray, x1024(432F), y1024(546F), x1024(384F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(416F), y1024(32F), 2.0F, y1024(608F));
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(64F), y1024(40F), x1024(240F), y1024(32F), 0, i18n("singleSelect.Country"));
            draw(x1024(64F), y1024(156F), x1024(240F), y1024(32F), 0, i18n("singleSelect.MissType"));
            draw(x1024(64F), y1024(264F), x1024(240F), y1024(32F), 0, i18n("singleSelect.Miss"));
            draw(x1024(464F), y1024(264F), x1024(248F), y1024(32F), 0, i18n("singleSelect.Desc"));
            draw(x1024(104F), y1024(592F), x1024(192F), y1024(48F), 0, i18n("singleSelect.MainMenu"));
            draw(x1024(528F), y1024(592F), x1024(216F), y1024(48F), 2, i18n("singleSelect.Brief"));
        }

        public void setPosSize()
        {
            set1024PosSize(80F, 64F, 848F, 672F);
            wPrev.setPosC(x1024(56F), y1024(616F));
            wNext.setPosC(x1024(792F), y1024(616F));
            wCountry.setPosSize(x1024(48F), y1024(80F), x1024(336F), M(2.0F));
            wDirs.setPosSize(x1024(48F), y1024(192F), x1024(336F), M(2.0F));
            wTable.setPosSize(x1024(48F), y1024(304F), x1024(336F), y1024(256F));
            wDescript.setPosSize(x1024(448F), y1024(312F), x1024(354F), y1024(212F));
        }

        public DialogClient()
        {
        }
    }

    public class WDescript extends GWindow
    {

        public void render()
        {
            String s = null;
            if(wTable.selectRow >= 0)
            {
                s = ((FileMission)wTable.files.get(wTable.selectRow)).description;
                if(s != null && s.length() == 0)
                    s = null;
            }
            if(s != null)
            {
                setCanvasFont(0);
                setCanvasColorBLACK();
                drawLines(0.0F, -root.C.font.descender, s, 0, s.length(), win.dx, root.C.font.height);
            }
        }

        public WDescript()
        {
        }
    }

    public class Table extends GWindowTable
    {

        public int countRows()
        {
            return files == null ? 0 : files.size();
        }

        public void renderCell(int i, int j, boolean flag, float f, float f1)
        {
            setCanvasFont(0);
            String s = ((FileMission)files.get(i)).name;
            if(flag)
            {
                setCanvasColorBLACK();
                draw(0.0F, 0.0F, f, f1, lookAndFeel().regionWhite);
                setCanvasColorWHITE();
                draw(0.0F, 0.0F, f, f1, 0, s);
            } else
            {
                setCanvasColorBLACK();
                draw(0.0F, 0.0F, f, f1, 0, s);
            }
        }

        public void afterCreated()
        {
            super.afterCreated();
            bColumnsSizable = false;
            addColumn(I18N.gui("singleSelect.MissFiles"), null);
            vSB.scroll = rowHeight(0);
            resized();
        }

        public void resolutionChanged()
        {
            vSB.scroll = rowHeight(0);
            super.resolutionChanged();
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

        public ArrayList files;

        public Table(GWindow gwindow)
        {
            super(gwindow, 2.0F, 4F, 20F, 16F);
            files = new ArrayList();
            bNotify = true;
            wClient.bNotify = true;
        }
    }

    static class FileMission
    {

        public String fileName;
        public String name;
        public String description;

        public FileMission(String s, String s1)
        {
            fileName = s1;
            try
            {
                String s2 = s1;
                int i = s2.lastIndexOf(".");
                if(i >= 0)
                    s2 = s2.substring(0, i);
                ResourceBundle resourcebundle = ResourceBundle.getBundle(s + "/" + s2, RTSConf.cur.locale);
                name = resourcebundle.getString("Name");
                description = resourcebundle.getString("Short");
            }
            catch(Exception exception)
            {
                name = s1;
                description = null;
            }
        }
    }

    public void _enter()
    {
        init();
        int i = wCountry.getSelected();
        if(i >= 0)
            Main3D.menuMusicPlay((String)countryLst.get(i));
        client.activateWindow();
    }

    public void _leave()
    {
    	try
    	{
    		IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);  //By PAL, save last settings
			inifile.set("LastSingleMission", "LastCountry", wCountry.getSelected());
			inifile.set("LastSingleMission", "LastDir", wDirs.getSelected());
			inifile.set("LastSingleMission", "LastMission", wTable.selectRow);
			inifile.saveFile();
    	}
    	catch(Exception exception)
		{
    		System.out.println(exception.getMessage());
    		exception.printStackTrace();
		}    	
        client.hideWindow();
    }

    private void init()
    {
        if(bInited)
            return;
        resCountry = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
        _scanMap.put(resCountry.getString("ru"), "ru");
        _scanMap.put(resCountry.getString("de"), "de");
        File file = new File(HomePath.get(0), "missions/single");
        if(file != null)
        {
            File afile[] = file.listFiles();
            if(afile != null)
            {
                for(int i = 0; i < afile.length; i++)
                {
                    if(!afile[i].isDirectory() || afile[i].isHidden())
                        continue;
                    String s1 = afile[i].getName().toLowerCase();
                    String s2 = null;
                    try
                    {
                        s2 = resCountry.getString(s1);
                    }
                    catch(Exception exception)
                    {
                        continue;
                    }
                    if(!_scanMap.containsKey(s2))
                        _scanMap.put(s2, s1);
                }
            }
        }
        String s;
        for(Iterator iterator = _scanMap.keySet().iterator(); iterator.hasNext(); wCountry.add(s))
        {
            s = (String)iterator.next();
            countryLst.add(_scanMap.get(s));
        }

        _scanMap.clear();
        wCountry.setSelected(-1, false, true);
        if(countryLst.size() > 0)
        {
        	try		//By PAL set Last Mission
        	{   //wCountry.setSelected(0, true, true);
        		IniFile inifile = new IniFile(World.cur().userCfg.iniFileName(), 1);  //By PAL, load last settings
        		int LastCountry = inifile.get("LastSingleMission", "LastCountry", 0);
        		int LastDir = inifile.get("LastSingleMission", "LastDir", 0);
        		int LastMission = inifile.get("LastSingleMission", "LastMission", 0);
        		if (LastCountry < wCountry.size()) //By PAL
        		{
        			wCountry.setSelected(LastCountry, true, true);
        			if (LastDir < wDirs.size()) //By PAL
        			{
		        		wDirs.setSelected(LastDir, true, true);
		        		if (LastMission < wTable.countRows()) //By PAL
		        		  wTable.setSelect(LastMission, 0);        				
        			}        			        			
        		}
            }
            catch(Exception exception)
        	{
            	System.out.println(exception.getMessage());
            	exception.printStackTrace();
        	}        		
        }
        bInited = true;
    }

    public void fillDirs()
    {
        countryIcon = null;
        country = null;
        int i = wCountry.getSelected();
        if(i < 0)
        {
            wDirs.clear(false);
            wTable.files.clear();
            wTable.setSelect(-1, 0);
            return;
        }
        country = (String)countryLst.get(i);
        File file = new File(HomePath.get(0), "missions/single/" + country);
        File afile[] = file.listFiles();
        wDirs.clear(false);
        if(afile == null || afile.length == 0)
        {
            wTable.files.clear();
            wTable.setSelect(-1, 0);
            return;
        }
        for(int j = 0; j < afile.length; j++)
            if(afile[j].isDirectory() && !afile[j].isHidden() && !".".equals(afile[j].getName()) && !"..".equals(afile[j].getName()))
                _scanMap.put(afile[j].getName(), null);

        for(Iterator iterator = _scanMap.keySet().iterator(); iterator.hasNext(); wDirs.add((String)iterator.next()));
        if(_scanMap.size() > 0)
            wDirs.setSelected(0, true, false);
        _scanMap.clear();
        fillFiles();
    }

    public void fillFiles()
    {
        wTable.files.clear();
        String s = wDirs.getValue();
//        int i = wCountry.getSelected();
        if(s != null)
        {
            String s1 = "missions/single/" + country + "/" + s;
            File file = new File(HomePath.get(0), s1);
            File afile[] = file.listFiles();
            if(afile != null && afile.length > 0)
            {
                for(int j = 0; j < afile.length; j++)
                    if(!afile[j].isDirectory() && !afile[j].isHidden() && afile[j].getName().toLowerCase().lastIndexOf(".properties") < 0)
                    {
                        FileMission filemission = new FileMission(s1, afile[j].getName());
                        _scanMap.put(filemission.fileName, filemission);
                    }

                for(Iterator iterator = _scanMap.keySet().iterator(); iterator.hasNext(); wTable.files.add(_scanMap.get(iterator.next())));
                if(_scanMap.size() > 0)
                    wTable.setSelect(0, 0);
                else
                    wTable.setSelect(-1, 0);
                _scanMap.clear();
            } else
            {
                wTable.setSelect(-1, 0);
            }
        } else
        {
            wTable.setSelect(-1, 0);
        }
        wTable.resized();
    }

    public GUISingleSelect(GWindowRoot gwindowroot)
    {
        super(3);
        bInited = false;
        countryLst = new ArrayList();
        _scanMap = new TreeMap();
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("singleSelect.info");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        wCountry = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wCountry.setEditable(false);
        wDirs = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20F + gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric()));
        wDirs.setEditable(false);
        wTable = new Table(dialogClient);
        dialogClient.create(wDescript = new WDescript());
        wDescript.bNotify = true;
        GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        wPrev = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        wNext = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public static final String HOME_DIR = "missions/single";
    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GUIButton wPrev;
    public GUIButton wNext;
    public GWindowComboControl wCountry;
    public GTexture countryIcon;
    public GWindowComboControl wDirs;
    public Table wTable;
    public WDescript wDescript;
    public String country;
    public boolean bInited;
    public ResourceBundle resCountry;
    public ArrayList countryLst;
    public TreeMap _scanMap;
}
