package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class BF_110G2 extends BF_110 {

    public BF_110G2() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
    }

    public void blisterRemoved(int i) {
        if (i == 1) {
            this.doWreck("Blister1A_D0");
            this.doWreck("Blister1B_D0");
        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            bChangedPit = true;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0: // '\0'
                if (f1 < -19F) {
                    f1 = -19F;
                    flag = false;
                }
                if (f1 > 30F) {
                    f1 = 30F;
                    flag = false;
                }
                float f2;
                if (f1 < 0.0F) {
                    f2 = cvt(f1, -19F, 0.0F, 20F, 30F);
                } else if (f1 < 12F) {
                    f2 = cvt(f1, 0.0F, 12F, 30F, 35F);
                } else {
                    f2 = cvt(f1, 12F, 30F, 35F, 40F);
                }
                if (f < 0.0F) {
                    if (f < -f2) {
                        f = -f2;
                        flag = false;
                    }
                } else if (f > f2) {
                    f = f2;
                    flag = false;
                }
                if ((Math.abs(f) > 17.8F) && (Math.abs(f) < 25F) && (f1 < -12F)) {
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = BF_110G2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf-110");
        Property.set(class1, "meshName", "3DO/Plane/Bf-110G-2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-110G-2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_110G.class, CockpitBF_110G_Gunner.class });
        Property.set(class1, "LOSElevation", 0.66895F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 10, 10, 9, 9, 9, 3, 3, 3, 3, 3, 3, 0, 0, 1, 9, 9, 9, 9, 2, 2, 2, 2, 9, 9, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_CANNON01", "_CANNON02", "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_CANNON03", "_CANNON04", "_CANNON05", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08" });
        weaponsRegister(class1, "default", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGk 350", "MGunMG15120MGk 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "m1", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, "PylonBF110R3", null, null, null, null, null, null, "MGunMG15120k 200", "MGunMG15120k 200", null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2sc250", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, null, "BombGunSC250", "BombGunSC250", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2ab250", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, null, "BombGunAB250", "BombGunAB250", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2sc500", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, null, "BombGunSC500", "BombGunSC500", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2ab500", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, null, "BombGunAB500", "BombGunAB500", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2sd500", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, null, "BombGunSD500", "BombGunSD500", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "4sc50", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGk 350", "MGunMG15120MGk 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", "PylonETC71", "PylonETC71", null, null, null, "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2sc2504sc50", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", "PylonETC71", "PylonETC71", null, "BombGunSC250", "BombGunSC250", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2ab2504sc50", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", "PylonETC71", "PylonETC71", null, "BombGunAB250", "BombGunAB250", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2sc5004sc50", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", "PylonETC71", "PylonETC71", null, "BombGunSC500", "BombGunSC500", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2ab5004sc50", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", "PylonETC71", "PylonETC71", null, "BombGunAB500", "BombGunAB500", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2sd5004sc50", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", "PylonETC71", "PylonETC71", null, "BombGunSD500", "BombGunSD500", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "m5", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGk 350", "MGunMG15120MGk 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, null, null, null, null, null, null, null, null, null, null, "PylonRO_WfrGr21Dual", null, null, "PylonRO_WfrGr21Dual", "RocketGunWfrGr21", "RocketGunWfrGr21", "RocketGunWfrGr21", "RocketGunWfrGr21", null, null, null, null });
        weaponsRegister(class1, "m1m3", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", "PylonETC71", "PylonETC71", "PylonBF110R3", null, null, "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "MGunMG15120k 200", "MGunMG15120k 200", null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "m1m5", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, "PylonBF110R3", null, null, null, null, null, null, "MGunMG15120k 200", "MGunMG15120k 200", null, "PylonRO_WfrGr21Dual", null, null, "PylonRO_WfrGr21Dual", "RocketGunWfrGr21", "RocketGunWfrGr21", "RocketGunWfrGr21", "RocketGunWfrGr21", null, null, null, null });
        weaponsRegister(class1, "r4", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", null, null, null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, "PylonBF110R4", null, null, null, null, null, null, null, null, "MGunBK37BF110G2 72", null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "r1r7", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", null, null, null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, "PylonBF110R4", null, null, null, null, null, null, null, null, "MGunBK37BF110G2 72", null, null, null, null, null, null, null, null, "PylonETC250", "PylonETC250", "FuelTankGun_Type_D", "FuelTankGun_Type_D" });
        weaponsRegister(class1, "r3", new String[] { null, null, null, null, null, null, "MGunMG15120MGk 350", "MGunMG15120MGk 300", "MGunMK108k 135", "MGunMK108k 135", "MGunMG81t 800", "MGunMG81t 800", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "r3m1", new String[] { null, null, null, null, null, null, "MGunMG15120MGki 350", "MGunMG15120MGki 300", "MGunMK108k 135", "MGunMK108k 135", "MGunMG81t 800", "MGunMG81t 800", null, null, "PylonBF110R3", null, null, null, null, null, null, "MGunMG15120k 200", "MGunMG15120k 200", null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "r3m2sc250", new String[] { null, null, null, null, null, null, "MGunMG15120MGki 350", "MGunMG15120MGki 300", "MGunMK108k 135", "MGunMK108k 135", "MGunMG81t 800", "MGunMG81t 800", null, null, null, "BombGunSC250", "BombGunSC250", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "r3m2ab250", new String[] { null, null, null, null, null, null, "MGunMG15120MGki 350", "MGunMG15120MGki 300", "MGunMK108k 135", "MGunMK108k 135", "MGunMG81t 800", "MGunMG81t 800", null, null, null, "BombGunAB250", "BombGunAB250", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "r3m2sc500", new String[] { null, null, null, null, null, null, "MGunMG15120MGki 350", "MGunMG15120MGki 300", "MGunMK108k 135", "MGunMK108k 135", "MGunMG81t 800", "MGunMG81t 800", null, null, null, "BombGunSC500", "BombGunSC500", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "r3m2sd500", new String[] { null, null, null, null, null, null, "MGunMG15120MGki 350", "MGunMG15120MGki 300", "MGunMK108k 135", "MGunMK108k 135", "MGunMG81t 800", "MGunMG81t 800", null, null, null, "BombGunSD500", "BombGunSD500", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "r3r7m1", new String[] { null, null, null, null, null, null, "MGunMG15120MGki 350", "MGunMG15120MGki 300", "MGunMK108k 135", "MGunMK108k 135", "MGunMG81t 800", "MGunMG81t 800", null, null, "PylonBF110R3", null, null, null, null, null, null, "MGunMG15120k 200", "MGunMG15120k 200", null, null, null, null, null, null, null, null, null, "PylonETC250", "PylonETC250", "FuelTankGun_Type_D", "FuelTankGun_Type_D" });
        weaponsRegister(class1, "r3r7m2sc250", new String[] { null, null, null, null, null, null, "MGunMG15120MGki 350", "MGunMG15120MGki 300", "MGunMK108k 135", "MGunMK108k 135", "MGunMG81t 800", "MGunMG81t 800", null, null, null, "BombGunSC250", "BombGunSC250", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonETC250", "PylonETC250", "FuelTankGun_Type_D", "FuelTankGun_Type_D" });
        weaponsRegister(class1, "r3r7m2ab250", new String[] { null, null, null, null, null, null, "MGunMG15120MGki 350", "MGunMG15120MGki 300", "MGunMK108k 135", "MGunMK108k 135", "MGunMG81t 800", "MGunMG81t 800", null, null, null, "BombGunAB250", "BombGunAB250", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonETC250", "PylonETC250", "FuelTankGun_Type_D", "FuelTankGun_Type_D" });
        weaponsRegister(class1, "r3r7m5", new String[] { null, null, null, null, null, null, "MGunMG15120MGk 350", "MGunMG15120MGk 300", "MGunMK108k 135", "MGunMK108k 135", "MGunMG81t 800", "MGunMG81t 800", null, null, null, null, null, null, null, null, null, null, null, null, "PylonRO_WfrGr21Dual", null, null, "PylonRO_WfrGr21Dual", "RocketGunWfrGr21", "RocketGunWfrGr21", "RocketGunWfrGr21", "RocketGunWfrGr21", "PylonETC250", "PylonETC250", "FuelTankGun_Type_D", "FuelTankGun_Type_D" });
        weaponsRegister(class1, "r4_", new String[] { null, null, null, null, null, null, null, null, "MGunMK108k 135", "MGunMK108k 135", "MGunMG81t 800", "MGunMG81t 800", null, null, "PylonBF110R4", null, null, null, null, null, null, null, null, "MGunBK37BF110G2 72", null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "r7", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGk 350", "MGunMG15120MGk 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonETC250", "PylonETC250", "FuelTankGun_Type_D", "FuelTankGun_Type_D" });
        weaponsRegister(class1, "r7m1", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, "PylonBF110R3", null, null, null, null, null, null, "MGunMG15120k 200", "MGunMG15120k 200", null, null, null, null, null, null, null, null, null, "PylonETC250", "PylonETC250", "FuelTankGun_Type_D", "FuelTankGun_Type_D" });
        weaponsRegister(class1, "r7m2sc250", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, null, "BombGunSC250", "BombGunSC250", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonETC250", "PylonETC250", "FuelTankGun_Type_D", "FuelTankGun_Type_D" });
        weaponsRegister(class1, "r7m2ab250", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", null, null, null, "BombGunAB250", "BombGunAB250", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "PylonETC250", "PylonETC250", "FuelTankGun_Type_D", "FuelTankGun_Type_D" });
        weaponsRegister(class1, "r7m3", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGk 350", "MGunMG15120MGk 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", "PylonETC71", "PylonETC71", null, null, null, "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", null, null, null, null, null, null, null, null, null, null, null, "PylonETC250", "PylonETC250", "FuelTankGun_Type_D", "FuelTankGun_Type_D" });
        weaponsRegister(class1, "r7m2m3sc", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", "PylonETC71", "PylonETC71", null, "BombGunSC250", "BombGunSC250", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", null, null, null, null, null, null, null, null, null, null, null, "PylonETC250", "PylonETC250", "FuelTankGun_Type_D", "FuelTankGun_Type_D" });
        weaponsRegister(class1, "r7m2m3ab", new String[] { "MGunMG17ki 1000", "MGunMG17ki 990", "MGunMG17ki 1029", "MGunMG17ki 1008", "MGunMG15120MGki 350", "MGunMG15120MGki 300", null, null, null, null, "MGunMG81t 800", "MGunMG81t 800", "PylonETC71", "PylonETC71", null, "BombGunAB250", "BombGunAB250", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", null, null, null, null, null, null, null, null, null, null, null, "PylonETC250", "PylonETC250", "FuelTankGun_Type_D", "FuelTankGun_Type_D" });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
