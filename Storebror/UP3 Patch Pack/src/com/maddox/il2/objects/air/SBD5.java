package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SBD5 extends SBD
    implements TypeStormovik, TypeDiveBomber
{

    public SBD5()
    {
        numFlapps = 7;
    }

    static 
    {
        Class class1 = SBD5.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SBD");
        Property.set(class1, "meshName", "3DO/Plane/SBD-5(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/SBD-5(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_rz", "3DO/Plane/SBD-5(RZ)/hier.him");
        Property.set(class1, "PaintScheme_rz", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/SBD-5.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSBD5.class, CockpitSBD3_TGunner.class
        });
        Property.set(class1, "LOSElevation", 1.1058F);
        weaponTriggersRegister(class1, new int[] {
            0, 0, 10, 10, 3, 3, 3
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01"
        });
        weaponsRegister(class1, "default", new String[] {
            "MGunBrowning50s 350", "MGunBrowning50s 350", "MGunBrowning303t 500", "MGunBrowning303t 500", null, null, null
        });
        weaponsRegister(class1, "1x250", new String[] {
            "MGunBrowning50s 350", "MGunBrowning50s 350", "MGunBrowning303t 500", "MGunBrowning303t 500", null, null, "BombGun250lbs"
        });
        weaponsRegister(class1, "2x250", new String[] {
            "MGunBrowning50s 350", "MGunBrowning50s 350", "MGunBrowning303t 500", "MGunBrowning303t 500", "BombGun250lbs", "BombGun250lbs", null
        });
        weaponsRegister(class1, "3x250", new String[] {
            "MGunBrowning50s 350", "MGunBrowning50s 350", "MGunBrowning303t 500", "MGunBrowning303t 500", "BombGun250lbs", "BombGun250lbs", "BombGun250lbs"
        });
        weaponsRegister(class1, "1x500", new String[] {
            "MGunBrowning50s 350", "MGunBrowning50s 350", "MGunBrowning303t 500", "MGunBrowning303t 500", null, null, "BombGun500lbs"
        });
        weaponsRegister(class1, "2x500", new String[] {
            "MGunBrowning50s 350", "MGunBrowning50s 350", "MGunBrowning303t 500", "MGunBrowning303t 500", "BombGun500lbs", "BombGun500lbs", null
        });
        weaponsRegister(class1, "3x500", new String[] {
            "MGunBrowning50s 350", "MGunBrowning50s 350", "MGunBrowning303t 500", "MGunBrowning303t 500", "BombGun500lbs", "BombGun500lbs", "BombGun500lbs"
        });
        weaponsRegister(class1, "1x1000", new String[] {
            "MGunBrowning50s 350", "MGunBrowning50s 350", "MGunBrowning303t 500", "MGunBrowning303t 500", null, null, "BombGun1000lbs"
        });
        weaponsRegister(class1, "1x1600", new String[] {
            "MGunBrowning50s 350", "MGunBrowning50s 350", "MGunBrowning303t 500", "MGunBrowning303t 500", null, null, "BombGun1600lbs"
        });
        weaponsRegister(class1, "none", new String[] {
            null, null, null, null, null, null, null
        });
    }
}
