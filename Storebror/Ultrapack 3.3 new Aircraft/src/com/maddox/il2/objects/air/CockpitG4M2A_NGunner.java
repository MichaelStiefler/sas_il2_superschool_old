package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitG4M2A_NGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        mesh.chunkSetAngles("Turret1A", 0.0F, -f, 0.0F);
        mesh.chunkSetAngles("Turret1B", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient)
    {
        if(!isRealMode())
            return;
        if(!aiTurret().bIsOperable)
        {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if(f < -35F)
            f = -35F;
        if(f > 35F)
            f = 35F;
        if(f1 > 25F)
            f1 = 25F;
        if(f1 < -45F)
            f1 = -45F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick()
    {
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        if(bGunFire)
        {
            if(iCocking > 0)
                iCocking = 0;
            else
                iCocking = 1;
        } else
        {
            iCocking = 0;
        }
        resetYPRmodifier();
        Cockpit.xyz[1] = -0.07F * (float)iCocking;
        Cockpit.ypr[1] = 0.0F;
        mesh.chunkSetLocate("Turret1C", Cockpit.xyz, Cockpit.ypr);
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        else
            bGunFire = flag;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
    }

    public CockpitG4M2A_NGunner()
    {
        super("3DO/Cockpit/G4M2A-NGun/hier.him", "he111");
        iCocking = 0;
    }

    public void reflectCockpitState()
    {
    }

    private int iCocking;

    static 
    {
        Property.set(CockpitG4M2A_NGunner.class, "aiTuretNum", 0);
        Property.set(CockpitG4M2A_NGunner.class, "weaponControlNum", 10);
        Property.set(CockpitG4M2A_NGunner.class, "astatePilotIndx", 2);
    }
}
