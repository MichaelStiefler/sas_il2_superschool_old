package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class P_47D30 extends P_47X
{

    public P_47D30()
    {
        bCanopyInit = false;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.CT.bHasCockpitDoorControl = true;
        FM.CT.dvCockpitDoor = 1.0F;
    }

    public void update(float f)
    {
        super.update(f);
        if(!bCanopyInit && FM.isStationedOnGround())
        {
            bCanopyInit = true;
            System.out.println("*** Initial canopy state: " + (FM.CT.getCockpitDoor() != 1.0F ? "closed" : "open"));
        }
    }

    private boolean bCanopyInit;

    static 
    {
        Class class1 = P_47D30.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-47");
        Property.set(class1, "meshName", "3DO/Plane/P-47D-30(Multi1)/hier30.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_us", "3DO/Plane/P-47D-30(USA)/hier30.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1947.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-47D-30.fmd:REPUBLIC");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_47D30.class });
        Property.set(class1, "LOSElevation", 1.1104F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 1, 1, 1, 1, 9, 3, 
            3, 3, 9, 9, 2, 2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalDev01", "_ExternalBomb02", 
            "_ExternalBomb03", "_ExternalBomb01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 20;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 350);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 350);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 350);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 350);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 350);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x45";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonRO_4andHalfInch_3", 0);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonRO_4andHalfInch_3", 0);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x500+6x45";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonRO_4andHalfInch_3", 0);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonRO_4andHalfInch_3", 0);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x1000";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x1000+2x500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x1000+6x45";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonRO_4andHalfInch_3", 0);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonRO_4andHalfInch_3", 0);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "tank";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank75gal", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "tank+6x45";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank75gal", 1);
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonRO_4andHalfInch_3", 0);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "PylonRO_4andHalfInch_3", 0);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGun4andHalfInch", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "tank+2x500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank75gal", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
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
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
