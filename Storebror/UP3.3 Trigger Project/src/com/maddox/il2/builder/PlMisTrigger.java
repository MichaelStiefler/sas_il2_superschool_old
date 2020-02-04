package com.maddox.il2.builder;

import com.maddox.JGP.*;
import com.maddox.gwindow.*;
import com.maddox.il2.ai.Army;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.I18N;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;
import com.maddox.util.UnicodeTo8bit;
import java.util.*;

public class PlMisTrigger extends Plugin
{
    static class Item
    {

        public String name;
        public int indx;

        public Item(String s, int i)
        {
            name = s;
            indx = i;
        }
    }


    public PlMisTrigger()
    {
        allActors = new ArrayList();
        line2XYZ = new float[6];
        p2d = new Point2d();
        p2dt = new Point2d();
        p3d = new Point3d();
    }

    private int TriggerColor(boolean flag)
    {
        if(flag)
            return Builder.colorSelected();
        else
            return 0xffc8c800;
    }

    public void renderMap2DBefore()
    {
        if(Plugin.builder.isFreeView())
            return;
        if(!viewType.bChecked)
            return;
        Actor actor = Plugin.builder.selectedActor();
        for(int j = 0; j < allActors.size(); j++)
        {
            ActorTrigger actortrigger = (ActorTrigger)allActors.get(j);
            if(Actor.isValid(actortrigger.getTrigger()) && Plugin.builder.project2d(((Actor) (actortrigger)).pos.getAbsPoint(), p2d) && Plugin.builder.project2d(actortrigger.getTrigger().pos.getAbsPoint(), p2dt) && p2d.distance(p2dt) > 4D)
            {
                int k = TriggerColor(actortrigger == actor);
                line2XYZ[0] = (float)((Tuple2d) (p2d)).x;
                line2XYZ[1] = (float)((Tuple2d) (p2d)).y;
                line2XYZ[2] = 0.0F;
                line2XYZ[3] = (float)((Tuple2d) (p2dt)).x;
                line2XYZ[4] = (float)((Tuple2d) (p2dt)).y;
                line2XYZ[5] = 0.0F;
                Render.drawBeginLines(-1);
                Render.drawLines(line2XYZ, 2, 1.25F, k, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND, 3);
                Render.drawEnd();
            }
            if(Actor.isValid(actortrigger.getLink()) && Plugin.builder.project2d(((Actor) (actortrigger)).pos.getAbsPoint(), p2d) && Plugin.builder.project2d(actortrigger.getLink().pos.getAbsPoint(), p2dt) && p2d.distance(p2dt) > 4D)
            {
                int k = 0xff00ffff;
                line2XYZ[0] = (float)((Tuple2d) (p2d)).x;
                line2XYZ[1] = (float)((Tuple2d) (p2d)).y;
                line2XYZ[2] = 0.0F;
                line2XYZ[3] = (float)((Tuple2d) (p2dt)).x;
                line2XYZ[4] = (float)((Tuple2d) (p2dt)).y;
                line2XYZ[5] = 0.0F;
                Render.drawBeginLines(-1);
                Render.drawLines(line2XYZ, 2, 1.25F, k, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE | Mat.BLEND, 3);
                Render.drawEnd();
            }
        }

    }

    public void renderMap2DAfter()
    {
        if(Plugin.builder.isFreeView())
            return;
        if(!viewType.bChecked)
            return;
        Actor actor = Plugin.builder.selectedActor();
        for(int j = 0; j < allActors.size(); j++)
        {
            ActorTrigger actortrigger = (ActorTrigger)allActors.get(j);
            if(Plugin.builder.project2d(((Actor) (actortrigger)).pos.getAbsPoint(), p2d))
            {
                int k = TriggerColor(actortrigger == actor);
                IconDraw.setColor(k);
                if(Actor.isValid(actortrigger.getTrigger()) && Plugin.builder.project2d(actortrigger.getTrigger().pos.getAbsPoint(), p2dt) && p2d.distance(p2dt) > 4D)
                    Render.drawTile((float)(((Tuple2d) (p2dt)).x - (double)(Plugin.builder.conf.iconSize / 2)), (float)(((Tuple2d) (p2dt)).y - (double)(Plugin.builder.conf.iconSize / 2)), Plugin.builder.conf.iconSize, Plugin.builder.conf.iconSize, 0.0F, Plugin.targetIcon, k, 0.0F, 1.0F, 1.0F, -1F);
                IconDraw.render(actortrigger, ((Tuple2d) (p2d)).x, ((Tuple2d) (p2d)).y);
                ((Actor) (actortrigger)).pos.getAbs(p3d);
                p3d.x += actortrigger.r;
                if(Plugin.builder.project2d(p3d, p2dt))
                {
                    double d = ((Tuple2d) (p2dt)).x - ((Tuple2d) (p2d)).x;
                    if(d > (double)(Plugin.builder.conf.iconSize / 3))
                        drawCircle(((Tuple2d) (p2d)).x, ((Tuple2d) (p2d)).y, d, k);
                }
            }
        }

    }

