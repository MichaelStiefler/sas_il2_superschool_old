package com.maddox.il2.objects.air;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Finger;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.util.HashMapInt;

public class F_102A extends F_102 {

    public F_102A() {
        this.counter = 0;
        this.mSystem = "M-10";
    }

    public void rareAction(float f, boolean flag) {
        if ((this.counter++ % 5) == 0) {
            this.M_3_10();
        }
        if ((this.counter++ % 12) == 3) {
            this.IRST();
        }
        super.rareAction(f, flag);
    }

    private boolean IRST() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag1 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
        if (aircraft instanceof F_102A) {
            flag1 = true;
        }
        if (d3 < 0.0D) {
            d3 = 0.0D;
        }
        int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90F));
        if (i < 0) {
            i += 360;
        }
        int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90F));
        if (j < 0) {
            j += 360;
        }
        for (Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (flag1 && (actor instanceof Aircraft) && (actor.getArmy() != World.getPlayerArmy()) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(vector3d) > 20D) && ((Main.cur().clouds == null) || (Main.cur().clouds.getVisibility(actor.pos.getAbsPoint(), aircraft.pos.getAbsPoint()) >= 1.0F))) {
                this.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                double d7 = Math.ceil((d2 - d6) / 10D) * 10D;
                String s = "level with us";
                if ((d2 - d6 - 300D) >= 0.0D) {
                    s = "below us";
                }
                if (((d2 - d6) + 300D) <= 0.0D) {
                    s = "above us";
                }
                if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                    s = "slightly below";
                }
                if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) {
                    s = "slightly above";
                }
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f = 57.32484F * (float) Math.atan2(d9, -d8);
                int k = (int) (Math.floor((int) f) - 90D);
                if (k < 0) {
                    k += 360;
                }
                int l = k - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                float f1 = ((TrueRandom.nextFloat(20F) - 10F) / 100F) + 1.0F;
                int i1 = TrueRandom.nextInt(6) - 3;
                float f2 = 50000F;
                float f3 = f2;
                if (d3 < 15D) {
                    f3 = (float) (d3 * 0.8D * 3D);
                }
                int j1 = (int) (Math.ceil(Math.sqrt(((d11 * d11) + (d10 * d10)) * f1) / 10D) * 10D);
                if (j1 > f2) {
                    j1 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D);
                }
                float f4 = 57.32484F * (float) Math.atan2(j1, d7);
                int k1 = (int) (Math.floor((int) f4) - 90D);
                int l1 = (k1 - (90 - j)) + i1;
                int j2 = l + i1;
                int k2 = j2;
                if (k2 < 0) {
                    k2 += 360;
                }
                float f5 = (float) (f3 + (Math.sin(Math.toRadians(Math.abs(l) * 3)) * (f3 * 0.25F)));
                int l2 = (int) (f5 * Math.cos(Math.toRadians(l1)));
                String s3 = "  ";
                if (k2 < 5) {
                    s3 = "dead ahead, ";
                }
                if ((k2 >= 5) && (k2 < 8)) {
                    s3 = "right by 5\260, ";
                }
                if ((k2 > 7) && (k2 < 13)) {
                    s3 = "right by 10\260, ";
                }
                if ((k2 > 12) && (k2 < 18)) {
                    s3 = "right by 15\260, ";
                }
                if ((k2 > 17) && (k2 < 26)) {
                    s3 = "right by 20\260, ";
                }
                if ((k2 > 25) && (k2 < 36)) {
                    s3 = "right by 30\260, ";
                }
                if ((k2 > 35) && (k2 < 46)) {
                    s3 = "right by 40\260, ";
                }
                if ((k2 > 45) && (k2 <= 60)) {
                    s3 = "off our right, ";
                }
                if (k2 > 355) {
                    s3 = "dead ahead, ";
                }
                if ((k2 <= 355) && (k2 > 352)) {
                    s3 = "left by 5\260, ";
                }
                if ((k2 < 353) && (k2 > 347)) {
                    s3 = "left by 10\260, ";
                }
                if ((k2 < 348) && (k2 > 342)) {
                    s3 = "left by 15\260, ";
                }
                if ((k2 < 343) && (k2 > 334)) {
                    s3 = "left by 20\260, ";
                }
                if ((k2 < 335) && (k2 > 324)) {
                    s3 = "left by 30\260, ";
                }
                if ((k2 < 325) && (k2 > 314)) {
                    s3 = "left by 40\260, ";
                }
                if ((k2 < 315) && (k2 >= 300)) {
                    s3 = "off our left, ";
                }
                double d12 = 0.0D;
                EnginesInterface enginesinterface = ((Aircraft) actor).FM.EI;
                for (int i3 = 0; i3 < enginesinterface.engines.length; i3++) {
                    float f6 = 0.0F;
                    if ((enginesinterface.engines[i3].getType() == 2) || (enginesinterface.engines[i3].getType() == 3)) {
                        f6 = enginesinterface.engines[i3].thrustMax;
                        if (enginesinterface.engines[i3].getPowerOutput() > 1.0F) {
                            f6 *= enginesinterface.engines[i3].getPowerOutput() * 1.22F;
                        } else {
                            f6 *= (enginesinterface.engines[i3].getPowerOutput() * 0.8F) + 0.2F;
                        }
                    } else {
                        f6 = Reflection.getFloat(enginesinterface.engines[i3], "horsePowers") * ((enginesinterface.engines[i3].getPowerOutput() * 0.9F) + 0.1F) * 0.36F;
                    }
                    d12 += f6;
                }

                if (enginesinterface.engines.length > 4) {
                    d12 *= 2D / enginesinterface.engines.length;
                } else {
                    d12 /= Math.sqrt(enginesinterface.engines.length);
                }
                d12 *= 1.5D;
                if ((j1 <= l2) && (l1 >= -20) && (l1 <= 20) && (Math.abs(j2) <= 60) && (j1 < d12)) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "IR Tracking! " + s3 + s);
                    this.freq = 1;
                    this.setTimer(this.freq);
                    return true;
                }
            }
        }

        return true;
    }

    public void computeJ57_AB() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 20000D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 17F) {
                f1 = 1.0F;
            } else {
                float f2 = f * f;
                float f3 = f2 * f;
                f1 = ((0.0166647F * f3) - (0.24023F * f2)) + (0.444286F * f);
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    private int counter;

    static {
        Class class1 = F_102A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-102A");
        Property.set(class1, "meshName", "3DO/Plane/F-102A/hier102late.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1955F);
        Property.set(class1, "yearExpired", 1970F);
        Property.set(class1, "FlightModel", "FlightModels/F-102.fmd:F102");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF_102.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_InternalRock01", "_InternalRock02", "_InternalRock03", "_InternalRock04", "_InternalRock05", "_InternalRock05", "_InternalRock06", "_InternalRock06", "_InternalRock07", "_InternalRock07", "_InternalRock08", "_InternalRock08", "_InternalRock09", "_InternalRock09", "_InternalRock10", "_InternalRock10", "_ExternalDev01", "_ExternalDev02" });
        String s = "unknown";
        try {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 19;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24x70mmRockets";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xAIM4A+3xAIM4C";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xAIM4A+3xAIM4C+24x70mmRockets";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xDroptanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24x70mmRockets+2xDroptanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xAIM4A+3xAIM4C+2xDroptanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xAIM4A+3xAIM4C+24x70mmRockets+2xDroptanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunNull", 250);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunFFARMk4_gn16", 6);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(4, "RocketGunAIM4A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM4C", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF102", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "None";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        } catch (Exception exception) {
            // Show where the error happened, don't just throw away the exception!!!
            System.out.println("Exception in creating F-102A Weapon Slot " + s);
            exception.printStackTrace();
        }
    }
}
