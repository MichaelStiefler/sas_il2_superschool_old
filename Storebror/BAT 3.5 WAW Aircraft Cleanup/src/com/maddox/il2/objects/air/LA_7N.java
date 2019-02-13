package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class LA_7N extends LA_XN {

    public LA_7N() {
        this.arrestor = 0.0F;
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -37F * f, 0.0F);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.58F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).CT.bHasCockpitDoorControl = true;
        ((FlightModelMain) (super.FM)).CT.dvCockpitDoor = 0.75F;
    }

    public void update(float f) {
        if (((FlightModelMain) (super.FM)).CT.getArrestor() > 0.2F) {
            if (((FlightModelMain) (super.FM)).Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.arrestorVAngle, -26F, 11F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = (-42F * ((FlightModelMain) (super.FM)).Gears.arrestorVSink) / 37F;
                if ((f2 < 0.0F) && (super.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, ((FlightModelMain) (super.FM)).Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f2 > 0.0F) && (((FlightModelMain) (super.FM)).CT.getArrestor() < 0.95F)) {
                    f2 = 0.0F;
                }
                if (f2 > 0.0F) {
                    this.arrestor = (0.7F * this.arrestor) + (0.3F * (this.arrestor + f2));
                } else {
                    this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f2));
                }
                if (this.arrestor < 0.0F) {
                    this.arrestor = 0.0F;
                } else if (this.arrestor > 1.0F) {
                    this.arrestor = 1.0F;
                }
                this.moveArrestorHook(this.arrestor);
            }
        }
        super.update(f);
    }

    private float arrestor;

    static {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "La");
        Property.set(class1, "meshName", "3do/Plane/La-7N/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/La-7N.fmd:La_7N_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLA_5.class });
        Property.set(class1, "LOSElevation", 0.730618F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 1, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
