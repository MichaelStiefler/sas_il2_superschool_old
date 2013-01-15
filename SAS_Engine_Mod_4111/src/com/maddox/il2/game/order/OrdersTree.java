// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 12/02/2011 10:09:13 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   OrdersTree.java

package com.maddox.il2.game.order;

import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeTransport;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.radios.Beacon;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import com.maddox.util.HashMapIntEntry;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.game.order:
//            Orders, Order, OrderBack, OrderCover_Me, 
//            OrderTarget_All, OrderAttack_Fighters, OrderAttack_Bombers, OrderAttack_My_Target, 
//            OrderDrop_Tanks, OrderBreak, OrderRejoin, OrderChange_Formation_To_1, 
//            OrderChange_Formation_To_2, OrderChange_Formation_To_3, OrderChange_Formation_To_4, OrderChange_Formation_To_5, 
//            OrderChange_Formation_To_6, OrderChange_Formation_To_7, OrderTighten_Formation, OrderLoosen_Formation, 
//            OrderRequest_Assistance, OrderVector_To_Home_Base, OrderVector_To_Target, OrderRequest_For_Landing, 
//            OrderRequest_For_Takeoff, OrderRequest_For_RunwayLights, OrderAnyone_Help_Me, OrderGT

public class OrdersTree
{
    class HotKeyCmdFire extends HotKeyCmd
    {

        public void begin()
        {
            doCmd(cmd, true);
        }

        public void end()
        {
            doCmd(cmd, false);
        }

        public boolean isDisableIfTimePaused()
        {
            return true;
        }

        int cmd;

        public HotKeyCmdFire(String s, String s1, int i, int j)
        {
            super(true, s1, s);
            cmd = i;
            setRecordId(j);
        }
    }


    protected boolean isLocalServer()
    {
        return context == 0;
    }

    protected boolean isLocalClient()
    {
        return context == 1;
    }

    protected boolean isRemoteServer()
    {
        return context == 2;
    }

    protected boolean isRemote()
    {
        return context == 2;
    }

    protected boolean isLocal()
    {
        return context != 2;
    }

    protected boolean _isEnableVoice()
    {
        return isLocalServer() || isRemoteServer();
    }

    private void chatLog(Order order)
    {
        if(!Actor.isValid(Player))
            return;
        int i = Player.getArmy();
        ArrayList arraylist = new ArrayList();
        do
        {
            if(order == null)
                break;
            arraylist.add(0, order.name(i));
            if(order.orders.upOrders == null)
                break;
            if(order != order.orders.order[0])
            {
                order = order.orders.order[0];
                continue;
            }
            if(order.orders.upOrders.upOrders == null)
                break;
            order = order.orders.upOrders.order[0];
        } while(true);
        StringBuffer stringbuffer = new StringBuffer();
        for(int j = 0; j < arraylist.size(); j++)
        {
            if(j > 0)
                stringbuffer.append(" ");
            stringbuffer.append((String)arraylist.get(j));
        }

        NetUser netuser = null;
        List list = NetEnv.hosts();
        ArrayList arraylist1 = new ArrayList();
        for(int k = 0; k < list.size(); k++)
        {
            NetUser netuser1 = (NetUser)list.get(k);
            if(this == netuser1.ordersTree)
            {
                netuser = netuser1;
                continue;
            }
            if(i == netuser1.getArmy())
                arraylist1.add(netuser1);
        }

        if(netuser == null)
            netuser = (NetUser)NetEnv.host();
        else
        if(i == ((NetUser)NetEnv.host()).getArmy())
            arraylist1.add(NetEnv.host());
        arraylist1.add(netuser);
        Main.cur().chat.send(netuser, stringbuffer.toString(), arraylist1, (byte)1, i == World.getPlayerArmy());
    }

    public Boolean frequency()
    {
        return frequency;
    }

    public void setFrequency(Boolean boolean1)
    {
        frequency = boolean1;
    }

    public boolean alone()
    {
        return alone;
    }

