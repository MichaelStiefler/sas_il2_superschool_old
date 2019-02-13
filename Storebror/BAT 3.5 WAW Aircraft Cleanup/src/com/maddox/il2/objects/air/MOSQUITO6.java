package com.maddox.il2.objects.air;

import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class MOSQUITO6 extends MOSQUITO implements TypeFighter, TypeStormovik {

    public MOSQUITO6() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("8xRockets")) {
            this.hierMesh().chunkVisible("Rack1_D0", false);
            this.hierMesh().chunkVisible("Rack2_D0", false);
        }
    }

    static {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mosquito");
        Property.set(class1, "meshName", "3DO/Plane/Mosquito_FB_MkVI(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "meshName_gb", "3DO/Plane/Mosquito_FB_MkVI(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Mosquito-FBMkVI.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMosquito6.class} );
        Property.set(class1, "LOSElevation", 0.6731F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn01", "_BombSpawn02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
        weaponsRegister(class1, "default", new String[] { "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "extra", new String[] { "MGunBrowning303kipzl 780", "MGunBrowning303kipzl 780", "MGunBrowning303kipzl 780", "MGunBrowning303kipzl 780", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", "MGunHispanoMkIkpzl 175", null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2x250", new String[] { "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", null, null, "BombGun250lbsE 1", "BombGun250lbsE 1", null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "4x250", new String[] { "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", "BombGun250lbsE 1", null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2x500", new String[] { "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", null, null, "BombGun500lbsE 1", "BombGun500lbsE 1", null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "4x500", new String[] { "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "BombGun500lbsE 1", "BombGun500lbsE 1", "BombGun500lbsE 1", "BombGun500lbsE 1", null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "8xRockets", new String[] { "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunBrowning303kipzl 500", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", "MGunHispanoMkIkpzl 150", null, null, null, null, "PylonTEMPESTPLN3", "PylonTEMPESTPLN4", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1", "RocketGunHVAR5BEAU 1" });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
