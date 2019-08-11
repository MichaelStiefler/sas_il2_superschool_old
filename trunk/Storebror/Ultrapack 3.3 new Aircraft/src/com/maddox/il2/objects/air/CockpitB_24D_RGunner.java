package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB_24D_RGunner extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Tail1_D0", false);
            aircraft().hierMesh().chunkVisible("Turret5B_D0", false);
            aircraft().hierMesh().chunkVisible("Turret4B_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot7_D0", false);
            aircraft().hierMesh().chunkVisible("Head7_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot8_D0", false);
            aircraft().hierMesh().chunkVisible("Head8_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Tail1_D0", true);
        aircraft().hierMesh().chunkVisible("Turret5B_D0", true);
        aircraft().hierMesh().chunkVisible("Turret4B_D0", true);
        aircraft().hierMesh().chunkVisible("Pilot7_D0", true);
        aircraft().hierMesh().chunkVisible("Head7_D0", true);
        aircraft().hierMesh().chunkVisible("Pilot8_D0", true);
        aircraft().hierMesh().chunkVisible("Head8_D0", true);
        super.doFocusLeave();
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretRA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretRB", 7.5F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretRC", 0.0F, -orient.getTangage(), 0.0F);
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
                float f_0_ = orient.getTangage();
                if(f < -60F)
                    f = -60F;
                if(f > 32F)
                    f = 32F;
                if(f_0_ > 32F)
                    f_0_ = 32F;
                if(f_0_ < -40F)
                    f_0_ = -40F;
                orient.setYPR(f, f_0_, 0.0F);
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
                if(hook1 == null)
                    hook1 = new HookNamed(aircraft(), "_MGUN08");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN08");
            }
        }
    }

    public void doGunFire(boolean bool)
    {
        if(isRealMode())
        {
            if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
                this.bGunFire = false;
            else
                this.bGunFire = bool;
            this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
        }
    }

    public CockpitB_24D_RGunner()
    {
        super("3DO/Cockpit/B-25J-RGun/RGunnerB24D.him", "he111_gunner");
        hook1 = null;
    }

    public void reflectWorldToInstruments(float f)
    {
        this.mesh.chunkSetAngles("TurretLA", 0.0F, aircraft().FM.turret[3].tu[0], 0.0F);
        this.mesh.chunkSetAngles("TurretLB", 0.0F, aircraft().FM.turret[3].tu[1], 0.0F);
        this.mesh.chunkSetAngles("TurretLC", 0.0F, aircraft().FM.turret[3].tu[1], 0.0F);
        this.mesh.chunkVisible("TurretLC", false);
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 4) != 0)
            this.mesh.chunkVisible("XGlassDamage1", true);
        if((this.fm.AS.astateCockpitState & 8) != 0)
        {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if((this.fm.AS.astateCockpitState & 0x10) != 0)
        {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if((this.fm.AS.astateCockpitState & 0x20) != 0)
        {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
    }

    private Hook hook1;

    static 
    {
        Property.set(CockpitB_24D_RGunner.class, "aiTuretNum", 4);
        Property.set(CockpitB_24D_RGunner.class, "weaponControlNum", 14);
        Property.set(CockpitB_24D_RGunner.class, "astatePilotIndx", 6);
    }
}
