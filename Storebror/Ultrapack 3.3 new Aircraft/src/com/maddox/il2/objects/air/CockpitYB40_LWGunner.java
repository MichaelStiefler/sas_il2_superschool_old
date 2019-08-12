package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitYB40_LWGunner extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Turret6B_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Turret6B_D0", aircraft().hierMesh().isChunkVisible("Turret6A_D0"));
        super.doFocusLeave();
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret_Base", 0.0F, orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("MGun", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("Turret_Base2", 0.0F, orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("MGun2", 0.0F, cvt(orient.getTangage(), -20F, 45F, -20F, 45F), 0.0F);
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
                if(f < -34F)
                    f = -34F;
                if(f > 50F)
                    f = 50F;
                if(f1 > 32F)
                    f1 = 32F;
                if(f1 < -30F)
                    f1 = -30F;
                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
            }
    }

    protected void interpTick()
    {
        if(this.bGunFire)
            this.mesh.chunkSetAngles("Trigger", 0.0F, 17.5F, 0.0F);
        else
            this.mesh.chunkSetAngles("Trigger", 0.0F, 0.0F, 0.0F);
        if(!isRealMode())
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
            this.bGunFire = false;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
        if(this.bGunFire)
        {
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUN11");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN11");
            if(hook2 == null)
                hook2 = new HookNamed(aircraft(), "_MGUN12");
            doHitMasterAircraft(aircraft(), hook2, "_MGUN12");
        }
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode())
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
            this.bGunFire = false;
        else
            this.bGunFire = flag;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
    }

    public CockpitYB40_LWGunner()
    {
        super("3DO/Cockpit/YB-40-WGuns/hierL.him", "he111_gunner");
        hook1 = null;
        hook2 = null;
        bNeedSetUp = true;
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private Hook hook1;
    private Hook hook2;
    private boolean bNeedSetUp;

    static 
    {
        Property.set(CockpitYB40_LWGunner.class, "aiTuretNum", 5);
        Property.set(CockpitYB40_LWGunner.class, "weaponControlNum", 16);
        Property.set(CockpitYB40_LWGunner.class, "astatePilotIndx", 5);
    }
}