    protected void _cset(Actor actor)
    {
        for(int i = 0; i < 16; i++)
            CommandSet[i] = null;

        if(frequency == null || !frequency.booleanValue())
            return;
        alone = false;
        if(!Actor.isAlive(actor))
            return;
        if(actor instanceof Aircraft)
        {
            CommandSet[0] = (Aircraft)actor;
            alone = true;
        } else
        if(actor instanceof Wing)
        {
            Wing wing = (Wing)actor;
            for(int j = 0; j < 4; j++)
                if(Actor.isAlive(wing.airc[j]))
                    CommandSet[j] = wing.airc[j];

        } else
        if(actor instanceof Squadron)
        {
            Squadron squadron = (Squadron)actor;
            for(int k = 0; k < 16; k++)
                if(squadron.wing[k >> 2] != null && Actor.isAlive(squadron.wing[k >> 2].airc[k & 3]))
                    CommandSet[k] = squadron.wing[k >> 2].airc[k & 3];

        }
    }

    public boolean isActive()
    {
        return bActive;
    }

    public void missionLoaded()
    {
        context = 0;
        missionLoaded(World.getPlayerAircraft());
    }

    public void netMissionLoaded(Aircraft aircraft)
    {
        if(aircraft == null)
        {
            context = 2;
        } else
        {
            boolean flag = Mission.isNet() && aircraft.netUser() != NetEnv.host();
            if(aircraft == World.getPlayerAircraft() && !flag)
            {
                if(Mission.isSingle() || Mission.isServer())
                    context = 0;
                else
                    context = 1;
            } else
            {
                context = 2;
            }
        }
        missionLoaded(aircraft);
    }

    private void missionLoaded(Aircraft aircraft)
    {
        Player = aircraft;
        if(Player == null)
            return;
        curOrdersTree = this;
        Mission _tmp = Main.cur().mission;
        if(Mission.isDogfight())
        {
            setAsDogfight();
            curOrdersTree = null;
            return;
        }
        Wing wing = Player.getWing();
        PlayerWing = wing;
        PlayerSquad = wing.squadron();
        PlayerRegiment = wing.regiment();
        int i = Player.aircIndex();
        int j = Player.aircNumber();
        int k = wing.indexInSquadron();
        int l = wing.squadron().getWingsNumber();
        if(j == 2)
            setAsSquadLeader();
        else
            setAsGroupLeader();
        Wing wing1 = null;
        int i1 = 0;
        do
        {
            if(i1 >= PlayerSquad.wing.length)
                break;
            if(PlayerSquad.wing[i1] != null)
            {
                wing1 = PlayerSquad.wing[i1];
                break;
            }
            i1++;
        } while(true);
        if(PlayerWing != wing1 || l < 2)
        {
            if(i == 0 && j > 1)
            {
                setAsWingLeader(k);
            } else
            {
                setAsWingman(i);
                if(!isRemote() && (Player instanceof TypeTransport))
                    home.order[1].attrib |= 1;
            }
        } else
        if(i != 0)
            setAsWingman(i);
        else
        if(!isRemote())
        {
            for(int j1 = 0; j1 < 4; j1++)
                if(PlayerSquad.wing[j1] == null)
                    home.order[2 + j1].attrib |= 1;

        }
        curOrdersTree = null;
    }

    public void resetGameClear()
    {
        Player = null;
        PlayerWing = null;
        PlayerSquad = null;
        PlayerRegiment = null;
        for(int i = 0; i < CommandSet.length; i++)
            CommandSet[i] = null;

    }

    private void setAsSquadLeader()
    {
        home = SquadLeader;
        if(isRemote())
            return;
        for(int i = 0; i < home.order.length; i++)
            if(home.order[i] != null)
                home.order[i].attrib &= -2;

    }

    private void setAsGroupLeader()
    {
        home = GroupLeader;
        if(isRemote())
            return;
        for(int i = 0; i < home.order.length; i++)
            if(home.order[i] != null)
                home.order[i].attrib &= -2;

    }

    private void setAsWingLeader(int i)
    {
        if(isRemote())
            return;
        for(int j = 0; j < home.order.length; j++)
            if(home.order[j] != null)
                home.order[j].attrib &= -2;

        home.order[2].attrib |= 1;
        home.order[3].attrib |= 1;
        home.order[4].attrib |= 1;
        home.order[5].attrib |= 1;
        home.order[6].attrib |= 1;
        if(i >= 0 && i < 4)
            home.order[2 + i].attrib &= -2;
    }

