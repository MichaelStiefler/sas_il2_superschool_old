package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class FW190D9early extends FW_190D implements TypeFighter, TypeBNZFighter {

    public FW190D9early() {
        this.kangle = 0.0F;
        this.patate = World.Rnd().nextFloat(1.07F, 1.096F);
        this.prout = World.Rnd().nextFloat(2350F, 2710F);
        this.prout1 = 0.0F;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (super.FM.isPlayers()) {
            bChangedPit = true;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).CT.bHasLockGearControl = false;
        super.FM.SensPitch = 0.603F;
        super.FM.SensRoll = 0.34F;
        super.FM.SensYaw = 0.49F;
        ((FlightModelMain) (super.FM)).EI.engines[0].tOilOutMaxRPM = World.Rnd().nextFloat(106.4F, 111.3F);
        super.FM.setGCenter(0.122F);
        ((FlightModelMain) (super.FM)).Gears.bTailwheelLocked = false;
        ((FlightModelMain) (super.FM)).EI.engines[0].setPropReductorValue(World.Rnd().nextFloat(0.54952F, 0.5523F));
        ((FlightModelMain) (super.FM)).EI.engines[0].setControlPropAuto(false);
        if (super.FM.isPlayers()) {
            ((FlightModelMain) (super.FM)).CT.setMagnetoControl(0);
        }
        ((FlightModelMain) (super.FM)).CT.bHasAirBrakeControl = true;
        if (this.getGunByHookName("_MGUN01") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("7mmC_D0", false);
            this.hierMesh().chunkVisible("7mmCowl_D0", true);
        }
        if (this.getGunByHookName("_CANNON03") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmL1_D0", false);
        }
        if (this.getGunByHookName("_CANNON04") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmR1_D0", false);
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (super.FM.isPlayers()) {
            bChangedPit = true;
        }
    }

    protected void moveGear(float f) {
        FW_190D.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (((FlightModelMain) (super.FM)).CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    public void update(float f) {
        World.Rnd().nextFloat(1.0F, 1.08F);
        for (int i = 1; i < 13; i++) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -20F * this.kangle, 0.0F);
        }

        this.kangle = (0.95F * this.kangle) + (0.05F * ((FlightModelMain) (super.FM)).EI.engines[0].getControlRadiator());
        if (!((FlightModelMain) (super.FM)).Gears.onGround()) {
            ((FlightModelMain) (super.FM)).EI.engines[0].addVside *= 1.0171699999999999D;
            ((FlightModelMain) (super.FM)).EI.engines[0].addVflow *= 1.0076499999999999D;
        }
        if (((FlightModelMain) (super.FM)).AS.isMaster() && Config.isUSE_RENDER()) {
            if (((FlightModelMain) (super.FM)).CT.PowerControl > this.patate) {
                ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 1);
            } else {
                ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 0);
            }
        }
        if (((FlightModelMain) (super.FM)).CT.getTrimElevatorControl() > 0.361F) {
            ((FlightModelMain) (super.FM)).CT.setTrimElevatorControl(0.361F);
        }
        if (((FlightModelMain) (super.FM)).CT.getTrimElevatorControl() < -0.261F) {
            ((FlightModelMain) (super.FM)).CT.setTrimElevatorControl(-0.261F);
        }
        super.update(f);
        if (((FlightModelMain) (super.FM)).Gears.onGround() && (((FlightModelMain) (super.FM)).EI.engines[0].getRPM() < 2600F)) {
            super.FM.SensYaw = 0.86F;
        } else {
            super.FM.SensYaw = 0.49F;
        }
        if (((FlightModelMain) (super.FM)).Gears.onGround() && (((FlightModelMain) (super.FM)).CT.ElevatorControl >= 0.06F)) {
            ((FlightModelMain) (super.FM)).Gears.bTailwheelLocked = true;
            ((FlightModelMain) (super.FM)).EI.engines[0].addVside *= 1.0074099999999999D;
            ((FlightModelMain) (super.FM)).EI.engines[0].addVflow *= 1.0076499999999999D;
            super.FM.SensYaw = 0.63F;
        } else {
            if (((FlightModelMain) (super.FM)).Gears.onGround() && (((FlightModelMain) (super.FM)).CT.ElevatorControl <= 0.06F)) {
                ((FlightModelMain) (super.FM)).Gears.bTailwheelLocked = false;
                super.FM.SensYaw = 0.86F;
                ((FlightModelMain) (super.FM)).EI.engines[0].addVside *= 1.0171699999999999D;
                ((FlightModelMain) (super.FM)).EI.engines[0].addVflow *= 1.0076499999999999D;
            }
            if (super.FM.isPlayers() && ((FlightModelMain) (super.FM)).Gears.onGround()) {
                super.FM.VmaxAllowed = super.FM.getSpeed();
            }
            if (((FlightModelMain) (super.FM)).VmaxAllowed > 251.3F) {
                super.FM.VmaxAllowed = 251.3F;
            }
            if (!super.FM.isPlayers() && ((FlightModelMain) (super.FM)).Gears.onGround()) {
                if (((FlightModelMain) (super.FM)).EI.engines[0].getRPM() < 100F) {
                    ((FlightModelMain) (super.FM)).CT.cockpitDoorControl = 1.0F;
                } else {
                    ((FlightModelMain) (super.FM)).CT.cockpitDoorControl = 0.0F;
                }
            }
            if ((((FlightModelMain) (super.FM)).CT.cockpitDoorControl > 0.5F) && (super.FM.getSpeed() > 18F)) {
                super.FM.VmaxAllowed = super.FM.getSpeed() + 1.0F;
            }
            if (((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle() > 1.0F) {
                if (((FlightModelMain) (super.FM)).CT.getElevator() < 0.0F) {
                    super.FM.SensPitch = 0.534F;
                } else {
                    super.FM.SensPitch = 0.603F;
                }
            }
            if (((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle() > 1.05F) {
                this.prout1 += f;
                if (this.prout1 > this.prout) {
                    ((FlightModelMain) (super.FM)).EI.engines[0].tOilOutMaxRPM = 119.8F;
                    ((FlightModelMain) (super.FM)).Sq.dragEngineCx[0] *= 5.4F;
                    if (World.Rnd().nextFloat(0.0F, 1.0F) > 0.97F) {
                        ((FlightModelMain) (super.FM)).EI.engines[0].doSetCyliderKnockOut(1);
                    }
                }
            } else {
                this.prout1 = 0.0F;
            }
            this.calcg();
            this.calcgneg();
            this.calccg();
        }
    }

    private void calcg() {
        this.G = super.FM.getOverload();
        this.limite = 35400F / ((FlightModelMain) (super.FM)).M.mass;
        if (this.G > (this.limite * 1.1040000000000001D)) {
            super.FM.VmaxAllowed = super.FM.getSpeed() - 30F;
        } else if ((((FlightModelMain) (super.FM)).EI.engines[0].getRPM() > 2800F) && (((FlightModelMain) (super.FM)).EI.engines[0].getRPM() <= 3000F)) {
            super.FM.VmaxAllowed = super.FM.getSpeed() + 14.5F;
        } else if (((FlightModelMain) (super.FM)).EI.engines[0].getRPM() > 3000F) {
            super.FM.VmaxAllowed = super.FM.getSpeed() + 11F;
        } else {
            super.FM.VmaxAllowed = 251.3F;
        }
    }

    private void calcgneg() {
        this.Gneg = super.FM.getOverload();
        this.limiteneg = -17700F / ((FlightModelMain) (super.FM)).M.mass;
        if (this.Gneg < this.limiteneg) {
            super.FM.VmaxAllowed = super.FM.getSpeed() - 30F;
        }
    }

    private void calccg() {
        if ((((FlightModelMain) (super.FM)).CT.Weapons[3] != null) && (((FlightModelMain) (super.FM)).CT.Weapons[3][0] != null) && ((FlightModelMain) (super.FM)).CT.Weapons[3][((FlightModelMain) (super.FM)).CT.Weapons[3].length - 1].haveBullets()) {
            super.FM.setGCenter(0.157F);
        } else {
            for (int l = 0; l < ((FlightModelMain) (super.FM)).CT.Weapons.length; l++) {
                if (((FlightModelMain) (super.FM)).CT.Weapons[l] != null) {
                    for (int j1 = 0; j1 < ((FlightModelMain) (super.FM)).CT.Weapons[l].length; j1++) {
                        if ((((FlightModelMain) (super.FM)).CT.Weapons[l][j1] instanceof FuelTankGun) && ((FlightModelMain) (super.FM)).CT.Weapons[l][j1].haveBullets()) {
                            super.FM.setGCenter(0.154F);
                        }
                    }

                } else {
                    super.FM.setGCenter(0.122F);
                }
            }

        }
    }

    public static boolean bChangedPit = false;
    private float         kangle;
    private float         patate;
    private float         G;
    private float         limite;
    private float         Gneg;
    private float         limiteneg;
    private float         prout;
    private float         prout1;

    static {
        Class class1 = FW190D9early.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190D-9early/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944.6F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190D9early.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190D9early.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 3, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02" });
    }
}
