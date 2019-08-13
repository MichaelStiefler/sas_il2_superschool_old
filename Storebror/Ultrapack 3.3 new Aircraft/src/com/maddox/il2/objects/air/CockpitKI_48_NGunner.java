package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitKI_48_NGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        mesh.chunkSetAngles("TurretA", 0.0F, f, 0.0F);
        mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
        if(f > 15F)
            f = 15F;
        if(f1 < -21F)
            f1 = -21F;
        mesh.chunkSetAngles("CameraRodA", 0.0F, f, 0.0F);
        mesh.chunkSetAngles("CameraRodB", 0.0F, f1, 0.0F);
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
                if(f < -25F)
                    f = -25F;
                if(f > 25F)
                    f = 25F;
                if(f1 > 20F)
                    f1 = 20F;
                if(f1 < -40F)
                    f1 = -40F;
                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
            }
    }

    protected void interpTick()
    {
        if(isRealMode())
        {
            if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
                bGunFire = false;
            fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
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

    public CockpitKI_48_NGunner()
    {
        super("3DO/Cockpit/He-111H-6-NGun/FGunnerki48.him", "he111_gunner");
    }

    public void reflectWorldToInstruments(float f)
    {
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("ZHolesL_D1", true);
        if((fm.AS.astateCockpitState & 8) != 0)
            mesh.chunkVisible("ZHolesL_D2", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("ZHolesR_D1", true);
        if((fm.AS.astateCockpitState & 0x20) != 0)
            mesh.chunkVisible("ZHolesR_D2", true);
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("ZHolesF_D1", true);
        if((fm.AS.astateCockpitState & 0x80) != 0)
            mesh.chunkVisible("zOil_D1", true);
    }

    static 
    {
        Property.set(CockpitKI_48_NGunner.class, "aiTuretNum", 0);
        Property.set(CockpitKI_48_NGunner.class, "weaponControlNum", 10);
        Property.set(CockpitKI_48_NGunner.class, "astatePilotIndx", 1);
    }
}