    private void setAsWingman(int i)
    {
        setAsWingLeader(-1);
        if(isRemote())
            return;
        if((i & 1) == 0)
            home.order[1].attrib &= -2;
        else
            home.order[1].attrib |= 1;
    }

    public void activate()
    {
        if(isActive())
            return;
        if(Main.cur().netServerParams != null && Main.cur().netServerParams.isDogfight() && Main.cur().mission.zutiRadar_EnableTowerCommunications)
        {
            if(World.getPlayerAircraft() != null)
                ((Maneuver)World.getPlayerAircraft().FM).silence = false;
            missionLoaded();
        }
        if(isLocal())
        {
            if(!Actor.isAlive(Player) || World.isPlayerDead() || World.isPlayerParatrooper())
                return;
            activateHotKeyCmd(true);
            disableAircraftCmds();
            HotKeyCmdEnv.enable("gui", false);
        }
        if(isLocalClient())
            ((NetUser)NetEnv.host()).orderCmd(-1);
        bActive = true;
        curOrdersTree = this;
        home.run();
        curOrdersTree = null;
    }

    public void unactivate()
    {
        if(!isActive())
            return;
        if(isLocal())
        {
            activateHotKeyCmd(false);
            enableAircraftCmds();
            HotKeyCmdEnv.enable("gui", true);
            HUD.order(null);
        }
        if(isLocalClient())
            ((NetUser)NetEnv.host()).orderCmd(-2);
        bActive = false;
    }

    private void disableAircraftCmds()
    {
        HashMapInt hashmapint = HotKeyEnv.env("pilot").all();
        HashMapInt hashmapint1 = HotKeyEnv.env("orders").all();
        for(HashMapIntEntry hashmapintentry = hashmapint1.nextEntry(null); hashmapintentry != null; hashmapintentry = hashmapint1.nextEntry(hashmapintentry))
        {
            int i = hashmapintentry.getKey();
            String s = (String)hashmapint.get(i);
            if(s != null)
                disabledHotKeys.put(i, s);
        }

        for(HashMapIntEntry hashmapintentry1 = disabledHotKeys.nextEntry(null); hashmapintentry1 != null; hashmapintentry1 = disabledHotKeys.nextEntry(hashmapintentry1))
        {
            int j = hashmapintentry1.getKey();
            hashmapint.remove(j);
        }

    }

    private void enableAircraftCmds()
    {
        HashMapInt hashmapint = HotKeyEnv.env("pilot").all();
        for(HashMapIntEntry hashmapintentry = disabledHotKeys.nextEntry(null); hashmapintentry != null; hashmapintentry = disabledHotKeys.nextEntry(hashmapintentry))
            hashmapint.put(hashmapintentry.getKey(), hashmapintentry.getValue());

        disabledHotKeys.clear();
    }

    public void execCmd(int i)
    {
        Order aorder[] = cur.order;
        int j = i;
        curOrdersTree = this;
        if(i == 0)
        {
            for(int k = aorder.length - 1; k >= 0; k--)
                if(aorder[k] != null)
                {
                    aorder[k].preRun();
                    curOrdersTree = null;
                    if(isLocalClient())
                        ((NetUser)NetEnv.host()).orderCmd(0);
                    return;
                }

        } else
        {
            for(int l = 0; l < aorder.length; l++)
            {
                if(aorder[l] != null && i == 0)
                {
                    if(isLocal() && (aorder[l].attrib & 1) != 0)
                    {
                        unactivate();
                    } else
                    {
                        if(isLocalClient())
                            if("Attack_My_Target".equals(aorder[l].name))
                                ((NetUser)NetEnv.host()).orderCmd(j, Selector.getTarget());
                            else
                                ((NetUser)NetEnv.host()).orderCmd(j);
                        try
                        {
                            aorder[l].preRun();
                            if(isLocalServer() || isRemoteServer())
                                aorder[l].run();
                        }
                        catch(Exception exception)
                        {
                            System.out.println("User command failed: " + exception.getMessage());
                            exception.printStackTrace();
                        }
                        if(Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster() && Main.cur().netServerParams.isCoop() && aorder[l].subOrders == null)
                            chatLog(aorder[l]);
                        if(aorder[l].subOrders == null)
                            unactivate();
                    }
                    curOrdersTree = null;
                    return;
                }
                i--;
            }

        }
        curOrdersTree = null;
    }

