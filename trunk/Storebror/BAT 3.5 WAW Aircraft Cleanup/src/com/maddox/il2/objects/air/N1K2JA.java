package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class N1K2JA extends N1K {

    public N1K2JA() {
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.35F, 0.95F, 0.0F, -82F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, cvt(f, 0.35F, 0.4F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, cvt(f, 0.35F, 0.95F, 0.0F, -48F), 0.0F);
        hiermesh.chunkSetAngles("GearL8_D0", 0.0F, cvt(f, 0.35F, 0.95F, 0.0F, -58F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f1, 0.05F, 0.65F, 0.0F, -82F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, cvt(f1, 0.05F, 0.1F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, cvt(f1, 0.05F, 0.65F, 0.0F, -48F), 0.0F);
        hiermesh.chunkSetAngles("GearR8_D0", 0.0F, cvt(f1, 0.05F, 0.65F, 0.0F, -58F), 0.0F);
        xyz[0] = xyz[1] = xyz[2] = ypr[0] = ypr[1] = ypr[2] = 0.0F;
        xyz[0] = cvt(f2, 0.0F, 1.0F, -0.075F, 0.0F);
        ypr[1] = cvt(f2, 0.0F, 1.0F, 40F, 0.0F);
        hiermesh.chunkSetLocate("GearC2_D0", xyz, ypr);
    }

    protected void moveGear(float f, float f1, float f2) {
        moveGear(this.hierMesh(), f, f1, f2);
    }

    public void moveWheelSink() {
        if (this.FM.CT.getGear() == 1.0F) {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.1F, 0.0F, 20F), 0.0F);
        }
        this.resetYPRmodifier();
        xyz[1] = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.23F, 0.0F, 0.23F);
        this.hierMesh().chunkSetLocate("GearL3_D0", xyz, ypr);
        this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.23F, 0.0F, -42F), 0.0F);
        this.hierMesh().chunkSetAngles("GearL5_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.23F, 0.0F, -45F), 0.0F);
        xyz[1] = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.27625F, 0.0F, 0.27625F);
        this.hierMesh().chunkSetLocate("GearR3_D0", xyz, ypr);
        this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.27625F, 0.0F, -33F), 0.0F);
        this.hierMesh().chunkSetAngles("GearR5_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.27625F, 0.0F, -66F), 0.0F);
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            for (int i = 1; i < 9; i++) {
                this.hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 0.0F, -20F * f1, 0.0F);
            }

        }
    }

    static {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "N1K");
        Property.set(class1, "meshName", "3DO/Plane/N1K2-Ja(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ja", "3DO/Plane/N1K2-Ja(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/N1K2-Ja.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitN1K2JA.class} );
        Property.set(class1, "LOSElevation", 1.1716F);
        weaponTriggersRegister(class1, new int[] { 1, 1, 1, 1, 3, 3, 3, 3, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02" });
        weaponsRegister(class1, "default", new String[] { "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", null, null, null, null, null, null });
        weaponsRegister(class1, "1x400dt", new String[] { "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", null, null, null, null, "PylonN1K1PLN1", "FuelTankGun_TankN1K1" });
        weaponsRegister(class1, "2x60", new String[] { "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", "BombGun60kgJ 1", "BombGun60kgJ 1", null, null, null, null });
        weaponsRegister(class1, "4x60", new String[] { "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", "BombGun60kgJ 1", "BombGun60kgJ 1", "BombGun60kgJ 1", "BombGun60kgJ 1", null, null });
        weaponsRegister(class1, "2x100", new String[] { "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", "BombGun100kgJ 1", "BombGun100kgJ 1", null, null, null, null });
        weaponsRegister(class1, "4x100", new String[] { "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", "BombGun100kgJ 1", "BombGun100kgJ 1", "BombGun100kgJ 1", "BombGun100kgJ 1", null, null });
        weaponsRegister(class1, "2x250", new String[] { "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", "MGunHo5k 200", "BombGun250kgJ 1", "BombGun250kgJ 1", null, null, null, null });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null });
    }
}
