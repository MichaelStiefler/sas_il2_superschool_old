//////////////////////////////////////////////////////////////////////////////////////////
//   FMBFull MODded
//   4.111 / 4.101 MODded by PAL
//   lifted to 4.12 by SAS~Storebror
//   Changed public void load(SectFile sectfile) to avoid problems loading properties
/////////////////////////////////////////////////////////////////////////////////////////

// ************* 4.111 version doesn't have any change, equal to 4.101  ***************
// *************       4.12 version doesn't have any change either      ***************

package com.maddox.il2.builder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowEditText;
import com.maddox.gwindow.GWindowEditTextControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.gwindow.GWindowTabDialogClient;
import com.maddox.rts.HomePath;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.util.UnicodeTo8bit;

public class PlMisBrief extends Plugin
{
    class WEditor extends GWindowFramed
    {

        public void windowShown()
        {
            mEditor.bChecked = true;
            super.windowShown();
        }

        public void windowHidden()
        {
            mEditor.bChecked = false;
            super.windowHidden();
        }

        public void created()
        {
            bAlwaysOnTop = true;
            super.created();
            title = Plugin.i18n("Texts");
            clientWindow = create(new GWindowTabDialogClient());
            GWindowTabDialogClient gwindowtabdialogclient = (GWindowTabDialogClient)clientWindow;
            gwindowtabdialogclient.addTab(Plugin.i18n("textName"), gwindowtabdialogclient.create(nameClient = new GWindowDialogClient()));
            nameClient.addControl(wName = new GWindowEditControl(nameClient, 0.0F, 0.0F, 1.0F, 1.0F, "") {

                public boolean notify(int i, int j)
                {
                    if(i == 2 && j == 0)
                        PlMission.setChanged();
                    return super.notify(i, j);
                }

            }
);
            gwindowtabdialogclient.addTab(Plugin.i18n("textShort"), gwindowtabdialogclient.create(wShort = new GWindowEditTextControl() {

                public boolean notify(GWindow gwindow, int i, int j)
                {
                    if(gwindow == edit && i == 2 && j == 0)
                        PlMission.setChanged();
                    return super.notify(gwindow, i, j);
                }

            }
));
            gwindowtabdialogclient.addTab(Plugin.i18n("textDescr"), gwindowtabdialogclient.create(wInfo = new GWindowEditTextControl() {

                public boolean notify(GWindow gwindow, int i, int j)
                {
                    if(gwindow == edit && i == 2 && j == 0)
                        PlMission.setChanged();
                    return super.notify(gwindow, i, j);
                }

            }
));
        }

        public void deleteAll()
        {
            wName.setValue("", false);
            wShort.edit.clear(false);
            wInfo.edit.clear(false);
        }

        public void resized()
        {
            super.resized();
            if(wName != null)
                wName.setSize(nameClient.win.dx, 1.3F * lookAndFeel().metric());
        }

        public void afterCreated()
        {
            super.afterCreated();
            resized();
            close(false);
        }

        public GWindowDialogClient nameClient;
        public GWindowEditControl wName;
        public GWindowEditTextControl wShort;
        public GWindowEditTextControl wInfo;

