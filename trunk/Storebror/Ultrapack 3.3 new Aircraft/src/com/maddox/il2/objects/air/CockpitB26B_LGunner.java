package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB26B_LGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretLA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretLB", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretLC", 0.0F, -orient.getTangage(), 0.0F);
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
                if(f < -34F)
                    f = -34F;
                if(f > 30F)
                    f = 30F;
                if(f_0_ > 32F)
                    f_0_ = 32F;
                if(f_0_ < -30F)
                    f_0_ = -30F;
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
                    hook1 = new HookNamed(aircraft(), "_MGUN12");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN12");
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

    public CockpitB26B_LGunner()
    {
        super("3DO/Cockpit/B-26B-LGun/hier.him", "he111_gunner");
        hook1 = null;
    }

    public void reflectWorldToInstruments(float f)
    {
        this.mesh.chunkSetAngles("TurretRA", 0.0F, aircraft().FM.turret[3].tu[0], 0.0F);
        this.mesh.chunkSetAngles("TurretRB", 0.0F, aircraft().FM.turret[3].tu[1], 0.0F);
        this.mesh.chunkSetAngles("TurretRC", 0.0F, aircraft().FM.turret[3].tu[1], 0.0F);
        this.mesh.chunkVisible("TurretRC", false);
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
        Property.set(CockpitB26B_LGunner.class, "aiTuretNum", 4);
        Property.set(CockpitB26B_LGunner.class, "weaponControlNum", 14);
        Property.set(CockpitB26B_LGunner.class, "astatePilotIndx", 6);
    }
}
