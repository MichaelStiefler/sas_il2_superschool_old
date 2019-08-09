package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHampden_TGunner extends CockpitGunner
{

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
        aircraft().hierMesh().chunkVisible("Turret1B_D0", aircraft().hierMesh().isChunkVisible("Turret1A_D0"));
        super.doFocusLeave();
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
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
        if(f < -40F)
            f = -40F;
        if(f > 40F)
            f = 40F;
        if(f1 > 45.0F)
            f1 = 45.0F;
        if(f1 < -3F)
            f1 = -3F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }
    
    protected void interpTick()
    {
        if(!isRealMode())
            return;
        if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
            this.bGunFire = false;
        this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
        if(this.bGunFire)
        {
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUN01");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
            if(hook2 == null)
                hook2 = new HookNamed(aircraft(), "_MGUN02");
            doHitMasterAircraft(aircraft(), hook2, "_MGUN02");
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

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 4) != 0)
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        if((this.fm.AS.astateCockpitState & 0x10) != 0)
            this.mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public CockpitHampden_TGunner()
    {
        super("3DO/Cockpit/A-20G-TGun/TGunnerHampden.him", "he111_gunner");
        hook1 = null;
        hook2 = null;
    }

    private Hook hook1;
    private Hook hook2;

    static 
    {
        Property.set(CockpitA_20G_TGunner.class, "aiTuretNum", 0);
        Property.set(CockpitA_20G_TGunner.class, "weaponControlNum", 10);
        Property.set(CockpitA_20G_TGunner.class, "astatePilotIndx", 1);
    }
}
