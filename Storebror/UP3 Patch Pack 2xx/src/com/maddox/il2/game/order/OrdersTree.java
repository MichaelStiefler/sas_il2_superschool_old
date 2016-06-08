/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import java.util.ArrayList;
import java.util.List;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Squadron;
import com.maddox.il2.ai.VisCheck;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.Selector;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeTransport;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.radios.Beacon;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.HotKeyCmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.NetEnv;
import com.maddox.util.HashMapInt;
import com.maddox.util.HashMapIntEntry;

public class OrdersTree {
    class HotKeyCmdFire extends HotKeyCmd {

        public void begin() {
            OrdersTree.this.doCmd(this.cmd, true);
        }

        public void end() {
            OrdersTree.this.doCmd(this.cmd, false);
        }

        public boolean isDisableIfTimePaused() {
            return true;
        }

        int cmd;

        public HotKeyCmdFire(String s, String s1, int i, int j) {
            super(true, s1, s);
            this.cmd = i;
            this.setRecordId(j);
        }
    }

    protected boolean isLocalServer() {
        return this.context == 0;
    }

    protected boolean isLocalClient() {
        return this.context == 1;
    }

    protected boolean isRemoteServer() {
        return this.context == 2;
    }

    protected boolean isRemote() {
        return this.context == 2;
    }

    protected boolean isLocal() {
        return this.context != 2;
    }

    protected boolean _isEnableVoice() {
        return this.isLocalServer() || this.isRemoteServer();
    }

    private void chatLog(Order order) {
        if (!Actor.isValid(this.Player))
            return;
        int i = this.Player.getArmy();
        ArrayList arraylist = new ArrayList();
        while (order != null) {
            arraylist.add(0, order.name(i));
            if (order.orders.upOrders == null)
                break;
            if (order != order.orders.order[0]) {
                order = order.orders.order[0];
                continue;
            }
            if (order.orders.upOrders.upOrders == null)
                break;
            order = order.orders.upOrders.order[0];
        }
        StringBuffer stringbuffer = new StringBuffer();
        for (int j = 0; j < arraylist.size(); j++) {
            if (j > 0)
                stringbuffer.append(" ");
            stringbuffer.append((String) arraylist.get(j));
        }

        NetUser netuser = null;
        List list = NetEnv.hosts();
        ArrayList arraylist1 = new ArrayList();
        for (int k = 0; k < list.size(); k++) {
            NetUser netuser1 = (NetUser) list.get(k);
            if (this == netuser1.ordersTree)
                netuser = netuser1;
            else if (i == netuser1.getArmy())
                arraylist1.add(netuser1);
        }

        if (netuser == null)
            netuser = (NetUser) NetEnv.host();
        else if (i == ((NetUser) NetEnv.host()).getArmy())
            arraylist1.add(NetEnv.host());
        arraylist1.add(netuser);
        Main.cur().chat.send(netuser, stringbuffer.toString(), arraylist1, (byte) 1, i == World.getPlayerArmy());
    }

    public Boolean frequency() {
        return this.frequency;
    }

    public void setFrequency(Boolean boolean1) {
        this.frequency = boolean1;
    }

    public boolean alone() {
        return this.alone;
    }

    protected void _cset(Actor actor) {
        for (int i = 0; i < 16; i++)
            this.CommandSet[i] = null;

        if (this.frequency == null || !this.frequency.booleanValue())
            return;
        this.alone = false;
        if (!Actor.isAlive(actor))
            return;
        if (actor instanceof Aircraft) {
            this.CommandSet[0] = (Aircraft) actor;
            this.alone = true;
        } else if (actor instanceof Section) {
            Section section = (Section) actor;
            for (int j = 0; j < 2; j++)
                if (Actor.isAlive(section.airc[j]))
                    this.CommandSet[j] = section.airc[j];

        } else if (actor instanceof Wing) {
            Wing wing = (Wing) actor;
            for (int k = 0; k < 4; k++)
                if (Actor.isAlive(wing.airc[k]))
                    this.CommandSet[k] = wing.airc[k];

        } else if (actor instanceof Squadron) {
            Squadron squadron = (Squadron) actor;
            for (int l = 0; l < 16; l++)
                if (squadron.wing[l >> 2] != null && Actor.isAlive(squadron.wing[l >> 2].airc[l & 3]))
                    this.CommandSet[l] = squadron.wing[l >> 2].airc[l & 3];

        }
    }

