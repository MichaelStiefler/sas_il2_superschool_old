package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.ai.Regiment;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class F_102A_Early extends F_102 {

    public F_102A_Early() {
        this.counter = 0;
        this.mSystem = "M-3";
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "Early_";
    }

    public void rareAction(float f, boolean flag) {
        if ((this.counter++ % 5) == 0) {
            this.M_3_10();
        }
        super.rareAction(f, flag);
    }

    private int counter;

    static {
        Class class1 = F_102A_Early.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-102A");
        Property.set(class1, "meshName", "3DO/Plane/F-102A/hier102early.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1955F);
        Property.set(class1, "yearExpired", 1970F);
        Property.set(class1, "FlightModel", "FlightModels/F-102.fmd:F102");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF_102.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_InternalRock01", "_InternalRock02", "_InternalRock03", "_InternalRock04", "_InternalRock05", "_InternalRock05", "_InternalRock06", "_InternalRock06", "_InternalRock07", "_InternalRock07", "_InternalRock08", "_InternalRock08", "_InternalRock09", "_InternalRock09", "_InternalRock10", "_InternalRock10", "_ExternalDev01", "_ExternalDev02" });
        String s = "unknown";
        try {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 19;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24x70mmRockets";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xAIM4A+3xAIM4C";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xAIM4A+3xAIM4C+24x70mmRockets";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xDroptanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24x70mmRockets+2xDroptanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xAIM4A+3xAIM4C+2xDroptanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xAIM4A+3xAIM4C+24x70mmRockets+2xDroptanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "None";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (Exception exception) {
            // Show where the error happened, don't just throw away the exception!!!
            System.out.println("Exception in creating F-102A-Early Weapon Slot " + s);
            exception.printStackTrace();
        }
    }
}
