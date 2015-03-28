package com.maddox.il2.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeDockable;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.HotKeyCmdEnv;
import com.maddox.rts.HotKeyCmdMouseMove;
import com.maddox.rts.HotKeyCmdMove;
import com.maddox.rts.HotKeyCmdTrackIRAngles;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;

public class DeviceLink {
    private class Listener extends Thread {

        public void run() {
            while ((RTSConf.cur != null) && (!RTSConf.isRequestExitApp())) {
                try {
                    DatagramPacket datagramPacket;
                    datagramPacket = new DatagramPacket(new byte[DeviceLink.PACKET_SIZE], DeviceLink.PACKET_SIZE);
                    DeviceLink.this.serverSocket.receive(datagramPacket);
                    if (datagramPacket.getLength() < 1)
                        continue;
                    if (enableClientAdr.size() > 0) {
                        String hostAddress = datagramPacket.getAddress().getHostAddress();
                        int size = enableClientAdr.size();
                        boolean b = false;
                        for (int i = 0; i < size; i++) {
                            if (hostAddress.equals(DeviceLink.this.enableClientAdr.get(i))) {
                                b = true;
                                break;
                            }
                        }
                        if (!b)
                            continue;

                    }
                    synchronized (DeviceLink.this.inputAction) {
                        DeviceLink.this.inputPackets.add(datagramPacket);
                        DeviceLink.this.inputAction.activate();
                    }

                } catch (Throwable t) {}

            }
        }

        private Listener() {}

    }

    private class ActionReceivedPacket extends MsgAction {

        public void doAction() {
            while (inputPackets.size() > 0) {
                DatagramPacket datagrampacket = (DatagramPacket) inputPackets.get(0);
                inputPackets.remove(0);
                try {
                    serverReceivePacket(datagrampacket);
                } catch (Throwable throwable) {}
            }
        }

        public void activate() {
            if (!busy())
                post(64, this, 0.0D);
        }

        private ActionReceivedPacket() {}

    }

    private class Parameter {

        public void set(List list) {}

        public void get(List list) {}

        public boolean get_accessible() {
            return true;
        }

        public boolean set_accessible() {
            return true;
        }

        protected boolean isMissionValid() {
            return Mission.isPlaying();
        }

        protected boolean isAircraftValid() {
            if (!isMissionValid())
                return false;
            else
                return Actor.isAlive(World.getPlayerAircraft());
        }

        protected boolean isFMValid() {
            return isAircraftValid() && !World.isPlayerGunner() && !World.isPlayerParatrooper() && !World.isPlayerDead();
        }

        protected boolean isNetAccessible() {
            // TODO: +++ Enhanced DeviceLink +++
//            return Mission.isSingle() || NetMissionTrack.isPlaying();
            return true;
            // TODO: --- Enhanced DeviceLink ---
        }

        protected int engineNum(List list) {
            if (list == null || list.size() == 0)
                return 0;
            try {
                return Integer.parseInt((String) list.get(0));
            } catch (Exception ex) {
                return -1;
            }
        }

        // TODO: +++ Enhanced DeviceLink +++
//        protected Actor actorForName(List list) {
//            if (list == null || list.size() == 0) {
//                return null;
//            } else {
//                String s = (String) list.get(0);
//                return Actor.getByName(s);
//            }
//        }
        // TODO: --- Enhanced DeviceLink ---

        protected String fmt(float f) {
            boolean flag = f < 0.0F;
            if (flag)
                f = -f;
            float f1 = (f + 0.005F) - (float) (int) f;
            if (f1 >= 0.1F)
                return (flag ? "-" : "") + (int) f + "." + (int) (f1 * 100F);
            else
                return (flag ? "-" : "") + (int) f + ".0" + (int) (f1 * 100F);
        }

        public void answer(String s) {
            do_answer(id, s);
        }

        public void answer(String as[]) {
            do_answer(id, as);
        }

        // TODO: +++ Enhanced DeviceLink +++
//        public void answer(List list) {
//            do_answer(id, list);
//        }
        // TODO: --- Enhanced DeviceLink ---

        public int id;

        public Parameter(int i) {
            i *= 2;
            paramMap.put(i, this);
            id = i;
        }
    }

    private class ParameterMisc extends Parameter {

        public boolean get_accessible() {
            return false;
        }

        protected boolean isFMValid() {
            return isAircraftValid() && !World.isPlayerParatrooper() && !World.isPlayerDead();
        }

        public boolean set_accessible() {
            if (!cmd.isEnabled())
                return false;
            HotKeyCmdEnv hotkeycmdenv = cmd.hotKeyCmdEnv();
            if (!hotkeycmdenv.isEnabled())
                return false;
            return HotKeyEnv.isEnabled(hotkeycmdenv.name());
        }

        public void set(List list) {
            cmd._exec(true);
            cmd._exec(false);
        }

        HotKeyCmd cmd;

        public ParameterMisc(int i, String s, String s1) {
            super(i);
            cmd = HotKeyCmdEnv.env(s).get(s1);
        }
    }

    private class ParameterTrackIR extends Parameter {

        public boolean get_accessible() {
            return false;
        }

        public boolean set_accessible() {
            if (!cmd.isEnabled())
                return false;
            HotKeyCmdEnv hotkeycmdenv = cmd.hotKeyCmdEnv();
            if (!hotkeycmdenv.isEnabled())
                return false;
            return HotKeyEnv.isEnabled(hotkeycmdenv.name());
        }

        public void set(List list) {
            float f;
            float f1;
            float f2;
            try {
                f = Float.parseFloat((String) list.get(0));
                f1 = Float.parseFloat((String) list.get(1));
                f2 = Float.parseFloat((String) list.get(2));
            } catch (Exception exception) {
                return;
            }
            cmd._exec(f, f1, f2);
        }

        HotKeyCmdTrackIRAngles cmd;

        public ParameterTrackIR(int i, String s, String s1) {
            super(i);
            cmd = (HotKeyCmdTrackIRAngles) HotKeyCmdEnv.env(s).get(s1);
        }
    }

    private class ParameterView extends Parameter {

        public boolean get_accessible() {
            return false;
        }

        public boolean set_accessible() {
            if (!cmd.isEnabled())
                return false;
            HotKeyCmdEnv hotkeycmdenv = cmd.hotKeyCmdEnv();
            if (!hotkeycmdenv.isEnabled())
                return false;
            return HotKeyEnv.isEnabled(hotkeycmdenv.name());
        }

        public void set(List list) {
            cmd._exec(true);
            cmd._exec(false);
        }

        HotKeyCmd cmd;

        public ParameterView(int i, String s, String s1) {
            super(i);
            cmd = HotKeyCmdEnv.env(s).get(s1);
        }
    }

    private class ParameterPilot extends Parameter {

        public boolean get_accessible() {
            return isFMValid();
        }

        public boolean set_accessible() {
            if (!cmd.isEnabled())
                return false;
            HotKeyCmdEnv hotkeycmdenv = cmd.hotKeyCmdEnv();
            if (!hotkeycmdenv.isEnabled())
                return false;
            if (!HotKeyEnv.isEnabled(hotkeycmdenv.name()))
                return false;
            else
                return isFMValid() && ((RealFlightModel) World.getPlayerFM()).isRealMode() && !cmd.isActive();
        }

        protected boolean set_accessible0() {
            if (!cmd.isEnabled())
                return false;
            HotKeyCmdEnv hotkeycmdenv = cmd.hotKeyCmdEnv();
            if (!hotkeycmdenv.isEnabled())
                return false;
            if (!HotKeyEnv.isEnabled(hotkeycmdenv.name()))
                return false;
            else
                return isFMValid() && ((RealFlightModel) World.getPlayerFM()).isRealMode();
        }

        public void set(List list) {
            cmd._exec(true);
            cmd._exec(false);
        }

        HotKeyCmd cmd;

        public ParameterPilot(int i, String s, String s1) {
            super(i);
            cmd = HotKeyCmdEnv.env(s).get(s1);
        }
    }

    private class ParameterGunner extends ParameterPilot {

        protected boolean isFMValid() {
            return isAircraftValid() && !World.isPlayerParatrooper() && !World.isPlayerDead();
        }

        public boolean set_accessible() {
            if (!cmd.isEnabled())
                return false;
            HotKeyCmdEnv hotkeycmdenv = cmd.hotKeyCmdEnv();
            if (!hotkeycmdenv.isEnabled())
                return false;
            if (!HotKeyEnv.isEnabled(hotkeycmdenv.name()))
                return false;
            else
                return isFMValid() && !cmd.isActive();
        }