    public boolean isActive() {
        return this.bActive;
    }

    public void missionLoaded() {
        this.context = 0;
        this.missionLoaded(World.getPlayerAircraft());
    }

    public void netMissionLoaded(Aircraft aircraft) {
        if (aircraft == null) {
            this.context = 2;
        } else {
            boolean flag = Mission.isNet() && aircraft.netUser() != NetEnv.host();
            if (aircraft == World.getPlayerAircraft() && !flag) {
                if (Mission.isSingle() || Mission.isServer())
                    this.context = 0;
                else
                    this.context = 1;
            } else {
                this.context = 2;
            }
        }
        this.missionLoaded(aircraft);
    }

    private void missionLoaded(Aircraft aircraft) {
        this.Player = aircraft;
        if (this.Player == null)
            return;
        curOrdersTree = this;
        Main.cur();
        if (Mission.isDogfight()) {
            this.setAsDogfight();
            curOrdersTree = null;
            return;
        }
        Wing wing = this.Player.getWing();
        this.PlayerWing = wing;
        this.Section = this.getSection(wing);
        this.PlayerSquad = wing.squadron();
        this.PlayerRegiment = wing.regiment();
        int i = this.Player.aircIndex();
        int j = this.Player.aircNumber();
        int k = wing.indexInSquadron();
        int l = wing.squadron().getWingsNumber();
        if (j <= 2)
            this.setAsSquadLeader();
        else
            this.setAsGroupLeader();
        Wing wing1 = null;
        for (int i1 = 0; i1 < this.PlayerSquad.wing.length; i1++) {
            if (this.PlayerSquad.wing[i1] == null)
                continue;
            wing1 = this.PlayerSquad.wing[i1];
            break;
        }

        if (this.PlayerWing != wing1 || l < 2) {
            if (i == 0 && j > 1) {
                this.setAsWingLeader(k);
            } else {
                this.setAsWingman(i);
                if (!this.isRemote() && (this.Player instanceof TypeTransport))
                    this.home.order[1].attrib |= 1;
            }
        } else if (i != 0)
            this.setAsWingman(i);
        else if (!this.isRemote()) {
            for (int j1 = 0; j1 < 4; j1++)
                if (this.PlayerSquad.wing[j1] == null)
                    if (this.Player.aircNumber() <= 2)
                        this.home.order[3 + j1].attrib |= 1;
                    else
                        this.home.order[3 + j1].attrib |= 1;

        }
        curOrdersTree = null;
    }

    public void resetGameClear() {
        this.Player = null;
        this.Section = null;
        this.PlayerWing = null;
        this.PlayerSquad = null;
        this.PlayerRegiment = null;
        for (int i = 0; i < this.CommandSet.length; i++)
            this.CommandSet[i] = null;

    }

    private Section getSection(Wing wing) {
        if (wing.airc.length < 3) {
            return null;
        } else {
//            OrdersTree _tmp = this;
            Section section = com.maddox.il2.game.order.Section.New(wing, "playerSection");
            section.airc[0] = wing.airc[2];
            section.airc[1] = wing.airc[3];
            return section;
        }
    }

    private void setAsSquadLeader() {
        this.home = SquadLeader;
        if (this.isRemote())
            return;
        for (int i = 0; i < this.home.order.length; i++)
            if (this.home.order[i] != null)
                this.home.order[i].attrib &= -2;

        this.home.order[2].attrib |= 1;
        Orders orders = Technical;
        if (orders.order[1] != null)
            orders.order[1].attrib &= -2;
    }