    protected void doCmd(int i, boolean flag)
    {
        if(flag)
            return;
        if(i != 0 && (!Actor.isAlive(Player) || World.isPlayerDead() || World.isPlayerParatrooper()))
        {
            return;
        } else
        {
            execCmd(i);
            return;
        }
    }

    private void activateHotKeyCmd(boolean flag)
    {
        if(flag)
        {
            for(int i = 0; i < hotKeyCmd.length; i++)
                hotKeyCmd[i].enable(true);

        } else
        {
            for(int j = 0; j < hotKeyCmd.length; j++)
                hotKeyCmd[j].enable(false);

        }
    }

    public OrdersTree(boolean flag)
    {
        bActive = false;
        CommandSet = new Aircraft[16];
        frequency = new Boolean(true);
        alone = false;
        shipIDList = new String[10];
        disabledHotKeys = new HashMapInt();
        if(!flag)
        {
            return;
        } else
        {
            HotKeyCmdEnv.setCurrentEnv("orders");
            HotKeyEnv.fromIni("orders", Config.cur.ini, "HotKey orders");
            cmdEnv = HotKeyCmdEnv.currentEnv();
            HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "deactivate", null) {

                public boolean isDisableIfTimePaused()
                {
                    return true;
                }

                public void end()
                {
                    if(Main3D.cur3D().ordersTree.isActive())
                        Main3D.cur3D().ordersTree.unactivate();
                }

                public void created()
                {
                    setRecordId(261);
                }

            }
);
            HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "activate", "0") {

                public boolean isDisableIfTimePaused()
                {
                    return true;
                }

                public void begin()
                {
                    if(Main3D.cur3D().ordersTree.isActive())
                        Main3D.cur3D().ordersTree.unactivate();
                    else
                        Main3D.cur3D().ordersTree.activate();
                }

                public void created()
                {
                    setRecordId(260);
                }

            }
);
            hotKeyCmd = new HotKeyCmdFire[10];
            HotKeyCmdEnv _tmp = cmdEnv;
            HotKeyCmdEnv.addCmd(hotKeyCmd[0] = new HotKeyCmdFire(null, "order0", 0, 250));
            HotKeyCmdEnv _tmp1 = cmdEnv;
            HotKeyCmdEnv.addCmd(hotKeyCmd[1] = new HotKeyCmdFire(null, "order1", 1, 251));
            HotKeyCmdEnv _tmp2 = cmdEnv;
            HotKeyCmdEnv.addCmd(hotKeyCmd[2] = new HotKeyCmdFire(null, "order2", 2, 252));
            HotKeyCmdEnv _tmp3 = cmdEnv;
            HotKeyCmdEnv.addCmd(hotKeyCmd[3] = new HotKeyCmdFire(null, "order3", 3, 253));
            HotKeyCmdEnv _tmp4 = cmdEnv;
            HotKeyCmdEnv.addCmd(hotKeyCmd[4] = new HotKeyCmdFire(null, "order4", 4, 254));
            HotKeyCmdEnv _tmp5 = cmdEnv;
            HotKeyCmdEnv.addCmd(hotKeyCmd[5] = new HotKeyCmdFire(null, "order5", 5, 255));
            HotKeyCmdEnv _tmp6 = cmdEnv;
            HotKeyCmdEnv.addCmd(hotKeyCmd[6] = new HotKeyCmdFire(null, "order6", 6, 256));
            HotKeyCmdEnv _tmp7 = cmdEnv;
            HotKeyCmdEnv.addCmd(hotKeyCmd[7] = new HotKeyCmdFire(null, "order7", 7, 257));
            HotKeyCmdEnv _tmp8 = cmdEnv;
            HotKeyCmdEnv.addCmd(hotKeyCmd[8] = new HotKeyCmdFire(null, "order8", 8, 258));
            HotKeyCmdEnv _tmp9 = cmdEnv;
            HotKeyCmdEnv.addCmd(hotKeyCmd[9] = new HotKeyCmdFire(null, "order9", 9, 259));
            activateHotKeyCmd(false);
            return;
        }
    }

    public String[] getShipIDs()
    {
        return shipIDList;
    }

    public void addShipIDs(int i, int j, Actor actor, String s, String s1)
    {
        if(j == -1)
            shipIDList[i] = null;
        else
            shipIDList[i] = "ID:" + Beacon.getBeaconID(j) + "  " + s + " " + s1;
    }

    private void setAsDogfight()
    {
        home = GroupLeader;
        if(!isRemote())
        {
            for(int i = 0; i < home.order.length; i++)
                if(home.order[i] != null)
                    home.order[i].attrib &= -2;

        }
    }

    static Orders GroundTargets;
    static Orders ToWingman;
    static Orders Change_Formation;
    static Orders Tactical;
    static Orders Navigation;
    static Orders ToParaZveno;
    static Orders ToGroupSquad;
    static Orders ToGroundControl;
    static Orders Frequency;
    static Orders SquadLeader;
    static Orders GroupLeader;
    public static final String envName = "orders";
    public static OrdersTree curOrdersTree;
    protected Orders home;
    protected Orders cur;
    protected boolean bActive;
    protected HotKeyCmdEnv cmdEnv;
    protected Aircraft Player;
    protected Wing PlayerWing;
    protected Squadron PlayerSquad;
    protected Regiment PlayerRegiment;
    protected Aircraft CommandSet[];
    protected Boolean frequency;
    protected boolean alone;
    public static final Boolean FREQ_FRIENDLY = new Boolean(true);
    public static final Boolean FREQ_ENEMY = new Boolean(true);
    public static final int shipIDListSize = 10;
    private String shipIDList[];
    protected static final int CONTEXT_LOCAL_SERVER = 0;
    protected static final int CONTEXT_LOCAL_CLIENT = 1;
    protected static final int CONTEXT_REMOTE_SERVER = 2;
    protected int context;
    private HashMapInt disabledHotKeys;
    private HotKeyCmdFire hotKeyCmd[];
    static Orders ZutiDogfightOptions;

    static 
    {
        GroundTargets = new Orders(new Order[] {
            new Order("GroundTargets"), new OrderGT("Attack_All") {

                public void run()
                {
                    run(0);
                }

            }
, new OrderGT("Attack_Tanks") {

                public void run()
                {
                    run(1);
                }

            }
, new OrderGT("Attack_Flak") {

                public void run()
                {
                    run(2);
                }

            }
, new OrderGT("Attack_Vehicles") {

                public void run()
                {
                    run(3);
                }

            }
, new OrderGT("Attack_Train") {

                public void run()
                {
                    run(4);
                }

            }
, new OrderGT("Attack_Bridge") {

                public void run()
                {
                    run(5);
                }

            }
, new OrderGT("Attack_Ships") {

                public void run()
                {
                    run(6);
                }

            }
, null, new OrderBack()
        });
        ToWingman = new Orders(new Order[] {
            new Order("Task"), new OrderCover_Me(), new OrderTarget_All(), new OrderAttack_Fighters(), new OrderAttack_Bombers(), new OrderAttack_My_Target(), new Order("Ground_Targets", GroundTargets), new OrderDrop_Tanks(), new OrderBreak(), new OrderRejoin(), 
            null, new OrderBack()
        });
        Change_Formation = new Orders(new Order[] {
            new Order("C_F"), new OrderChange_Formation_To_1(), new OrderChange_Formation_To_2(), new OrderChange_Formation_To_3(), new OrderChange_Formation_To_4(), new OrderChange_Formation_To_5(), new OrderChange_Formation_To_6(), new OrderChange_Formation_To_7(), null, new OrderBack()
        });
        Tactical = new Orders(new Order[] {
            new Order("Tactical"), new OrderBreak(), new OrderRejoin(), new OrderTighten_Formation(), new OrderLoosen_Formation(), new Order("Change_Formation_Submenu", Change_Formation), null, new OrderBack()
        });
        Navigation = new Orders(new Order[] {
            new Order("Navigation"), new Order("Next_Checkpoint") {

                public void run()
                {
                    Voice.setSyncMode(1);
                    for(int i = 0; i < CommandSet().length; i++)
                    {
                        if(!Actor.isAlive(CommandSet()[i]) || !(CommandSet()[i].FM instanceof Pilot))
                            continue;
                        Pilot pilot = (Pilot)CommandSet()[i].FM;
                        if(!pilot.AP.way.isLanding())
                            pilot.AP.way.next();
                        if(isEnableVoice() && CommandSet()[i] != Player() && (CommandSet()[i].getWing() == Player().getWing() || CommandSet()[i].aircIndex() == 0))
                            Voice.speakNextWayPoint(CommandSet()[i]);
                    }

                    Voice.setSyncMode(0);
                }

            }
, new Order("Prev_Checkpoint") {

                public void run()
                {
                    Voice.setSyncMode(1);
                    for(int i = 0; i < CommandSet().length; i++)
                    {
                        if(!Actor.isAlive(CommandSet()[i]) || !(CommandSet()[i].FM instanceof Pilot))
                            continue;
                        Pilot pilot = (Pilot)CommandSet()[i].FM;
                        if(!pilot.AP.way.isLanding())
                            pilot.AP.way.prev();
                        if(isEnableVoice() && CommandSet()[i] != Player() && (CommandSet()[i].getWing() == Player().getWing() || CommandSet()[i].aircIndex() == 0))
                            Voice.speakPrevWayPoint(CommandSet()[i]);
                    }

                    Voice.setSyncMode(0);
                }

            }
, new Order("Return_To_Base") {

                public void run()
                {
                    Voice.setSyncMode(1);
                    boolean flag = false;
                    Maneuver maneuver = (Maneuver)Player().FM;
                    for(int i = 0; i < CommandSet().length; i++)
                    {
                        Aircraft aircraft = CommandSet()[i];
                        if(!Actor.isAlive(aircraft) || !(aircraft.FM instanceof Pilot))
                            continue;
                        Pilot pilot = (Pilot)CommandSet()[i].FM;
                        if(!pilot.AP.way.isLanding())
                        {
                            pilot.AP.way.last();
                            pilot.AP.way.prev();
                        }
                        if(pilot.Group != null)
                        {
                            pilot.Group.setGroupTask(1);
                            pilot.Group.timeOutForTaskSwitch = 480;
                        }
                        if(isEnableVoice() && aircraft != Player() && (aircraft.getWing() == Player().getWing() || aircraft.aircIndex() == 0))
                            Voice.speakReturnToBase(CommandSet()[i]);
                        if(aircraft != Player() && pilot.Group == maneuver.Group)
                            flag = true;
                    }

                    Voice.setSyncMode(0);
                    if(flag)
                    {
                        AirGroup airgroup = maneuver.Group;
                        AirGroup airgroup1 = new AirGroup(maneuver.Group);
                        airgroup1.rejoinGroup = null;
                        maneuver.Group.delAircraft(Player());
                        airgroup1.addAircraft(Player());
                        airgroup.setGroupTask(1);
                    }
                }

            }
, new Order("Hang_On_Here") {

                public void run()
                {
                    for(int i = 0; i < CommandSet().length; i++)
                    {
                        Voice.setSyncMode(1);
                        if(!Actor.isAlive(CommandSet()[i]) || !(CommandSet()[i].FM instanceof Pilot))
                            continue;
                        Pilot pilot = (Pilot)CommandSet()[i].FM;
                        if(pilot.Group != null && pilot.Group.nOfAirc > 0 && pilot == pilot.Group.airc[0].FM && !pilot.isBusy() && Actor.isAlive(pilot.Group.airc[pilot.Group.nOfAirc - 1]) && (pilot.Group.airc[pilot.Group.nOfAirc - 1].FM instanceof Pilot))
                        {
                            Pilot pilot1 = (Pilot)pilot.Group.airc[pilot.Group.nOfAirc - 1].FM;
                            pilot.Group.setFormationAndScale(pilot1.formationType, pilot1.formationScale, false);
                            pilot.Group.setGroupTask(1);
                            pilot.clear_stack();
                            pilot.push(45);
                            pilot.push(45);
                            pilot.pop();
                        }
                        if(isEnableVoice() && CommandSet()[i] != Player() && (CommandSet()[i].getWing() == Player().getWing() || CommandSet()[i].aircIndex() == 0))
                            Voice.speakHangOn(CommandSet()[i]);
                    }

                    Voice.setSyncMode(0);
                }

            }
, null, new OrderBack()
        });
        ToParaZveno = new Orders(new Order[] {
            new Order("Task"), new OrderCover_Me(), new OrderTarget_All(), new OrderAttack_Fighters(), new OrderAttack_Bombers(), new OrderAttack_My_Target(), new Order("Ground_Targets", GroundTargets), new OrderDrop_Tanks(), new Order("Tactical_Submenu", Tactical), new Order("Navigation_Submenu", Navigation), 
            null, new OrderBack()
        });
        ToGroupSquad = ToParaZveno;
        ToGroundControl = new Orders(new Order[] {
            new Order("GroundControl"), new OrderRequest_Assistance(), new OrderVector_To_Home_Base(), new OrderVector_To_Target(), new OrderRequest_For_Landing(), new OrderRequest_For_Takeoff(), new OrderRequest_For_RunwayLights(), new OrderBack()
        });
        Frequency = new Orders(new Order[] {
            new Order("Frequency"), new Order("Friendly") {

                public void preRun()
                {
                    Player().FM.AS.setBeacon(0);
                    OrdersTree.curOrdersTree.frequency = OrdersTree.FREQ_FRIENDLY;
                }

            }
, new Order("Enemy") {

                public void preRun()
                {
                    Player().FM.AS.setBeacon(0);
                    OrdersTree.curOrdersTree.frequency = OrdersTree.FREQ_ENEMY;
                }

            }
, null, new OrderBack()
        });
        SquadLeader = new Orders(new Order[] {
            new Order("MainMenu"), new Order("Wingman", "deWingman", ToWingman) {

                public void run()
                {
                    cset(Wingman());
                    super.run();
                }

            }
, new Order("Para_1", "dePara_1", ToParaZveno) {

                public void run()
                {
                    cset(PlayerSquad().wing[0]);
                    super.run();
                }

            }
, new Order("Para_2", "dePara_2", ToParaZveno) {

                public void run()
                {
                    cset(PlayerSquad().wing[1]);
                    super.run();
                }

            }
, new Order("Para_3", "dePara_3", ToParaZveno) {

                public void run()
                {
                    cset(PlayerSquad().wing[2]);
                    super.run();
                }

            }
, new Order("Para_4", "dePara_4", ToParaZveno) {

                public void run()
                {
                    cset(PlayerSquad().wing[3]);
                    super.run();
                }

            }
, new Order("Group", "deGroup", ToGroupSquad) {

                public void run()
                {
                    cset(PlayerSquad());
                    super.run();
                }

            }
, new OrderAnyone_Help_Me(), new Order("Ground_Control", ToGroundControl), new Order("Frequency", Frequency), 
            null, new OrderBack()
        });
        GroupLeader = new Orders(new Order[] {
            new Order("MainMenu"), new Order("Wingman", "deWingman", ToWingman) {

                public void run()
                {
                    cset(Wingman());
                    super.run();
                }

            }
, new Order("Zveno_1", "deZveno_1", ToParaZveno) {

                public void run()
                {
                    cset(PlayerSquad().wing[0]);
                    super.run();
                }

            }
, new Order("Zveno_2", "deZveno_2", ToParaZveno) {

                public void run()
                {
                    cset(PlayerSquad().wing[1]);
                    super.run();
                }

            }
, new Order("Zveno_3", "deZveno_3", ToParaZveno) {

                public void run()
                {
                    cset(PlayerSquad().wing[2]);
                    super.run();
                }

            }
, new Order("Zveno_4", "deZveno_4", ToParaZveno) {

                public void run()
                {
                    cset(PlayerSquad().wing[3]);
                    super.run();
                }

            }
, new Order("Squadron", "deSquadron", ToGroupSquad) {

                public void run()
                {
                    cset(PlayerSquad());
                    super.run();
                }

            }
, new OrderAnyone_Help_Me(), new Order("Ground_Control", ToGroundControl), new Order("Frequency", Frequency), 
            null, new OrderBack()
        });
        ZutiDogfightOptions = new Orders(new Order[] {
            new Order("MainMenu"), new Order("Wingman", "deWingman", ToWingman) {

                public void run()
                {
                    cset(Wingman());
                    super.run();
                }

            }
, new Order("Squadron", "deSquadron", ToGroupSquad) {

                public void run()
                {
                    cset(PlayerSquad());
                    super.run();
                }

            }
, new OrderAnyone_Help_Me(), new Order("Frequency", Frequency), new Order("Ground_Control", ToGroundControl), null, new OrderBack()
        });
    }
}