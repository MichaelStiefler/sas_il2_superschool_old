package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class B_29B extends B_29X implements TypeBomber, TypeX4Carrier, TypeGuidedBombCarrier {

    public B_29B() {
        this.bToFire = false;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.isGuidingBomb = false;
    }

    public boolean typeGuidedBombCisMasterAlive() {
        return this.isMasterAlive;
    }

    public void typeGuidedBombCsetMasterAlive(boolean flag) {
        this.isMasterAlive = flag;
    }

    public boolean typeGuidedBombCgetIsGuiding() {
        return this.isGuidingBomb;
    }

    public void typeGuidedBombCsetIsGuiding(boolean flag) {
        this.isGuidingBomb = flag;
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 0.002F;
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -0.002F;
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 0.002F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -0.002F;
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberUpdate(float f) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void onAircraftLoaded() {
        this.FM.M.massEmpty -= 1000F;
    }

    public boolean  bToFire;
    private float   deltaAzimuth;
    private float   deltaTangage;
    private boolean isGuidingBomb;
    private boolean isMasterAlive;

    static {
        Class class1 = B_29B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-29");
        Property.set(class1, "meshName", "3DO/Plane/B-29B(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/B-29.fmd");
        weaponTriggersRegister(class1, new int[] { 14, 14, 3, 3, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04" });
        ArrayList arraylist = new ArrayList();
        Property.set(class1, "weaponsList", arraylist);
        HashMapInt hashmapint = new HashMapInt();
        Property.set(class1, "weaponsMap", hashmapint);
        byte byte0 = 6;
        try {
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x1600";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x300";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun300lbs", 3);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun300lbs", 3);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "20x100";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGun50kg", 3);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGun50kg", 3);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun50kg", 7);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun50kg", 7);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 2);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x1000";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x2000";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x1000";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 2);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x2000";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "16x300";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGun300lbs", 8);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGun300lbs", 8);
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "10x500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 5);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 5);
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "20x250";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 8);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 8);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 2);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x1600";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 2);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "20x500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 8);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 8);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 2);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x1000";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 2);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 2);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 4);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6x2000";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 2);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12x1600";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 2);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 2);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 4);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "20x1000";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 8);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 8);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 2);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "10x2000";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 3);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 3);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 2);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRazon";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "RocketGunRazon", 2);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunRazon", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "10xRazon";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "RocketGunRazon", 5);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "RocketGunRazon", 5);
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "20xRazon";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(3, "RocketGunRazon", 8);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(3, "RocketGunRazon", 8);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "RocketGunRazon", 2);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunRazon", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (Exception exception) {
        }
        try {
            String s1 = "20x20FragClusters";
            Aircraft._WeaponSlot a_lweaponslot1[] = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot1[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot1[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50t", 500);
            a_lweaponslot1[2] = new Aircraft._WeaponSlot(3, "BombGunM26A2", 8);
            a_lweaponslot1[3] = new Aircraft._WeaponSlot(3, "BombGunM26A2", 8);
            a_lweaponslot1[4] = new Aircraft._WeaponSlot(3, "BombGunM26A2", 2);
            a_lweaponslot1[5] = new Aircraft._WeaponSlot(3, "BombGunM26A2", 2);
            arraylist.add(s1);
            hashmapint.put(Finger.Int(s1), a_lweaponslot1);
        } catch (Exception exception1) {
        }
        try {
            String s2 = "none";
            Aircraft._WeaponSlot a_lweaponslot2[] = new Aircraft._WeaponSlot[byte0];
            for (int i = 0; i < byte0; i++) {
                a_lweaponslot2[i] = null;
            }

            arraylist.add(s2);
            hashmapint.put(Finger.Int(s2), a_lweaponslot2);
        } catch (Exception exception2) {
        }
    }
}