        public WEditor()
        {
            doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 26F, 20F, true);
        }
    }


    public PlMisBrief()
    {
    }

    public void load(SectFile sectfile) //By PAL, to avoid problems with incomplete Descriptions
    {
        wEditor.wName.clear(false);
        wEditor.wShort.edit.clear(false);
        wEditor.wInfo.edit.clear(false);
        String s = sectfile.fileName();
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
        String nameB = null;
        String shortB = null;                
        String descriptionB = null;                        	            
        try
        {
            ResourceBundle resourcebundleN = ResourceBundle.getBundle(s, RTSConf.cur.locale);
            nameB = resourcebundleN.getString("Name");
        }
        catch(Exception exceptionProps)
        {
			System.out.println("No Name for Mission");      	
        }            
        try
        {

            ResourceBundle resourcebundleS = ResourceBundle.getBundle(s, RTSConf.cur.locale);
            shortB = resourcebundleS.getString("Short");            
        }
        catch(Exception exceptionShort)
        {
			System.out.println("No Short for Mission");        	       	
        }
        try
        {

            ResourceBundle resourcebundleD = ResourceBundle.getBundle(s, RTSConf.cur.locale);
            descriptionB = resourcebundleD.getString("Description");
        }
        catch(Exception exceptionDescription)
        {
			System.out.println("No Description for Mission");        	       	
        }            
        if (nameB != null) wEditor.wName.setValue(UnicodeTo8bit.load(nameB), false);
        if (shortB != null) fillEditor(wEditor.wShort.edit, shortB);
        if (descriptionB != null) fillEditor(wEditor.wInfo.edit, descriptionB);                             	
    }   	

   	/*
    public void load(SectFile sectfile)
    {
        String s = textFileName(sectfile);
        wEditor.wName.clear(false);
        wEditor.wShort.edit.clear(false);
        wEditor.wInfo.edit.clear(false);
        BufferedReader bufferedreader = null;
        try
        {
            bufferedreader = new BufferedReader(new SFSReader(s));
            do
            {
                String s1 = bufferedreader.readLine();
                if(s1 == null)
                    break;
                int i = s1.length();
                if(i != 0)
                {
                    SharedTokenizer.set(s1);
                    String s2 = SharedTokenizer.next();
                    if(s2 != null)
                        if("Name".compareToIgnoreCase(s2) == 0)
                        {
                            String s3 = SharedTokenizer.getGap();
                            if(s3 != null)
                                wEditor.wName.setValue(UnicodeTo8bit.load(s3), false);
                        } else
                        if("Short".compareToIgnoreCase(s2) == 0)
                        {
                            String s4 = SharedTokenizer.getGap();
                            if(s4 != null)
                                fillEditor(wEditor.wShort.edit, s4);
                        } else
                        if("Description".compareToIgnoreCase(s2) == 0)
                        {
                            String s5 = SharedTokenizer.getGap();
                            if(s5 != null)
                                fillEditor(wEditor.wInfo.edit, s5);
                        }
                }
            } while(true);
        }
        catch(Exception exception) { }
        if(bufferedreader != null)
            try
            {
                bufferedreader.close();
            }
            catch(Exception exception1) { }
    }
    */

    private void fillEditor(GWindowEditText gwindowedittext, String s)
    {
        String s1 = UnicodeTo8bit.load(s);
        ArrayList arraylist = new ArrayList();
        int i = 0;
        int j = 0;
        for(int k = s1.length(); j < k; j++)
        {
            char c = s1.charAt(j);
            if(c != '\n')
                continue;
            if(i < j)
                arraylist.add(s1.substring(i, j));
            else
                arraylist.add("");
            i = j + 1;
        }

        if(i < j)
            arraylist.add(s1.substring(i, j));
        gwindowedittext.insert(arraylist, true);
        gwindowedittext.clearUnDo();
    }

    private String getEditor(GWindowEditText gwindowedittext)
    {
        if(gwindowedittext.isEmpty())
            return "";
        ArrayList arraylist = gwindowedittext.text;
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < arraylist.size(); i++)
        {
            StringBuffer stringbuffer1 = (StringBuffer)arraylist.get(i);
            if(stringbuffer1 != null && stringbuffer1.length() > 0)
                stringbuffer.append(stringbuffer1.toString());
            stringbuffer.append('\n');
        }

        return UnicodeTo8bit.save(stringbuffer.toString(), false);
    }

    public boolean save(SectFile sectfile)
    {
        String s = textFileName(sectfile);
        PrintWriter printwriter = null;
        try
        {
            printwriter = new PrintWriter(new BufferedWriter(new FileWriter(HomePath.toFileSystemName(s, 0))));
            String s1 = wEditor.wName.getValue();
            if(s1 != null && s1.length() > 0)
                printwriter.println("Name " + UnicodeTo8bit.save(s1, false));
            String s2 = getEditor(wEditor.wShort.edit);
            if(s2 != null && s2.length() > 0)
                printwriter.println("Short " + s2);
            s2 = getEditor(wEditor.wInfo.edit);
            if(s2 != null && s2.length() > 0)
                printwriter.println("Description " + s2);
        }
        catch(Exception exception) { }
        if(printwriter != null)
            try
            {
                printwriter.close();
            }
            catch(Exception exception1) { }
        return true;
    }

    private String textFileName(SectFile sectfile)
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
        String s2 = sectfile.fileName();
        int i = s2.length() - 1;
        do
        {
            if(i < 0)
                break;
            char c = s2.charAt(i);
            if(c == '/' || c == '\\')
                break;
            if(c == '.')
                return s2.substring(0, i) + s + ".properties";
            i--;
        } while(true);
        return s2 + s + ".properties";
    }
/*
    private String textFileName(SectFile sectfile) //By PAL
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
    }*/    

    public void deleteAll()
    {
        wEditor.deleteAll();
    }

    public void configure()
    {
        if(getPlugin("Mission") == null)
        {
            throw new RuntimeException("PlMisTarget: plugin 'Mission' not found");
        } else
        {
            pluginMission = (PlMission)getPlugin("Mission");
            return;
        }
    }

    public void createGUI()
    {
        mEditor = builder.mEdit.subMenu.addItem(0, new GWindowMenuItem(builder.mEdit.subMenu, i18n("&Texts"), i18n("TIPTexts")) {

            public void execute()
            {
                if(wEditor.isVisible())
                    wEditor.hideWindow();
                else
                    wEditor.showWindow();
            }

        }
);
        wEditor = new WEditor();
    }

    public void freeResources()
    {
        if(wEditor.isVisible())
            wEditor.hideWindow();
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

    WEditor wEditor;
    GWindowMenuItem mEditor;
    
    //By PAL:
    public PlMission pluginMission;

    static 
    {
        Property.set(com.maddox.il2.builder.PlMisBrief.class, "name", "MisBrief");
    }
}
