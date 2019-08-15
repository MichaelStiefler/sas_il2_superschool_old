package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitHE_111H20_LGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretLA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretLB", 0.0F, orient.getTangage(), 0.0F);
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
                if(f < -55F)
                    f = -55F;
                if(f > 23F)
                    f = 23F;
                if(f1 > 30F)
                    f1 = 30F;
                if(f1 < -40F)
                    f1 = -40F;
                if(f1 < -55F - 0.5F * f)
                    f1 = -55F - 0.5F * f;
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
            this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
            if(this.bGunFire)
            {
                if(hook1 == null)
                    hook1 = new HookNamed(aircraft(), "_MGUN04");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN04");
                if(hook2 == null)
                    hook2 = new HookNamed(aircraft(), "_MGUN08");
                doHitMasterAircraft(aircraft(), hook2, "_MGUN08");
                if(iCocking > 0)
                    iCocking = 0;
                else
                    iCocking = 1;
            } else
            {
                iCocking = 0;
            }
            resetYPRmodifier();
            Cockpit.xyz[0] = -0.07F * (float)iCocking;
            this.mesh.chunkSetLocate("LeverL", Cockpit.xyz, Cockpit.ypr);
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

    public CockpitHE_111H20_LGunner()
    {
        super("3DO/Cockpit/He-111H-20-LGun/hier_H20.him", "he111_gunner");
        hook1 = null;
        hook2 = null;
        iCocking = 0;
    }

    public void toggleLight()
    {
        this.cockpitLightControl = !this.cockpitLightControl;
        if(this.cockpitLightControl)
        {
            this.mesh.chunkVisible("Flare", true);
            setNightMats(true);
        } else
        {
            this.mesh.chunkVisible("Flare", false);
            setNightMats(false);
        }
    }

    public void reflectCockpitState()
    {
        if(this.fm.AS.astateCockpitState != 0)
            this.mesh.chunkVisible("Holes_D1", true);
    }

    private Hook hook1;
    private Hook hook2;
    private int iCocking;

    static 
    {
        Property.set(CockpitHE_111H20_LGunner.class, "aiTuretNum", 3);
        Property.set(CockpitHE_111H20_LGunner.class, "weaponControlNum", 13);
        Property.set(CockpitHE_111H20_LGunner.class, "astatePilotIndx", 4);
    }
}
