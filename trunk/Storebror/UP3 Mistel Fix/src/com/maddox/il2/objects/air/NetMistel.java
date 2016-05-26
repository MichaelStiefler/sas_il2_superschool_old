package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;

public class NetMistel {
    public static void netSendMsg(Aircraft aircraft, boolean bool, NetMsgGuaranted netmsgguaranted) throws IOException {
        if (bool) {
            aircraft.net.postTo(aircraft.net.masterChannel(), netmsgguaranted);
        } else {
            aircraft.net.post(netmsgguaranted);
        }
    }
    
    public static boolean netSendExplosionToDroneMaster(Aircraft aircraft) {
        System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 1");
        if (!aircraft.isNet())
            return false;
        System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        if (!aircraft.isNetMaster())
            return false;
        System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 3");
        if (!(aircraft instanceof Mistel))
            return false;
        System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");
        Aircraft drone = ((Mistel)aircraft).getDrone();
        if (drone == null)
            return false;
        System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 5");
        if (!drone.isNetMirror())
            return false;
        System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 6");

        try {
            System.out.println("NetMistel > netSendExplosionToDroneMaster < to drone master!");
            NetMsgGuaranted netMsgGuaranted = new NetMsgGuaranted();
            netMsgGuaranted.writeByte(92);
            netMsgGuaranted.writeByte(1);
            netMsgGuaranted.writeNetObj(drone.net);
            netMsgGuaranted.writeNetObj(aircraft.net);
            netSendMsg(drone, true, netMsgGuaranted);
            System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 7");
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        System.out.println("NetMistel netSendExplosionToDroneMaster(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") ERROR");
        return false;
    }
    
    public static boolean netSendPowerControlToMirrors(Aircraft aircraft) {
        System.out.println("NetMistel netSendPowerControlToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 1");
        if (!aircraft.isNet())
            return false;
        System.out.println("NetMistel netSendPowerControlToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        if (!aircraft.isNetMaster())
            return false;
        System.out.println("NetMistel netSendPowerControlToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 3");
        if (!(aircraft instanceof Mistel))
            return false;
        System.out.println("NetMistel netSendPowerControlToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");

        try {
            System.out.println("NetMistel > netSendPowerControl < to mirrors!");
            NetMsgGuaranted netMsgGuaranted = new NetMsgGuaranted();
            netMsgGuaranted.writeByte(92);
            netMsgGuaranted.writeByte(2);
            netMsgGuaranted.writeNetObj(aircraft.net);
            netMsgGuaranted.writeFloat(aircraft.FM.CT.getPowerControl());
            netSendMsg(aircraft, false, netMsgGuaranted);
            System.out.println("NetMistel netSendPowerControlToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 5");
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        System.out.println("NetMistel netSendPowerControlToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") ERROR");
        return false;
    }

    public static boolean netSendTailwheelLockToMirrors(Aircraft aircraft) {
        System.out.println("NetMistel netSendTailwheelLockToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 1");
        if (!aircraft.isNet())
            return false;
        System.out.println("NetMistel netSendTailwheelLockToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        if (!aircraft.isNetMaster())
            return false;
        System.out.println("NetMistel netSendTailwheelLockToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 3");
        if (!(aircraft instanceof Mistel))
            return false;
        System.out.println("NetMistel netSendTailwheelLockToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");

        try {
            System.out.println("NetMistel > netSendTailwheelLock < to mirrors!");
            NetMsgGuaranted netMsgGuaranted = new NetMsgGuaranted();
            netMsgGuaranted.writeByte(92);
            netMsgGuaranted.writeByte(3);
            netMsgGuaranted.writeNetObj(aircraft.net);
            netMsgGuaranted.writeBoolean(aircraft.FM.Gears.bTailwheelLocked);
            netSendMsg(aircraft, false, netMsgGuaranted);
            System.out.println("NetMistel netSendTailwheelLockToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");
            return true;
         } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        System.out.println("NetMistel netSendTailwheelLockToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") ERROR");
        return false;
    }
    
    public static boolean netSendBrakesToMirrors(Aircraft aircraft) {
        System.out.println("NetMistel netSendBrakesToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 1");
        if (!aircraft.isNet())
            return false;
        System.out.println("NetMistel netSendBrakesToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 2");
        if (!aircraft.isNetMaster())
            return false;
        System.out.println("NetMistel netSendBrakesToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 3");
        if (!(aircraft instanceof Mistel))
            return false;
        System.out.println("NetMistel netSendBrakesToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");

        try {
            System.out.println("NetMistel > netSendBrakes < to mirrors!");
            NetMsgGuaranted netMsgGuaranted = new NetMsgGuaranted();
            netMsgGuaranted.writeByte(92);
            netMsgGuaranted.writeByte(4);
            netMsgGuaranted.writeNetObj(aircraft.net);
            netMsgGuaranted.writeFloat(aircraft.FM.CT.getBrake());
            netMsgGuaranted.writeFloat(aircraft.FM.CT.getBrakeLeft());
            netMsgGuaranted.writeFloat(aircraft.FM.CT.getBrakeRight());
            netSendMsg(aircraft, false, netMsgGuaranted);
            System.out.println("NetMistel netSendBrakesToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") 4");
            return true;
         } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        System.out.println("NetMistel netSendBrakesToMirrors(" + aircraft.getClass().getName() + " - " + aircraft.name() + ") ERROR");
        return false;
    }

}
