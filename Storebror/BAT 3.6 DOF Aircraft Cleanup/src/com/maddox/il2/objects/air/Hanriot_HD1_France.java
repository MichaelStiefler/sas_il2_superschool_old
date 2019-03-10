package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class Hanriot_HD1_France extends Hanriot_HD implements TypeFighter {

    public Hanriot_HD1_France() {
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
            Hanriot_HD1_France.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            Hanriot_HD1_France.bChangedPit = true;
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
        Class class1 = Hanriot_HD1_France.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hanriot_HD1_France");
        Property.set(class1, "meshName", "3DO/Plane/Hanriot_HD1_France/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1933F);
        Property.set(class1, "yearExpired", 1967F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitHanriot_HD1_France.class });
        Property.set(class1, "FlightModel", "FlightModels/Hanriot_HD1_France.fmd:Hanriot_HD_FM");
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
