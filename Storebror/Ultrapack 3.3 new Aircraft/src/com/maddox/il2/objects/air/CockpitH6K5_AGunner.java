package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitH6K5_AGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        mesh.chunkSetAngles("Turret2A", 0.0F, f, 0.0F);
        mesh.chunkSetAngles("Turret2B", 0.0F, f1, 0.0F);
        if(f < -33F)
            f = -33F;
        if(f > 33F)
            f = 33F;
        if(f1 < -23F)
            f1 = -23F;
        if(f1 > 33F)
            f1 = 33F;
        mesh.chunkSetAngles("Turret2C", 0.0F, f, 0.0F);
        mesh.chunkSetAngles("Turret2D", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient)
    {
        if(isRealMode())
            if(!aiTurret().bIsOperable)
            {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else
            {
                float f = orient.getYaw();
                float f1 = orient.getTangage();
                if(f < -45F)
                    f = -45F;
                if(f > 45F)
                    f = 45F;
                if(f1 > 25F)
                    f1 = 25F;
                if(f1 < -45F)
                    f1 = -45F;
                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
            }
    }

    public void doGunFire(boolean flag)
    {
        if(isRealMode())
        {
            if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
                bGunFire = false;
            else
                bGunFire = flag;
            fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        }
    }

    public CockpitH6K5_AGunner()
    {
        super("3DO/Cockpit/H6K5-AGun/hier.him", "he111");
    }

    public void reflectCockpitState()
    {
    }

    static 
    {
        Property.set(CockpitH6K5_AGunner.class, "aiTuretNum", 4);
        Property.set(CockpitH6K5_AGunner.class, "weaponControlNum", 14);
        Property.set(CockpitH6K5_AGunner.class, "astatePilotIndx", 6);
    }
}
