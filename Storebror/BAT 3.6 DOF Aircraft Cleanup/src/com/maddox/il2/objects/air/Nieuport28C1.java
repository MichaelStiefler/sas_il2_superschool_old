package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class Nieuport28C1 extends Nieuport28 implements TypeFighter {

    public Nieuport28C1() {
    }

    public void update(float f) {
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER() && (this.FM.CT.PowerControl > 0.95F) && (this.FM.EI.engines[0].getRPM() > 400F)) {
            this.FM.AS.setSootState(this, 0, 1);
        } else {
            this.FM.AS.setSootState(this, 0, 0);
        }
        super.update(f);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            Nieuport28C1.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            Nieuport28C1.bChangedPit = true;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = Nieuport28C1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Nieuport28C1");
        Property.set(class1, "meshName", "3DO/Plane/Nieuport28C1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1915F);
        Property.set(class1, "yearExpired", 1923F);
        Property.set(class1, "FlightModel", "FlightModels/NIE28.fmd:NIE28C1");
        Property.set(class1, "cockpitClass", new Class[] { CockpitNieuport28C1.class });
        Aircraft.weaponTriggersRegister(class1, new int[2]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
