package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.Property;

public class AH1_AI extends AH1 implements TypeScout, TypeTransport, TypeStormovik {

    public AH1_AI() {
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (World.Sun().ToSun.z < -0.22F) {
            this.FM.AS.setNavLightsState(true);
        } else {
            this.FM.AS.setNavLightsState(false);
        }
    }

    public static boolean bChangedPit = false;
    public Loc            suka;

    static {
        Class class1 = AH1_AI.class;
        AH1.initialize(class1);
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Cobra AI");
        Property.set(class1, "FlightModel", "FlightModels/AH1AI.fmd:AH1FM");
        Property.set(class1, "cockpitClass", new Class[] {});
    }
}
