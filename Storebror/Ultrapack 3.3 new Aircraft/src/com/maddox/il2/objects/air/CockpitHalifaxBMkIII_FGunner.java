package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHalifaxBMkIII_FGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        mesh.chunkSetAngles("TurretA", 0.0F, orient.getYaw(), 0.0F);
        mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
        mesh.chunkSetAngles("TurretC", 0.0F, cvt(orient.getYaw(), -17F, 17F, -17F, 17F), 0.0F);
        mesh.chunkSetAngles("TurretD", 0.0F, cvt(orient.getTangage(), -10F, 15F, -10F, 15F), 0.0F);
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
                if(f < -23F)
                    f = -23F;
                if(f > 23F)
                    f = 23F;
                if(f1 > 15F)
                    f1 = 15F;
                if(f1 < -25F)
                    f1 = -25F;
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

    public CockpitHalifaxBMkIII_FGunner()
    {
        super("3DO/Cockpit/Halifax-Bombardier/Halifax-FGun.him", "bf109");
        cockpitNightMats = (new String[] {
            "textrbm9", "texture25"
        });
        setNightMats(false);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("XGlassDamage1", true);
        if((fm.AS.astateCockpitState & 8) != 0)
            mesh.chunkVisible("XGlassDamage2", true);
        if((fm.AS.astateCockpitState & 0x20) != 0)
            mesh.chunkVisible("XGlassDamage2", true);
        if((fm.AS.astateCockpitState & 2) != 0)
            mesh.chunkVisible("XGlassDamage3", true);
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("XHullDamage1", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("XHullDamage2", true);
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
        super.doFocusLeave();
    }

    static 
    {
        Property.set(CockpitHalifaxBMkIII_FGunner.class, "aiTuretNum", 0);
        Property.set(CockpitHalifaxBMkIII_FGunner.class, "weaponControlNum", 10);
        Property.set(CockpitHalifaxBMkIII_FGunner.class, "astatePilotIndx", 2);
    }
}