    private void setAsGroupLeader() {
        this.home = GroupLeader;
        if (this.isRemote())
            return;
        for (int i = 0; i < this.home.order.length; i++)
            if (this.home.order[i] != null)
                this.home.order[i].attrib &= -2;

        Orders orders = Technical;
        if (orders.order[1] != null)
            orders.order[1].attrib &= -2;
    }

    private void setAsWingLeader(int i) {
        if (this.isRemote())
            return;
        for (int j = 0; j < this.home.order.length; j++)
            if (this.home.order[j] != null)
                this.home.order[j].attrib &= -2;

        for (int k = 3; k < 8; k++)
            if (this.home.order[k] != null)
                this.home.order[k].attrib |= 1;

        Orders orders = Technical;
        if (orders.order[1] != null)
            orders.order[1].attrib &= -2;
        if (i >= 0 && i < 4)
            this.home.order[3 + i].attrib &= -2;
    }

    private void setAsWingman(int i) {
        this.setAsWingLeader(-1);
        if (this.isRemote())
            return;
        Orders orders = Technical;
        if (i != 0)
            orders.order[1].attrib |= 1;
        if ((i & 1) == 0) {
            this.home.order[1].attrib &= -2;
            if (i == 2)
                this.home.order[2].attrib &= -2;
        } else {
            this.home.order[1].attrib |= 1;
            this.home.order[2].attrib |= 1;
        }
    }

    public void activate() {
        if (this.isActive())
            return;
        if (Main.cur().netServerParams != null && Main.cur().netServerParams.isDogfight() && Mission.MDS_VARIABLES().zutiMisc_EnableTowerCommunications) {
            if (World.getPlayerAircraft() != null)
                ((Maneuver) World.getPlayerAircraft().FM).silence = false;
            this.missionLoaded();
        }
        if (this.isLocal()) {
            if (!Actor.isAlive(this.Player) || World.isPlayerDead() || World.isPlayerParatrooper())
                return;
            this.activateHotKeyCmd(true);
            this.disableAircraftCmds();
            HotKeyCmdEnv.enable("gui", false);
        }
        if (this.isLocalClient())
            ((NetUser) NetEnv.host()).orderCmd(-1);
        this.bActive = true;
        curOrdersTree = this;
        this.home.run();
        curOrdersTree = null;
    }

    public void unactivate() {
        if (!this.isActive())
            return;
        if (this.isLocal()) {
            this.activateHotKeyCmd(false);
            this.enableAircraftCmds();
            HotKeyCmdEnv.enable("gui", true);
            HUD.order(null);
        }
        if (this.isLocalClient())
            ((NetUser) NetEnv.host()).orderCmd(-2);
        this.bActive = false;
    }

    private void disableAircraftCmds() {
        for (int i = 0; i < hotKeyEnvNames.length; i++) {
            HashMapInt hashmapint = HotKeyEnv.env(hotKeyEnvNames[i]).all();
            HashMapInt hashmapint1 = HotKeyEnv.env("orders").all();
            for (HashMapIntEntry hashmapintentry = hashmapint1.nextEntry(null); hashmapintentry != null; hashmapintentry = hashmapint1.nextEntry(hashmapintentry)) {
                int j = hashmapintentry.getKey();
                String s = (String) hashmapint.get(j);
                if (s != null)
                    this.disabledHotKeys[i].put(j, s);
            }

            for (HashMapIntEntry hashmapintentry1 = this.disabledHotKeys[i].nextEntry(null); hashmapintentry1 != null; hashmapintentry1 = this.disabledHotKeys[i].nextEntry(hashmapintentry1)) {
                int k = hashmapintentry1.getKey();
                hashmapint.remove(k);
            }

        }

    }

