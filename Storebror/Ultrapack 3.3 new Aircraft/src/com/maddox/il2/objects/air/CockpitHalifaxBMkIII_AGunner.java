package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHalifaxBMkIII_AGunner extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
            aircraft().hierMesh().chunkVisible("Turret3C_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
        aircraft().hierMesh().chunkVisible("Turret3C_D0", true);
        super.doFocusLeave();
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
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
                if(f < -38F)
                    f = -38F;
                if(f > 38F)
                    f = 38F;
                if(f1 > 38F)
                    f1 = 38F;
                if(f1 < -41F)
                    f1 = -41F;
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
            if(bGunFire)
            {
                if(hook1 == null)
                    hook1 = new HookNamed(aircraft(), "_MGUN05");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN05");
                if(hook2 == null)
                    hook2 = new HookNamed(aircraft(), "_MGUN06");
                doHitMasterAircraft(aircraft(), hook2, "_MGUN06");
                if(hook3 == null)
                    hook3 = new HookNamed(aircraft(), "_MGUN09");
                doHitMasterAircraft(aircraft(), hook3, "_MGUN09");
                if(hook4 == null)
                    hook4 = new HookNamed(aircraft(), "_MGUN10");
                doHitMasterAircraft(aircraft(), hook4, "_MGUN10");
            }
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

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    public CockpitHalifaxBMkIII_AGunner()
    {
        super("3DO/Cockpit/Halifax-AGun/hier.him", "bf109");
        hook1 = null;
        hook2 = null;
        hook3 = null;
        hook4 = null;
    }

    private Hook hook1;
    private Hook hook2;
    private Hook hook3;
    private Hook hook4;

    static 
    {
        Property.set(CockpitHalifaxBMkIII_AGunner.class, "aiTuretNum", 2);
        Property.set(CockpitHalifaxBMkIII_AGunner.class, "weaponControlNum", 12);
        Property.set(CockpitHalifaxBMkIII_AGunner.class, "astatePilotIndx", 4);
    }
}
