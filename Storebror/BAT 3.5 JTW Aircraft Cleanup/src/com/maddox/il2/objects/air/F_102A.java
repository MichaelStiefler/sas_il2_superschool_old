package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F_102A extends F_102
{

    public F_102A()
    {
        counter = 0;
    }

    public void rareAction(float f, boolean flag)
    {
        boolean flag1 = false;
        if(counter++ % 5 == 0)
            flag1 = TrackingSystem(1, 25000, 600);
        if(!flag1 && counter++ % 12 == 3)
            TrackingSystem(2, 50000, 15);
        super.rareAction(f, flag);
    }

    private int counter;

    static 
    {
        Class class1 = F_102A.class;
        F_102.initCommon(class1);
        Property.set(class1, "meshName", "3DO/Plane/F-102A/hier102late.him");
    }
}
