package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class Placeholder extends Scheme1
    implements TypeScout
{

    public Placeholder()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.air.Placeholder.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SAS");
        Property.set(class1, "meshName", "3DO/Plane/SASPlaceholder/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1910F);
        Property.set(class1, "yearExpired", 1970F);
        Property.set(class1, "FlightModel", "FlightModels/Generic.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitBF_109F2.class
        });
        Property.set(class1, "LOSElevation", 1.01885F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            ""
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 20;
            String s = "SAS_Placeholder";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            for(int i = 1; i < byte0; i++)
                a_lweaponslot[i] = null;

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            for(int j = 0; j < byte0; j++)
                a_lweaponslot[j] = null;

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
