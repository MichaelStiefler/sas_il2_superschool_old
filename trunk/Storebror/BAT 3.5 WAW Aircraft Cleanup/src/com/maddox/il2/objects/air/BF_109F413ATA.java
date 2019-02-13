package com.maddox.il2.objects.air;

import java.util.Random;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.MGunMGFFki;
import com.maddox.rts.Property;

public class BF_109F413ATA extends BF_109 {

    public BF_109F413ATA() {
        this.cockpitDoor_ = 0.0F;
        this.fMaxKMHSpeedForOpenCanopy = 250F;
        this.kangle = 0.0F;
        this.bHasBlister = true;
        this.burst_fire = new int[2][2];
    }

    public void update(float f) {
        if ((this.getGunByHookName("_CANNON01") instanceof MGunMGFFki) && (((FlightModelMain) (super.FM)).EI.engines[0].tOilOut > 90F)) {
            Random random = new Random();
            int i = 1;
            if (((FlightModelMain) (super.FM)).CT.WeaponControl[i]) {
                for (int j = 0; j < 1; j++) {
                    int l = ((FlightModelMain) (super.FM)).CT.Weapons[i][j].countBullets();
                    if (l < this.burst_fire[j][1]) {
                        this.burst_fire[j][0]++;
                        this.burst_fire[j][1] = l;
                        int i1 = Math.abs(random.nextInt()) % 100;
                        float f1 = this.burst_fire[j][0] * 1.0F;
                        if (i1 < f1) {
                            ((FlightModelMain) (super.FM)).AS.setJamBullets(i, j);
                        }
                    }
                }

            } else {
                for (int k = 0; k < 1; k++) {
                    this.burst_fire[k][0] = 0;
                    this.burst_fire[k][1] = ((FlightModelMain) (super.FM)).CT.Weapons[i][k].countBullets();
                }

            }
        }
        if (super.FM.getSpeed() > 5F) {
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
        }
        this.hierMesh().chunkSetAngles("Flap01L_D0", 0.0F, -16F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap01U_D0", 0.0F, 16F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02L_D0", 0.0F, -16F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02U_D0", 0.0F, 16F * this.kangle, 0.0F);
        this.kangle = (0.95F * this.kangle) + (0.05F * ((FlightModelMain) (super.FM)).EI.engines[0].getControlRadiator());
        if (this.kangle > 1.0F) {
            this.kangle = 1.0F;
        }
        super.update(f);
        if ((((FlightModelMain) (super.FM)).CT.getCockpitDoor() > 0.20000000000000001D) && this.bHasBlister && (super.FM.getSpeedKMH() > this.fMaxKMHSpeedForOpenCanopy) && (this.hierMesh().chunkFindCheck("Blister1_D0") != -1)) {
            try {
                if (this == World.getPlayerAircraft()) {
                    ((CockpitBF_109F2) Main3D.cur3D().cockpitCur).removeCanopy();
                }
            } catch (Exception exception) {
            }
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(((FlightModelMain) (super.FM)).Vwld);
            wreckage.setSpeed(vector3d);
            this.bHasBlister = false;
            ((FlightModelMain) (super.FM)).CT.bHasCockpitDoorControl = false;
            super.FM.setGCenter(-0.5F);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = 0.8F;
        float f2 = (-0.5F * (float) Math.cos(f / f1 * 3.1415926535897931D)) + 0.5F;
        if ((f <= f1) || (f == 1.0F)) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", -33.5F * f2, 0.0F, 0.0F);
        }
        f2 = (-0.5F * (float) Math.cos((f - (1.0F - f1)) / f1 * 3.1415926535897931D)) + 0.5F;
        if (f >= (1.0F - f1)) {
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 33.5F * f2, 0.0F, 0.0F);
        }
        hiermesh.chunkSetAngles("GearC3_D0", 70F * f, 0.0F, 0.0F);
        if (f > 0.99F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", -33.5F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 33.5F, 0.0F, 0.0F);
        }
        if (f < 0.01F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 0.0F);
        }
    }

    protected void moveGear(float f) {
        float f1 = 0.9F - (((Wing) this.getOwner()).aircIndex(this) * 0.1F);
        float f2 = (-0.5F * (float) Math.cos(f / f1 * 3.1415926535897931D)) + 0.5F;
        if ((f <= f1) || (f == 1.0F)) {
            this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -77.5F * f2, 0.0F);
            this.hierMesh().chunkSetAngles("GearL2_D0", -33.5F * f2, 0.0F, 0.0F);
        }
        f2 = (-0.5F * (float) Math.cos((f - (1.0F - f1)) / f1 * 3.1415926535897931D)) + 0.5F;
        if (f >= (1.0F - f1)) {
            this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 77.5F * f2, 0.0F);
            this.hierMesh().chunkSetAngles("GearR2_D0", 33.5F * f2, 0.0F, 0.0F);
        }
        this.hierMesh().chunkSetAngles("GearC3_D0", 70F * f, 0.0F, 0.0F);
        if (f > 0.99F) {
            this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -77.5F, 0.0F);
            this.hierMesh().chunkSetAngles("GearL2_D0", -33.5F, 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 77.5F, 0.0F);
            this.hierMesh().chunkSetAngles("GearR2_D0", 33.5F, 0.0F, 0.0F);
        }
    }

    public void moveSteering(float f) {
        if (((FlightModelMain) (super.FM)).CT.getGear() >= 0.98F) {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
        }
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if ((regiment == null) || (regiment.country() == null)) {
            return "";
        }
        if (regiment.country().equals(PaintScheme.countryHungary)) {
            return PaintScheme.countryHungary + "_";
        } else {
            return "";
        }
    }

    public void moveCockpitDoor(float f) {
        if (this.bHasBlister) {
            this.resetYPRmodifier();
            this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
            if (Config.isUSE_RENDER()) {
                if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                    Main3D.cur3D().cockpits[0].onDoorMoved(f);
                }
                this.setDoorSnd(f);
            }
        }
    }

    public float   cockpitDoor_;
    private float  fMaxKMHSpeedForOpenCanopy;
    private float  kangle;
    public boolean bHasBlister;
    private int    burst_fire[][];

    static {
        Class class1 = BF_109F413ATA.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109F-4/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_sk", "3DO/Plane/Bf-109F-4(sk)/hier.him");
        Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1944.5F);
        Property.set(class1, "FlightModel", "FlightModels/bf-109f-4_1_13ATA.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109F2.class });
        Property.set(class1, "LOSElevation", 0.74205F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 1, 9, 9, 1, 1, 9, 9, 3, 3, 3, 3, 3, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON01", "_ExternalDev02", "_ExternalDev03", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalDev04" });
    }
}
