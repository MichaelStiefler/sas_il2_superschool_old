package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class F6F3 extends F6F {

    public F6F3() {
        this.flapps = 0.0F;
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            for (int i = 1; i < 8; i++) {
                this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -22F * f1, 0.0F);
            }

        }
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if (s.equals("3DO/Plane/F6F-3(USA)/hier.him") && (Mission.getMissionDate(true) >= 0x1287e02)) {
            return "late_";
        } else {
            return "";
        }
    }

    private float flapps;

    static {
        Class class1 = F6F3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F6F");
        Property.set(class1, "meshName", "3DO/Plane/F6F-3(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_us", "3DO/Plane/F6F-3(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFCSPar02());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/F6F-3.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF6F3.class} );
        Property.set(class1, "LOSElevation", 1.16055F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 3, 9, 3, 3, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06" });
        weaponsRegister(class1, "default", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 270", "MGunBrowning50kWF 270", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "6xhvar2", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, null, null, null, "PylonF6FPLN2", "PylonF6FPLN2", "PylonF6FPLN2", "PylonF6FPLN2", "PylonF6FPLN2", "PylonF6FPLN2", "RocketGunHVAR2", "RocketGunHVAR2", "RocketGunHVAR2", "RocketGunHVAR2", "RocketGunHVAR2", "RocketGunHVAR2" });
        weaponsRegister(class1, "6xhvargp", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, null, null, null, null, null, null, null, null, null, "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5" });
        weaponsRegister(class1, "6xhvarap", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, null, null, null, null, null, null, null, null, null, "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP" });
        weaponsRegister(class1, "1x150dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, "FuelTankGun_Tank150gal", null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "1x150dt6xhvar2", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, "FuelTankGun_Tank150gal", null, null, "PylonF6FPLN2", "PylonF6FPLN2", "PylonF6FPLN2", "PylonF6FPLN2", "PylonF6FPLN2", "PylonF6FPLN2", "RocketGunHVAR2", "RocketGunHVAR2", "RocketGunHVAR2", "RocketGunHVAR2", "RocketGunHVAR2", "RocketGunHVAR2" });
        weaponsRegister(class1, "1x150dt6xhvargp", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, "FuelTankGun_Tank150gal", null, null, null, null, null, null, null, null, "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5" });
        weaponsRegister(class1, "1x150dt6xhvarap", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, "FuelTankGun_Tank150gal", null, null, null, null, null, null, null, null, "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP" });
        weaponsRegister(class1, "2x100", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, null, "BombGun100lbs 1", "BombGun100lbs 1", null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2x1006xhvargp", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, null, "BombGun100lbs 1", "BombGun100lbs 1", null, null, null, null, null, null, "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5" });
        weaponsRegister(class1, "2x1006xhvargp1x150dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, "FuelTankGun_Tank150gal", "BombGun100lbs 1", "BombGun100lbs 1", null, null, null, null, null, null, "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5" });
        weaponsRegister(class1, "2x1006xhvarap", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, null, "BombGun100lbs 1", "BombGun100lbs 1", null, null, null, null, null, null, "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP" });
        weaponsRegister(class1, "2x1006xhvarap1x150dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, "FuelTankGun_Tank150gal", "BombGun100lbs 1", "BombGun100lbs 1", null, null, null, null, null, null, "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP" });
        weaponsRegister(class1, "2x1001x150dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, "FuelTankGun_Tank150gal", "BombGun100lbs 1", "BombGun100lbs 1", null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2x250", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, null, "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2x2506xhvargp", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, null, "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5" });
        weaponsRegister(class1, "2x2506xhvargp1x150dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, "FuelTankGun_Tank150gal", "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5" });
        weaponsRegister(class1, "2x2506xhvarap", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, null, "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP" });
        weaponsRegister(class1, "2x2506xhvarap1x150dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, "FuelTankGun_Tank150gal", "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP" });
        weaponsRegister(class1, "2x2501x150dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, "FuelTankGun_Tank150gal", "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "1x500", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", "PylonF6FPLN1", "BombGun500lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "1x5006xhvargp", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", "PylonF6FPLN1", "BombGun500lbs 1", null, null, null, null, null, null, null, null, null, "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5" });
        weaponsRegister(class1, "1x5006xhvarap", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", "PylonF6FPLN1", "BombGun500lbs 1", null, null, null, null, null, null, null, null, null, "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP" });
        weaponsRegister(class1, "2x500", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, null, "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2x5006xhvargp", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, null, "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null, null, null, "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5" });
        weaponsRegister(class1, "2x5006xhvarap", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, null, "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null, null, null, "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP" });
        weaponsRegister(class1, "2x5001x150dt", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", null, null, "FuelTankGun_Tank150gal", "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "1x1000", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", "PylonF6FPLN1", "BombGun1000lbs 1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "1x10006xhvargp", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", "PylonF6FPLN1", "BombGun1000lbs 1", null, null, null, null, null, null, null, null, null, "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5" });
        weaponsRegister(class1, "1x10006xhvarap", new String[] { "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 400", "MGunBrowning50kWF 380", "MGunBrowning50kWF 380", "PylonF6FPLN1", "BombGun1000lbs 1", null, null, null, null, null, null, null, null, null, "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP" });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