    private void drawCircle(double d, double d1, double d2, int i)
    {
        int j = 48;
        double d3 = 6.2831853071795862D / (double)j;
        double d4 = 0.0D;
        for(int k = 0; k < j; k++)
        {
            _circleXYZ[k * 3 + 0] = (float)(d + d2 * Math.cos(d4));
            _circleXYZ[k * 3 + 1] = (float)(d1 + d2 * Math.sin(d4));
            _circleXYZ[k * 3 + 2] = 0.0F;
            d4 += d3;
        }

        Render.drawBeginLines(-1);
        Render.drawLines(_circleXYZ, j, 1.0F, i, Mat.NOWRITEZ | Mat.MODULATE | Mat.NOTEXTURE, 4);
        Render.drawEnd();
    }

    public boolean save(SectFile sectfile)
    {
        int i = allActors.size();
        if(i == 0)
            return true;
        int j = sectfile.sectionAdd("Trigger");
        sectfile.lineAdd(j, "Version 12");
        for(int k = 0; k < i; k++)
        {
            ActorTrigger actortrigger = (ActorTrigger)allActors.get(k);
            String s = "";
            String s2 = "";
            if(actortrigger.type != 3)
            {
                if(actortrigger.type != 3 && !Actor.isValid(actortrigger.trigger))
                    continue;
                if(actortrigger.trigger instanceof PPoint)
                    s = actortrigger.trigger.getOwner().name();
                else
                    s = actortrigger.trigger.name();
            }
            boolean bLink = false;
            if(Actor.isValid(actortrigger.link))
            {
                if(actortrigger.link instanceof PPoint)
                    s2 = actortrigger.link.getOwner().name();
                else
                    s2 = actortrigger.link.name();
                bLink = true;
            }
            String s3 = actortrigger.textDisplay;
            sectfile.lineAdd(j, actortrigger.name(), actortrigger.type + " " + actortrigger.army + " " + (actortrigger.bTimeout ? "1 " : "0 ") + actortrigger.timeout + " " + (int)((Tuple3d) (((Actor) (actortrigger)).pos.getAbsPoint())).x + " " + (int)((Tuple3d) (((Actor) (actortrigger)).pos.getAbsPoint())).y + " " + actortrigger.r + " " + actortrigger.altiMin + " " + actortrigger.altiMax + (actortrigger.type == 3 ? "" : " " + s) + " " + actortrigger.iaHumans + " " + (actortrigger.bTSortie ? "1" : "0") + " " + actortrigger.avionMin + " " + actortrigger.proba + (actortrigger.type != 2 ? "" : " " + actortrigger.altiDiff) + (bLink ? " 1 " + s2 : " 0") + (s3 == null || s3.length() <= 0 ? "" : " " + actortrigger.textDuree + " " + s3));
        }

        return true;
    }

    private void makeName(Actor actor, String s)
    {
        if(s != null && Actor.getByName(s) == null)
        {
            actor.setName(s);
            return;
        }
        int i = 0;
        do
        {
            s = i + "_Trigger";
            if(Actor.getByName(s) != null)
            {
                i++;
            } else
            {
                actor.setName(s);
                return;
            }
        } while(true);
    }