        public ParameterGunner(int i, String s, String s1) {
            super(i, s, s1);
        }
    }

    private class ParameterMove extends Parameter {

        public boolean get_accessible() {
            return isFMValid();
        }

        public boolean set_accessible() {
            if (!cmd.isEnabled())
                return false;
            HotKeyCmdEnv hotkeycmdenv = cmd.hotKeyCmdEnv();
            if (!hotkeycmdenv.isEnabled())
                return false;
            if (!HotKeyEnv.isEnabled(hotkeycmdenv.name()))
                return false;
            else
                return isFMValid() && ((RealFlightModel) World.getPlayerFM()).isRealMode() && !cmd.isActive();
        }

        public void set(List list) {
            float f = 0.0F;
            try {
                f = Float.parseFloat((String) list.get(0));
            } catch (Exception exception) {
                return;
            }
            if (f < -1F)
                f = -1F;
            if (f > 1.0F)
                f = 1.0F;
            cmd._exec(Math.round(f * 125F));
        }

        HotKeyCmdMove cmd;

        public ParameterMove(int i, String s, String s1) {
            super(i);
            cmd = (HotKeyCmdMove) HotKeyCmdEnv.env(s).get(s1);
        }
    }

    private class ParameterMouseMove extends Parameter {

        public boolean get_accessible() {
            return false;
        }

        public boolean set_accessible() {
            if (!cmd.isEnabled())
                return false;
            HotKeyCmdEnv hotkeycmdenv = cmd.hotKeyCmdEnv();
            if (!hotkeycmdenv.isEnabled())
                return false;
            return HotKeyEnv.isEnabled(hotkeycmdenv.name());
        }

        public void set(List list) {
            int i;
            int j;
            int k;
            try {
                i = Integer.parseInt((String) list.get(0));
                j = Integer.parseInt((String) list.get(1));
                k = Integer.parseInt((String) list.get(2));
            } catch (Exception exception) {
                return;
            }
            cmd._exec(i, j, k);
        }

        HotKeyCmdMouseMove cmd;

        public ParameterMouseMove(int i, String s, String s1) {
            super(i);
            cmd = (HotKeyCmdMouseMove) HotKeyCmdEnv.env(s).get(s1);
        }
    }

