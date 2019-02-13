package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.CLASS;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class CantZ1007bis extends CantZ1007 implements TypeBomber, TypeTransport {

    public CantZ1007bis() {
        super.fSightCurAltitude = 850F;
        super.fSightCurSpeed = 150F;
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
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TorpedoAngle", new Object[] { new Integer((int) (super.fSightCurSideslip * 1.0F)) });
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
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TorpedoAngle", new Object[] { new Integer((int) (super.fSightCurSideslip * 1.0F)) });
        } else {
            if (super.fSightCurSideslip < -10F) {
                super.fSightCurSideslip = -10F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip " + super.fSightCurSideslip);
        }
    }

    public void typeBomberAdjAltitudeReset() {
        super.fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus() {
        super.fSightCurAltitude += 10F;
        if (super.fSightCurAltitude > 6000F) {
            super.fSightCurAltitude = 6000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) super.fSightCurAltitude) });
    }

    public void typeBomberAdjAltitudeMinus() {
        super.fSightCurAltitude -= 10F;
        if (super.fSightCurAltitude < 300F) {
            super.fSightCurAltitude = 300F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) super.fSightCurAltitude) });
    }

    public void typeBomberAdjSpeedReset() {
        super.fSightCurSpeed = 50F;
    }

    public void typeBomberAdjSpeedPlus() {
        super.fSightCurSpeed += 5F;
        if (super.fSightCurSpeed > 650F) {
            super.fSightCurSpeed = 650F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) super.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        super.fSightCurSpeed -= 5F;
        if (super.fSightCurSpeed < 50F) {
            super.fSightCurSpeed = 50F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) super.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
        double d = (super.fSightCurSpeed / 3.6000000000000001D) * Math.sqrt(super.fSightCurAltitude * 0.20387359799999999D);
        d -= super.fSightCurAltitude * super.fSightCurAltitude * 1.419E-005D;
        super.fSightSetForwardAngle = (float) Math.atan(d / super.fSightCurAltitude);
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeFloat(super.fSightCurAltitude);
        netmsgguaranted.writeFloat(super.fSightCurSpeed);
        netmsgguaranted.writeFloat(super.fSightCurForwardAngle);
        netmsgguaranted.writeFloat(super.fSightCurSideslip);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        super.fSightCurAltitude = netmsginput.readFloat();
        super.fSightCurSpeed = netmsginput.readFloat();
        super.fSightCurForwardAngle = netmsginput.readFloat();
        super.fSightCurSideslip = netmsginput.readFloat();
    }

    static {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "CantZ");
        Property.set(class1, "meshName_it", "3do/plane/CantZ1007bis(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
        Property.set(class1, "meshName", "3do/plane/CantZ1007bis(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar09());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/CantZ1007.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitCant.class, CockpitCant_Bombardier.class, CockpitCant_TGunner.class, CockpitCant_BGunner.class, CockpitCantZ1007bis_LGunner.class, CockpitCantZ1007bis_RGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10" });
    }
}