    private void enableAircraftCmds() {
        for (int i = 0; i < hotKeyEnvNames.length; i++) {
            HashMapInt hashmapint = HotKeyEnv.env(hotKeyEnvNames[i]).all();
            for (HashMapIntEntry hashmapintentry = this.disabledHotKeys[i].nextEntry(null); hashmapintentry != null; hashmapintentry = this.disabledHotKeys[i].nextEntry(hashmapintentry))
                hashmapint.put(hashmapintentry.getKey(), hashmapintentry.getValue());

            this.disabledHotKeys[i].clear();
        }

    }

    public void execCmd(int i) {
        Order aorder[] = this.cur.order;
        int j = i;
        curOrdersTree = this;
        if (i == 0) {
            for (int k = aorder.length - 1; k >= 0; k--)
                if (aorder[k] != null) {
                    aorder[k].preRun();
                    curOrdersTree = null;
                    if (this.isLocalClient())
                        ((NetUser) NetEnv.host()).orderCmd(0);
                    return;
                }

        } else {
            for (int l = 0; l < aorder.length; l++) {
                if (aorder[l] != null && i == 0) {
                    if (this.isLocal() && (aorder[l].attrib & 1) != 0) {
                        this.unactivate();
                    } else {
                        if (this.isLocalClient())
                            if ("Attack_My_Target".equals(aorder[l].name) || "Target_All".equals(aorder[l].name) || "Attack_Fighters".equals(aorder[l].name) || "Attack_Bombers".equals(aorder[l].name)) {
                                VisCheck.playerVisibilityCheck(World.getPlayerAircraft(), false, 1.0F);
                                ((NetUser) NetEnv.host()).orderCmd(j, Selector.getTarget());
                            } else {
                                ((NetUser) NetEnv.host()).orderCmd(j);
                            }
                        try {
                            aorder[l].preRun();
                            if (this.isLocalServer() || this.isRemoteServer())
                                aorder[l].run();
                        } catch (Exception exception) {
                            System.out.println("User command failed: " + exception.getMessage());
                            exception.printStackTrace();
                        }
                        if (Main.cur().netServerParams != null && Main.cur().netServerParams.isMaster() && Main.cur().netServerParams.isCoop() && aorder[l].subOrders == null)
                            this.chatLog(aorder[l]);
                        if (aorder[l].subOrders == null)
                            this.unactivate();
                    }
                    curOrdersTree = null;
                    return;
                }
                i--;
            }

        }
        curOrdersTree = null;
    }

    protected void doCmd(int i, boolean flag) {
        if (flag)
            return;
        if (i != 0 && (!Actor.isAlive(this.Player) || World.isPlayerDead() || World.isPlayerParatrooper())) {
            return;
        } else {
            this.execCmd(i);
            return;
        }
    }

    private void activateHotKeyCmd(boolean flag) {
        if (flag) {
            for (int i = 0; i < this.hotKeyCmd.length; i++)
                this.hotKeyCmd[i].enable(true);

        } else {
            for (int j = 0; j < this.hotKeyCmd.length; j++)
                this.hotKeyCmd[j].enable(false);

        }
    }

