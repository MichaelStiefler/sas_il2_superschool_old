package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class Leo_451 extends Leoxyz implements TypeBomber, TypeTransport {

    public Leo_451() {
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0: // '\0'
                if (f < -45F) {
                    f = -45F;
                    flag = false;
                }
                if (f > 45F) {
                    f = 45F;
                    flag = false;
                }
                if (f1 < -1F) {
                    f1 = -1F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;

            case 1: // '\001'
                if (f < -30F) {
                    f = -30F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 0.0F) {
                    f1 = 0.0F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
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
        super.fSightCurForwardAngle += 0.2F;
        if (super.fSightCurForwardAngle > 75F) {
            super.fSightCurForwardAngle = 75F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (super.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjDistanceMinus() {
        super.fSightCurForwardAngle -= 0.2F;
        if (super.fSightCurForwardAngle < -15F) {
            super.fSightCurForwardAngle = -15F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (super.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjSideslipReset() {
        super.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        super.fSightCurSideslip++;
        if (super.fSightCurSideslip > 45F) {
            super.fSightCurSideslip = 45F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (super.fSightCurSideslip * 1.0F)) });
    }

    public void typeBomberAdjSideslipMinus() {
        super.fSightCurSideslip--;
        if (super.fSightCurSideslip < -45F) {
            super.fSightCurSideslip = -45F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (super.fSightCurSideslip * 1.0F)) });
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
        super.fSightSetForwardAngle = (float) Math.toDegrees(Math.atan(d / super.fSightCurAltitude));
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
        Class class1 = Leo_451.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "LeO-45");
        Property.set(class1, "meshName", "3DO/Plane/LeO-451/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1937.8F);
        Property.set(class1, "yearExpired", 1943.5F);
        Property.set(class1, "FlightModel", "FlightModels/LeO-451.fmd:LEO451_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLeo.class, CockpitLeo_Bombardier.class, CockpitLeo_TGunner.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 11, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_CANNON01", "_MGUN02", "_BombSpawn01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn02", "_BombSpawn03", "_BombSpawn03", "_BombSpawn04", "_BombSpawn04", "_BombSpawn05", "_BombSpawn05", "_BombSpawn06", "_BombSpawn06", "_BombSpawn09", "_BombSpawn09", "_BombSpawn10", "_BombSpawn10", "_BombSpawn11", "_BombSpawn21", "_BombSpawn12", "_BombSpawn22", "_BombSpawn13", "_BombSpawn23", "_BombSpawn14", "_BombSpawn24", "_BombSpawn15", "_BombSpawn25", "_BombSpawn16", "_BombSpawn26", "_BombSpawn17", "_BombSpawn27", "_BombSpawn18", "_BombSpawn28", "_BombSpawn07", "_BombSpawn08", "_BombSpawn51", "_BombSpawn61", "_BombSpawn71", "_BombSpawn52", "_BombSpawn62", "_BombSpawn72", "_BombSpawn53", "_BombSpawn63", "_BombSpawn73", "_BombSpawn54", "_BombSpawn64", "_BombSpawn74", "_BombSpawn55", "_BombSpawn65", "_BombSpawn75", "_BombSpawn56", "_BombSpawn66", "_BombSpawn76", "_BombSpawn57", "_BombSpawn67", "_BombSpawn77", "_BombSpawn58", "_BombSpawn68", "_BombSpawn78" });
    }
}
