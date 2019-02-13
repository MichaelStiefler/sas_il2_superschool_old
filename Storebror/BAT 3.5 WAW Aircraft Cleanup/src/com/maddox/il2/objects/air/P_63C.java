package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class P_63C extends P_39 {

    public P_63C() {
        this.fSteer = 0.0F;
    }

    public float getEyeLevelCorrection() {
        return -0.075F;
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -90F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, -85F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, cvt(f2, 0.01F, 0.12F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, cvt(f2, 0.01F, 0.12F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -85F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, -90F * f1, 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        moveGear(this.hierMesh(), f, f1, f2);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        xyz[1] = cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.24F, 0.0F, 0.2405F);
        ypr[1] = this.fSteer;
        this.hierMesh().chunkSetLocate("GearC3_D0", xyz, ypr);
        this.hierMesh().chunkSetAngles("GearC5_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.24F, 0.0F, -72F), 0.0F);
        this.hierMesh().chunkSetAngles("GearC4_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.24F, 0.0F, -40F), 0.0F);
        this.resetYPRmodifier();
        xyz[1] = cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.115F, 0.0F, 0.11675F);
        this.hierMesh().chunkSetLocate("GearL3_D0", xyz, ypr);
        this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.115F, 0.0F, -15F), 0.0F);
        this.hierMesh().chunkSetAngles("GearL5_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.115F, 0.0F, -27F), 0.0F);
        this.resetYPRmodifier();
        xyz[1] = cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.115F, 0.0F, 0.11675F);
        this.hierMesh().chunkSetLocate("GearR3_D0", xyz, ypr);
        this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.115F, 0.0F, -15F), 0.0F);
        this.hierMesh().chunkSetAngles("GearR5_D0", 0.0F, cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.115F, 0.0F, -27F), 0.0F);
    }

    public void moveSteering(float f) {
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -31F * f, 0.0F);
        this.fSteer = 20F * f;
        this.resetYPRmodifier();
        xyz[1] = cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.24F, 0.0F, 0.2405F);
        ypr[1] = this.fSteer;
        this.hierMesh().chunkSetLocate("GearC3_D0", xyz, ypr);
    }

    private float fSteer;

    static {
        Class class1 = P_63C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P63");
        Property.set(class1, "meshName", "3DO/Plane/P-63C(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/P-63C(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944.5F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-63C.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_63C.class} );
        Property.set(class1, "LOSElevation", 0.70305F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 0, 0, 9, 9, 9, 3, 3, 3, 3, 9, 9, 9, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev04", "_ExternalDev05" });
        weaponsRegister(class1, "default", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "1x75", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, null, null, "PylonP63CPLN2", null, null, null, null, "FuelTankGun_Tank75gal2", null, null, null, null });
        weaponsRegister(class1, "2x75", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, "PylonP63CPLN2", "PylonP63CPLN2", null, null, null, null, null, null, "FuelTankGun_Tank75gal2", "FuelTankGun_Tank75gal2", null, null });
        weaponsRegister(class1, "3x75", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, "PylonP63CPLN2", "PylonP63CPLN2", "PylonP63CPLN2", null, null, null, null, "FuelTankGun_Tank75gal2", "FuelTankGun_Tank75gal2", "FuelTankGun_Tank75gal2", null, null });
        weaponsRegister(class1, "2xM2", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", "MGunBrowning50kh 300", "MGunBrowning50kh 300", null, null, null, null, null, null, null, null, null, null, "PylonP63CGUNPOD", "PylonP63CGUNPOD" });
        weaponsRegister(class1, "2xM2_1x75", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", "MGunBrowning50kh 300", "MGunBrowning50kh 300", null, null, "PylonP63CPLN2", null, null, null, null, "FuelTankGun_Tank75gal2", null, null, "PylonP63CGUNPOD", "PylonP63CGUNPOD" });
        weaponsRegister(class1, "2xM2_2x75", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", "MGunBrowning50kh 300", "MGunBrowning50kh 300", "PylonP63CPLN2", "PylonP63CPLN2", null, null, null, null, null, null, "FuelTankGun_Tank75gal2", "FuelTankGun_Tank75gal2", "PylonP63CGUNPOD", "PylonP63CGUNPOD" });
        weaponsRegister(class1, "2xM2_3x75", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", "MGunBrowning50kh 300", "MGunBrowning50kh 300", "PylonP63CPLN2", "PylonP63CPLN2", "PylonP63CPLN2", null, null, null, null, "FuelTankGun_Tank75gal2", "FuelTankGun_Tank75gal2", "FuelTankGun_Tank75gal2", "PylonP63CGUNPOD", "PylonP63CGUNPOD" });
        weaponsRegister(class1, "2xM2_1xFAB100", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", "MGunBrowning50kh 300", "MGunBrowning50kh 300", null, null, "PylonP63CPLN2", "BombGunFAB100 1", null, null, null, null, null, null, "PylonP63CGUNPOD", "PylonP63CGUNPOD" });
        weaponsRegister(class1, "2xM2_1xFAB100_2x75", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", "MGunBrowning50kh 300", "MGunBrowning50kh 300", "PylonP63CPLN2", "PylonP63CPLN2", "PylonP63CPLN2", "BombGunFAB100 1", null, null, null, null, "FuelTankGun_Tank75gal2", "FuelTankGun_Tank75gal2", "PylonP63CGUNPOD", "PylonP63CGUNPOD" });
        weaponsRegister(class1, "2xM2_2xFAB100", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", "MGunBrowning50kh 300", "MGunBrowning50kh 300", "PylonP63CPLN2", "PylonP63CPLN2", null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", null, null, null, "PylonP63CGUNPOD", "PylonP63CGUNPOD" });
        weaponsRegister(class1, "2xM2_3xFAB100", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", "MGunBrowning50kh 300", "MGunBrowning50kh 300", "PylonP63CPLN2", "PylonP63CPLN2", "PylonP63CPLN2", "BombGunFAB100 1", "BombGunNull 1", "BombGunFAB100 1", "BombGunFAB100 1", null, null, null, "PylonP63CGUNPOD", "PylonP63CGUNPOD" });
        weaponsRegister(class1, "2xM2_1xFAB250", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", "MGunBrowning50kh 300", "MGunBrowning50kh 300", null, null, "PylonP63CPLN2", "BombGunFAB250 1", null, null, null, null, null, null, "PylonP63CGUNPOD", "PylonP63CGUNPOD" });
        weaponsRegister(class1, "2xM2_1xFAB250_2x75", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", "MGunBrowning50kh 300", "MGunBrowning50kh 300", "PylonP63CPLN2", "PylonP63CPLN2", "PylonP63CPLN2", "BombGunFAB250 1", null, null, null, null, "FuelTankGun_Tank75gal2", "FuelTankGun_Tank75gal2", "PylonP63CGUNPOD", "PylonP63CGUNPOD" });
        weaponsRegister(class1, "2xM2_2xFAB250", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", "MGunBrowning50kh 300", "MGunBrowning50kh 300", "PylonP63CPLN2", "PylonP63CPLN2", null, null, null, "BombGunFAB250 1", "BombGunFAB250 1", null, null, null, "PylonP63CGUNPOD", "PylonP63CGUNPOD" });
        weaponsRegister(class1, "2xM2_2xFAB250_1x75", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", "MGunBrowning50kh 300", "MGunBrowning50kh 300", "PylonP63CPLN2", "PylonP63CPLN2", "PylonP63CPLN2", null, null, "BombGunFAB250 1", "BombGunFAB250 1", "FuelTankGun_Tank75gal2", null, null, "PylonP63CGUNPOD", "PylonP63CGUNPOD" });
        weaponsRegister(class1, "1xFAB100", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, null, null, "PylonP63CPLN2", "BombGunFAB100 1", null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2xFAB100", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, "PylonP63CPLN2", "PylonP63CPLN2", null, null, null, "BombGunFAB100 1", "BombGunFAB100 1", null, null, null, null, null });
        weaponsRegister(class1, "3xFAB100", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, "PylonP63CPLN2", "PylonP63CPLN2", "PylonP63CPLN2", "BombGunFAB100 1", "BombGunNull 1", "BombGunFAB100 1", "BombGunFAB100 1", null, null, null, null, null });
        weaponsRegister(class1, "1xFAB250", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, null, null, "PylonP63CPLN2", "BombGunFAB250 1", null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2xFAB250", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, "PylonP63CPLN2", "PylonP63CPLN2", null, null, null, "BombGunFAB250 1", "BombGunFAB250 1", null, null, null, null, null });
        weaponsRegister(class1, "2xFAB250_1x75", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, "PylonP63CPLN2", "PylonP63CPLN2", "PylonP63CPLN2", null, null, "BombGunFAB250 1", "BombGunFAB250 1", "FuelTankGun_Tank75gal2", null, null, null, null });
        weaponsRegister(class1, "3xFAB250", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, "PylonP63CPLN2", "PylonP63CPLN2", "PylonP63CPLN2", "BombGunFAB250 1", "BombGunNull 1", "BombGunFAB250 1", "BombGunFAB250 1", null, null, null, null, null });
        weaponsRegister(class1, "1x250", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, null, null, "PylonP63CPLN2", "BombGun250lbs 1", null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2x250", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, "PylonP63CPLN2", "PylonP63CPLN2", null, null, null, "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null, null });
        weaponsRegister(class1, "1x500", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, null, null, "PylonP63CPLN2", "BombGun500lbs 1", null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "1x500_2x75", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, "PylonP63CPLN2", "PylonP63CPLN2", "PylonP63CPLN2", "BombGun500lbs 1", null, null, null, null, "FuelTankGun_Tank75gal2", "FuelTankGun_Tank75gal2", null, null });
        weaponsRegister(class1, "2x500", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, "PylonP63CPLN2", "PylonP63CPLN2", null, null, null, "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null, null });
        weaponsRegister(class1, "2x500_1x75", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, "PylonP63CPLN2", "PylonP63CPLN2", "PylonP63CPLN2", null, null, "BombGun500lbs 1", "BombGun500lbs 1", "FuelTankGun_Tank75gal2", null, null, null, null });
        weaponsRegister(class1, "3x500", new String[] { "MGunBrowning50s 200", "MGunBrowning50s 200", "MGunM9k 58", null, null, "PylonP63CPLN2", "PylonP63CPLN2", "PylonP63CPLN2", "BombGun500lbs 1", "BombGunNull 1", "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null, null });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
