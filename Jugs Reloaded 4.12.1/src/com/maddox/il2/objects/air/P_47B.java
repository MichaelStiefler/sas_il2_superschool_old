package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class P_47B extends P_47
{

    public P_47B()
    {
    }

    static 
    {
        Class class1 = P_47B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-47");
        Property.set(class1, "meshName", "3DO/Plane/P-47B(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_us", "3DO/Plane/P-47B(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1947.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-47D-10.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_47D10.class });
        Property.set(class1, "LOSElevation", 0.9879F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 0, 0, 0, 0
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08"
        });
        Aircraft.weaponsRegister(class1, "default", new String[] {
            "MGunBrowning50k 350", "MGunBrowning50k 350", "MGunBrowning50k 350", "MGunBrowning50k 350", "MGunBrowning50k 350", "MGunBrowning50k 350", "MGunBrowning50k 350", "MGunBrowning50k 350"
        });
        Aircraft.weaponsRegister(class1, "none", new String[] {
            null, null, null, null, null, null, null, null
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 8;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
