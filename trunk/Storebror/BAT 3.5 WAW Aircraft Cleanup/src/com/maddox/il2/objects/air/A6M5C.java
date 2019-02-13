package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class A6M5C extends A6M {

    public A6M5C() {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.equals("xxarmorg")) {
                this.getEnergyPastArmor(World.Rnd().nextFloat(44F, 46F), shot);
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                debugprintln(this, "*** Armor Glass: Hit..");
                if (shot.power <= 0.0F) {
                    debugprintln(this, "*** Armor Glass: Bullet Stopped..");
                    if (World.Rnd().nextFloat() < 0.5F) {
                        this.doRicochetBack(shot);
                    }
                }
                return;
            }
            if (s.startsWith("xxarmorg2")) {
                this.getEnergyPastArmor(World.Rnd().nextFloat(49F, 51F), shot);
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                debugprintln(this, "*** Armor Glass: Hit..");
                if (shot.power <= 0.0F) {
                    debugprintln(this, "*** Armor Glass: Bullet Stopped..");
                    if (World.Rnd().nextFloat() < 0.5F) {
                        this.doRicochetBack(shot);
                    }
                }
                return;
            }
            if (s.equals("xxarmors")) {
                this.getEnergyPastArmor(8D / (Math.abs(v1.x) + 9.9999997473787516E-006D), shot);
                return;
            }
            if (s.startsWith("xxammor")) {
                if (World.Rnd().nextFloat() < 0.01F) {
                    this.debuggunnery("Armament: Machine Gun Chain Broken..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                return;
            }
            if (s.startsWith("xxammowmgl")) {
                if (World.Rnd().nextFloat() < 0.01F) {
                    this.debuggunnery("Armament: Machine Gun Chain Broken..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                return;
            }
            if (s.startsWith("xxammowmgr")) {
                if (World.Rnd().nextFloat() < 0.01F) {
                    this.debuggunnery("Armament: Machine Gun Chain Broken..");
                    this.FM.AS.setJamBullets(0, 2);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int i = s.charAt(6) - 49;
                if (i < 3) {
                    if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.45F)) {
                        if (this.FM.AS.astateTankStates[i] == 0) {
                            this.debuggunnery("Fuel Tank (" + i + "): Pierced..");
                            this.FM.AS.hitTank(shot.initiator, i, 2);
                            this.FM.AS.doSetTankState(shot.initiator, i, 2);
                        }
                        if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.995F)) {
                            this.FM.AS.hitTank(shot.initiator, i, 1);
                            this.debuggunnery("Fuel Tank (" + i + "): Hit..");
                        }
                    }
                    return;
                }
            }
        }
        super.hitBone(s, shot, point3d);
    }

    static {
        Class class1 = A6M5C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A6M");
        Property.set(class1, "meshName", "3DO/Plane/A6M5c(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_ja", "3DO/Plane/A6M5c(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar02());
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/A6M5c.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA6M5c.class} );
        Property.set(class1, "LOSElevation", 1.01885F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 1, 1, 9, 9, 3, 9, 9, 2, 2, 2, 2 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalDev01", "_ExternalBomb02", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04" });
        weaponsRegister(class1, "default", new String[] { "MGunMG131s 230", "MGunMG131k 240", "MGunMG131k 240", "MGunMGFFk 125", "MGunMGFFk 125", null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2x150dt", new String[] { "MGunMG131s 230", "MGunMG131k 240", "MGunMG131k 240", "MGunMGFFk 125", "MGunMGFFk 125", null, null, null, "FuelTankGun_Tank0Underwing", "FuelTankGun_Tank0Underwing", null, null, null, null });
        weaponsRegister(class1, "1xwdt4s", new String[] { "MGunMG131s 230", "MGunMG131k 240", "MGunMG131k 240", "MGunMGFFk 125", "MGunMGFFk 125", "FuelTankGun_Tank0Centre", null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "4xtype3", new String[] { "MGunMG131s 230", "MGunMG131k 240", "MGunMG131k 240", "MGunMGFFk 125", "MGunMGFFk 125", null, null, null, null, null, "RocketGunType3Mk27", "RocketGunType3Mk27", "RocketGunType3Mk27", "RocketGunType3Mk27" });
        weaponsRegister(class1, "1x250", new String[] { "MGunMG131s 230", "MGunMG131k 240", "MGunMG131k 240", "MGunMGFFk 125", "MGunMGFFk 125", null, "PylonA6MPLN1", "BombGun250kg", null, null, null, null, null, null });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
