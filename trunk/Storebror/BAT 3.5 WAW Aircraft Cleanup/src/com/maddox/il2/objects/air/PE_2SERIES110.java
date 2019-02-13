package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class PE_2SERIES110 extends PE_2 implements TypeBomber, TypeDiveBomber, TypeTransport {

    public PE_2SERIES110() {
        this.tme = 0L;
    }

    public boolean canOpenBombBay() {
        for (int i = 0; i < this.FM.CT.Weapons[3].length; i++) {
            if (this.FM.CT.Weapons[3][i].haveBullets() && (this.FM.CT.Weapons[3][i].getHookName().startsWith("_ExternalBomb01") || this.FM.CT.Weapons[3][i].getHookName().startsWith("_ExternalBomb02"))) {
                return false;
            }
        }

        return true;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.FM.turret.length != 0) {
            this.FM.turret[1].bIsOperable = false;
            this.FM.turret[2].bIsOperable = false;
            this.FM.turret[3].bIsOperable = false;
        }
        this.gun3 = this.getGunByHookName("_MGUN03");
        this.gun4 = this.getGunByHookName("_MGUN04");
    }

    public void update(float f) {
        if (!this.FM.turret[1].bIsAIControlled) {
            if (!this.hierMesh().isChunkVisible("Pilot3a_D0")) {
                this.setRadist(1);
            }
        } else if (Time.current() > this.tme) {
            this.tme = Time.current() + World.Rnd().nextLong(5000L, 20000L);
            if (this.FM.turret.length != 0) {
                this.gun3.loadBullets(Math.min(this.gun3.countBullets(), this.gun4.countBullets()));
                this.gun4.loadBullets(this.gun3.countBullets());
                Actor actor = null;
                for (int i = 1; i < 4; i++) {
                    if (this.FM.turret[i].bIsOperable) {
                        actor = this.FM.turret[i].target;
                    }
                }

                for (int j = 1; j < 4; j++) {
                    this.FM.turret[j].target = actor;
                }

                if (actor == null) {
                    this.setRadist(0);
                } else if (Actor.isValid(actor)) {
                    this.pos.getAbs(tmpLoc2);
                    actor.pos.getAbs(tmpLoc3);
                    tmpLoc2.transformInv(tmpLoc3.getPoint());
                    if (tmpLoc3.getPoint().x < -Math.abs(tmpLoc3.getPoint().y)) {
                        this.setRadist(1);
                    } else if (tmpLoc3.getPoint().y < 0.0D) {
                        this.setRadist(2);
                    } else {
                        this.setRadist(3);
                    }
                }
            }
        }
        super.update(f);
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0: // '\0'
                if (f < -110F) {
                    f = -110F;
                    flag = false;
                }
                if (f > 88F) {
                    f = 88F;
                    flag = false;
                }
                if (f1 < -1F) {
                    f1 = -1F;
                    flag = false;
                }
                if (f1 > 55F) {
                    f1 = 55F;
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

            case 2: // '\002'
                if (!this.FM.turret[1].bIsAIControlled) {
                    f = 0.0F;
                    f1 = 0.0F;
                    flag = false;
                    break;
                }
                if (f < -30F) {
                    f = -30F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 30F) {
                    f1 = 30F;
                    flag = false;
                }
                break;

            case 3: // '\003'
                if (!this.FM.turret[1].bIsAIControlled) {
                    f = 0.0F;
                    f1 = 0.0F;
                    flag = false;
                    break;
                }
                if (f < -30F) {
                    f = -30F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 30F) {
                    f1 = 30F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    private void setRadist(int i) {
        this.hierMesh().chunkVisible("Pilot3_D0", false);
        this.hierMesh().chunkVisible("Pilot3a_D0", false);
        this.hierMesh().chunkVisible("Pilot3b_D0", false);
        this.hierMesh().chunkVisible("Pilot3c_D0", false);
        this.FM.turret[1].bIsOperable = false;
        this.FM.turret[2].bIsOperable = false;
        this.FM.turret[3].bIsOperable = false;
        switch (i) {
            case 0: // '\0'
                this.hierMesh().chunkVisible("Pilot3_D0", true);
                this.FM.turret[1].bIsOperable = true;
                break;

            case 1: // '\001'
                this.hierMesh().chunkVisible("Pilot3a_D0", true);
                this.FM.turret[1].bIsOperable = true;
                break;

            case 2: // '\002'
                this.hierMesh().chunkVisible("Pilot3b_D0", true);
                this.FM.turret[2].bIsOperable = true;
                this.hierMesh().chunkVisible("Turret3B_D0", true);
                this.hierMesh().chunkVisible("Turret4B_D0", false);
                break;

            case 3: // '\003'
                this.hierMesh().chunkVisible("Pilot3c_D0", true);
                this.FM.turret[3].bIsOperable = true;
                this.hierMesh().chunkVisible("Turret3B_D0", false);
                this.hierMesh().chunkVisible("Turret4B_D0", true);
                break;
        }
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1: // '\001'
                this.FM.turret[0].setHealth(f);
                break;

            case 2: // '\002'
                this.FM.turret[1].setHealth(f);
                this.FM.turret[2].setHealth(f);
                this.FM.turret[3].setHealth(f);
                break;
        }
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle += 0.2F;
        if (this.fSightCurForwardAngle > 75F) {
            this.fSightCurForwardAngle = 75F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle -= 0.2F;
        if (this.fSightCurForwardAngle < -15F) {
            this.fSightCurForwardAngle = -15F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip++;
        if (this.fSightCurSideslip > 45F) {
            this.fSightCurSideslip = 45F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip--;
        if (this.fSightCurSideslip < -45F) {
            this.fSightCurSideslip = -45F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
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
        this.fSightSetForwardAngle = (float) Math.toDegrees(Math.atan(d / this.fSightCurAltitude));
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeFloat(this.fSightCurSpeed);
        netmsgguaranted.writeFloat(this.fSightCurForwardAngle);
        netmsgguaranted.writeFloat(this.fSightCurSideslip);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readFloat();
        this.fSightCurForwardAngle = netmsginput.readFloat();
        this.fSightCurSideslip = netmsginput.readFloat();
    }

    private long tme;
    private Gun  gun3;
    private Gun  gun4;

    static {
        Class class1 = PE_2SERIES110.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Pe-2");
        Property.set(class1, "meshName", "3DO/Plane/Pe-2series110/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1942.7F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Pe-2series110.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitPE2_110.class, CockpitPE2_Bombardier.class, CockpitPE2_110_TGunner.class, CockpitPE2_BGunner.class });
        Property.set(class1, "LOSElevation", 0.76315F);
        weaponTriggersRegister(class1, new int[] { 0, 1, 10, 11, 12, 13, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_CANNON02", "_CANNON01", "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06" });
        weaponsRegister(class1, "default", new String[] { "MGunShKASki 450", "MGunUBki 150", "MGunUBt 200", "MGunUBt 200", "MGunShKASt 450", "MGunShKASt 450", null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "6fab50", new String[] { "MGunShKASki 450", "MGunUBki 150", "MGunUBt 200", "MGunUBt 200", "MGunShKASt 450", "MGunShKASt 450", null, null, null, null, "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50", "BombGunFAB50" });
        weaponsRegister(class1, "6fab100", new String[] { "MGunShKASki 450", "MGunUBki 150", "MGunUBt 200", "MGunUBt 200", "MGunShKASt 450", "MGunShKASt 450", null, null, null, null, "BombGunFAB100", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100", "BombGunFAB100" });
        weaponsRegister(class1, "2fab250", new String[] { "MGunShKASki 450", "MGunUBki 150", "MGunUBt 200", "MGunUBt 200", "MGunShKASt 450", "MGunShKASt 450", null, null, "BombGunFAB250", "BombGunFAB250", null, null, null, null, null, null });
        weaponsRegister(class1, "2fab2502fab100", new String[] { "MGunShKASki 450", "MGunUBki 150", "MGunUBt 200", "MGunUBt 200", "MGunShKASt 450", "MGunShKASt 450", null, null, "BombGunFAB250", "BombGunFAB250", null, null, null, null, "BombGunFAB100", "BombGunFAB100" });
        weaponsRegister(class1, "4fab250", new String[] { "MGunShKASki 450", "MGunUBki 150", "MGunUBt 200", "MGunUBt 200", "MGunShKASt 450", "MGunShKASt 450", "BombGunFAB250", "BombGunFAB250", "BombGunFAB250", "BombGunFAB250", null, null, null, null, null, null });
        weaponsRegister(class1, "2fab500", new String[] { "MGunShKASki 450", "MGunUBki 150", "MGunUBt 200", "MGunUBt 200", "MGunShKASt 450", "MGunShKASt 450", "BombGunFAB500", "BombGunFAB500", null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2fab5002fab250", new String[] { "MGunShKASki 450", "MGunUBki 150", "MGunUBt 200", "MGunUBt 200", "MGunShKASt 450", "MGunShKASt 450", "BombGunFAB500", "BombGunFAB500", "BombGunFAB250", "BombGunFAB250", null, null, null, null, null, null });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