    public OrdersTree(boolean flag) {
        this.bActive = false;
        this.CommandSet = new Aircraft[16];
        this.frequency = new Boolean(true);
        this.alone = false;
        this.shipIDList = new String[10];
        if (!flag) {
            return;
        } else {
            HotKeyCmdEnv.setCurrentEnv("orders");
            HotKeyEnv.fromIni("orders", Config.cur.ini, "HotKey orders");
            this.cmdEnv = HotKeyCmdEnv.currentEnv();
            HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "deactivate", null) {

                public boolean isDisableIfTimePaused() {
                    return true;
                }

                public void end() {
                    if (Main3D.cur3D().ordersTree.isActive())
                        Main3D.cur3D().ordersTree.unactivate();
                }

                public void created() {
                    this.setRecordId(261);
                }

            });
            HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "activate", "0") {

                public boolean isDisableIfTimePaused() {
                    return true;
                }

                public void begin() {
                    if (Main3D.cur3D().ordersTree.isActive())
                        Main3D.cur3D().ordersTree.unactivate();
                    else
                        Main3D.cur3D().ordersTree.activate();
                }

                public void created() {
                    this.setRecordId(260);
                }

            });
            this.hotKeyCmd = new HotKeyCmdFire[18];
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[0] = new HotKeyCmdFire(null, "order0", 0, 250));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[1] = new HotKeyCmdFire(null, "order1", 1, 251));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[2] = new HotKeyCmdFire(null, "order2", 2, 252));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[3] = new HotKeyCmdFire(null, "order3", 3, 253));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[4] = new HotKeyCmdFire(null, "order4", 4, 254));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[5] = new HotKeyCmdFire(null, "order5", 5, 255));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[6] = new HotKeyCmdFire(null, "order6", 6, 256));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[7] = new HotKeyCmdFire(null, "order7", 7, 257));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[8] = new HotKeyCmdFire(null, "order8", 8, 258));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[9] = new HotKeyCmdFire(null, "order9", 9, 259));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[10] = new HotKeyCmdFire(null, "order10", 10, 262));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[11] = new HotKeyCmdFire(null, "order11", 11, 263));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[12] = new HotKeyCmdFire(null, "order12", 12, 264));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[13] = new HotKeyCmdFire(null, "order13", 13, 265));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[14] = new HotKeyCmdFire(null, "order14", 14, 266));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[15] = new HotKeyCmdFire(null, "order15", 15, 267));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[16] = new HotKeyCmdFire(null, "order16", 16, 268));
            HotKeyCmdEnv.addCmd(this.hotKeyCmd[17] = new HotKeyCmdFire(null, "order17", 17, 269));
            this.activateHotKeyCmd(false);
            return;
        }
    }

    public String[] getShipIDs() {
        return this.shipIDList;
    }

    public void addShipIDs(int i, int j, Actor actor, String s, String s1) {
        if (j == -1)
            this.shipIDList[i] = null;
        else
            this.shipIDList[i] = "ID:" + Beacon.getBeaconID(j) + "  " + s + " " + s1;
    }

    private void setAsDogfight() {
        this.home = GroupLeader;
        if (!this.isRemote()) {
            for (int i = 0; i < this.home.order.length; i++)
                if (this.home.order[i] != null)
                    this.home.order[i].attrib &= -2;

        }
    }

    static Orders               DropCommand           = new Orders(new Order[] { new Order("D_C"), new OrderDrop_With_Me(), new OrderDrop_At_Will(), null, new OrderBack() });
    static Orders               GroundTargets;
    static Orders               ToWingman;
    static Orders               ToSection;
    static Orders               Change_Formation;
    static Orders               Tactical;
    static Orders               Navigation;
    static Orders               Technical;
    static Orders               ToParaZveno;
    static Orders               ToGroupSquad;
    static Orders               ToGroundControl;
    static Orders               Frequency;
    static Orders               SquadLeader;
    static Orders               GroupLeader;
    public static final String  envName               = "orders";
    public static OrdersTree    curOrdersTree;
    protected Orders            home;
    protected Orders            cur;
    protected boolean           bActive;
    protected HotKeyCmdEnv      cmdEnv;
    protected Aircraft          Player;
    protected Section           Section;
    protected Wing              PlayerWing;
    protected Squadron          PlayerSquad;
    protected Regiment          PlayerRegiment;
    public Aircraft             CommandSet[];
    protected Boolean           frequency;
    protected boolean           alone;
    public static final Boolean FREQ_FRIENDLY         = new Boolean(true);
    public static final Boolean FREQ_ENEMY            = new Boolean(false);
    public static final int     shipIDListSize        = 10;
    private String              shipIDList[];
    protected static final int  CONTEXT_LOCAL_SERVER  = 0;
    protected static final int  CONTEXT_LOCAL_CLIENT  = 1;
    protected static final int  CONTEXT_REMOTE_SERVER = 2;
    protected int               context;
    private static String       hotKeyEnvNames[]      = { "pilot", "aircraftView", "misc", "SnapView" };
    private HashMapInt          disabledHotKeys[]     = { new HashMapInt(), new HashMapInt(), new HashMapInt(), new HashMapInt() };
    private HotKeyCmdFire       hotKeyCmd[];

    static {
        GroundTargets = new Orders(new Order[] { new Order("GroundTargets"), new OrderGT("Attack_All") {

            public void run() {
                this.run(0);
            }

        }, new OrderGT("Attack_Tanks") {

            public void run() {
                this.run(1);
            }

        }, new OrderGT("Attack_Flak") {

            public void run() {
                this.run(2);
            }

        }, new OrderGT("Attack_Vehicles") {

            public void run() {
                this.run(3);
            }

        }, new OrderGT("Attack_Train") {

            public void run() {
                this.run(4);
            }

        }, new OrderGT("Attack_Bridge") {

            public void run() {
                this.run(5);
            }

        }, new OrderGT("Attack_Ships") {

            public void run() {
                this.run(6);
            }

        }, new OrderDrop_With_Me(), new OrderDrop_At_Will(), null, new OrderBack() });
        ToWingman = new Orders(new Order[] { new Order("Task"), new OrderCover_Me(), new OrderTarget_All(), new OrderAttack_Fighters(), new OrderAttack_Bombers(), new OrderAttack_My_Target(), new Order("Ground_Targets", GroundTargets),
                new OrderDrop_Tanks(), new OrderBreak(), new OrderRejoin(), null, new OrderBack() });
        ToSection = new Orders(new Order[] { new Order("Task"), new OrderCover_Me(), new OrderTarget_All(), new OrderAttack_Fighters(), new OrderAttack_Bombers(), new OrderAttack_My_Target(), new Order("Ground_Targets", GroundTargets),
                new OrderDrop_Tanks(), new OrderBreak(), new OrderRejoin(), null, new OrderBack() });
        Change_Formation = new Orders(new Order[] { new Order("C_F"), new OrderChange_Formation_To_1(), new OrderChange_Formation_To_2(), new OrderChange_Formation_To_3(), new OrderChange_Formation_To_4(), new OrderChange_Formation_To_5(),
                new OrderChange_Formation_To_6(), new OrderChange_Formation_To_7(), new OrderChange_Formation_To_8(), new OrderBack() });
        Tactical = new Orders(new Order[] { new Order("Tactical"), new OrderBreak(), new OrderRejoin(), new OrderTighten_Formation(), new OrderLoosen_Formation(), new Order("Change_Formation_Submenu", Change_Formation), new OrderKeepOrdnance(),
                new OrderOrdnanceAtWill(), null, new OrderBack() });
        Navigation = new Orders(new Order[] { new Order("Navigation"), new Order("Next_Checkpoint") {

            public void run() {
                Voice.setSyncMode(1);
                for (int i = 0; i < this.CommandSet().length; i++)
                    if (Actor.isAlive(this.CommandSet()[i]) && (this.CommandSet()[i].FM instanceof Pilot)) {
                        Pilot pilot = (Pilot) this.CommandSet()[i].FM;
                        if (!pilot.AP.way.isLanding())
                            pilot.AP.way.next();
                        if (this.isEnableVoice() && this.CommandSet()[i] != this.Player() && (this.CommandSet()[i].getWing() == this.Player().getWing() || this.CommandSet()[i].aircIndex() == 0))
                            Voice.speakNextWayPoint(this.CommandSet()[i]);
                    }

                Voice.setSyncMode(0);
            }

        }, new Order("Prev_Checkpoint") {

            public void run() {
                Voice.setSyncMode(1);
                for (int i = 0; i < this.CommandSet().length; i++)
                    if (Actor.isAlive(this.CommandSet()[i]) && (this.CommandSet()[i].FM instanceof Pilot)) {
                        Pilot pilot = (Pilot) this.CommandSet()[i].FM;
                        if (!pilot.AP.way.isLanding())
                            pilot.AP.way.prev();
                        if (this.isEnableVoice() && this.CommandSet()[i] != this.Player() && (this.CommandSet()[i].getWing() == this.Player().getWing() || this.CommandSet()[i].aircIndex() == 0))
                            Voice.speakPrevWayPoint(this.CommandSet()[i]);
                    }

                Voice.setSyncMode(0);
            }

        }, new Order("Return_To_Base") {

            public void run() {
                Voice.setSyncMode(1);
                boolean flag = false;
                Maneuver maneuver = (Maneuver) this.Player().FM;
                for (int i = 0; i < this.CommandSet().length; i++) {
                    Aircraft aircraft = this.CommandSet()[i];
                    if (Actor.isAlive(aircraft) && (aircraft.FM instanceof Pilot)) {
                        Pilot pilot = (Pilot) this.CommandSet()[i].FM;
                        if (!pilot.AP.way.isLanding()) {
                            pilot.AP.way.last();
                            pilot.AP.way.prev();
                        }
                        if (pilot.Group != null) {
                            pilot.Group.setGroupTask(1);
                            pilot.Group.timeOutForTaskSwitch = 480;
                        }
                        if (this.isEnableVoice() && aircraft != this.Player() && (aircraft.getWing() == this.Player().getWing() || aircraft.aircIndex() == 0))
                            Voice.speakReturnToBase(this.CommandSet()[i]);
                        if (aircraft != this.Player() && pilot.Group == maneuver.Group)
                            flag = true;
                    }
                }

                Voice.setSyncMode(0);
                if (flag) {
                    AirGroup airgroup = maneuver.Group;
                    AirGroup airgroup1 = new AirGroup(maneuver.Group);
                    airgroup1.rejoinGroup = null;
                    maneuver.Group.delAircraft(this.Player());
                    airgroup1.addAircraft(this.Player());
                    airgroup.setGroupTask(1);
                }
            }

        }, new Order("Hang_On_Here") {

            public void run() {
                for (int i = 0; i < this.CommandSet().length; i++) {
                    Voice.setSyncMode(1);
                    if (Actor.isAlive(this.CommandSet()[i]) && (this.CommandSet()[i].FM instanceof Pilot)) {
                        Pilot pilot = (Pilot) this.CommandSet()[i].FM;
                        if (pilot.Group != null && pilot.Group.nOfAirc > 0 && (pilot == pilot.Group.airc[0].FM || (pilot.Leader instanceof RealFlightModel)) && !pilot.isBusy() && Actor.isAlive(pilot.Group.airc[pilot.Group.nOfAirc - 1])
                                && (pilot.Group.airc[pilot.Group.nOfAirc - 1].FM instanceof Pilot)) {
                            if (pilot.Leader instanceof RealFlightModel) {
                                pilot.set_task(1);
                            } else {
                                Pilot pilot1 = (Pilot) pilot.Group.airc[pilot.Group.nOfAirc - 1].FM;
                                pilot.Group.setFormationAndScale(pilot1.formationType, pilot1.formationScale, false);
                                pilot.Group.setGroupTask(1);
                            }
                            pilot.clear_stack();
                            pilot.push(45);
                            pilot.push(45);
                            pilot.pop();
                        }
                        if (this.isEnableVoice() && this.CommandSet()[i] != this.Player() && (this.CommandSet()[i].getWing() == this.Player().getWing() || this.CommandSet()[i].aircIndex() == 0))
                            Voice.speakHangOn(this.CommandSet()[i]);
                    }
                }

                Voice.setSyncMode(0);
            }

        }, null, new OrderBack() });
        Technical = new Orders(new Order[] { new Order("Technical"), new OrderStartEngines(), new OrderAborting(), null, new OrderBack() });
        ToParaZveno = new Orders(new Order[] { new Order("Task"), new OrderCover_Me(), new OrderTarget_All(), new OrderAttack_Fighters(), new OrderAttack_Bombers(), new OrderAttack_My_Target(), new Order("Ground_Targets", GroundTargets),
                new OrderDrop_Tanks(), new Order("Tactical_Submenu", Tactical), new Order("Navigation_Submenu", Navigation), null, new OrderBack() });
        ToGroupSquad = ToParaZveno;
        ToGroundControl = new Orders(new Order[] { new Order("GroundControl"), new OrderRequest_Assistance(), new OrderVector_To_Home_Base(), new OrderVector_To_Target(), new OrderRequest_For_Landing(), new OrderRequest_For_Takeoff(),
                new OrderRequest_For_RunwayLights(), new OrderRequest_For_Weather(), null, new OrderBack() });
        Frequency = new Orders(new Order[] { new Order("Frequency"), new Order("Friendly") {

            public void preRun() {
                this.Player().FM.AS.setBeacon(0);
                OrdersTree.curOrdersTree.frequency = OrdersTree.FREQ_FRIENDLY;
            }

        }, new Order("Enemy") {

            public void preRun() {
                this.Player().FM.AS.setBeacon(0);
                OrdersTree.curOrdersTree.frequency = OrdersTree.FREQ_ENEMY;
            }

        }, null, new OrderBack() });
        SquadLeader = new Orders(new Order[] { new Order("MainMenu"), new Order("Wingman", "deWingman", ToWingman) {

            public void run() {
                this.cset(this.Wingman());
                super.run();
            }

        }, new Order("Section", "deSection", ToSection) {

            public void run() {
                this.cset(this.PlayerWingSection());
                super.run();
            }

        }, new Order("Para_1", "dePara_1", ToParaZveno) {

            public void run() {
                this.cset(this.PlayerSquad().wing[0]);
                super.run();
            }

        }, new Order("Para_2", "dePara_2", ToParaZveno) {

            public void run() {
                this.cset(this.PlayerSquad().wing[1]);
                super.run();
            }

        }, new Order("Para_3", "dePara_3", ToParaZveno) {

            public void run() {
                this.cset(this.PlayerSquad().wing[2]);
                super.run();
            }

        }, new Order("Para_4", "dePara_4", ToParaZveno) {

            public void run() {
                this.cset(this.PlayerSquad().wing[3]);
                super.run();
            }

        }, new Order("Group", "deGroup", ToGroupSquad) {

            public void run() {
                this.cset(this.PlayerSquad());
                super.run();
            }

        }, new OrderAnyone_Help_Me(), new Order("Ground_Control", ToGroundControl), new Order("Frequency", Frequency), new OrderBandits(), new Order("Technical_Submenu", Technical), null, new OrderBack() });
        GroupLeader = new Orders(new Order[] { new Order("MainMenu"), new Order("Wingman", "deWingman", ToWingman) {

            public void run() {
                this.cset(this.Wingman());
                super.run();
            }

        }, new Order("Section", "deSection", ToSection) {

            public void run() {
                this.cset(this.PlayerWingSection());
                super.run();
            }

        }, new Order("Zveno_1", "deZveno_1", ToParaZveno) {

            public void run() {
                this.cset(this.PlayerSquad().wing[0]);
                super.run();
            }

        }, new Order("Zveno_2", "deZveno_2", ToParaZveno) {

            public void run() {
                this.cset(this.PlayerSquad().wing[1]);
                super.run();
            }

        }, new Order("Zveno_3", "deZveno_3", ToParaZveno) {

            public void run() {
                this.cset(this.PlayerSquad().wing[2]);
                super.run();
            }

        }, new Order("Zveno_4", "deZveno_4", ToParaZveno) {

            public void run() {
                this.cset(this.PlayerSquad().wing[3]);
                super.run();
            }

        }, new Order("Squadron", "deSquadron", ToGroupSquad) {

            public void run() {
                this.cset(this.PlayerSquad());
                super.run();
            }

        }, new OrderAnyone_Help_Me(), new Order("Ground_Control", ToGroundControl), new Order("Frequency", Frequency), new OrderBandits(), new Order("Technical_Submenu", Technical), null, new OrderBack() });
    }
}
