package com.maddox.il2.objects.air;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

public class P_47M extends P_47X
{

    public P_47M()
    {
    }

    static 
    {
        Class class1 = P_47M.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-47");
        Property.set(class1, "meshName", "3DO/Plane/P-47M(Multi1)/hierM.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_us", "3DO/Plane/P-47M(USA)/hierM.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1947.5F);
        Property.set(class1, "FlightModel", "FlightModels/P47Mg.fmd:P47Mg_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_47D30.class });
        Property.set(class1, "LOSElevation", 1.1104F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 0, 0, 0, 0, 9, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalDev01", "_ExternalBomb01"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 10;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Empty";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "267 rounds";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "267 rounds, 75galtank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank75gal", 1);
            a_lweaponslot[9] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "267 rounds, 200galtank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 267);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank200gal", 1);
            a_lweaponslot[9] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "425 rounds";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "425 rounds, 75galtank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank75gal", 1);
            a_lweaponslot[9] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "425 rounds, 200galtank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 425);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank200gal", 1);
            a_lweaponslot[9] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
