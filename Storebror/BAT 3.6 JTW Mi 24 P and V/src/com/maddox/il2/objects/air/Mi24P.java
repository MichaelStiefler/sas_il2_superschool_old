package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class Mi24P extends Mi24X {

    public Mi24P() {
        this.shotFreqCannon = 0;
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 4) {
            this.k14Mode = 0;
        }
        if (this.k14Mode == 0) {
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: Grid");
            }
        } else if (this.k14Mode == 1) {
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: S-8");
            }
        } else if (this.k14Mode == 2) {
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: S-13");
            }
        } else if (this.k14Mode == 3) {
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: Unguided Bomb");
            }
        } else if ((this.k14Mode == 4) && (((Interpolate) (this.FM)).actor == World.getPlayerAircraft())) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: Cannon");
        }
        return true;
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -80F) {
                    f = -80F;
                    flag = false;
                }
                if (f > 20F) {
                    f = 20F;
                    flag = false;
                }
                if (f1 < -60F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.turret[0].bIsAIControlled = false;
    }

    public void auxPressed(int i) {
        super.auxPressed(i);
        if (i == 25) {
            this.shotFreqCannon++;
            if (this.shotFreqCannon > 1) {
                this.shotFreqCannon = 0;
            }
            if (this.shotFreqCannon == 0) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "GSh-2-30K: Max.");
            }
            if (this.shotFreqCannon == 1) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "GSh-2-30K: Min.");
            }
        }
    }

    protected static Aircraft._WeaponSlot[] GenerateDefaultConfig(byte bt) {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[bt];
        try {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2h", 250);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(7, "RocketGunFlareLO56_gn16", 96);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(7, "RocketGunFlareLO56_gn16", 96);
        } catch (Exception exception) {
            System.out.println("Weapon register error - Mi-24P : Default loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    public int shotFreqCannon;

    static {
        Class class1 = Mi24P.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mi-24P");
        Property.set(class1, "meshName", "3DO/Plane/Mi-24P/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1950F);
        Property.set(class1, "yearExpired", 1960.5F);
        Property.set(class1, "FlightModel", "FlightModels/Mi-24V.fmd:HELIFMD");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMi24.class, CockpitMi24_FLIR.class });
        Property.set(class1, "LOSElevation", 0.0F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 9, 2, 2, 2, 2, 7, 7, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_BombSpawn01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_Flare01", "_Flare02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev11", "_ExternalDev12" });
    }
}
