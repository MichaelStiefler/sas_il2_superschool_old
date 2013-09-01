package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class P_47D40 extends P_47X
    implements TypeFighterAceMaker
{

    public P_47D40()
    {
        bCanopyInitState = false;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset()
    {
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
        k14Distance += 10F;
        if(k14Distance > 800F)
            k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerUp", new Object[] {
            new Integer((int)k14Distance)
        });
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDown", new Object[] {
            new Integer((int)k14Distance)
        });
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 11)
            k14WingspanType = 11;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
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
        if(!bCanopyInitState && FM.isStationedOnGround())
        {
            FM.AS.setCockpitDoor((Aircraft)FM.actor, 1);
            FM.CT.cockpitDoorControl = 1.0F;
            bCanopyInitState = true;
            System.out.println("*** Initial canopy state: " + (FM.CT.getCockpitDoor() != 1.0F ? "closed" : "open"));
        }
    }

    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    private boolean bCanopyInitState;

    static 
    {
        Class class1 = P_47D40.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P-47");
        Property.set(class1, "meshName", "3DO/Plane/P-47D-30(Multi1)/hier40.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/P-47D-30(USA)/hier40.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1947.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-47D-30.fmd:REPUBLIC");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_47D40.class });
        Property.set(class1, "LOSElevation", 1.1104F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 1, 1, 1, 1, 9, 3, 
            3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 
            9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalDev01", "_ExternalBomb02", 
            "_ExternalBomb03", "_ExternalBomb01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", 
            "_ExternalDev02", "_ExternalDev03"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 22;
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
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xhvargp";
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
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
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
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x500+8xhvargp";
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
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
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
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
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
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x1000+8xhvargp";
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
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "tank2";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50k", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(1, "MGunBrowning50k", 200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank200gal", 1);
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
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "tank+8xhvargp";
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
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunHVAR5", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
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
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xtank";
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
            a_lweaponslot[20] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank165gal", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank165gal", 1);
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
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
