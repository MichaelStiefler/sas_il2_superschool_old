package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.CLASS;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class CantZ1007_Cargo extends CantZ1007 implements TypeBomber, TypeTransport {

    public CantZ1007_Cargo() {
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 150F;
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 3: // '\003'
                if (f < -45F) {
                    f = -45F;
                    flag = false;
                }
                if (f > 60F) {
                    f = 60F;
                    flag = false;
                }
                if (f1 < -35F) {
                    f1 = -35F;
                    flag = false;
                }
                if (f1 > 35F) {
                    f1 = 35F;
                    flag = false;
                }
                break;

            case 0: // '\0'
                if (f1 < -7F) {
                    f1 = -7F;
                    flag = false;
                }
                if (f1 > 80F) {
                    f1 = 80F;
                    flag = false;
                }
                break;

            case 1: // '\001'
                if (f < -40F) {
                    f = -40F;
                    flag = false;
                }
                if (f > 40F) {
                    f = 40F;
                    flag = false;
                }
                if (f1 < -25F) {
                    f1 = -25F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;

            case 2: // '\002'
                if (f < -60F) {
                    f = -60F;
                    flag = false;
                }
                if (f > 45F) {
                    f = 45F;
                    flag = false;
                }
                if (f1 < -35F) {
                    f1 = -35F;
                    flag = false;
                }
                if (f1 > 35F) {
                    f1 = 35F;
                    flag = false;
                }
                break;
        }
        af[0] = f;
        af[1] = f1;
        return flag;
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
        super.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        super.fSightCurForwardAngle += 0.4F;
        if (super.fSightCurForwardAngle > 75F) {
            super.fSightCurForwardAngle = 75F;
        }
    }

    public void typeBomberAdjDistanceMinus() {
        super.fSightCurForwardAngle -= 0.4F;
        if (super.fSightCurForwardAngle < -15F) {
            super.fSightCurForwardAngle = -15F;
        }
    }

    public void typeBomberAdjSideslipReset() {
        super.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        super.fSightCurSideslip += 0.5D;
        if (super.thisWeaponsName.startsWith("1x")) {
            if (super.fSightCurSideslip > 40F) {
                super.fSightCurSideslip = 40F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Torpedo Angle  " + super.fSightCurSideslip);
        } else {
            if (super.fSightCurSideslip > 10F) {
                super.fSightCurSideslip = 10F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip " + super.fSightCurSideslip);
        }
    }

    public void typeBomberAdjSideslipMinus() {
        super.fSightCurSideslip -= 0.5D;
        if (super.thisWeaponsName.startsWith("1x")) {
            if (super.fSightCurSideslip < -40F) {
                super.fSightCurSideslip = -40F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Torpedo Angle  " + super.fSightCurSideslip);
        } else {
            if (super.fSightCurSideslip < -10F) {
                super.fSightCurSideslip = -10F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip " + super.fSightCurSideslip);
        }
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 10F;
        if (this.fSightCurAltitude > 6000F) {
            this.fSightCurAltitude = 6000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 10F;
        if (this.fSightCurAltitude < 300F) {
            this.fSightCurAltitude = 300F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 50F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 5F;
        if (this.fSightCurSpeed > 650F) {
            this.fSightCurSpeed = 650F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 5F;
        if (this.fSightCurSpeed < 50F) {
            this.fSightCurSpeed = 50F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
        double d = (this.fSightCurSpeed / 3.6000000000000001D) * Math.sqrt(this.fSightCurAltitude * 0.20387359799999999D);
        d -= this.fSightCurAltitude * this.fSightCurAltitude * 1.419E-005D;
        super.fSightSetForwardAngle = (float) Math.atan(d / this.fSightCurAltitude);
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeFloat(this.fSightCurSpeed);
        netmsgguaranted.writeFloat(super.fSightCurForwardAngle);
        netmsgguaranted.writeFloat(super.fSightCurSideslip);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readFloat();
        super.fSightCurForwardAngle = netmsginput.readFloat();
        super.fSightCurSideslip = netmsginput.readFloat();
    }

    public float fSightCurAltitude;
    public float fSightCurSpeed;

    static {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "CantZ");
        Property.set(class1, "meshName", "3do/plane/CantZ1007_Early(multi)/hier2.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar09());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/CantZ1007_Early.fmd:Cant1007_Early");
        Property.set(class1, "cockpitClass", new Class[] { CockpitCant.class, CockpitCant_Bombardier.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_BombSpawn01" });
    }
}