    private void registerParams() {
        new Parameter(VERSION) {

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                answer("1.00");
            }

        };
        new Parameter(GET_ACCESSIBLE) {

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                int i = 0;
                Parameter parameter = null;
                try {
                    i = Integer.parseInt((String) list.get(0)) & -2;
                    parameter = (Parameter) paramMap.get(i);
                } catch (Exception exception) {
                    return;
                }
                if (parameter != null)
                    answer(new String[] { "" + i, parameter.get_accessible() ? "1" : "0" });
            }

        };
        new Parameter(SET_ACCESSIBLE) {

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                int i = 0;
                Parameter parameter = null;
                try {
                    i = Integer.parseInt((String) list.get(0)) & -2;
                    parameter = (Parameter) paramMap.get(i);
                } catch (Exception exception) {
                    return;
                }
                if (parameter != null)
                    answer(new String[] { "" + (i + 1), parameter.set_accessible() ? "1" : "0" });
            }

        };
        new Parameter(TIME_OF_DAY) {

            public boolean get_accessible() {
                return isMissionValid();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                answer("" + World.getTimeofDay());
            }

        };
        new Parameter(PLANE) {

            public boolean get_accessible() {
                return isAircraftValid();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                answer(Property.stringValue(World.getPlayerAircraft().getClass(), "keyName", ""));
            }

        };
        new Parameter(COCKPITS) {

            public boolean get_accessible() {
                return isAircraftValid();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                answer("" + Main3D.cur3D().cockpits.length);
            }

        };
        new Parameter(COCKPIT_CUR) {

            public boolean get_accessible() {
                return isAircraftValid();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                int i = Main3D.cur3D().cockpitCurIndx();
                if (Main3D.cur3D().isViewOutside())
                    i = -1;
                answer("" + i);
            }

        };
        new Parameter(ENGINES) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                answer("" + World.getPlayerFM().EI.getNum());
            }

        };
        new Parameter(SPEEDOMETER) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                answer("" + fmt(Pitot.Indicator((float) World.getPlayerFM().Loc.z, World.getPlayerFM().getSpeedKMH())));
            }

        };
        new Parameter(VARIOMETER) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                answer("" + fmt((float) World.getPlayerFM().Vwld.z));
            }

        };
        new Parameter(SLIP) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                float f = World.getPlayerFM().getSpeedKMH() <= 10F ? 0.0F : World.getPlayerFM().getAOS();
                if (f < -45F)
                    f = -45F;
                if (f > 45F)
                    f = 45F;
                answer("" + fmt(f));
            }

        };
        new Parameter(TURN) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                Vector3f vector3f = new Vector3f();
                vector3f.set(World.getPlayerFM().getW());
                World.getPlayerFM().Or.transform(vector3f);
                float f = vector3f.z / 6F;
                if (f < -1F)
                    f = -1F;
                if (f > 1.0F)
                    f = 1.0F;
                answer("" + fmt(f));
            }

        };
        new Parameter(ANGULAR_SPEED) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                Vector3f vector3f = new Vector3f();
                vector3f.set(World.getPlayerFM().getW());
                World.getPlayerFM().Or.transform(vector3f);
                float f = vector3f.z;
                answer("" + fmt(f));
            }

        };
        new Parameter(ALTIMETER) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                answer("" + fmt((float) World.getPlayerFM().Loc.z));
            }

        };
        new Parameter(AZIMUT) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                Orientation orientation = World.getPlayerFM().Or;
                o.set(orientation.azimut(), orientation.tangage(), orientation.kren());
                o.wrap();
                float f;
                for (f = 90F - o.getYaw(); f < 0.0F; f += 360F)
                    ;
                for (; f > 360F; f -= 360F)
                    ;
                answer("" + fmt(f));
            }

            Orient o;

            {
                o = new Orient();
            }
        };
        new Parameter(BEACON_AZIMUT) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                Point3d point3d = new Point3d();
                Vector3d vector3d = new Vector3d();
                WayPoint waypoint = World.getPlayerFM().AP.way.curr();
                float f = 0.0F;
                if (waypoint != null) {
                    waypoint.getP(point3d);
                    vector3d.sub(point3d, World.getPlayerFM().Loc);
                    f = (float) (57.295779513082323D * Math.atan2(vector3d.y, vector3d.x));
                }
                answer("" + fmt(f));
            }

        };
        new Parameter(ROLL) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                Orientation orientation = World.getPlayerFM().Or;
                o.set(orientation.azimut(), orientation.tangage(), orientation.kren());
                o.wrap();
                answer("" + fmt(-o.getKren()));
            }

            Orient o;

            {
                o = new Orient();
            }
        };
        new Parameter(PITCH) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                Orientation orientation = World.getPlayerFM().Or;
                o.set(orientation.azimut(), orientation.tangage(), orientation.kren());
                o.wrap();
                answer("" + fmt(o.getTangage()));
            }

            Orient o;

            {
                o = new Orient();
            }
        };
        new Parameter(FUEL) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().M.fuel));
            }

        };
        new Parameter(OVERLOAD) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().getOverload()));
            }

        };
        new Parameter(SHAKE_LEVEL) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                answer("" + fmt(((RealFlightModel) World.getPlayerFM()).shakeLevel));
            }

        };
        new Parameter(GEAR_POS_L) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                float f = 0.0F;
                if (World.getPlayerFM().Gears.lgear)
                    f = World.getPlayerFM().CT.getGear();
                answer("" + fmt(f));
            }

        };
        new Parameter(GEAR_POS_R) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                float f = 0.0F;
                if (World.getPlayerFM().Gears.rgear)
                    f = World.getPlayerFM().CT.getGear();
                answer("" + fmt(f));
            }

        };
        new Parameter(GEAR_POS_C) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                float f = World.getPlayerFM().CT.getGear();
                answer("" + fmt(f));
            }

        };
        new Parameter(MAGNETO) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                int i = engineNum(list);
                if (i < 0) {
                    return;
                } else {
                    answer(new String[] { "" + i, "" + World.getPlayerFM().EI.engines[i].getControlMagnetos() });
                    return;
                }
            }

        };
        new Parameter(RPM) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                int i = engineNum(list);
                if (i < 0) {
                    return;
                } else {
                    answer(new String[] { "" + i, "" + fmt(World.getPlayerFM().EI.engines[i].getRPM()) });
                    return;
                }
            }

        };
        new Parameter(MANIFOLD) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                int i = engineNum(list);
                if (i < 0) {
                    return;
                } else {
                    answer(new String[] { "" + i, "" + fmt(World.getPlayerFM().EI.engines[i].getManifoldPressure()) });
                    return;
                }
            }

        };
        new Parameter(TEMP_OILIN) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                int i = engineNum(list);
                if (i < 0) {
                    return;
                } else {
                    answer(new String[] { "" + i, "" + fmt(World.getPlayerFM().EI.engines[i].tOilIn) });
                    return;
                }
            }

        };
        new Parameter(TEMP_OILOUT) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                int i = engineNum(list);
                if (i < 0) {
                    return;
                } else {
                    answer(new String[] { "" + i, "" + fmt(World.getPlayerFM().EI.engines[i].tOilOut) });
                    return;
                }
            }

        };
        new Parameter(TEMP_WATER) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                int i = engineNum(list);
                if (i < 0) {
                    return;
                } else {
                    answer(new String[] { "" + i, "" + fmt(World.getPlayerFM().EI.engines[i].tWaterOut) });
                    return;
                }
            }

        };
        new Parameter(TEMP_CYLINDERS) {

            public boolean get_accessible() {
                return isFMValid() && isNetAccessible();
            }

            public boolean set_accessible() {
                return false;
            }

            public void get(List list) {
                int i = engineNum(list);
                if (i < 0) {
                    return;
                } else {
                    answer(new String[] { "" + i, "" + fmt(World.getPlayerFM().EI.engines[i].tWaterOut) });
                    return;
                }
            }

        };
        new ParameterPilot(STABILIZER, "pilot", "Stabilizer") {

            public boolean set_accessible() {
                return super.set_accessible() && (World.getPlayerAircraft() instanceof TypeBomber);
            }

            public void get(List list) {
                answer(World.getPlayerFM().CT.StabilizerControl ? "1" : "0");
            }

        };
        new ParameterPilot(TOGGLE_ENGINE, "pilot", "AIRCRAFT_TOGGLE_ENGINE") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(BOOST, "pilot", "Boost") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().EI.isSelectionHasControlAfterburner();
            }

            public void get(List list) {
                answer(Main3D.cur3D().aircraftHotKeys.isAfterburner() ? "1" : "0");
            }

        };
        new ParameterPilot(MAGNETO_PLUS, "pilot", "MagnetoPlus") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().EI.isSelectionHasControlMagnetos() && World.getPlayerFM().EI.getFirstSelected() != null && World.getPlayerFM().EI.getFirstSelected().getControlMagnetos() < 3;
            }

            public boolean get_accessible() {
                return super.get_accessible() && World.getPlayerFM().EI.getFirstSelected() != null;
            }

            public void get(List list) {
                answer("" + World.getPlayerFM().EI.getFirstSelected().getControlMagnetos());
            }

        };
        new ParameterPilot(MAGNETO_MINUS, "pilot", "MagnetoMinus") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().EI.isSelectionHasControlMagnetos() && World.getPlayerFM().EI.getFirstSelected() != null && World.getPlayerFM().EI.getFirstSelected().getControlMagnetos() > 0;
            }

            public boolean get_accessible() {
                return super.get_accessible() && World.getPlayerFM().EI.getFirstSelected() != null;
            }

            public void get(List list) {
                answer("" + World.getPlayerFM().EI.getFirstSelected().getControlMagnetos());
            }

        };
        new ParameterPilot(COMPRESSOR_PLUS, "pilot", "CompressorPlus") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().EI.isSelectionHasControlCompressor() && World.getPlayerFM().EI.getFirstSelected() != null && World.cur().diffCur.ComplexEManagement;
            }

            public void get(List list) {
                answer("" + World.getPlayerFM().CT.getCompressorControl());
            }

        };
        new ParameterPilot(COMPRESSOR_MINUS, "pilot", "CompressorMinus") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().EI.isSelectionHasControlCompressor() && World.getPlayerFM().EI.getFirstSelected() != null && World.cur().diffCur.ComplexEManagement;
            }

            public void get(List list) {
                answer("" + World.getPlayerFM().CT.getCompressorControl());
            }

        };
        new ParameterPilot(ENGINE_SELECT_ALL, "pilot", "EngineSelectAll") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_SELECT_NONE, "pilot", "EngineSelectNone") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_SELECT_LEFT, "pilot", "EngineSelectLeft") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_SELECT_RIGHT, "pilot", "EngineSelectRight") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_SELECT_1, "pilot", "EngineSelect1") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_SELECT_2, "pilot", "EngineSelect2") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_SELECT_3, "pilot", "EngineSelect3") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_SELECT_4, "pilot", "EngineSelect4") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_SELECT_5, "pilot", "EngineSelect5") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_SELECT_6, "pilot", "EngineSelect6") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_SELECT_7, "pilot", "EngineSelect7") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_SELECT_8, "pilot", "EngineSelect8") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_TOGGLE_ALL, "pilot", "EngineToggleAll") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_TOGGLE_LEFT, "pilot", "EngineToggleLeft") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_TOGGLE_RIGHT, "pilot", "EngineToggleRight") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_TOGGLE_1, "pilot", "EngineToggle1") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_TOGGLE_2, "pilot", "EngineToggle2") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_TOGGLE_3, "pilot", "EngineToggle3") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_TOGGLE_4, "pilot", "EngineToggle4") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_TOGGLE_5, "pilot", "EngineToggle5") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_TOGGLE_6, "pilot", "EngineToggle6") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_TOGGLE_7, "pilot", "EngineToggle7") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_TOGGLE_8, "pilot", "EngineToggle8") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(ENGINE_EXTINGUISHER, "pilot", "EngineExtinguisher") {

            public boolean get_accessible() {
                return false;
            }

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().EI.isSelectionHasControlExtinguisher();
            }

        };
        new ParameterPilot(ENGINE_FEATHER, "pilot", "EngineFeather") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().EI.isSelectionHasControlFeather() && World.getPlayerFM().EI.getFirstSelected() != null && World.cur().diffCur.ComplexEManagement;
            }

            public boolean get_accessible() {
                return super.get_accessible() && World.getPlayerFM().EI.isSelectionHasControlFeather() && World.getPlayerFM().EI.getFirstSelected() != null;
            }

            public void get(List list) {
                answer("" + World.getPlayerFM().EI.getFirstSelected().getControlFeather());
            }

        };
        new ParameterPilot(GEAR, "pilot", "Gear") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().CT.bHasGearControl && !World.getPlayerFM().Gears.onGround() && World.getPlayerFM().Gears.isHydroOperable();
            }

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().CT.GearControl));
            }

        };
        new ParameterPilot(GEAR_UP_MANUAL, "pilot", "AIRCRAFT_GEAR_UP_MANUAL") {

            public boolean get_accessible() {
                return false;
            }

            public boolean set_accessible() {
                return super.set_accessible() && !World.getPlayerFM().Gears.onGround() && World.getPlayerFM().CT.GearControl > 0.0F && World.getPlayerFM().Gears.isOperable() && !World.getPlayerFM().Gears.isHydroOperable();
            }

        };
        new ParameterPilot(GEAR_DOWN_MANUAL, "pilot", "AIRCRAFT_GEAR_DOWN_MANUAL") {

            public boolean get_accessible() {
                return false;
            }

            public boolean set_accessible() {
                return super.set_accessible() && !World.getPlayerFM().Gears.onGround() && World.getPlayerFM().CT.GearControl < 1.0F && World.getPlayerFM().Gears.isOperable() && !World.getPlayerFM().Gears.isHydroOperable();
            }

        };
        new ParameterPilot(RADIATOR, "pilot", "Radiator") {

            public boolean get_accessible() {
                return false;
            }

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().EI.isSelectionHasControlRadiator();
            }

        };
        new ParameterPilot(TOGGLE_AIRBRAKE, "pilot", "AIRCRAFT_TOGGLE_AIRBRAKE") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().CT.bHasAirBrakeControl;
            }

            public void get(List list) {
                answer(World.getPlayerFM().CT.AirBrakeControl != 0.0F ? "1" : "0");
            }

        };
        new ParameterPilot(TAILWHEELLOCK, "pilot", "AIRCRAFT_TAILWHEELLOCK") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().CT.bHasLockGearControl;
            }

            public void get(List list) {
                answer(World.getPlayerFM().Gears.bTailwheelLocked ? "1" : "0");
            }

        };
        new ParameterPilot(DROP_TANKS, "pilot", "AIRCRAFT_DROP_TANKS") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(DOCK_UNDOCK, "pilot", "AIRCRAFT_DOCK_UNDOCK") {

            public boolean get_accessible() {
                return false;
            }

            public boolean set_accessible() {
                return super.set_accessible() && (World.getPlayerFM().actor instanceof TypeDockable);
            }

        };
        new ParameterPilot(WEAPON0, "pilot", "Weapon0") {

            public boolean set_accessible() {
                return set_accessible0();
            }

            public void set(List list) {
                if (list == null || list.size() != 1) {
                    return;
                } else {
                    boolean flag = "1".equals(list.get(0));
                    HotKeyCmd.exec(flag, cmd.hotKeyCmdEnv().name(), cmd.name());
                    return;
                }
            }

            public void get(List list) {
                answer(World.getPlayerFM().CT.WeaponControl[0] ? "1" : "0");
            }

        };
        new ParameterPilot(WEAPON1, "pilot", "Weapon1") {

            public boolean set_accessible() {
                return set_accessible0();
            }

            public void set(List list) {
                if (list == null || list.size() != 1) {
                    return;
                } else {
                    boolean flag = "1".equals(list.get(0));
                    HotKeyCmd.exec(flag, cmd.hotKeyCmdEnv().name(), cmd.name());
                    return;
                }
            }

            public void get(List list) {
                answer(World.getPlayerFM().CT.WeaponControl[1] ? "1" : "0");
            }

        };
        new ParameterPilot(WEAPON2, "pilot", "Weapon2") {

            public boolean set_accessible() {
                return set_accessible0();
            }

            public void set(List list) {
                if (list == null || list.size() != 1) {
                    return;
                } else {
                    boolean flag = "1".equals(list.get(0));
                    HotKeyCmd.exec(flag, cmd.hotKeyCmdEnv().name(), cmd.name());
                    return;
                }
            }

            public void get(List list) {
                answer(World.getPlayerFM().CT.WeaponControl[2] ? "1" : "0");
            }

        };
        new ParameterPilot(WEAPON3, "pilot", "Weapon3") {

            public boolean set_accessible() {
                return set_accessible0();
            }

            public void set(List list) {
                if (list == null || list.size() != 1) {
                    return;
                } else {
                    boolean flag = "1".equals(list.get(0));
                    HotKeyCmd.exec(flag, cmd.hotKeyCmdEnv().name(), cmd.name());
                    return;
                }
            }

            public void get(List list) {
                answer(World.getPlayerFM().CT.WeaponControl[3] ? "1" : "0");
            }

        };
        new ParameterPilot(WEAPON01, "pilot", "Weapon01") {

            public boolean set_accessible() {
                return set_accessible0();
            }

            public void set(List list) {
                if (list == null || list.size() != 1) {
                    return;
                } else {
                    boolean flag = "1".equals(list.get(0));
                    HotKeyCmd.exec(flag, cmd.hotKeyCmdEnv().name(), cmd.name());
                    return;
                }
            }

            public void get(List list) {
                answer(!World.getPlayerFM().CT.WeaponControl[0] || !World.getPlayerFM().CT.WeaponControl[1] ? "0" : "1");
            }

        };
        new ParameterPilot(GUNPODS, "pilot", "GunPods") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerAircraft().isGunPodsExist();
            }

            public void get(List list) {
                answer(World.getPlayerAircraft().isGunPodsOn() ? "1" : "0");
            }

        };
        new ParameterPilot(SIGHT_AUTO_ONOFF, "pilot", "SIGHT_AUTO_ONOFF") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(SIGHT_DIST_PLUS, "pilot", "SIGHT_DIST_PLUS") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(SIGHT_DIST_MINUS, "pilot", "SIGHT_DIST_MINUS") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(SIGHT_SIDE_RIGHT, "pilot", "SIGHT_SIDE_RIGHT") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(SIGHT_SIDE_LEFT, "pilot", "SIGHT_SIDE_LEFT") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(SIGHT_ALT_PLUS, "pilot", "SIGHT_ALT_PLUS") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(SIGHT_ALT_MINUS, "pilot", "SIGHT_ALT_MINUS") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(SIGHT_SPD_PLUS, "pilot", "SIGHT_SPD_PLUS") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(SIGHT_SPD_MINUS, "pilot", "SIGHT_SPD_MINUS") {

            public boolean get_accessible() {
                return false;
            }

        };
        new ParameterPilot(WINGFOLD, "pilot", "WINGFOLD") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().CT.bHasWingControl;
            }

            public void get(List list) {
                answer(World.getPlayerFM().CT.getWing() <= 0.99F ? "0" : "1");
            }

        };
        new ParameterPilot(COCKPITDOOR, "pilot", "COCKPITDOOR") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().CT.bHasCockpitDoorControl;
            }

            public void get(List list) {
                answer(World.getPlayerFM().CT.getCockpitDoor() <= 0.99F ? "0" : "1");
            }

        };
        new ParameterPilot(CARRIERHOOK, "pilot", "AIRCRAFT_CARRIERHOOK") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().CT.bHasArrestorControl;
            }

            public void get(List list) {
                answer(World.getPlayerFM().CT.arrestorControl <= 0.5F ? "0" : "1");
            }

        };
        new ParameterPilot(BRAKESHOE, "pilot", "AIRCRAFT_BRAKESHOE") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().canChangeBrakeShoe;
            }

            public void get(List list) {
                answer(World.getPlayerFM().brakeShoe ? "1" : "0");
            }

        };
        // TODO: +++ Enhanced DeviceLink +++
        new ParameterPilot(GEAR_ENH, "pilot", "Gear") {

            public boolean set_accessible() {
                if (super.set_accessible() && World.getPlayerFM().CT.bHasGearControl && !World.getPlayerFM().Gears.onGround() && World.getPlayerFM().Gears.isHydroOperable())
                    return true;
                if (super.set_accessible() && !World.getPlayerFM().Gears.onGround() && World.getPlayerFM().CT.GearControl > 0.0F && World.getPlayerFM().Gears.isOperable() && !World.getPlayerFM().Gears.isHydroOperable())
                    return true;
                if (super.set_accessible() && !World.getPlayerFM().Gears.onGround() && World.getPlayerFM().CT.GearControl < 1.0F && World.getPlayerFM().Gears.isOperable() && !World.getPlayerFM().Gears.isHydroOperable())
                    return true;
                return false;
            }

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().CT.GearControl));
            }

            public void set(List list) {
                if (list == null || list.size() != 1) {
                    return;
                } else {
                    if ("1".equals(list.get(0))) {
                        if (super.set_accessible() && World.getPlayerFM().CT.bHasGearControl && !World.getPlayerFM().Gears.onGround() && World.getPlayerFM().Gears.isHydroOperable() && World.getPlayerFM().CT.GearControl < 1.0F) {
                            cmd = HotKeyCmdEnv.env("pilot").get("Gear");
                            cmd._exec(true);
                            cmd._exec(false);
                        } else if (super.set_accessible() && !World.getPlayerFM().Gears.onGround() && World.getPlayerFM().CT.GearControl < 1.0F && World.getPlayerFM().Gears.isOperable() && !World.getPlayerFM().Gears.isHydroOperable()) {
                            cmd = HotKeyCmdEnv.env("pilot").get("AIRCRAFT_GEAR_DOWN_MANUAL");
                            cmd._exec(true);
                            cmd._exec(false);
                        }
                    } else if ("0".equals(list.get(0))) {
                        if (super.set_accessible() && World.getPlayerFM().CT.bHasGearControl && !World.getPlayerFM().Gears.onGround() && World.getPlayerFM().Gears.isHydroOperable() && World.getPlayerFM().CT.GearControl > 0.0F) {
                            cmd = HotKeyCmdEnv.env("pilot").get("Gear");
                            cmd._exec(true);
                            cmd._exec(false);
                        } else if (super.set_accessible() && !World.getPlayerFM().Gears.onGround() && World.getPlayerFM().CT.GearControl > 0.0F && World.getPlayerFM().Gears.isOperable() && !World.getPlayerFM().Gears.isHydroOperable()) {
                            cmd = HotKeyCmdEnv.env("pilot").get("AIRCRAFT_GEAR_UP_MANUAL");
                            cmd._exec(true);
                            cmd._exec(false);
                        }
                    }
                    return;
                }
            }

        };

        new ParameterPilot(BOMB_BAY_DOOR, "pilot", "BombBayDoor") {
            public boolean set_accessible() {
                return super.set_accessible();
            }

            public boolean get_accessible() {
                return super.get_accessible();
            }

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().CT.BayDoorControl));
            }

            public void set(List list) {
                boolean openBayDoors = false;
                if (list == null || list.size() != 1) {
                    if (World.getPlayerFM().CT.BayDoorControl < 1.0F)
                        openBayDoors = true;
                } else {
                    openBayDoors= (list.get(0).toString().equalsIgnoreCase("1"));
                }
                if (openBayDoors && World.getPlayerFM().CT.BayDoorControl > 0.0F)
                    return;
                if (!openBayDoors && World.getPlayerFM().CT.BayDoorControl < 1.0F)
                    return;
                cmd._exec(true);
                cmd._exec(false);
            }
        };

        // TODO: --- Enhanced DeviceLink ---

        new ParameterGunner(GUNNER_FIRE, "gunner", "Fire") {

            public boolean set_accessible() {
                return set_accessible0();
            }

            public void set(List list) {
                if (list == null || list.size() != 1) {
                    return;
                } else {
                    boolean flag = "1".equals(list.get(0));
                    HotKeyCmd.exec(flag, cmd.hotKeyCmdEnv().name(), cmd.name());
                    return;
                }
            }

            public void get(List list) {
                answer(cmd.isActive() ? "1" : "0");
            }

        };

        new ParameterMouseMove(GUNNER_MOVE, "gunner", "Mouse") {

            public boolean set_accessible() {
                return super.set_accessible() && isAircraftValid() && !World.isPlayerParatrooper() && !World.isPlayerDead();
            }

        };
        new ParameterMove(M_POWER, "move", "power") {

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().CT.PowerControl / 0.55F - 1.0F));
            }

        };
        new ParameterMove(M_FLAPS, "move", "flaps") {

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().CT.FlapsControl / 0.5F - 1.0F));
            }

        };
        new ParameterMove(M_AILERON, "move", "aileron") {

            public boolean set_accessible() {
                return super.set_accessible() && !World.getPlayerFM().CT.StabilizerControl;
            }

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().CT.AileronControl));
            }

        };
        new ParameterMove(M_ELEVATOR, "move", "elevator") {

            public boolean set_accessible() {
                return super.set_accessible() && !World.getPlayerFM().CT.StabilizerControl;
            }

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().CT.ElevatorControl));
            }

        };
        new ParameterMove(M_RUDDER, "move", "rudder") {

            public boolean set_accessible() {
                return super.set_accessible() && !World.getPlayerFM().CT.StabilizerControl;
            }

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().CT.RudderControl));
            }

        };
        new ParameterMove(M_BRAKES, "move", "brakes") {

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().CT.BrakeControl / 0.5F - 1.0F));
            }

        };
        new ParameterMove(M_PITCH, "move", "pitch") {

            public boolean set_accessible() {
                return super.set_accessible() && World.cur().diffCur.ComplexEManagement && World.getPlayerFM().EI.isSelectionHasControlProp();
            }

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().CT.getStepControl() / 0.5F - 1.0F));
            }

        };
        new ParameterMove(M_TRIMAILERON, "move", "trimaileron") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().CT.bHasAileronTrim;
            }

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().CT.getTrimAileronControl() * 2.0F));
            }

        };
        new ParameterMove(M_TRIMELEVATOR, "move", "trimelevator") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().CT.bHasElevatorTrim;
            }

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().CT.getTrimElevatorControl() * 2.0F));
            }

        };
        new ParameterMove(M_TRIMRUDDER, "move", "trimrudder") {

            public boolean set_accessible() {
                return super.set_accessible() && World.getPlayerFM().CT.bHasRudderTrim;
            }

            public void get(List list) {
                answer("" + fmt(World.getPlayerFM().CT.getTrimRudderControl() * 2.0F));
            }

        };
        new ParameterView(VchangeCockpit, "aircraftView", "changeCockpit") {

            public boolean get_accessible() {
                return Main3D.cur3D().cockpitCurIndx() >= 0;
            }

            public void get(List list) {
                answer("" + Main3D.cur3D().cockpitCurIndx());
            }

        };
        new ParameterView(VcockpitView0, "aircraftView", "cockpitView0");
        new ParameterView(VcockpitView1, "aircraftView", "cockpitView1");
        new ParameterView(VcockpitView2, "aircraftView", "cockpitView2");
        new ParameterView(VcockpitView3, "aircraftView", "cockpitView3");
        new ParameterView(VcockpitView4, "aircraftView", "cockpitView4");
        new ParameterView(VcockpitView5, "aircraftView", "cockpitView5");
        new ParameterView(VcockpitView6, "aircraftView", "cockpitView6");
        new ParameterView(VcockpitView7, "aircraftView", "cockpitView7");
        new ParameterView(VcockpitView8, "aircraftView", "cockpitView8");
        new ParameterView(VcockpitView9, "aircraftView", "cockpitView9");
        new ParameterView(Vfov90, "aircraftView", "fov90");
        new ParameterView(Vfov85, "aircraftView", "fov85");
        new ParameterView(Vfov80, "aircraftView", "fov80");
        new ParameterView(Vfov75, "aircraftView", "fov75");
        new ParameterView(Vfov70, "aircraftView", "fov70");
        new ParameterView(Vfov65, "aircraftView", "fov65");
        new ParameterView(Vfov60, "aircraftView", "fov60");
        new ParameterView(Vfov55, "aircraftView", "fov55");
        new ParameterView(Vfov50, "aircraftView", "fov50");
        new ParameterView(Vfov45, "aircraftView", "fov45");
        new ParameterView(Vfov40, "aircraftView", "fov40");
        new ParameterView(Vfov35, "aircraftView", "fov35");
        new ParameterView(Vfov30, "aircraftView", "fov30");
        new ParameterView(VfovSwitch, "aircraftView", "fovSwitch") {

            public boolean get_accessible() {
                return true;
            }

            public void get(List list) {
                answer("" + fmt(Main3D.FOVX));
            }

        };
        new ParameterView(VfovInc, "aircraftView", "fovInc");
        new ParameterView(VfovDec, "aircraftView", "fovDec");
        new ParameterView(VCockpitView, "aircraftView", "CockpitView");
        new ParameterView(VCockpitShow, "aircraftView", "CockpitShow");
        new ParameterView(VOutsideView, "aircraftView", "OutsideView");
        new ParameterView(VNextView, "aircraftView", "NextView");
        new ParameterView(VNextViewEnemy, "aircraftView", "NextViewEnemy");
        new ParameterView(VOutsideViewFly, "aircraftView", "OutsideViewFly");
        new ParameterView(VPadlockView, "aircraftView", "PadlockView");
        new ParameterView(VPadlockViewFriend, "aircraftView", "PadlockViewFriend");
        new ParameterView(VPadlockViewGround, "aircraftView", "PadlockViewGround");
        new ParameterView(VPadlockViewFriendGround, "aircraftView", "PadlockViewFriendGround");
        new ParameterView(VPadlockViewNext, "aircraftView", "PadlockViewNext");
        new ParameterView(VPadlockViewPrev, "aircraftView", "PadlockViewPrev");
        new ParameterView(VPadlockViewForward, "aircraftView", "PadlockViewForward") {

            public void set(List list) {
                if (list == null || list.size() != 1) {
                    return;
                } else {
                    boolean flag = "1".equals(list.get(0));
                    HotKeyCmd.exec(flag, cmd.hotKeyCmdEnv().name(), cmd.name());
                    return;
                }
            }

        };
        new ParameterView(VViewEnemyAir, "aircraftView", "ViewEnemyAir");
        new ParameterView(VViewFriendAir, "aircraftView", "ViewFriendAir");
        new ParameterView(VViewEnemyDirectAir, "aircraftView", "ViewEnemyDirectAir");
        new ParameterView(VViewEnemyGround, "aircraftView", "ViewEnemyGround");
        new ParameterView(VViewFriendGround, "aircraftView", "ViewFriendGround");
        new ParameterView(VViewEnemyDirectGround, "aircraftView", "ViewEnemyDirectGround");
        new ParameterView(VOutsideViewFollow, "aircraftView", "OutsideViewFollow");
        new ParameterView(VNextViewFollow, "aircraftView", "NextViewFollow");
        new ParameterView(VNextViewEnemyFollow, "aircraftView", "NextViewEnemyFollow");
        new ParameterView(VcockpitAim, "aircraftView", "cockpitAim");
        new ParameterTrackIR(TRACKIR, "PanView", "TrackIR");
        new ParameterMisc(AUTOPILOT, "misc", "autopilot") {

            public boolean set_accessible() {
                return super.set_accessible() && isFMValid();
            }

        };
        new ParameterMisc(COCKPIT_DIM, "misc", "cockpitDim") {

            public boolean set_accessible() {
                return super.set_accessible() && isFMValid() && !Main3D.cur3D().isViewOutside();
            }

            // TODO: +++ Enhanced DeviceLink +++
            public boolean get_accessible() {
                return isFMValid();
            }

            public void get(List list) {
                answer(Main3D.cur3D().cockpitCur.isToggleDim() ? "1" : "0");
            }
            // TODO: --- Enhanced DeviceLink ---

        };
        new ParameterMisc(COCKPIT_LIGHT, "misc", "cockpitLight") {

            public boolean set_accessible() {
                return super.set_accessible() && isFMValid() && !Main3D.cur3D().isViewOutside();
            }

            // TODO: +++ Enhanced DeviceLink +++
            public boolean get_accessible() {
                return isFMValid();
            }

            public void get(List list) {
                answer(Main3D.cur3D().cockpitCur.isToggleLight() ? "1" : "0");
            }
            // TODO: --- Enhanced DeviceLink ---

        };
        new ParameterMisc(TORGLE_NAV_LIGHTS, "misc", "toggleNavLights") {

            public boolean set_accessible() {
                return super.set_accessible() && isFMValid();
            }

            // TODO: +++ Enhanced DeviceLink +++
            public boolean get_accessible() {
                return isFMValid();
            }

            public void get(List list) {
                answer(World.getPlayerFM().AS.bNavLightsOn ? "1" : "0");
            }
            // TODO: --- Enhanced DeviceLink ---

        };
        new ParameterMisc(TORGLE_LANDING_LIGHTS, "misc", "toggleLandingLight") {

            public boolean set_accessible() {
                return super.set_accessible() && isFMValid();
            }

            // TODO: +++ Enhanced DeviceLink +++
            public boolean get_accessible() {
                return isFMValid();
            }

            public void get(List list) {
                answer(World.getPlayerFM().AS.bLandingLightOn ? "1" : "0");
            }
            // TODO: --- Enhanced DeviceLink ---

        };
        new ParameterMisc(TORGLE_SMOKES, "misc", "toggleSmokes") {

            public boolean set_accessible() {
                return super.set_accessible() && isFMValid();
            }

            // TODO: +++ Enhanced DeviceLink +++
            public boolean get_accessible() {
                return isFMValid();
            }

            public void get(List list) {
                answer(World.getPlayerFM().AS.bShowSmokesOn ? "1" : "0");
            }
            // TODO: --- Enhanced DeviceLink ---

        };
        new ParameterMisc(AUTOPILOT_AUTO, "misc", "autopilotAuto") {

            public boolean set_accessible() {
                return super.set_accessible() && isFMValid();
            }

            // TODO: +++ Enhanced DeviceLink +++
            public boolean get_accessible() {
                return isFMValid();
            }

            public void get(List list) {
                answer(Main3D.cur3D().aircraftHotKeys.isAutoAutopilot() ? "1" : "0");
            }
            // TODO: --- Enhanced DeviceLink ---

        };
        new ParameterMisc(EJECTPILOT, "misc", "ejectPilot") {

            public boolean set_accessible() {
                return super.set_accessible() && isFMValid() && !World.isPlayerGunner();
            }

        };
        new ParameterMisc(PAD, "misc", "pad");
        new ParameterMisc(CHAT, "misc", "chat");
        new ParameterMisc(SHOW_POSITION_HINT, "misc", "showPositionHint");
        new ParameterMisc(ICON_TYPES, "misc", "iconTypes");
        new ParameterMisc(SHOW_MIRROR, "misc", "showMirror");
        new ParameterMisc(QUICK_SAVE_NET_TRACK, "$$$misc", "quickSaveNetTrack");
        new ParameterMisc(RADIO_CHANNEL_SWITCH, "$$$misc", "radioChannelSwitch");
        new ParameterMisc(ONLINE_RATING, "misc", "onlineRating") {

            public void set(List list) {
                if (list == null || list.size() != 1) {
                    return;
                } else {
                    boolean flag = "1".equals(list.get(0));
                    HotKeyCmd.exec(flag, cmd.hotKeyCmdEnv().name(), cmd.name());
                    return;
                }
            }

        };
        new ParameterMisc(RADIO_MUTE_KEY, "$$$misc", "radioMuteKey") {

            public void set(List list) {
                if (list == null || list.size() != 1) {
                    return;
                } else {
                    boolean flag = "1".equals(list.get(0));
                    HotKeyCmd.exec(flag, cmd.hotKeyCmdEnv().name(), cmd.name());
                    return;
                }
            }

        };
        new ParameterMisc(TIME_SPEED_UP, "timeCompression", "timeSpeedUp") {

            public boolean set_accessible() {
                return super.set_accessible() && Time.isEnableChangeSpeed();
            }

        };
        new ParameterMisc(TIME_SPEED_NORMAL, "timeCompression", "timeSpeedNormal") {

            public boolean set_accessible() {
                return super.set_accessible() && Time.isEnableChangeSpeed();
            }

        };
        new ParameterMisc(TIME_SPEED_DOWN, "timeCompression", "timeSpeedDown") {

            public boolean set_accessible() {
                return super.set_accessible() && Time.isEnableChangeSpeed();
            }

        };
        new ParameterMisc(TIME_SPEED_PAUSE, "hotkeys", "pause") {

            public boolean set_accessible() {
                return super.set_accessible() && Time.isEnableChangeSpeed();
            }

        };
        new ParameterMisc(ONLINE_RATING_PAGE, "misc", "onlineRatingPage");
        new ParameterMisc(SOUND_MUTE_KEY, "misc", "soundMuteKey");
        new ParameterView(COCKPIT_UP, "aircraftView", "cockpitUp");
        new ParameterMisc(TIME_SKIP, "timeCompression", "timeSkip") {

            public boolean set_accessible() {
                return super.set_accessible() && Time.isEnableChangeSpeed();
            }

        };
    }

    private void receiveParam(int i, List list) {
        try {
            Parameter parameter = (Parameter) paramMap.get(i & -2);
            if (parameter != null)
                if ((i & 1) == 0) {
                    if (parameter.get_accessible())
                        parameter.get(list);
                } else if (parameter.set_accessible())
                    parameter.set(list);
        } catch (Throwable throwable) {
            System.out.println(throwable.getMessage());
            throwable.printStackTrace();
        }
    }

    private void serverReceivePacket(DatagramPacket datagrampacket) throws IOException {
        if (datagrampacket.getLength() < 1)
            return;
        inputMsg.setData(null, false, datagrampacket.getData(), datagrampacket.getOffset(), datagrampacket.getLength());
        int i = inputMsg.readUnsignedByte();
        if (i != 82)
            return;
        inputMsg.fixed();
        outList.clear();
        do {
            int j = receiveKey();
            if (j == 0)
                break;
            inArg.clear();
            do {
                String s = receiveArg();
                if (s == null)
                    break;
                inArg.add(s);
            } while (true);
            receiveParam(j, inArg);
        } while (true);
        for (; outList.size() > 0; serverSocket.send(outPacket)) {
            int k = PACKET_SIZE - 1;
            byte abyte0[] = outPacket.getData();
            int l = 0;
            abyte0[l++] = 65;
            do {
                if (outList.size() <= 0)
                    break;
                String s1 = (String) outList.get(0);
                int i1 = s1.length();
                if (i1 > k)
                    break;
                outList.remove(0);
                int j1 = 0;
                while (j1 < i1) {
                    abyte0[l++] = (byte) (s1.charAt(j1) & 0x7f);
                    j1++;
                }
            } while (true);
            outPacket.setAddress(datagrampacket.getAddress());
            outPacket.setPort(datagrampacket.getPort());
            outPacket.setLength(l);
        }

    }

    private void do_answer(int i, String s) {
        if (inOutBuf.length() > 0)
            inOutBuf.delete(0, inOutBuf.length());
        inOutBuf.append("/" + i);
        if (s != null) {
            inOutBuf.append('\\');
            int j = s.length();
            for (int k = 0; k < j; k++) {
                char c = s.charAt(k);
                if (c == '\\' || c == '/')
                    inOutBuf.append('\\');
                inOutBuf.append(c);
            }

        }
        outList.add(inOutBuf.toString());
    }

    private void do_answer(int i, String as[]) {
        if (inOutBuf.length() > 0)
            inOutBuf.delete(0, inOutBuf.length());
        inOutBuf.append("/" + i);
        if (as != null) {
            int j = as.length;
            for (int k = 0; k < j; k++) {
                String s = as[k];
                inOutBuf.append('\\');
                int l = s.length();
                for (int i1 = 0; i1 < l; i1++) {
                    char c = s.charAt(i1);
                    if (c == '\\' || c == '/')
                        inOutBuf.append('\\');
                    inOutBuf.append(c);
                }

            }

        }
        outList.add(inOutBuf.toString());
    }

    // TODO: +++ Enhanced DeviceLink +++
