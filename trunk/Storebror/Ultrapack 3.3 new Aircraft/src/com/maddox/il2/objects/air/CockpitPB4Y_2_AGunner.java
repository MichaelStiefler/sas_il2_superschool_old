package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitPB4Y_2_AGunner extends CockpitGunner
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
        aircraft().hierMesh().chunkVisible("Turret6B_D0", true);
        super.doFocusLeave();
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, -orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient)
    {
        if(isRealMode())
            if(!aiTurret().bIsOperable)
            {
                orient.setYPR(0.0F, -0F, 0.0F);
            } else
            {
                float f = orient.getYaw();
                float f1 = orient.getTangage();
                if(f < -60F)
                    f = -60F;
                if(f > 60F)
                    f = 60F;
                if(f1 > 45F)
                    f1 = 45F;
                if(f1 < -35F)
                    f1 = -35F;
                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
            }
    }

    protected void interpTick()
    {
        if(isRealMode())
        {
            if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
                this.bGunFire = false;
            this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
            if(this.bGunFire)
            {
                if(hook9 == null)
                    hook9 = new HookNamed(aircraft(), "_MGUN09");
                doHitMasterAircraft(aircraft(), hook9, "_MGUN09");
                if(hook10 == null)
                    hook10 = new HookNamed(aircraft(), "_MGUN10");
                doHitMasterAircraft(aircraft(), hook10, "_MGUN10");
            }
        }
    }

    public void doGunFire(boolean flag)
    {
        if(isRealMode())
        {
            if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
                this.bGunFire = false;
            else
                this.bGunFire = flag;
            this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
        }
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            bNeedSetUp = false;
            reflectPlaneMats();
        }
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 4) != 0)
            this.mesh.chunkVisible("XGlassDamage1", true);
        if((this.fm.AS.astateCockpitState & 0x10) != 0)
            this.mesh.chunkVisible("XGlassDamage2", true);
    }

    public CockpitPB4Y_2_AGunner()
    {
        super("3DO/Cockpit/PB4Y2-AGun/hier.him", "bf109");
        bNeedSetUp = true;
        hook9 = null;
        hook10 = null;
    }

    private boolean bNeedSetUp;
    private Hook hook9;
    private Hook hook10;

    static 
    {
        Property.set(CockpitPB4Y_2_AGunner.class, "aiTuretNum", 5);
        Property.set(CockpitPB4Y_2_AGunner.class, "weaponControlNum", 15);
        Property.set(CockpitPB4Y_2_AGunner.class, "astatePilotIndx", 4);
    }
}
