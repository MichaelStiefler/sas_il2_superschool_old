package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F_102A extends F_102 {

    public F_102A() {
        this.counter = 0;
    }

    public void rareAction(float f, boolean flag) {
        boolean flag1 = false;
        if ((this.counter++ % 5) == 0) {
            flag1 = this.TrackingSystem(1, 25000, 600);
        }
        if (!flag1 && ((this.counter++ % 12) == 3)) {
            this.TrackingSystem(2, 50000, 15);
        }
        super.rareAction(f, flag);
    }

    private int counter;

    static {
        Class class1 = F_102A.class;
        F_102.initCommon(class1);
        Property.set(class1, "meshName", "3DO/Plane/F-102A/hier102late.him");
    }
}