//    private void do_answer(int i, List list) {
//        if (inOutBuf.length() > 0)
//            inOutBuf.delete(0, inOutBuf.length());
//        inOutBuf.append("/" + i);
//        if (list != null) {
//            int j = list.size();
//            for (int k = 0; k < j; k++) {
//                String s = (String) list.get(k);
//                inOutBuf.append('\\');
//                int l = s.length();
//                for (int i1 = 0; i1 < l; i1++) {
//                    char c = s.charAt(i1);
//                    if (c == '\\' || c == '/')
//                        inOutBuf.append('\\');
//                    inOutBuf.append(c);
//                }
//
//            }
//
//        }
//        outList.add(inOutBuf.toString());
//    }
    // TODO: --- Enhanced DeviceLink ---

    private int receiveKey() throws IOException {
        if (receiveChar() != '/')
            return 0;
        int i;
        char c;
        for (i = 0; inputMsg.available() > 0; i = i * 10 + (c - 48)) {
            c = receiveChar();
            if (c == '/' || c == '\\') {
                inputMsg.reset();
                return i;
            }
            if (c < '0' || c > '9')
                return 0;
        }

        return i;
    }

    private String receiveArg() throws IOException {
        char c = receiveChar();
        if (c == '/') {
            inputMsg.reset();
            return null;
        }
        if (c != '\\') {
            inputMsg.reset();
            return null;
        }
        if (inOutBuf.length() > 0)
            inOutBuf.delete(0, inOutBuf.length());
        do {
            char c1 = receiveChar(false);
            if (c1 == 0) {
                inputMsg.fixed();
                break;
            }
            if (c1 == '/') {
                inputMsg.reset();
                break;
            }
            if (c1 == '\\') {
                if (inputMsg.available() > 0) {
                    char c2 = receiveChar(false);
                    if (c2 == '\\' || c2 == '/') {
                        inputMsg.fixed();
                        inOutBuf.append(c2);
                        continue;
                    }
                    inputMsg.reset();
                } else {
                    inputMsg.reset();
                }
                break;
            }
            inputMsg.fixed();
            inOutBuf.append(c1);
        } while (true);
        if (inOutBuf.length() > 0)
            return inOutBuf.toString();
        else
            return null;
    }

    private char receiveChar() throws IOException {
        return receiveChar(true);
    }

    private char receiveChar(boolean flag) throws IOException {
        while (inputMsg.available() > 0) {
            if (flag)
                inputMsg.fixed();
            int i = inputMsg.readUnsignedByte();
            if (i >= 33 && i <= 126)
                return (char) i;
        }
        return '\0';
    }

    private DeviceLink(int i, ArrayList arraylist) {
        paramMap = new HashMapInt();
        outPacket = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
        inputMsg = new NetMsgInput();
        inArg = new ArrayList();
        inOutBuf = new StringBuffer();
        outList = new LinkedList();
        enableClientAdr = arraylist;
        try {
            InetAddress inetaddress = null;
            String s = Config.cur.ini.get("DeviceLink", "host", (String) null);
            if (s != null && s.length() > 0)
                inetaddress = InetAddress.getByName(s);
            else
                inetaddress = InetAddress.getLocalHost();
            serverSocket = new DatagramSocket(i, inetaddress);
            serverSocket.setSoTimeout(0);
            serverSocket.setSendBufferSize(PACKET_SIZE);
            serverSocket.setReceiveBufferSize(PACKET_SIZE);
            inputPackets = new LinkedList();
            inputAction = new ActionReceivedPacket();
            registerParams();
            listener = new Listener();
            listener.setPriority(Thread.currentThread().getPriority() + 1);
            listener.start();
        } catch (Throwable throwable) {
            System.out.println(throwable.getMessage());
            throwable.printStackTrace();
        }
    }

    public static void start() {
        if (deviceLink != null)
            return;
        int i = Config.cur.ini.get("DeviceLink", "port", 0, 0, 65000);
        if (i == 0)
            return;
        ArrayList arraylist = new ArrayList();
        String s = Config.cur.ini.get("DeviceLink", "IPS", (String) null);
        if (s != null) {
            String s1;
            for (StringTokenizer stringtokenizer = new StringTokenizer(s); stringtokenizer.hasMoreTokens(); arraylist.add(s1))
                s1 = stringtokenizer.nextToken();

        }
        deviceLink = new DeviceLink(i, arraylist);
    }

    private static int           PACKET_SIZE              = 2048;
    private static final int     VERSION                  = 1;
    private static final int     GET_ACCESSIBLE           = 2;
    private static final int     SET_ACCESSIBLE           = 3;
    private static final int     TRACKIR                  = 5;
    private static final int     TIME_OF_DAY              = 10;
    private static final int     PLANE                    = 11;
    private static final int     COCKPITS                 = 12;
    private static final int     COCKPIT_CUR              = 13;
    private static final int     ENGINES                  = 14;
    private static final int     SPEEDOMETER              = 15;
    private static final int     VARIOMETER               = 16;
    private static final int     SLIP                     = 17;
    private static final int     TURN                     = 18;
    private static final int     ANGULAR_SPEED            = 19;
    private static final int     ALTIMETER                = 20;
    private static final int     AZIMUT                   = 21;
    private static final int     BEACON_AZIMUT            = 22;
    private static final int     ROLL                     = 23;
    private static final int     PITCH                    = 24;
    private static final int     FUEL                     = 25;
    private static final int     OVERLOAD                 = 26;
    private static final int     SHAKE_LEVEL              = 27;
    private static final int     GEAR_POS_L               = 28;
    private static final int     GEAR_POS_R               = 29;
    private static final int     GEAR_POS_C               = 30;
    private static final int     MAGNETO                  = 31;
    private static final int     RPM                      = 32;
    private static final int     MANIFOLD                 = 33;
    private static final int     TEMP_OILIN               = 34;
    private static final int     TEMP_OILOUT              = 35;
    private static final int     TEMP_WATER               = 36;
    private static final int     TEMP_CYLINDERS           = 37;
    private static final int     M_POWER                  = 40;
    private static final int     M_FLAPS                  = 41;
    private static final int     M_AILERON                = 42;
    private static final int     M_ELEVATOR               = 43;
    private static final int     M_RUDDER                 = 44;
    private static final int     M_BRAKES                 = 45;
    private static final int     M_PITCH                  = 46;
    private static final int     M_TRIMAILERON            = 47;
    private static final int     M_TRIMELEVATOR           = 48;
    private static final int     M_TRIMRUDDER             = 49;
    private static final int     STABILIZER               = 50;
    private static final int     TOGGLE_ENGINE            = 51;
    private static final int     BOOST                    = 52;
    private static final int     MAGNETO_PLUS             = 53;
    private static final int     MAGNETO_MINUS            = 54;
    private static final int     COMPRESSOR_PLUS          = 55;
    private static final int     COMPRESSOR_MINUS         = 56;
    private static final int     ENGINE_SELECT_ALL        = 57;
    private static final int     ENGINE_SELECT_NONE       = 58;
    private static final int     ENGINE_SELECT_LEFT       = 59;
    private static final int     ENGINE_SELECT_RIGHT      = 60;
    private static final int     ENGINE_SELECT_1          = 61;
    private static final int     ENGINE_SELECT_2          = 62;
    private static final int     ENGINE_SELECT_3          = 63;
    private static final int     ENGINE_SELECT_4          = 64;
    private static final int     ENGINE_SELECT_5          = 65;
    private static final int     ENGINE_SELECT_6          = 66;
    private static final int     ENGINE_SELECT_7          = 67;
    private static final int     ENGINE_SELECT_8          = 68;
    private static final int     ENGINE_TOGGLE_ALL        = 69;
    private static final int     ENGINE_TOGGLE_LEFT       = 70;
    private static final int     ENGINE_TOGGLE_RIGHT      = 71;
    private static final int     ENGINE_TOGGLE_1          = 72;
    private static final int     ENGINE_TOGGLE_2          = 73;
    private static final int     ENGINE_TOGGLE_3          = 74;
    private static final int     ENGINE_TOGGLE_4          = 75;
    private static final int     ENGINE_TOGGLE_5          = 76;
    private static final int     ENGINE_TOGGLE_6          = 77;
    private static final int     ENGINE_TOGGLE_7          = 78;
    private static final int     ENGINE_TOGGLE_8          = 79;
    private static final int     ENGINE_EXTINGUISHER      = 80;
    private static final int     ENGINE_FEATHER           = 81;
    private static final int     GEAR                     = 82;
    private static final int     GEAR_UP_MANUAL           = 83;
    private static final int     GEAR_DOWN_MANUAL         = 84;
    private static final int     RADIATOR                 = 85;
    private static final int     TOGGLE_AIRBRAKE          = 86;
    private static final int     TAILWHEELLOCK            = 87;
    private static final int     DROP_TANKS               = 88;
    private static final int     DOCK_UNDOCK              = 89;
    private static final int     WEAPON0                  = 90;
    private static final int     WEAPON1                  = 91;
    private static final int     WEAPON2                  = 92;
    private static final int     WEAPON3                  = 93;
    private static final int     WEAPON01                 = 94;
    private static final int     GUNPODS                  = 95;
    private static final int     SIGHT_AUTO_ONOFF         = 96;
    private static final int     SIGHT_DIST_PLUS          = 97;
    private static final int     SIGHT_DIST_MINUS         = 98;
    private static final int     SIGHT_SIDE_RIGHT         = 99;
    private static final int     SIGHT_SIDE_LEFT          = 100;
    private static final int     SIGHT_ALT_PLUS           = 101;
    private static final int     SIGHT_ALT_MINUS          = 102;
    private static final int     SIGHT_SPD_PLUS           = 103;
    private static final int     SIGHT_SPD_MINUS          = 104;
    private static final int     WINGFOLD                 = 105;
    private static final int     COCKPITDOOR              = 106;
    private static final int     CARRIERHOOK              = 107;
    private static final int     BRAKESHOE                = 108;
    private static final int     GUNNER_FIRE              = 110;
    private static final int     GUNNER_MOVE              = 111;
    private static final int     VchangeCockpit           = 150;
    private static final int     VcockpitView0            = 151;
    private static final int     VcockpitView1            = 152;
    private static final int     VcockpitView2            = 153;
    private static final int     VcockpitView3            = 154;
    private static final int     VcockpitView4            = 155;
    private static final int     VcockpitView5            = 156;
    private static final int     VcockpitView6            = 157;
    private static final int     VcockpitView7            = 158;
    private static final int     VcockpitView8            = 159;
    private static final int     VcockpitView9            = 160;
    private static final int     Vfov90                   = 161;
    private static final int     Vfov85                   = 162;
    private static final int     Vfov80                   = 163;
    private static final int     Vfov75                   = 164;
    private static final int     Vfov70                   = 165;
    private static final int     Vfov65                   = 166;
    private static final int     Vfov60                   = 167;
    private static final int     Vfov55                   = 168;
    private static final int     Vfov50                   = 169;
    private static final int     Vfov45                   = 170;
    private static final int     Vfov40                   = 171;
    private static final int     Vfov35                   = 172;
    private static final int     Vfov30                   = 173;
    private static final int     VfovSwitch               = 174;
    private static final int     VfovInc                  = 175;
    private static final int     VfovDec                  = 176;
    private static final int     VCockpitView             = 177;
    private static final int     VCockpitShow             = 178;
    private static final int     VOutsideView             = 179;
    private static final int     VNextView                = 180;
    private static final int     VNextViewEnemy           = 181;
    private static final int     VOutsideViewFly          = 182;
    private static final int     VPadlockView             = 183;
    private static final int     VPadlockViewFriend       = 184;
    private static final int     VPadlockViewGround       = 185;
    private static final int     VPadlockViewFriendGround = 186;
    private static final int     VPadlockViewNext         = 187;
    private static final int     VPadlockViewPrev         = 188;
    private static final int     VPadlockViewForward      = 189;
    private static final int     VViewEnemyAir            = 190;
    private static final int     VViewFriendAir           = 191;
    private static final int     VViewEnemyDirectAir      = 192;
    private static final int     VViewEnemyGround         = 193;
    private static final int     VViewFriendGround        = 194;
    private static final int     VViewEnemyDirectGround   = 195;
    private static final int     VOutsideViewFollow       = 196;
    private static final int     VNextViewFollow          = 197;
    private static final int     VNextViewEnemyFollow     = 198;
    private static final int     VcockpitAim              = 199;
    private static final int     AUTOPILOT                = 200;
    private static final int     AUTOPILOT_AUTO           = 201;
    private static final int     EJECTPILOT               = 202;
    private static final int     COCKPIT_DIM              = 203;
    private static final int     COCKPIT_LIGHT            = 204;
    private static final int     TORGLE_NAV_LIGHTS        = 205;
    private static final int     TORGLE_LANDING_LIGHTS    = 206;
    private static final int     TORGLE_SMOKES            = 207;
    private static final int     PAD                      = 208;
    private static final int     CHAT                     = 209;
    private static final int     ONLINE_RATING            = 210;
    private static final int     SHOW_POSITION_HINT       = 211;
    private static final int     ICON_TYPES               = 212;
    private static final int     SHOW_MIRROR              = 213;
    private static final int     QUICK_SAVE_NET_TRACK     = 214;
    private static final int     RADIO_MUTE_KEY           = 215;
    private static final int     RADIO_CHANNEL_SWITCH     = 216;
    private static final int     TIME_SPEED_UP            = 217;
    private static final int     TIME_SPEED_NORMAL        = 218;
    private static final int     TIME_SPEED_DOWN          = 219;
    private static final int     TIME_SPEED_PAUSE         = 220;
    private static final int     ONLINE_RATING_PAGE       = 221;
    private static final int     SOUND_MUTE_KEY           = 222;
    private static final int     COCKPIT_UP               = 223;
    private static final int     TIME_SKIP                = 224;
