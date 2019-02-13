package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class DO_335V13 extends DO_335 {

    public DO_335V13() {
        this.bKeelUp = true;
    }

    public void update(float f) {
        float f1 = this.FM.EI.engines[1].getControlRadiator();
        if (Math.abs(this.flapps[1] - f1) > 0.01F) {
            this.flapps[1] = f1;
            this.hierMesh().chunkSetAngles("Water1_D0", 0.0F, -20F * f1, 0.0F);
            this.hierMesh().chunkSetAngles("Water2_D0", 0.0F, -10F * f1, 0.0F);
            this.hierMesh().chunkSetAngles("Water3_D0", 0.0F, -10F * f1, 0.0F);
        }
        f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps[0] - f1) > 0.01F) {
            this.flapps[0] = f1;
            for (int i = 2; i < 8; i++) {
                String s = "Cowflap" + i + "_D0";
                this.hierMesh().chunkSetAngles(s, 0.0F, -30F * f1, 0.0F);
            }

        }
        super.update(f);
        if (this.FM.AS.isMaster() && this.bKeelUp && (this.FM.AS.astateBailoutStep == 3) && !this.FM.isStationedOnGround()) {
            this.FM.AS.setInternalDamage(this, 5);
            this.FM.AS.setInternalDamage(this, 4);
            this.bKeelUp = false;
        }
    }

    public final void doKeelShutoff() {
        this.nextDMGLevels(4, 2, "Keel1_D" + this.chunkDamageVisible("Keel1"), this);
        this.oldProp[1] = 99;
        Wreckage wreckage;
        if (this.hierMesh().isChunkVisible("Prop2_D1")) {
            wreckage = new Wreckage(this, this.hierMesh().chunkFind("Prop2_D1"));
        } else {
            wreckage = new Wreckage(this, this.hierMesh().chunkFind("Prop2_D0"));
        }
        Eff3DActor.New(wreckage, null, null, 1.0F, Wreckage.SMOKE, 3F);
        Vector3d vector3d = new Vector3d();
        vector3d.set(this.FM.Vwld);
        wreckage.setSpeed(vector3d);
        this.hierMesh().chunkVisible("Prop2_D0", false);
        this.hierMesh().chunkVisible("Prop2_D1", false);
        this.hierMesh().chunkVisible("PropRot2_D0", false);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xxcannon04")) {
            this.debuggunnery("Armament System: Left Wing Cannon: Disabled..");
            this.FM.AS.setJamBullets(1, 1);
            this.getEnergyPastArmor(World.Rnd().nextFloat(6.98F, 24.35F), shot);
            return;
        }
        if (s.startsWith("xxcannon05")) {
            this.debuggunnery("Armament System: Right Wing Cannon: Disabled..");
            this.FM.AS.setJamBullets(1, 2);
            this.getEnergyPastArmor(World.Rnd().nextFloat(6.98F, 24.35F), shot);
            return;
        } else {
            super.hitBone(s, shot, point3d);
            return;
        }
    }

    private boolean bKeelUp;
    private float   flapps[] = { 0.0F, 0.0F };

    static {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Do-335");
        Property.set(class1, "meshName", "3DO/Plane/Do-335V-13/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Do-335V-13.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDO_335V13.class} );
        Property.set(class1, "LOSElevation", 1.00705F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN02", "_MGUN03", "_MGUN01", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn02", "_BombSpawn01", "_BombSpawn03", "_BombSpawn01", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11" });
        weaponsRegister(class1, "default", new String[] { "MGunMG15120si 200", "MGunMG15120si 200", "MGunMK103ki 70", "MGunMK103k 70", "MGunMK103k 70", null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "8sc50", new String[] { "MGunMG15120si 200", "MGunMG15120si 200", "MGunMK103ki 70", "MGunMK103k 70", "MGunMK103k 70", null, null, null, null, null, "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50", "BombGunSC50" });
        weaponsRegister(class1, "8sc70", new String[] { "MGunMG15120si 200", "MGunMG15120si 200", "MGunMK103ki 70", "MGunMK103k 70", "MGunMK103k 70", null, null, null, null, null, "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70", "BombGunSC70" });
        weaponsRegister(class1, "2sc250", new String[] { "MGunMG15120si 200", "MGunMG15120si 200", "MGunMK103ki 70", "MGunMK103k 70", "MGunMK103k 70", null, "BombGunSC250", "BombGunNull", "BombGunSC250", "BombGunNull", null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2xab250", new String[] { "MGunMG15120si 200", "MGunMG15120si 200", "MGunMK103ki 70", "MGunMK103k 70", "MGunMK103k 70", null, "BombGunAB250", "BombGunNull", "BombGunAB250", "BombGunNull", null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "1sc500", new String[] { "MGunMG15120si 200", "MGunMG15120si 200", "MGunMK103ki 70", "MGunMK103k 70", "MGunMK103k 70", "BombGunSC500", null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "1sd500", new String[] { "MGunMG15120si 200", "MGunMG15120si 200", "MGunMK103ki 70", "MGunMK103k 70", "MGunMK103k 70", "BombGunSD500", null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "1ab500", new String[] { "MGunMG15120si 200", "MGunMG15120si 200", "MGunMK103ki 70", "MGunMK103k 70", "MGunMK103k 70", "BombGunAB500", null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "1sc1000", new String[] { "MGunMG15120si 200", "MGunMG15120si 200", "MGunMK103ki 70", "MGunMK103k 70", "MGunMK103k 70", "BombGunSC1000", null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
