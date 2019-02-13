package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class A_26B extends A_26 implements TypeStormovik, TypeStormovikArmored {

    public A_26B() {
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        float f2 = 0.0F;
        switch (i) {
            default:
                break;

            case 0: // '\0'
                f2 = (Math.abs(f) / 66F) * 2.5F;
                if (f1 > f2) {
                    f1 = f2;
                    flag = false;
                }
                if (f1 < -70F) {
                    f1 = -70F;
                    flag = false;
                }
                break;

            case 1: // '\001'
                if (Math.abs(f) > 170F) {
                    f2 += Math.min(Math.abs(f) - 170F, 4F);
                }
                if (Math.abs(f) < 9F) {
                    f2 += ((9F - Math.abs(f)) / 9F) * 25F;
                }
                if (f1 < -70F) {
                    f1 = -70F;
                    flag = false;
                }
                if (f1 < f2) {
                    f1 = f2;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1: // '\001'
                super.FM.turret[0].setHealth(f);
                super.FM.turret[1].setHealth(f);
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if ((this.FM.AS.astateEngineStates[0] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 0, 1);
            }
            if ((this.FM.AS.astateEngineStates[1] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 1, 1);
            }
            if ((this.FM.AS.astateEngineStates[2] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 2, 1);
            }
            if ((this.FM.AS.astateEngineStates[3] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 3, 1);
            }
        }
        for (int i = 1; i < 4; i++) {
            if (super.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        if (super.thisWeaponsName.startsWith("PTO")) {
            this.FM.M.massEmpty -= 100F;
            this.FM.M.fuel += 260F;
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            super.FM.turret[0].setHealth(0.0F);
        } else {
            this.hierMesh().chunkVisible("Turret1A_D0", true);
            this.hierMesh().chunkVisible("Turret1B_D0", true);
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = A_26B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-26B");
        Property.set(class1, "meshName", "3DO/Plane/A26-Invader(multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1956.6F);
        Property.set(class1, "FlightModel", "FlightModels/A-26B.fmd:A26B_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA_26B.class, CockpitA26B_BGunner.class, CockpitA26B_TGunner.class });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 11, 11, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_MGUN13", "_MGUN14", "_MGUN15", "_MGUN16", "_MGUN17", "_MGUN18", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03",
                "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalDev17", "_ExternalDev18", "_ExternalR01", "_ExternalR02", "_ExternalR03", "_ExternalR04", "_ExternalR05", "_ExternalR06", "_ExternalR07", "_ExternalR08", "_ExternalR09", "_ExternalR10", "_ExternalR11", "_ExternalR12", "_ExternalR13", "_ExternalR14" });
    }
}