//    private static final int     POSITION                   = 300;
//    private static final int     ORIENTATION                = 301;
//    private static final int     SPEED                      = 302;
//    private static final int     ACCEL                      = 303;
//    private static final int     COWL_FLAPS_POS             = 310;
//    private static final int     FLAPS_POS                  = 311;
//    private static final int     BRAKES_POS                 = 312;
//    private static final int     MACHINE_GUNS               = 320;
//    private static final int     FUEL_TANKS                 = 321;
//    private static final int     ENGINE_STATUS_FIRE         = 322;
//    private static final int     ENGINE_STATUS_TEMPERATURE  = 323;
//    private static final int     ENGINE_STATUS_IGNITION     = 324;
//    private static final int     ENGINE_STATUS_SUPERCHARGER = 325;
//    private static final int     ENGINE_STATUS_FUEL         = 326;
//    private static final int     CONTROLS_STATUS            = 327;
//    private static final int     COWL_FLAPS                 = 330;
//    private static final int     CANOPY                     = 331;
//    private static final int     ENGINE_MIX                 = 332;
//    private static final int     MAGNETO_SET                = 333;

    // TODO: +++ Enhanced DeviceLink +++
    private static final int     GEAR_ENH                 = 2082;
    private static final int     BOMB_BAY_DOOR            = 2400;
    // TODO: --- Enhanced DeviceLink ---

    private HashMapInt           paramMap;
    private DatagramSocket       serverSocket;
    private ArrayList            enableClientAdr;
    private Listener             listener;
    private List                 inputPackets;
    private DatagramPacket       outPacket;
    private ActionReceivedPacket inputAction;
    private NetMsgInput          inputMsg;
    private ArrayList            inArg;
    private StringBuffer         inOutBuf;
    private LinkedList           outList;
    private static DeviceLink    deviceLink;

}