    public void load(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("Trigger");
        if(i >= 0)
        {
            int j = sectfile.vars(i);
            Point3d point3d = new Point3d();
            int k = 0;
            if(sectfile.line(i, k).indexOf("Version ") >= 0)
                k = 1;
            for(; k < j; k++)
            {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, k));
                String name = numbertokenizer.next(null);
                int type = 0;
                if(name.toLowerCase().indexOf("_trigger") > 0)
                {
                    type = numbertokenizer.next(0);
                } else
                {
                    type = Integer.parseInt(name);
                    name = null;
                }
                int army = numbertokenizer.next(1, 1, 2);
                boolean btimeout = numbertokenizer.next(0) == 1;
                int timeout = numbertokenizer.next(0, 0, 720);
                point3d.x = numbertokenizer.next(0);
                point3d.y = numbertokenizer.next(0);
                int l1 = numbertokenizer.next(1000);
                if(l1 < 2)
                    l1 = 2;
                if(l1 > 0x124f8)
                    l1 = 0x124f8;
                int zmin = numbertokenizer.next(0, 0, 20000);
                int zmax = numbertokenizer.next(10000, 0, 20000);
                String s = null;
                if(type != 3)
                    s = numbertokenizer.next(null);
                int zAppliesFor = numbertokenizer.next(0, 0, 11);
                boolean bSortie = numbertokenizer.next(0) == 1;
                int zAvionMin = numbertokenizer.next(1, 1, 1000);
                int zProba = numbertokenizer.next(100, 0, 100);
                int zAltiDiff = 0;
                if(type == 2)
                    zAltiDiff = numbertokenizer.next(0, -10000, 10000);
                boolean bLink = numbertokenizer.next(0) == 1;
                String s2 = null;
                if(bLink)
                    s2 = numbertokenizer.next(null);
                int zTextDuree = numbertokenizer.next(5, 1, 60);
                String sTextDisplay = "";
                while(numbertokenizer.hasMoreElements()) 
                {
                    sTextDisplay = sTextDisplay + numbertokenizer.next(null);
                    if(numbertokenizer.hasMoreElements())
                        sTextDisplay = sTextDisplay + " ";
                }
                ActorTrigger actorTrigger = insert(name, point3d, type, false, army, btimeout, timeout, l1, zmin, zmax, zAppliesFor, bSortie, zAvionMin, zProba, zAltiDiff, sTextDisplay, zTextDuree);
                if(actorTrigger != null)
                {
                    actorTrigger.addActor(s);
                    if(bLink)
                        actorTrigger.addLinkActor(s2);
                }
            }

        }
    }

    public void deleteAll()
    {
        int i = allActors.size();
        for(int j = 0; j < i; j++)
        {
            ActorTrigger actorTrigger = (ActorTrigger)allActors.get(j);
            actorTrigger.destroy();
        }

        allActors.clear();
    }

    public void delete(Actor actor)
    {
        allActors.remove(actor);
        actor.destroy();
    }

    public void afterDelete()
    {
        for(int i = 0; i < allActors.size();)
        {
            ActorTrigger actortrigger = (ActorTrigger)allActors.get(i);
            if(actortrigger.trigger != null && !Actor.isValid(actortrigger.trigger))
            {
                actortrigger.destroy();
                allActors.remove(i);
            } else
            {
                i++;
            }
        }

    }

  private ActorTrigger insert(String name, Point3d point3d, int i, boolean flag, int army, boolean btimeout, int timeout, int r, int zmin, int zmax, int ziaHumans, boolean bSortie, int zAvionMin, int zProba, int zAltiDiff, String sTextDisplay, int zTextDuree)
  {
    try
    {
      if (name != null)
      {
        if ("NONAME".equals(name)) {
          name = null;
        }
        if (Actor.getByName(name) != null) {
          name = null;
        }
      }
      ActorTrigger actortrigger = new ActorTrigger(point3d, i, army, btimeout, timeout, r, zmin, zmax, ziaHumans, bSortie, zAvionMin, zProba, zAltiDiff, sTextDisplay, zTextDuree);
      makeName(actortrigger, name);
      if (Actor.isValid(actortrigger.trigger)) {
        for (int k = 0; k < this.allActors.size(); k++)
        {
          ActorTrigger actortrigger1 = (ActorTrigger)this.allActors.get(k);
          if ((actortrigger.type == actortrigger1.type) && (Actor.isValid(actortrigger1.trigger)) && ((actortrigger1.trigger == actortrigger.trigger) || (((actortrigger.trigger instanceof PPoint)) && (actortrigger.trigger.getOwner() == actortrigger1.trigger.getOwner()))))
          {
            actortrigger.destroy();
            return null;
          }
        }
      }
      Plugin.builder.align(actortrigger);
      Property.set(actortrigger, "builderSpawn", "");
      Property.set(actortrigger, "builderPlugin", this);
      this.allActors.add(actortrigger);
      if (flag) {
        Plugin.builder.setSelected(actortrigger);
      }
      PlMission.setChanged();
      return actortrigger;
    }
    catch (Exception exception)
    {
      exception.getMessage();
      exception.printStackTrace();
    }
    return null;
  }

  public void insert(Loc loc, boolean flag)
    {
        int i = Plugin.builder.wSelect.comboBox1.getSelected();
        int j = Plugin.builder.wSelect.comboBox2.getSelected();
        if(i != startComboBox1)
            return;
        if(j < 0 || j >= item.length)
        {
            return;
        } else
        {
            insert(null, loc.getPoint(), item[j].indx, flag, 2, false, 0, 1000, 0, 10000, 0, false, 1, 100, 0, "", 5);
            return;
        }
    }

    public void changeType()
    {
        Plugin.builder.setSelected(null);
    }

    private void updateView()
    {
        int i = allActors.size();
        for(int j = 0; j < i; j++)
        {
            ActorTrigger actorTrigger = (ActorTrigger)allActors.get(j);
            actorTrigger.drawing(viewType.bChecked);
        }

    }

    public void configure()
    {
        if(Plugin.getPlugin("Mission") == null)
        {
            throw new RuntimeException("PlMisTrigger: plugin 'Mission' not found");
        } else
        {
            pluginMission = (PlMission)Plugin.getPlugin("Mission");
            return;
        }
    }

    private void fillComboBox2(int i)
    {
        if(i != startComboBox1)
            return;
        if(Plugin.builder.wSelect.curFilledType != i)
        {
            Plugin.builder.wSelect.curFilledType = i;
            Plugin.builder.wSelect.comboBox2.clear(false);
            for(int j = 0; j < item.length; j++)
                Plugin.builder.wSelect.comboBox2.add(Plugin.i18n(item[j].name));

            Plugin.builder.wSelect.comboBox1.setSelected(i, true, false);
        }
        Plugin.builder.wSelect.comboBox2.setSelected(0, true, false);
        Plugin.builder.wSelect.setMesh(null, true);
    }

    public void viewTypeAll(boolean flag)
    {
        viewType.bChecked = flag;
        updateView();
    }

    private void fillEditor(GWindowEditText gwindowedittext, String s)
    {
        String s1 = UnicodeTo8bit.load(s);
        ArrayList arraylist = new ArrayList();
        int i = 0;
        int j = 0;
        for(int k = s1.length(); j < k; j++)
        {
            char c = s1.charAt(j);
            if(c == '\n')
            {
                if(i < j)
                    arraylist.add(s1.substring(i, j));
                else
                    arraylist.add("");
                i = j + 1;
            }
        }

        if(i < j)
            arraylist.add(s1.substring(i, j));
        gwindowedittext.clear();
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
            if(i < arraylist.size() - 1)
                stringbuffer.append('\n');
        }

        return UnicodeTo8bit.save(stringbuffer.toString(), false);
    }

    public void syncSelector()
    {
        ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
        fillComboBox2(startComboBox1);
        Plugin.builder.wSelect.comboBox2.setSelected(actortrigger.type, true, false);
        Plugin.builder.wSelect.tabsClient.addTab(1, tabTrigger);
        ((GWindowDialogControl) (wType)).cap.set(Plugin.i18n(item[actortrigger.type].name));
        wBTimeout.setChecked(actortrigger.bTimeout, false);
        wTimeoutH.setEnable(actortrigger.bTimeout);
        wTimeoutM.setEnable(actortrigger.bTimeout);
        wTimeoutH.setValue("" + (actortrigger.timeout / 60) % 24, false);
        wTimeoutM.setValue("" + actortrigger.timeout % 60, false);
        wR.setPos(actortrigger.r / 500, false);
        ((GWindowDialogControl) (wRLabel)).cap.set(Plugin.i18n("Rayon") + " : " + actortrigger.r + " m");
        wProba.setValue("" + actortrigger.proba, false);
        wAMin.setValue("" + actortrigger.altiMin, false);
        wAMax.setValue("" + actortrigger.altiMax, false);
        wAvionMin.setValue("" + actortrigger.avionMin, false);
        wTIaHumans.list.set(0, Plugin.i18n("TAll"));
        for(int i = 0; i < 12; i++)
            wTIaHumans.posEnable[i] = true;

        if(actortrigger.type == 2)
        {
            wTIaHumans.list.set(0, Plugin.i18n("TAll2"));
            for(int i = 3; i < 12; i++)
                wTIaHumans.posEnable[i] = false;

        }
        wTIaHumans.setSelected(actortrigger.iaHumans, true, false);
        wTSortie.setChecked(actortrigger.bTSortie, false);
        if(actortrigger.army == 2)
            wArmy.setSelected(0, true, false);
        else
            wArmy.setSelected(1, true, false);
        if(actortrigger.type == 3)
        {
            wTriggerCibleClear.hideWindow();
            wTriggerCibleLabel.hideWindow();
            wTriggerCibleSet.hideWindow();
            wTriggerCibleTitre.hideWindow();
        } else
        {
            wTriggerCibleClear.showWindow();
            wTriggerCibleLabel.showWindow();
            wTriggerCibleSet.showWindow();
            wTriggerCibleTitre.showWindow();
            if(actortrigger.trigger != null)
            {
                if(actortrigger.trigger instanceof PPoint)
                    ((GWindowDialogControl) (wTriggerCibleLabel)).cap.set(actortrigger.trigger.getOwner().name());
                else
                    ((GWindowDialogControl) (wTriggerCibleLabel)).cap.set(actortrigger.trigger.name());
            } else
            {
                ((GWindowDialogControl) (wTriggerCibleLabel)).cap.set(Plugin.i18n("NoObject"));
            }
        }
        if(actortrigger.link != null)
        {
            if(actortrigger.link instanceof PPoint)
                ((GWindowDialogControl) (wTriggerLinkLabel)).cap.set(actortrigger.link.getOwner().name());
            else
                ((GWindowDialogControl) (wTriggerLinkLabel)).cap.set(actortrigger.link.name());
        } else
        {
            ((GWindowDialogControl) (wTriggerLinkLabel)).cap.set(Plugin.i18n("NoLink"));
        }
        if(actortrigger.type == 2)
        {
            wADiff.showWindow();
            wADiffLabel.showWindow();
            wADiff.setValue("" + actortrigger.altiDiff, false);
        } else
        {
            wADiff.hideWindow();
            wADiffLabel.hideWindow();
        }
        fillEditor(wTextToShow.edit, actortrigger.textDisplay);
        wTextDuree.setValue("" + actortrigger.textDuree, false);
    }

    public void createGUI()
    {
        Plugin.builder.wSelect.metricWin = new GRegion(2.0F, 2.0F, 40.0F, 36.0F);
        startComboBox1 = Plugin.builder.wSelect.comboBox1.size();
        Plugin.builder.wSelect.comboBox1.add(Plugin.i18n("tTrigger"));
        Plugin.builder.wSelect.comboBox1.addNotifyListener(new GNotifyListener() {

            public boolean notify(GWindow gwindow, int j, int k)
            {
                int l = Plugin.builder.wSelect.comboBox1.getSelected();
                if(l >= 0 && j == 2)
                    fillComboBox2(l);
                return false;
            }

        }
);
        int i;
        for(i = Plugin.builder.mDisplayFilter.subMenu.size() - 1; i >= 0 && pluginMission.viewBridge != Plugin.builder.mDisplayFilter.subMenu.getItem(i); i--);
        if(--i >= 0)
        {
            viewType = Plugin.builder.mDisplayFilter.subMenu.addItem(i, new GWindowMenuItem(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("showTrigger"), null) {

                public void execute()
                {
                    super.bChecked = !super.bChecked;
                    updateView();
                }

            }
);
            viewType.bChecked = true;
        }
        float f = 1.0F;
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)Plugin.builder.wSelect.tabsClient.create(new GWindowDialogClient());        
        tabTrigger = Plugin.builder.wSelect.tabsClient.createTab(Plugin.i18n("tTrigger"), gwindowdialogclient);
        gwindowdialogclient.addLabel(wType = new GWindowLabel(gwindowdialogclient, 1.0F, f, 20F, 1.3F, Plugin.i18n("lType"), null));
        f += 2.0F;
        gwindowdialogclient.addControl(wBTimeout = new GWindowCheckBox(gwindowdialogclient, 1.0F, f, null) {

            public boolean notify(int j, int k)
            {
                if(j != 2)
                {
                    return false;
                } else
                {
                    ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                    actortrigger.bTimeout = isChecked();
                    wTimeoutH.setEnable(actortrigger.bTimeout);
                    wTimeoutM.setEnable(actortrigger.bTimeout);
                    PlMission.setChanged();
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addLabel(wLTimeout = new GWindowLabel(gwindowdialogclient, 3F, f, 5F, 1.3F, Plugin.i18n("TimeOut"), null));
        gwindowdialogclient.addControl(wTimeoutH = new GWindowEditControl(gwindowdialogclient, 9F, f, 2.0F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int j, int k)
            {
                if(j != 2)
                {
                    return false;
                } else
                {
                    getTimeOut();
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addControl(wTimeoutM = new GWindowEditControl(gwindowdialogclient, 12F, f, 2.0F, 1.3F, "") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int j, int k)
            {
                if(j != 2)
                {
                    return false;
                } else
                {
                    getTimeOut();
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addLabel(wADiffLabel = new GWindowLabel(gwindowdialogclient, 23F, f, 8F, 1.3F, Plugin.i18n("AltiDiff"), null));
        gwindowdialogclient.addControl(wADiff = new GWindowEditControl(gwindowdialogclient, 32F, f, 5F, 1.3F, "0") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int j, int k)
            {
                if(j != 2)
                    return false;
                ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                int i = 0;
                try
                {
                    i = (int)Double.parseDouble(wADiff.getValue());
                }
                catch(Exception exception) { }
                actortrigger.altiDiff = i;
                if(actortrigger.altiDiff > 10000)
                    actortrigger.altiDiff = 10000;
                if(actortrigger.altiDiff < -10000)
                    actortrigger.altiDiff = -10000;
                PlMission.setChanged();
                return false;
            }

        }
);
        f += 2.0F;
        gwindowdialogclient.addControl(wR = new GWindowHSliderInt(gwindowdialogclient, 0, 151, 15, 1.0F, f, 20F) {

            public void afterCreated()
            {
                super.afterCreated();
                super.bSlidingNotify = true;
            }

            public boolean notify(int j, int k)
            {
                if(j != 2)
                    return false;
                ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                actortrigger.r = pos() * 500;
                if(actortrigger.r < 2)
                    actortrigger.r = 2;
                PlMission.setChanged();
                ((GWindowDialogControl) (wRLabel)).cap.set(Plugin.i18n("Rayon") + " : " + actortrigger.r + " m");
                return false;
            }

        }
);
        f += 2.0F;
        gwindowdialogclient.addLabel(wRLabel = new GWindowLabel(gwindowdialogclient, 1.0F, f, 20F, 1.3F, Plugin.i18n("Rayon"), null));
        f += 2.0F;
        gwindowdialogclient.addLabel(wTriggerLinkTitre = new GWindowLabel(gwindowdialogclient, 1.0F, f, 20F, 1.3F, Plugin.i18n("TiggerLink"), null));
        f += 2.0F;
        gwindowdialogclient.addControl(wTriggerLinkSet = new GWindowButton(gwindowdialogclient, 10F, f - 0.5F, 5F, 1.6F, Plugin.i18n("&Set"), null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                    Plugin.builder.beginSelectTriggerLink();
                return false;
            }

        }
);
        gwindowdialogclient.addControl(wTriggerLinkClear = new GWindowButton(gwindowdialogclient, 16F, f - 0.5F, 5F, 1.6F, Plugin.i18n("&Clear"), null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                    actortrigger.setLink(null);
                    ((GWindowDialogControl) (wTriggerLinkLabel)).cap.set(Plugin.i18n("NotSet"));
                    PlMission.setChanged();
                }
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wTriggerLinkLabel = new GWindowLabel(gwindowdialogclient, 1.0F, f - 0.5F, 8F, 1.3F, Plugin.i18n("NotSet"), null));
        f += 2.0F;
        gwindowdialogclient.addControl(wTSortie = new GWindowCheckBox(gwindowdialogclient, 1.0F, f, null) {

            public boolean notify(int j, int k)
            {
                if(j != 2)
                {
                    return false;
                } else
                {
                    ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                    actortrigger.bTSortie = isChecked();
                    PlMission.setChanged();
                    return false;
                }
            }

        }
);
        gwindowdialogclient.addLabel(wTSortieLabel = new GWindowLabel(gwindowdialogclient, 3F, f, 18F, 1.3F, Plugin.i18n("TExit"), null));
        f += 2.0F;
        gwindowdialogclient.addLabel(wProbaLabel = new GWindowLabel(gwindowdialogclient, 1.0F, f, 7F, 1.3F, Plugin.i18n("TProbability"), null));
        gwindowdialogclient.addControl(wProba = new GWindowEditControl(gwindowdialogclient, 9F, f, 3F, 1.3F, "100") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int j, int k)
            {
                if(j != 2)
                    return false;
                ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                int i = 0;
                try
                {
                    i = (int)Double.parseDouble(wProba.getValue());
                }
                catch(Exception exception) { }
                actortrigger.proba = i;
                if(actortrigger.proba > 100)
                    actortrigger.proba = 100;
                if(actortrigger.proba < 0)
                    actortrigger.proba = 0;
                PlMission.setChanged();
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 13F, f, 1.0F, 1.3F, "%", null));
        f += 2.0F;
        gwindowdialogclient.addLabel(wAMinLabel = new GWindowLabel(gwindowdialogclient, 1.0F, f, 7F, 1.3F, Plugin.i18n("AltiMin"), null));
        gwindowdialogclient.addControl(wAMin = new GWindowEditControl(gwindowdialogclient, 9F, f, 5F, 1.3F, "0") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int j, int k)
            {
                if(j != 2)
                    return false;
                ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                int i = 0;
                try
                {
                    i = (int)Double.parseDouble(wAMin.getValue());
                }
                catch(Exception exception) { }
                actortrigger.altiMin = i;
                if(actortrigger.altiMin > actortrigger.altiMax)
                    actortrigger.altiMin = actortrigger.altiMax;
                PlMission.setChanged();
                return false;
            }

        }
);
        f += 2.0F;
        gwindowdialogclient.addLabel(wAMaxLabel = new GWindowLabel(gwindowdialogclient, 1.0F, f, 7F, 1.3F, Plugin.i18n("AltiMax"), null));
        gwindowdialogclient.addControl(wAMax = new GWindowEditControl(gwindowdialogclient, 9F, f, 5F, 1.3F, "10000") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int j, int k)
            {
                if(j != 2)
                    return false;
                ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                int i = 10000;
                try
                {
                    i = (int)Double.parseDouble(wAMax.getValue());
                }
                catch(Exception exception) { }
                actortrigger.altiMax = i;
                if(actortrigger.altiMin > actortrigger.altiMax)
                    actortrigger.altiMax = actortrigger.altiMin;
                PlMission.setChanged();
                return false;
            }

        }
);
        f += 2.0F;
        gwindowdialogclient.addLabel(wAvionMinLabel = new GWindowLabel(gwindowdialogclient, 1.0F, f, 7F, 1.3F, Plugin.i18n("TAvionsMIN"), null));
        gwindowdialogclient.addControl(wAvionMin = new GWindowEditControl(gwindowdialogclient, 9F, f, 5F, 1.3F, "1") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int j, int k)
            {
                if(j != 2)
                    return false;
                ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                int i = 1;
                try
                {
                    i = (int)Double.parseDouble(wAvionMin.getValue());
                }
                catch(Exception exception) { }
                if(i < 1)
                    i = 1;
                actortrigger.avionMin = i;
                PlMission.setChanged();
                return false;
            }

        }
);
        f += 2.0F;
        gwindowdialogclient.addLabel(wTIaHumansLabel = new GWindowLabel(gwindowdialogclient, 1.0F, f, 7F, 1.3F, Plugin.i18n("AppliesArmy"), null));
        gwindowdialogclient.addControl(wTIaHumans = new GWindowComboControl(gwindowdialogclient, 9F, f, 12F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                add(Plugin.i18n("TAll"));
                add(Plugin.i18n("TIAOnly"));
                add(Plugin.i18n("THumansOnly"));
                add(Plugin.i18n("TObject"));
                add(Plugin.i18n("TAirOnly"));
                add(Plugin.i18n("TAirGroundOnly"));
                add(Plugin.i18n("TArmourOnly"));
                add(Plugin.i18n("TArtilleryOnly"));
                add(Plugin.i18n("TInfantryOnly"));
                add(Plugin.i18n("TShipsOnly"));
                add(Plugin.i18n("TTrainsOnly"));
                add(Plugin.i18n("TVehiclesOnly"));
                super.posEnable = new boolean[12];
                for(int i = 0; i < 12; i++)
                    super.posEnable[i] = true;

            }

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                    actortrigger.iaHumans = getSelected();
                    PlMission.setChanged();
                }
                return false;
            }

        }
);
        f += 2.0F;
        gwindowdialogclient.addLabel(wLArmy = new GWindowLabel(gwindowdialogclient, 1.0F, f, 14F, 1.3F, Plugin.i18n("TriggerArmy"), null));
        gwindowdialogclient.addControl(wArmy = new GWindowComboControl(gwindowdialogclient, 16F, f, 5F) {

            public void afterCreated()
            {
                super.afterCreated();
                setEditable(false);
                add(I18N.army(Army.name(1)));
                add(I18N.army(Army.name(2)));
            }

            public boolean notify(int j, int k)
            {
                if(j != 2)
                    return false;
                ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                if(getSelected() == 0)
                    actortrigger.army = 2;
                else
                    actortrigger.army = 1;
                PlMission.setChanged();
                return false;
            }

        }
);
        f += 2.0F;
        gwindowdialogclient.addLabel(wTriggerCibleTitre = new GWindowLabel(gwindowdialogclient, 1.0F, f, 20F, 1.3F, Plugin.i18n("TiggerCible"), null));
        f += 2.0F;
        gwindowdialogclient.addControl(wTriggerCibleSet = new GWindowButton(gwindowdialogclient, 10F, f - 0.5F, 5F, 1.6F, Plugin.i18n("&Set"), null) {

            public boolean notify(int i, int j)
            {
                if(i == 2) {
                    Plugin.builder.beginSelectTrigger();
//                    return true;
                }
                return false;
            }

        }
);
        gwindowdialogclient.addControl(wTriggerCibleClear = new GWindowButton(gwindowdialogclient, 16F, f - 0.5F, 5F, 1.6F, Plugin.i18n("&Clear"), null) {

            public boolean notify(int i, int j)
            {
                if(i == 2)
                {
                    ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                    actortrigger.setTrigger(null);
                    ((GWindowDialogControl) (wTriggerCibleLabel)).cap.set(Plugin.i18n("NotSet"));
                    PlMission.setChanged();
//                    return true;
                }
                return false;
            }

        }
);
        gwindowdialogclient.addLabel(wTriggerCibleLabel = new GWindowLabel(gwindowdialogclient, 1.0F, f - 0.5F, 8F, 1.3F, Plugin.i18n("NotSet"), null));
        gwindowdialogclient.addLabel(wTextToShowLabel = new GWindowLabel(gwindowdialogclient, 23F, 6.5F, 14F, 1.3F, Plugin.i18n("TTextToShow"), null));
        gwindowdialogclient.addControl(wTextToShow = new GWindowEditTextControl(gwindowdialogclient, 23F, 8F, 14F, 12F, "") {

            public boolean notify(GWindow gwindow, int i, int j)
            {
                if(gwindow == super.edit && i == 2 && j == 0)
                {
                    ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                    actortrigger.textDisplay = getEditor(super.edit);
                    PlMission.setChanged();
                }
                return super.notify(gwindow, i, j);
            }

        }
);
        gwindowdialogclient.addLabel(wTextDureeLabel = new GWindowLabel(gwindowdialogclient, 23F, 21F, 8F, 1.3F, Plugin.i18n("TTextDuree"), null));
        gwindowdialogclient.addControl(wTextDuree = new GWindowEditControl(gwindowdialogclient, 32F, 21F, 5F, 1.3F, "5") {

            public void afterCreated()
            {
                super.afterCreated();
                super.bNumericOnly = true;
                super.bDelayedNotify = true;
            }

            public boolean notify(int j, int k)
            {
                if(j != 2)
                    return false;
                ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
                int i = 1;
                try
                {
                    i = (int)Double.parseDouble(wTextDuree.getValue());
                }
                catch(Exception exception) { }
                if(i < 1)
                    i = 1;
                actortrigger.textDuree = i;
                PlMission.setChanged();
                return false;
            }

        }
);
    }

    private void getTimeOut()
    {
        ActorTrigger actortrigger = (ActorTrigger)Plugin.builder.selectedActor();
        String s = wTimeoutH.getValue();
        double d = 0.0D;
        try
        {
            d = Double.parseDouble(s);
        }
        catch(Exception exception) { }
        if(d < 0.0D)
            d = 0.0D;
        if(d > 12D)
            d = 12D;
        s = wTimeoutM.getValue();
        double d1 = 0.0D;
        try
        {
            d1 = Double.parseDouble(s);
        }
        catch(Exception exception1) { }
        if(d1 < 0.0D)
            d1 = 0.0D;
        if(d1 > 59D)
            d1 = 59D;
        actortrigger.timeout = (int)(d * 60D + d1);
        wTimeoutH.setValue("" + (actortrigger.timeout / 60) % 24, false);
        wTimeoutM.setValue("" + actortrigger.timeout % 60, false);
        PlMission.setChanged();
    }

    protected ArrayList allActors;
    Item item[] = {
        new Item("NewAircraftAir", 0), new Item("NewAircraftSol", 1), new Item("NewAircraftAirLevel", 2), new Item("NewMessage", 3)
    };
    private float line2XYZ[];
    private Point2d p2d;
    private Point2d p2dt;
    private Point3d p3d;
    private static float _circleXYZ[] = new float[144];
    private int startComboBox1;
    private GWindowMenuItem viewType;
    private PlMission pluginMission;
    com.maddox.gwindow.GWindowTabDialogClient.Tab tabTrigger;
    GWindowLabel wType;
    GWindowLabel wTrigger;
    GWindowCheckBox wBTimeout;
    GWindowLabel wLTimeout;
    GWindowEditControl wTimeoutH;
    GWindowEditControl wTimeoutM;
    GWindowHSliderInt wR;
    GWindowLabel wRLabel;
    GWindowLabel wLArmy;
    GWindowComboControl wArmy;
    GWindowButton wTriggerCibleSet;
    GWindowButton wTriggerCibleClear;
    GWindowLabel wTriggerCibleLabel;
    GWindowLabel wTriggerCibleTitre;
    GWindowLabel wAMinLabel;
    GWindowEditControl wAMin;
    GWindowLabel wAMaxLabel;
    GWindowEditControl wAMax;
    GWindowLabel wTIaHumansLabel;
    GWindowComboControl wTIaHumans;
    GWindowLabel wTSortieLabel;
    GWindowCheckBox wTSortie;
    GWindowLabel wAvionMinLabel;
    GWindowEditControl wAvionMin;
    GWindowLabel wADiffLabel;
    GWindowEditControl wADiff;
    GWindowLabel wProbaLabel;
    GWindowEditControl wProba;
    GWindowButton wTriggerLinkSet;
    GWindowButton wTriggerLinkClear;
    GWindowLabel wTriggerLinkLabel;
    GWindowLabel wTriggerLinkTitre;
    GWindowLabel wTextToShowLabel;
    GWindowEditTextControl wTextToShow;
    GWindowLabel wTextDureeLabel;
    GWindowEditControl wTextDuree;

    static 
    {
        Property.set(com.maddox.il2.builder.PlMisTrigger.class, "name", "MisTrigger");
    }




}
