package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.fm.FlightModelMain;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class F2G extends F4U implements TypeX4Carrier {

    public F2G() {
        this.bToFire = false;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 3000F;
        this.fSightCurSpeed = 200F;
        this.fSightCurReadyness = 0.0F;
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 0.002F;
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -0.002F;
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 0.002F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -0.002F;
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    protected void mydebug(String s) {
    }

    protected static void weaponsRegister(Class class1, String s, String as[]) {
        try {
            int ai[] = Aircraft.getWeaponTriggersRegistered(class1);
            int i = ai.length;
            int j = as.length;
            ArrayList arraylist = (ArrayList) Property.value(class1, "weaponsList");
            if (arraylist == null) {
                arraylist = new ArrayList();
                Property.set(class1, "weaponsList", arraylist);
            }
            HashMapInt hashmapint = (HashMapInt) Property.value(class1, "weaponsMap");
            if (hashmapint == null) {
                hashmapint = new HashMapInt();
                Property.set(class1, "weaponsMap", hashmapint);
            }
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[i];
            for (int k = 0; k < j; k++) {
                String s1 = as[k];
                int i1 = 1;
                if (s1 != null) {
                    for (int j1 = s1.length() - 1; j1 > 0; j1--) {
                        if (s1.charAt(j1) != ' ') {
                            continue;
                        }
                        try {
                            i1 = Integer.parseInt(s1.substring(j1 + 1));
                            s1 = s1.substring(0, j1);
                        } catch (Exception exception1) {
                        }
                        break;
                    }

                    a_lweaponslot[k] = new Aircraft._WeaponSlot(ai[k], s1, i1);
                } else {
                    a_lweaponslot[k] = null;
                }
            }

            for (int l = j; l < i; l++) {
                a_lweaponslot[l] = null;
            }

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public boolean        bToFire;
    private float         deltaAzimuth;
    private float         deltaTangage;
    public static boolean bChangedPit = false;
    public float          fSightCurDistance;
    public float          fSightCurForwardAngle;
    public float          fSightCurSideslip;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurReadyness;

    static {
        Class class1 = F2G.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F2G");
        Property.set(class1, "meshName", "3DO/Plane/F2G(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/F2G(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 2100.5F);
        byte byte0 = 6;
        try {
            if (FlightModelMain.sectFile("FlightModels/F2G.fmd:URA") != null) {
                byte0 = 10;
            } else if (FlightModelMain.sectFile("FlightModels/F8F-2.fmd") != null) {
                byte0 = 8;
            } else if (FlightModelMain.sectFile("FlightModels/SeaFuryMkI.fmd") != null) {
                byte0 = 0;
            } else {
                byte0 = 6;
            }
        } catch (Exception exception) {
        }
        switch (byte0) {
            case 10: // '\n'
                Property.set(class1, "FlightModel", "FlightModels/F2G.fmd:URA");
                break;

            case 8: // '\b'
                Property.set(class1, "FlightModel", "FlightModels/F8F-2.fmd");
                break;

            case 0: // '\0'
                Property.set(class1, "FlightModel", "FlightModels/SeaFuryMkI.fmd");
                break;

            default:
                Property.set(class1, "FlightModel", "FlightModels/F4U-1C.fmd");
                break;
        }
        Property.set(class1, "cockpitClass", new Class[] { CockpitF2G.class });
        Property.set(class1, "LOSElevation", 1.0585F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb02", "_ExternalBomb03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
    }
}
