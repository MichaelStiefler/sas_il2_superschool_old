package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class KI_49_II extends KI_49 implements TypeBomber {

    public KI_49_II() {
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, cvt(f, 0.01F, 0.06F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, cvt(f, 0.01F, 0.06F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearL10_D0", 0.0F, cvt(f, 0.05F, 0.75F, 0.0F, -38F), 0.0F);
        hiermesh.chunkSetAngles("GearL11_D0", 0.0F, 0.0F, cvt(f, 0.05F, 0.75F, 0.0F, -45F));
        hiermesh.chunkSetAngles("GearL13_D0", 0.0F, cvt(f, 0.05F, 0.75F, 0.0F, -157F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, cvt(f1, 0.3F, 0.35F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, cvt(f1, 0.3F, 0.35F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearR10_D0", 0.0F, cvt(f1, 0.34F, 0.99F, 0.0F, -38F), 0.0F);
        hiermesh.chunkSetAngles("GearR11_D0", 0.0F, 0.0F, cvt(f1, 0.05F, 0.75F, 0.0F, -45F));
        hiermesh.chunkSetAngles("GearR13_D0", 0.0F, cvt(f1, 0.34F, 0.99F, 0.0F, -157F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        moveGear(hierMesh(), f, f1, f2);
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0: // '\0'
                if (f < -35F) {
                    f = -35F;
                    flag = false;
                }
                if (f > 35F) {
                    f = 35F;
                    flag = false;
                }
                if (f1 < -25F) {
                    f1 = -25F;
                    flag = false;
                }
                if (f1 > 30F) {
                    f1 = 30F;
                    flag = false;
                }
                break;

            case 1: // '\001'
                if (f < -45F) {
                    f = -45F;
                    flag = false;
                }
                if (f > 45F) {
                    f = 45F;
                    flag = false;
                }
                if (f1 < -5F) {
                    f1 = -5F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                break;

            case 2: // '\002'
                if (f < -20F) {
                    f = -20F;
                    flag = false;
                }
                if (f > 20F) {
                    f = 20F;
                    flag = false;
                }
                if (f1 < -89F) {
                    f1 = -89F;
                    flag = false;
                }
                if (f1 > -12F) {
                    f1 = -12F;
                    flag = false;
                }
                break;

            case 3: // '\003'
                if (f < -10F) {
                    f = -10F;
                    flag = false;
                }
                if (f > 10F) {
                    f = 10F;
                    flag = false;
                }
                if (f1 < -10F) {
                    f1 = -10F;
                    flag = false;
                }
                if (f1 > 10F) {
                    f1 = 10F;
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
        hierMesh().chunkSetAngles("Blister4_D0", 0.0F, -45F, 0.0F);
        hierMesh().chunkSetAngles("Blister5_D0", 0.0F, -45F, 0.0F);
        hierMesh().chunkSetAngles("Blister6_D0", 0.0F, -45F, 0.0F);
        hierMesh().chunkSetAngles("Turret3C_D0", -45F, 0.0F, 0.0F);
    }

    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(int i) {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[i];
        try {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunBrowning303t", 750);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(11, "MGunBrowning303t", 750);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(12, "MGunHo5t", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(13, "MGunBrowning303t", 750);
            for (int j = 4; j < i; j++) {
                a_lweaponslot[j] = null;
            }

        } catch (Exception exception) {
        }
        return a_lweaponslot;
    }

    static {
        Class class1 = KI_49_II.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-21");
        Property.set(class1, "meshName", "3DO/Plane/Ki-49-II/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1948F);
//        Property.set(class1, "FlightModel", "FlightModels/Ki-21-II.fmd");
        Property.set(class1, "FlightModel", "FlightModels/Ki-49-II.fmd:KI49_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_49_II.class, CockpitKI_49_II_Bombardier.class, CockpitKI_49_II_NGunner.class, CockpitKI_49_II_AGunner.class, CockpitKI_49_II_TGunner.class, CockpitKI_49_II_BGunner.class });
        weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09" });
        weaponsRegister(class1, "default", new String[] { "MGunBrowning303t 750", "MGunBrowning303t 750", "MGunHo5t 250", "MGunBrowning303t 750", null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "20x15", new String[] { "MGunBrowning303t 750", "MGunBrowning303t 750", "MGunHo5t 250", "MGunBrowning303t 750", "BombGun15kgJ 2", "BombGun15kgJ 2", "BombGun15kgJ 2", "BombGun15kgJ 2", "BombGun15kgJ 2", "BombGun15kgJ 2", "BombGun15kgJ 3", "BombGun15kgJ 3", "BombGun15kgJ 2" });
        weaponsRegister(class1, "16x50", new String[] { "MGunBrowning303t 750", "MGunBrowning303t 750", "MGunHo5t 250", "MGunBrowning303t 750", "BombGun50kgJ 1", "BombGun50kgJ 1", "BombGun50kgJ 1", "BombGun50kgJ 1", "BombGun50kgJ 2", "BombGun50kgJ 2", "BombGun50kgJ 2", "BombGun50kgJ 3", "BombGun50kgJ 3" });
        weaponsRegister(class1, "9x100", new String[] { "MGunBrowning303t 750", "MGunBrowning303t 750", "MGunHo5t 250", "MGunBrowning303t 750", "BombGun100kgJ", "BombGun100kgJ", "BombGun100kgJ", "BombGun100kgJ", "BombGun100kgJ", "BombGun100kgJ", "BombGun100kgJ", "BombGun100kgJ", "BombGun100kgJ" });
        weaponsRegister(class1, "4x250", new String[] { "MGunBrowning303t 750", "MGunBrowning303t 750", "MGunHo5t 250", "MGunBrowning303t 750", "BombGun250kgJ", "BombGun250kgJ", null, null, "BombGun250kgJ", "BombGun250kgJ", null, null, null });
        weaponsRegister(class1, "2x500", new String[] { "MGunBrowning303t 750", "MGunBrowning303t 750", "MGunHo5t 250", "MGunBrowning303t 750", null, "BombGun500kgJ", null, null, "BombGun500kgJ", null, null, null, null });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null });
        
        String s = "unknown";
        try {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte slotLength = 13;
            s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = GenerateDefaultConfig(slotLength);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);

            s = "20x15";
            a_lweaponslot = GenerateDefaultConfig(slotLength);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun15kgJ", 2);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun15kgJ", 2);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGun15kgJ", 2);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGun15kgJ", 2);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGun15kgJ", 2);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGun15kgJ", 2);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGun15kgJ", 3);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGun15kgJ", 3);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun15kgJ", 2);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);

            s = "16x50";
            a_lweaponslot = GenerateDefaultConfig(slotLength);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun50kgJ", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun50kgJ", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGun50kgJ", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGun50kgJ", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGun50kgJ", 2);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGun50kgJ", 2);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGun50kgJ", 2);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGun50kgJ", 3);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun50kgJ", 3);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);

            s = "9x100";
            a_lweaponslot = GenerateDefaultConfig(slotLength);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun100kgJ", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun100kgJ", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGun100kgJ", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGun100kgJ", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGun100kgJ", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGun100kgJ", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGun100kgJ", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGun100kgJ", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun100kgJ", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);

            s = "4x250";
            a_lweaponslot = GenerateDefaultConfig(slotLength);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGun250kgJ", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);

            s = "2x500";
            a_lweaponslot = GenerateDefaultConfig(slotLength);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGun500kgJ", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(3, "BombGun500kgJ", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (Exception exception) {
            System.out.println("### ERROR ### at Ki-49-II Weapon Slot creation, slotname = " + s );
        }

        
    }
}
